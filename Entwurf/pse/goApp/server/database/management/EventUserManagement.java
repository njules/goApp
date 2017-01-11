package pse.goApp.server.database.management;

/**
 * Manages Event-User Table
 */
public class EventUserManagement implements Management {

	/**
	 * adds new entry to table
	 * @param userId userID to combine with Event
	 * @param eventId eventID to combine with user
	 * @return returns id of new entry
	 */
	int add(int userId, int eventId) {
		// TODO - implement EventUserManagement.add
		throw new UnsupportedOperationException();
	}

	/**
	 * updates Status of given user and eventID
	 * @param userID userID to update
	 * @param eventID eventID to upate
	 * @param newStatus new Status to set
	 * @return true, if update was successfull, otherwise false
	 */
	public boolean updateStatus(int userID, int eventID, boolean newStatus) {
		// TODO - implement EventUserManagement.updateStatus
		throw new UnsupportedOperationException();
	}

	/**
	 * removes entry (with userID and eventID)
	 * @param userID userID to remove
	 * @param eventID eventID to remove
	 * @return true, if update was successfull, otherwise false
	 */
	public boolean delete(int userID, int eventID) {
		// TODO - implement EventUserManagement.delete
		throw new UnsupportedOperationException();
	}

	/**
	 * get all events in which given user participates
	 * @param userID ID of user
	 * @return list of eventIDs
	 */
	public int getEvents(int userID) {
		// TODO - implement EventUserManagement.getEvents
		throw new UnsupportedOperationException();
	}

	/**
	 * get all users which participate in given Event
	 * @param eventID ID of event
	 * @return List of userIDs
	 */
	public int getUsers(int eventID) {
		// TODO - implement EventUserManagement.getUsers
		throw new UnsupportedOperationException();
	}

}