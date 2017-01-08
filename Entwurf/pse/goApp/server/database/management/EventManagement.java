package pse.goApp.server.database.management;

import pse.goApp.client.model.*;

/**
 * Management for Event Table
 */
public class EventManagement implements Management {

	/**
	 * adds a new Entry and returns the id
	 * @param name Eventname
	 * @param location Location, where Event takes place
	 * @param time Date and Time, when Event takes place
	 * @param userId ID of User who created the Event
	 */
	public int add(String name, Location location, DateTime time, int userId) {
		// TODO - implement EventManagement.add
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
	 * updates entry with given id - sets new status
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
	 * returns Location of selected Event
	 * @param eventID ID from entry
	 * @return Location of Event
	 */
	public Location getLocation(int eventID) {
		// TODO - implement EventManagement.getLocation
		throw new UnsupportedOperationException();
	}

	/**
	 * get Name of given Event
	 * @param eventID ID of entry
	 * @return Name of Event
	 */
	public String getName(int eventID) {
		// TODO - implement EventManagement.getName
		throw new UnsupportedOperationException();
	}

	/**
	 * get Time and Date of given Event
	 * @param eventID ID of entry
	 * @return Date and Time of Event
	 */
	public DateTime getDateTime(int eventID) {
		// TODO - implement EventManagement.getDateTime
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