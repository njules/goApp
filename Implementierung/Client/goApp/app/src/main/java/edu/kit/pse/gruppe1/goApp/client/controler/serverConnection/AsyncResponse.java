package edu.kit.pse.gruppe1.goApp.client.controler.serverConnection;

/**
 * This interface is used by the services to define what should be done with the response of the server.
 */
public interface AsyncResponse {

	/**
	 * The code in this method is executed after the server send his response back to the AsyncTask.
	 * This method is responsible for handling the response.
	 * @param answer The JSONObject to the JSON-string send back from the servlet.
	 */
	void httpRequestExecuted(JSONObject answer);

}