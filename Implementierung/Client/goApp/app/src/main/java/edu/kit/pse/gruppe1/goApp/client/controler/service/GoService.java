package edu.kit.pse.gruppe1.goApp.client.controler.service;

import java.util.List;

import edu.kit.pse.gruppe1.goApp.client.model.*;

/**
 * This Service provides methods to handle if the user already left for the destination of the event
 */
public class GoService {

	/**
	 * adds the user to the list of users who are on their way to the event after the user presses the "go" button in the GroupActivity
	 * @param user the user who starts. This user has to be a member of the group in which the event is and has to be a participant of this event
	 * @param event the event which the user goes to
	 * @return true, if method was successful, otherwise false
	 */
	private boolean setGo(User user, Event event) {
		// TODO - implement GoService.setGo
		throw new UnsupportedOperationException();
	}

	/**
	 * finds all participants of an event who have started. This is used by the EventActivity to show which participants are on their way
	 * @param event the event all participants started to
	 * @return all user who started this event
	 */
	private List<User> getStartedParticipants(Event event) {
		// TODO - implement GoService.getStartedParticipants
		throw new UnsupportedOperationException();
	}

}