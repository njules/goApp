package edu.kit.pse.gruppe1.goApp.server.servlet;

/**
 * This servlet is used by users to access and manage information about them.
 */
public class UserServlet {

	/**
	 * This method allows a user to change his name to a string value of his choice. A user may only change his own name.
	 * @param json The JSON object contains the user that wants to change his name and the string with the name the user wants to change it to.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String changeName(JSONObject json) {
		// TODO - implement UserServlet.changeName
		throw new UnsupportedOperationException();
	}

	/**
	 * A user can invoke this to retrieve any information about a given user such as groups he is a member of and events he wants to participate or is invited to.
	 * @param json This JSON object contains the user about whom the information shall be released.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String getUser(JSONObject json) {
		// TODO - implement UserServlet.getUser
		throw new UnsupportedOperationException();
	}

}