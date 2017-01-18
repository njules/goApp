package edu.kit.pse.gruppe1.goApp.server.database.management;

import edu.kit.pse.gruppe1.goApp.server.model.*;

/**
 * Manages User Table
 */
public class UserManagement implements Management {

	/**
	 * creates new User and adds new entry to table
	 * @param name name of User
	 * @param googleId googleID of User
	 * @return userID of entry
	 */
	public User add(String name, int googleId) {
		// TODO - implement UserManagement.add
		throw new UnsupportedOperationException();
	}

	/**
	 * changes entry in table
	 * @param chUser User Object with changes
	 * @return true, if change was successfull, otherwise false
	 */
	public boolean update(User chUser) {
		// TODO - implement UserManagement.update
		throw new UnsupportedOperationException();
	}

	/**
	 * updates Name of User
	 * @param userID ID from user to update
	 * @param newName name to set
	 * @return true, if update was successfull, otherwise false
	 */
	public boolean updateName(int userID, String newName) {
		// TODO - implement UserManagement.updateName
		throw new UnsupportedOperationException();
	}

	/**
	 * updates current location of user
	 * @param userId userID from entry to update
	 * @param newLocation new Location of user
	 * @return true, if update was successfull, otherwise false
	 */
	public boolean updateLocation(int userId, Location newLocation) {
		// TODO - implement UserManagement.updateLocation
		throw new UnsupportedOperationException();
	}

	/**
	 * get User with given userID
	 * @param userID ID of User to search for
	 * @return found User
	 */
	public User getUser(int userID) {
		// TODO - implement UserManagement.getUser
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean delete(int ID) {
		// TODO Auto-generated method stub
		return false;
	}

}