package edu.kit.pse.gruppe1.goApp.server.database.management;

import java.util.List;

import edu.kit.pse.gruppe1.goApp.server.model.*;

/**
 * Manages Request Table
 */
public class RequestManagement implements Management {

	/**
	 * creates new entry
	 * @param gruopID groupID to combine with user
	 * @param userID userID to combine with group
	 * @return ID of new entry
	 */
	public boolean add(int gruopID, int userID) {
		// TODO - implement RequestManagement.add
		throw new UnsupportedOperationException();
	}

	/**
	 * get all User to given Gruop
	 * @param gruopID ID of group
	 * @return List of matching Users
	 */
	public List<User> getRequestByGroup(int gruopID) {
		// TODO - implement RequestManagement.getRequestByGroup
		throw new UnsupportedOperationException();
	}

	/**
	 * get all groups to given User
	 * @param userID ID of User
	 * @return List of matching Groups
	 */
	public List<Group> getRequestByUser(int userID) {
		// TODO - implement RequestManagement.getRequestByUser
		throw new UnsupportedOperationException();
	}

	/**
	 * delete entry with given groupID and userID
	 * @param groupID
	 * @param userID
	 * @return true, if deletion was successful, otherwise false
	 */
	public boolean delete(int groupID, int userID) {
		// TODO - implement RequestManagement.delete
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean delete(int ID) {
		// TODO Auto-generated method stub
		return false;
	}

}