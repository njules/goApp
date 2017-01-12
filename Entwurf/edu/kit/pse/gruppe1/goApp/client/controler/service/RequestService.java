package edu.kit.pse.gruppe1.goApp.client.controler.service;

import edu.kit.pse.gruppe1.goApp.client.model.*;

/**
 * This Service provides methods to handle a Request.
 */
public class RequestService {

	/**
	 * creates a new Request
	 * @param user the user who sends the request
	 * @param group the group the user wants to be a member of
	 * @return true, if method was successful, otherwise false
	 */
	private boolean create(User user, Group group) {
		// TODO - implement RequestService.create
		throw new UnsupportedOperationException();
	}

	/**
	 * adds the user to the group and deletes the Request if the founder of the group wants the user in the group
	 * @param request the request the founder has made a decision about
	 * @return true, if method was successful, otherwise false
	 */
	private boolean accept(Request request) {
		// TODO - implement RequestService.accept
		throw new UnsupportedOperationException();
	}

	/**
	 * deletes the request if the founder decided that the user will not be in the group
	 * @param request the request the founder has made a decision about
	 * @return true, if method was successful, otherwise false
	 */
	private boolean reject(Request request) {
		// TODO - implement RequestService.reject
		throw new UnsupportedOperationException();
	}

}