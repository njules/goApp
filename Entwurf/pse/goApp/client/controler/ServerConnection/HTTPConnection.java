package pse.goApp.client.controler.ServerConnection;

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
	 * @param response The AsyncResponse that should be executed after receiving the answer from the server.
	 */
	public void sendGetRequest(String JSON,AsyncResponse response) {
		// TODO - implement HTTPConnection.sendGetRequest
		throw new UnsupportedOperationException();
	}

	/**
	 * Method that handles a get request.
	 * @param JSON The String which should be send to the server.
	 * @param response The AsyncResponse that should be executed after receiving the answer from the server.
	 */
	public void sendPostRequest(String JSON, AsyncResponse response) {
		// TODO - implement HTTPConnection.sendPostRequest
		throw new UnsupportedOperationException();
	}

}