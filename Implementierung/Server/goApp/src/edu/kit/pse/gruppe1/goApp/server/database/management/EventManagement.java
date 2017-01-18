package edu.kit.pse.gruppe1.goApp.server.database.management;

import edu.kit.pse.gruppe1.goApp.client.model.*;

/**
 * Management for Event Table
 */
public class EventManagement implements Management {

	/**
	 * creates new Group and adds new entry to table
	 * adds all User from Group to Event and sets Status
	 * @param name Name of Event
	 * @param location Location, where Event takes place
	 * @param time Date and Time, when Event takes place
	 * @param userId ID of User who created the Event
	 * @param groupID ID of Group to which Event is related to
	 */
	public Event add(String name, Location location, DateTime time, int userId, int groupID) {
		// TODO - implement EventManagement.add
		throw new UnsupportedOperationException();
	}

	/**
	 * updates entry in table
	 * @param chEvent Event with changes
	 * @return true, if update was successful, otherwise false
	 */
	public boolean update(Event chEvent) {
		// TODO - implement EventManagement.update
		throw new UnsupportedOperationException();
	}

	/**
	 * updates entry with given id - sets new name
	 * @param eventID ID of entry to be updated
	 * @param name new Name of Entry
	 * @return true, if update was successfull, otherwise false
	 */
	public boolean updateName(int eventID, String name) {
		// TODO - implement EventManagement.updateName
		throw new UnsupportedOperationException();
	}

	/**
	 * sets new status to entry with given ID
	 * @param userID ID from user
	 * @param eventID ID from Event
	 * @param newStatus status to set
	 * @return true, if update was successfull, otherwise false
	 */
	public boolean updateStatus(int userID, int eventID, boolean newStatus) {
		// TODO - implement EventManagement.updateStatus
		throw new UnsupportedOperationException();
	}

	/**
	 * gets Event with given eventID
	 * @param eventID ID of event
	 * @return matching  Event
	 */
	public Event getEvent(int eventID) {
		// TODO - implement EventManagement.getEvent
		throw new UnsupportedOperationException();
	}

	/**
	 * get User who created Event
	 * @param eventID ID of entry
	 * @return ID of User
	 */
	public int getUser(int eventID) {
		// TODO - implement EventManagement.getUser
		throw new UnsupportedOperationException();
	}

}