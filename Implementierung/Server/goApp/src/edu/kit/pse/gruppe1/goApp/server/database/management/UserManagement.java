package edu.kit.pse.gruppe1.goApp.server.database.management;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import edu.kit.pse.gruppe1.goApp.server.model.*;

/**
 * Manages User Table
 */
public class UserManagement implements Management {

	/**
	 * creates new User and adds new entry to table
	 * 
	 * @param name
	 *            name of User
	 * @param googleId
	 *            googleID of User
	 * @return userID of entry
	 */
	public User add(String name, int googleId) {
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
	 * @param userID
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
	 * @param userID
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

	@Override
	public boolean delete(int userId) {
		User user = getUser(userId);
		if (user == null) {
			return false;
		}
		Session session = DatabaseInitializer.getFactory().getCurrentSession();
		session.beginTransaction();
		session.delete(user);
		session.getTransaction().commit();
		return true;
	}
}