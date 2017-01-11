package edu.kit.sdqweb.pse.gruppe1.goApp.server.database.management;

/**
 * Manages GroupUser Table
 */
public class GroupUserManagement implements Management {

	/**
	 * creates new entry
	 * @param gruopID groupID to combine with user
	 * @param userID userID to combine with group
	 * @return ID of new entry
	 */
	public boolean add(int gruopID, int userID) {
		// TODO - implement GroupUserManagement.add
		throw new UnsupportedOperationException();
	}

	/**
	 * get all User to given Gruop
	 * @param gruopID ID of group
	 * @return List of matching User
	 */
	public List<pse.goApp.client.model.User> getUsers(int gruopID) {
		// TODO - implement GroupUserManagement.getUsers
		throw new UnsupportedOperationException();
	}

	/**
	 * get all groups to given User
	 * @param userID ID of User
	 * @return List of matching Groups
	 */
	public List<pse.goApp.client.model.Group> getGroups(int userID) {
		// TODO - implement GroupUserManagement.getGroups
		throw new UnsupportedOperationException();
	}

	/**
	 * deletes entry in table with given GroupID and userID
	 * @param groupID groupID to delete (in combination)
	 * @param userID userID to delete (in combination)
	 * @return true, if successful, otherwise false
	 */
	public boolean delete(int groupID, int userID) {
		// TODO - implement GroupUserManagement.delete
		throw new UnsupportedOperationException();
	}

}