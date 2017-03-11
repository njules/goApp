package edu.kit.pse.gruppe1.goApp.server.database.management;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Request;
import edu.kit.pse.gruppe1.goApp.server.model.User;

/**
 * Manages Request Table
 */
public class RequestManagement implements Management {

    /**
     * Constructor
     */
    public RequestManagement() {
        super();
    }

    private Request getRequest(int requestId) {
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        Request request = (Request) session.get(Request.class, requestId);
        session.getTransaction().commit();
        return request;
    }

    /**
     * Returns the request.
     * 
     * @param groupId
     *            the id of the group.
     * @param userId
     *            the id of the user.
     * @return The request with the given ids if such an request is in the db and else null.
     */
    public Request getRequest(int groupId, int userId) {
        Group group = new GroupManagement().getGroup(groupId);
        if (group == null) {
            return null;
        }
        return group.getRequest(userId);
    }

    /**
     * creates new entry
     * 
     * @param groupId
     *            groupID to combine with user
     * @param userId
     *            userID to combine with group
     * @return ID of new entry
     */
    public boolean add(int groupId, int userId) {
        Group group = new GroupManagement().getGroup(groupId);
        User user = new UserManagement().getUser(userId);
        if (group == null || user == null) {
            return false;
        }
        Request request = new Request(user, group);
        if (!group.addRequest(request)) {
            return false;
        }
        new GroupManagement().update(group);
        return true;
    }

    /**
     * get all User to given Gruop
     * 
     * @param groupId
     *            ID of group
     * @return List of matching Users
     */
    public List<User> getRequestByGroup(int groupId) {
        if (new GroupManagement().getGroup(groupId) == null) {
            return null;
        }
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        @SuppressWarnings("unchecked")
        List<User> users = session.createCriteria(User.class).createAlias("requests", "r")
                .add(Restrictions.eq("r.group.groupId", groupId))
                .addOrder(Property.forName("name").asc()).list();
        session.getTransaction().commit();
        return users;
    }

    /**
     * get all groups to given User
     * 
     * @param userId
     *            ID of User
     * @return List of matching Groups
     */
    public List<Group> getRequestByUser(int userId) {
        if (new UserManagement().getUser(userId) == null) {
            return null;
        }
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Group> groups = session.createCriteria(Group.class).createAlias("requests", "r")
                .add(Restrictions.eq("r.user.userId", userId))
                .addOrder(Property.forName("name").asc()).list();
        session.getTransaction().commit();
        return groups;
    }

    /**
     * delete entry with given groupID and userID
     * 
     * @param groupId
     *            the id of the group
     * @param userId
     *            the id of the user
     * @return true, if deletion was successful, otherwise false
     */
    public boolean delete(int groupId, int userId) {
        Request request = getRequest(groupId, userId);
        return delete(request);
    }

    @Override
    public boolean delete(int requestId) {
        Request request = getRequest(requestId);
        return delete(request);
    }

    private boolean delete(Request request) {
        if (request == null) {
            return false;
        }
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        session.delete(request);
        session.getTransaction().commit();
        return true;
    }

}