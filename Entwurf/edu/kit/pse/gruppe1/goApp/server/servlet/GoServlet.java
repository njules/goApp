package edu.kit.pse.gruppe1.goApp.server.servlet;

/**
 * This servlets functions revolve around the go button and allow users to signal they started approaching the events location and view the status of other participants in this event.
 */
public class GoServlet {

	/**
	 * A participant that has previously set his status to "go" may request a list of fellow participants that are also on the way.
	 * @param json JSON object containing the event the participant list is requested for.
	 * @return Returns a JSON string containing a list with all participants of this event that have set their status to "go".
	 */
	private String getStartedParticpants(JSONObject json) {
		// TODO - implement GoServlet.getStartedParticpants
		throw new UnsupportedOperationException();
	}

	/**
	 * Allows the participant of an event to set his status to "go" which enables position tracking and viewing of other participants positions. The participant must have previously accepted the invitation to this event.
	 * @param json The JSON object contains the user that updates his status and the event he updates it for.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String setStarted(JSONObject json) {
		// TODO - implement GoServlet.setStarted
		throw new UnsupportedOperationException();
	}

}