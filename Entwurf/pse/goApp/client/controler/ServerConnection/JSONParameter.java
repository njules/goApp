package pse.goApp.client.controler.ServerConnection;

/**
 * Enumeration with all possible parameter-types for the JSON-strings.
 */
public enum JSONParameter {
	/**
	 * ID of the request
	 */
	ID,
	/**
	 * Error code which is 0 if no error occurred.
	 */
	ErrorCode,
	/**
	 * ID of an user.
	 */
	UserID,
	/**
	 * ID of a group.
	 */
	GroupID,
	/**
	 * ID of an event.
	 */
	EventID,
	/**
	 * Name of an user.
	 */
	UserName,
	/**
	 * Name of a group.
	 */
	GroupName,
	/**
	 * Name of an event.
	 */
	EventName,
	/**
	 * The name of the method which should be executed on the server. For example the create method of the GroupServlet.
	 */
	Method;

	/**
	 * Gives the corresponding name to an enum literal. Normally the enum literal name.
	 * @return The corresponding name.
	 */
	public String toString() {
		// TODO - implement JSONParameter.toString
		throw new UnsupportedOperationException();
	}

	/**
	 * Gives the corresponding enum literal to a string.
	 * @param s The string to the searched enum literal.
	 * @return The corresponding enum literal.
	 */
	public static JSONParameter fromString(String s) {
		// TODO - implement JSONParameter.fromString
		throw new UnsupportedOperationException();
	}

}