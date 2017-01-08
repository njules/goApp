package pse.goApp.server.database.management;

/**
 * Manages GroupUser Table
 */
public class GroupUserManagement implements Management {

	/**
	 * creates new entry
	 * @param userID userID to combine with group
	 * @param gruopID groupID to combine with user
	 * @return ID of new entry
	 */
	public int add(int userID, int gruopID) {
		// TODO - implement GroupUserManagement.add
		throw new UnsupportedOperationException();
	}

	/**
	 * get all User to given Gruop
	 * @param gruopID ID of group
	 * @return List of UserIds
	 */
	public int getUsers(int gruopID) {
		// TODO - implement GroupUserManagement.getUsers
		throw new UnsupportedOperationException();
	}

	/**
	 * get all groups to given User
	 * @param userID ID of User
	 * @return List of Group IDs
	 */
	public int getGroups(int userID) {
		// TODO - implement GroupUserManagement.getGroups
		throw new UnsupportedOperationException();
	}

}