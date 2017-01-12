package edu.kit.pse.gruppe1.goApp.server.servlet;

/**
 * Methods in this class exist so that a user can request to join a group and the group founder may then accept or reject the join request.
 */
public class RequestServlet {

	/**
	 * This method creates a join request from a user to a group. The user may not have reached the group limit already and the group may not have reached the user limit. Also the user may not already be a member of this group.
	 * @param json The JSON Object contains the user that posts the join request and the ID of the group the request is posted to.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String create(JSONObject json) {
		// TODO - implement RequestServlet.create
		throw new UnsupportedOperationException();
	}

	/**
	 * This method accepts a users join request. Only a group founder may accept join requests. After a request has been accepted, the user is a new member of the group and the request is no longer valid.
	 * @param json The JSON object contains the request that is to be accepted.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String accept(JSONObject json) {
		// TODO - implement RequestServlet.accept
		throw new UnsupportedOperationException();
	}

	/**
	 * This method rejects a users join request. Only a group founder may reject join requests. After a request has been rejected it is no longer valid.
	 * @param json The JSON object contains the request that is to be rejected.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String reject(JSONObject json) {
		// TODO - implement RequestServlet.reject
		throw new UnsupportedOperationException();
	}

}