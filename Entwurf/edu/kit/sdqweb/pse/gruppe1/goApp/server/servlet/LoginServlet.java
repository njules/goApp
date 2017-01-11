package edu.kit.sdqweb.pse.gruppe1.goApp.server.servlet;

/**
 * This servlet is used to register users that start the app for the first time and login returning users.
 */
public class LoginServlet {

	/**
	 * Invoking this method creates a new user and registers his ID to the database. The new user may then join groups. A user can register only once, if he returns to the app he must use the login function.
	 * @param json The JSON object contains a user ID to which the user shall be registered.
	 * @return Returns a JSON string containing the user that just registered.
	 */
	private String register(JSONObject json) {
		// TODO - implement LoginServlet.register
		throw new UnsupportedOperationException();
	}

	/**
	 * If a user has already registered to the database but his client is currently not logged in, he can call this method to regain access to the functions of this app. A user may only login if he isn't already logged in and has registered himself previously at any point.
	 * @param json This JSON object contains the user that wants to login.
	 * @return Returns a JSON string containing the user that just logged in.
	 */
	private String login(JSONObject json) {
		// TODO - implement LoginServlet.login
		throw new UnsupportedOperationException();
	}

}