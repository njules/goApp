package pse.goApp.server.database.management;

import pse.goApp.client.model.*;

/**
 * Manages Group Table
 */
public class GroupManagement implements Management {

	/**
	 * adds new entry to table
	 * @param name Name of Group
	 * @param founder Founder of Group (User)
	 * @return true, if adding was successfull, otherwise false
	 */
	public int add(String name, User founder) {
		// TODO - implement GroupManagement.add
		throw new UnsupportedOperationException();
	}

	/**
	 * updates name of entry
	 * @param groupID ID of entry
	 * @param newName Name to set
	 * @return true, if update was successfull, otherwise false
	 */
	public boolean updateName(int groupID, String newName) {
		// TODO - implement GroupManagement.updateName
		throw new UnsupportedOperationException();
	}

	/**
	 * updates founder of given entry
	 * @param groupID ID of entry
	 * @param newFounder Founder to set
	 */
	public boolean updateFounder(int groupID, User newFounder) {
		// TODO - implement GroupManagement.updateFounder
		throw new UnsupportedOperationException();
	}

	/**
	 * add Member to Group
	 * @param groupID ID of group to add a new member to
	 * @param memberId ID of User to add to group
	 * @return true, if adding was successfull, otherwise false
	 */
	public boolean addMember(int groupID, int memberId) {
		// TODO - implement GroupManagement.addMember
		throw new UnsupportedOperationException();
	}

	/**
	 * delete member from group
	 * @param groupID ID of group to delete member from
	 * @param memberId ID of member to delete
	 * @return true, if deletion was successfull, otherwise false
	 */
	public boolean deleteMember(int groupID, int memberId) {
		// TODO - implement GroupManagement.deleteMember
		throw new UnsupportedOperationException();
	}

	/**
	 * get Name of given group
	 * @param groupID ID of group
	 * @return Name of Group
	 */
	public String getName(int groupID) {
		// TODO - implement GroupManagement.getName
		throw new UnsupportedOperationException();
	}

	/**
	 * get Founder of Group
	 * @param groupID ID of Group
	 */
	public void getFounder(int groupID) {
		// TODO - implement GroupManagement.getFounder
		throw new UnsupportedOperationException();
	}

}