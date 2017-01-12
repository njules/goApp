package edu.kit.pse.gruppe1.goApp.server.servlet;

/**
 * Users can access methods in this servlet to indicate whether they want to participate in an event from the group.
 */
public class ParticipateServlet {

	/**
	 * A user may accept an invitation to any event in a group he is a member of. He may accept or reject any event only once and can not invoke any of these two methods again later on for the same event.
	 * @param json A JSON object that contains the user wanting to accept the invite and the event he wants to participate in.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String accept(JSONObject json) {
		// TODO - implement ParticipateServlet.accept
		throw new UnsupportedOperationException();
	}

	/**
	 * A user may reject an invitation to any event in a group he is a member of. He may accept or reject any event only once and can not invoke any of these two methods again later on for the same event.
	 * @param json A JSON object that contains the user wanting to decline the invite and the event he wants to abstain from.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String reject(JSONObject json) {
		// TODO - implement ParticipateServlet.reject
		throw new UnsupportedOperationException();
	}

}