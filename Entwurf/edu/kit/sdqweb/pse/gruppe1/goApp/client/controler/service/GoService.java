package edu.kit.sdqweb.pse.gruppe1.goApp.client.controler.service;

import edu.kit.sdqweb.pse.gruppe1.goApp.client.model.*;

/**
 * This Service provides methods to handle if the user already left for the destination of the event
 */
public class GoService {

	/**
	 * adds the user to the list of users who are on their way to the event
	 * @param user
	 * @param event
	 * @return true, if method was successful, otherwise false
	 */
	private boolean setGo(User user, Event event) {
		// TODO - implement GoService.setGo
		throw new UnsupportedOperationException();
	}

	/**
	 * finds all user who already signaled the app that they have started
	 * @param event
	 * @return all user who's started value is true. This indicates that they are on the way to the event.
	 */
	private List<edu.kit.sdqweb.pse.gruppe1.goApp.client.model.User> getStartedParticipants(Event event) {
		// TODO - implement GoService.getStartedParticipants
		throw new UnsupportedOperationException();
	}

}