package edu.kit.sdqweb.pse.gruppe1.goApp.server.servlet;

/**
 * This servlet returns active requests from a group or a user.
 */
public class RequestSearchServlet {

	/**
	 * This method fetches all active join requests from a given user. Only the user himself may view all of his active join requests.
	 * @param json The JSON object contains the user that is asking for the list of his join requests
	 * @return Returns a JSON string containing a list of all join requests issued by the given user.
	 */
	private String getRequestsByUser(JSONObject json) {
		// TODO - implement RequestSearchServlet.getRequestsByUser
		throw new UnsupportedOperationException();
	}

	/**
	 * This method fetches all active join requests for a given group. Join requests may only be viewed by the group founder.
	 * @param json The JSON object contains the ID of the group from which the join requests shall be fetched.
	 * @return Returns a JSON string containing a list of all active join requests for the given group..
	 */
	private String getRequestsByGroup(JSONObject json) {
		// TODO - implement RequestSearchServlet.getRequestsByGroup
		throw new UnsupportedOperationException();
	}

}