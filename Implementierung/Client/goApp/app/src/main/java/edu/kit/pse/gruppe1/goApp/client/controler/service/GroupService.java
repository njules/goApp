package edu.kit.pse.gruppe1.goApp.client.controler.service;

import java.util.List;

import edu.kit.pse.gruppe1.goApp.client.model.*;

/**
 * This Service provides methods to handle a single Group.
 */
public class GroupService {

	/**
	 * creates a new Group
	 * @param name The name of the new group
	 * @param founder The user who creates the group
	 * @return true, if method was successful, otherwise false
	 */
	private boolean create(String name, User founder) {
		// TODO - implement GroupService.create
		throw new UnsupportedOperationException();
	}

	/**
	 * deletes a group
	 * @param group The group to be deleted
	 * �
	 * @return true, if methode was successful, otherwise false
	 */
	private boolean delete(Group group) {
		// TODO - implement GroupService.delete
		throw new UnsupportedOperationException();
	}

	/**
	 * removes a member from the group
	 * @param group The group in which the user currently is but will be deleted from
	 * @param user The user who will be removed from the group
	 * @return true, if method was successful, otherwise false
	 */
	private boolean deleteMember(Group group, User user) {
		// TODO - implement GroupService.deleteMember
		throw new UnsupportedOperationException();
	}

	/**
	 * changes the name of the group
	 * @param group The group which's founder changed the name
	 * @param newName The new name of the group
	 * @return true, if method was successful, otherwise false
	 */
	private boolean setName(Group group, String newName) {
		// TODO - implement GroupService.setName
		throw new UnsupportedOperationException();
	}

	/**
	 * gets the group from the server database
	 * @param groupID The unique id of the group to find it
	 * @return the group with the given id or null if it doesn't exist
	 */
	private Group getGroup(int groupID) {
		// TODO - implement GroupService.getGroup
		throw new UnsupportedOperationException();
	}

	/**
	 * changes the founder of the group
	 * @param group The group which founder changes
	 * @param newFounder The user who gets the rights of the founder
	 * @return true, if method was successful, otherwise false
	 */
	private boolean setFounder(Group group, User newFounder) {
		// TODO - implement GroupService.setFounder
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns all events which are associated with the group or null if no events exist in this group
	 * @param group The existing group to get events from
	 * @return all event in the group or null
	 */
	private List<Event> getEvents(Group group) {
		// TODO - implement GroupService.getEvents
		throw new UnsupportedOperationException();
	}

}