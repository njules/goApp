package edu.kit.sdqweb.pse.gruppe1.goApp.server.database.management;

/**
 * Manages Event-User Table
 */
public class EventUserManagement implements Management {

	/**
	 * adds new entry to table
	 * @param eventId eventID to combine with user
	 * @param userId userID to combine with Event
	 */
	public boolean add(int eventId, int userId) {
		// TODO - implement EventUserManagement.add
		throw new UnsupportedOperationException();
	}

	/**
	 * updates Status of given user and eventID
	 * @param eventID eventID to upate
	 * @param userID userID to update
	 * @param newStatus new Status to set
	 * @return true, if update was successfull, otherwise false
	 */
	public boolean updateStatus(int eventID, int userID, boolean newStatus) {
		// TODO - implement EventUserManagement.updateStatus
		throw new UnsupportedOperationException();
	}

	/**
	 * removes entry (with userID and eventID)
	 * @param eventID eventID to remove
	 * @param userID userID to remove
	 * @return true, if update was successfull, otherwise false
	 */
	public boolean delete(int eventID, int userID) {
		// TODO - implement EventUserManagement.delete
		throw new UnsupportedOperationException();
	}

	/**
	 * get all events in which given user participates
	 * @param userID ID of user
	 * @return List of matching Events
	 */
	public List<pse.goApp.client.model.Event> getEvents(int userID) {
		// TODO - implement EventUserManagement.getEvents
		throw new UnsupportedOperationException();
	}

	/**
	 * get all users which participate in given Event
	 * @param eventID ID of event
	 * @return List of matching User
	 */
	public List<edu.kit.sdqweb.pse.gruppe1.goApp.client.model.User> getUsers(int eventID) {
		// TODO - implement EventUserManagement.getUsers
		throw new UnsupportedOperationException();
	}

	/**
	 * get all User with given Status from an Event
	 * @param status status to search for
	 * @param eventID ID of event to search for
	 * @return List of matching Users
	 */
	public List<edu.kit.sdqweb.pse.gruppe1.goApp.client.model.User> getUserByStatus(String status, int eventID) {
		// TODO - implement EventUserManagement.getUserByStatus
		throw new UnsupportedOperationException();
	}

}