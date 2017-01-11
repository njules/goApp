package edu.kit.sdqweb.pse.gruppe1.goApp.server.servlet;

/**
 * This servlet can be used buy event participants to update their location and view the current clustered location of other participants that are on the move.
 */
public class LocationServlet {

	/**
	 * A user may update his current gps location if his status is "go" for at least one event. The updated gps location will be used for the next clustering to update the groups location.
	 * @param json The JSON object must contain the next location of the user.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String setGPS(JSONObject json) {
		// TODO - implement LocationServlet.setGPS
		throw new UnsupportedOperationException();
	}

	/**
	 * A user that participates in an event may request the clustered locations of all other participants whose status is set to "go" if his own status is also set to "go" for this specific event.
	 * @param json The JSON object contains the event for which the user requests the cluster.
	 * @return Returns a JSON string containing the results of the clustering algorithm.
	 */
	private String getCluster(JSONObject json) {
		// TODO - implement LocationServlet.getCluster
		throw new UnsupportedOperationException();
	}

}