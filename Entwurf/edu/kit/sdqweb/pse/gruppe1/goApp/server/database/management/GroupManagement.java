package edu.kit.sdqweb.pse.gruppe1.goApp.server.database.management;

import edu.kit.sdqweb.pse.gruppe1.goApp.client.model.*;

/**
 * Manages Group Table
 */
public class GroupManagement implements Management {

	/**
	 * creates new Group and adds new entry to table
	 * @param name Name of Group
	 * @param founder Founder of Group (User)
	 * @return created Group
	 */
	public Group add(String name, User founder) {
		// TODO - implement GroupManagement.add
		throw new UnsupportedOperationException();
	}

	/**
	 * updates entry in table
	 * @param chGroup Group with changed attributes
	 * @return true, if update was successfull, otherwise false
	 */
	public boolean update(Group chGroup) {
		// TODO - implement GroupManagement.update
		throw new UnsupportedOperationException();
	}

	/**
	 * updates name of entry
	 * @param groupID ID of entry to change
	 * @param newName Name to set
	 * @return true, if update was successfull, otherwise false
	 */
	public boolean updateName(int groupID, String newName) {
		// TODO - implement GroupManagement.updateName
		throw new UnsupportedOperationException();
	}

	/**
	 * updates founder of given entry
	 * @param groupID ID of entry to change
	 * @param newFounder Founder to set
	 */
	public boolean updateFounder(int groupID, User newFounder) {
		// TODO - implement GroupManagement.updateFounder
		throw new UnsupportedOperationException();
	}

	/**
	 * returns Group with given GruopID
	 * @param groupID ID of Group to get
	 * @return a Group
	 */
	public Group getGroup(int groupID) {
		// TODO - implement GroupManagement.getGroup
		throw new UnsupportedOperationException();
	}

	/**
	 * get all Groups with given Member
	 * @param member Name of Member
	 * @return List of Groups
	 */
	public List<edu.kit.sdqweb.pse.gruppe1.goApp.client.model.Group> getGroupsByMember(String member) {
		// TODO - implement GroupManagement.getGroupsByMember
		throw new UnsupportedOperationException();
	}

	/**
	 * get all Groups with given Name
	 * @param searchName name to search for
	 * @return List of matching Groups
	 */
	public List<pse.goApp.client.model.Group> getGroupsByName(String searchName) {
		// TODO - implement GroupManagement.getGroupsByName
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
	 * @param memberID ID of member to delete
	 * @return true, if deletion was successfull, otherwise false
	 */
	public boolean deleteMember(int groupID, int memberID) {
		// TODO - implement GroupManagement.deleteMember
		throw new UnsupportedOperationException();
	}

	/**
	 * return all Events which are connected with given Group
	 * @param groupID GroupID to search Events for
	 * @return List of Events
	 */
	public List<edu.kit.sdqweb.pse.gruppe1.goApp.client.model.Event> getEvents(int groupID) {
		// TODO - implement GroupManagement.getEvents
		throw new UnsupportedOperationException();
	}

}