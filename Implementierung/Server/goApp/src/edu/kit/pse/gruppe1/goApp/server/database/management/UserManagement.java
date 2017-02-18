package edu.kit.pse.gruppe1.goApp.server.database.management;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.User;

/**
 * Manages User Table
 */
public class UserManagement implements Management {

    /**
     * Constructor
     */
    public UserManagement() {
        super();
    }

    /**
     * creates new User and adds new entry to table
     * 
     * @param name
     *            name of User
     * @param googleId
     *            googleID of User
     * @return userID of entry
     */
    public User add(String name, String googleId) {
        User user = new User(googleId, name);
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        return user;
    }

    /**
     * changes entry in table
     * 
     * @param chUser
     *            User Object with changes
     * @return true, if change was successfull, otherwise false
     */
    public boolean update(User chUser) {
        if (chUser.getUserId() == null) {
            return false;
        }
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        session.update(chUser);
        session.getTransaction().commit();
        return true;
    }

    /**
     * updates Name of User
     * 
     * @param userId
     *            ID from user to update
     * @param newName
     *            name to set
     * @return true, if update was successfull, otherwise false
     */
    public boolean updateName(int userId, String newName) {
        User user = getUser(userId);
        if (user == null) {
            return false;
        }
        user.setName(newName);
        return update(user);
    }

    /**
     * updates current location of user
     * 
     * @param userId
     *            userID from entry to update
     * @param newLocation
     *            new Location of user
     * @return true, if update was successfull, otherwise false
     */
    public boolean updateLocation(int userId, Location newLocation) {
        User user = getUser(userId);
        if (user == null) {
            return false;
        }
        user.setLocation(newLocation);
        return update(user);
    }

    /**
     * get User with given userID
     * 
     * @param userId
     *            ID of User to search for
     * @return found User
     */
    public User getUser(int userId) {
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        User user = (User) session.get(User.class, userId);
        session.getTransaction().commit();
        return user;
    }

    /**
     * the user with the given googleId
     * 
     * @param googleId
     *            the googleId of the user
     * @return the user if such an user exist and else null
     */
    public User getUserByGoogleId(String googleId) {
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();

        @SuppressWarnings("unchecked")
        List<User> users = session.createCriteria(User.class)
                .add(Restrictions.eq("googleId", googleId)).list();
        session.getTransaction().commit();
        if (users.size() != 1) {
            return null;
        }

        return users.get(0);
    }

    @Override
    public boolean delete(int userId) {
        User user = getUser(userId);
        if (user == null) {
            return false;
        }
        if (user.getFoundedGroups().size() != 0) {
            return false;
        }
        for (Group group : user.getGroups()) {
            if (!new GroupUserManagement().delete(group.getGroupId(), user.getUserId())) {
                return false;
            }
        }
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        session.delete(user);
        session.getTransaction().commit();
        return true;
    }

    /**
     * deletes old user locations
     */
    public void deleteOldLocations() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Location> locations = session.createCriteria(Location.class)
                .add(Restrictions.isNotNull("deletionTime"))
                .add(Restrictions.lt("deletionTime", time)).list();

        for (Location location : locations) {
            session.delete(location);
        }
        session.getTransaction().commit();
    }
}