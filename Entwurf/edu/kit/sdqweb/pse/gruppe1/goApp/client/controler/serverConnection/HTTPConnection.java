package edu.kit.sdqweb.pse.gruppe1.goApp.client.controler.serverConnection;

/**
 * If a service wants to communicate with the server, the service has to create a HTTPConnection object and must call the sendGet/PostRequest method.
 * For later requests the service can use the same HTTPConnection object.
 */
public class HTTPConnection {

	/**
	 * Constructor which expects the name of the servlet.
	 * @param nameOfServlet The name of the servlet, the service wants to communicate with.
	 */
	public HTTPConnection(String nameOfServlet) {
		// TODO - implement HTTPConnection.HTTPConnection
		throw new UnsupportedOperationException();
	}

	/**
	 * Method that handles a get request.
	 * @param JSON The String which should be send to the server.
	 */
	public JSONObject sendGetRequest(String JSON) {
		// TODO - implement HTTPConnection.sendGetRequest
		throw new UnsupportedOperationException();
	}

	/**
	 * Method that handles a get request.
	 * @param JSON The String which should be send to the server.
	 */
	public JSONObject sendPostRequest(String JSON) {
		// TODO - implement HTTPConnection.sendPostRequest
		throw new UnsupportedOperationException();
	}

}