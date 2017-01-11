package pse.goApp.server.database.management;

import pse.goApp.client.model.*;

public class UserManagement implements Management {

	/**
	 * adds new User to DB
	 * @param name name of User
	 * @param googleId googleID of User
	 * @return returns userID of entry
	 */
	public int add(String name, int googleId) {
		// TODO - implement UserManagement.add
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
	 * gets name from DB
	 * @param userID ID from entry to get data
	 * @return name of User
	 */
	public String getName(int userID) {
		// TODO - implement UserManagement.getName
		throw new UnsupportedOperationException();
	}

	/**
	 * get Location from given user
	 * @param userID userID to get Location from
	 */
	public Location getLocation(int userID) {
		// TODO - implement UserManagement.getLocation
		throw new UnsupportedOperationException();
	}

	/**
	 * get googleID from given user
	 * @param userID ID of entry
	 * @return GoogleID
	 */
	public int getGoogelID(int userID) {
		// TODO - implement UserManagement.getGoogelID
		throw new UnsupportedOperationException();
	}

}