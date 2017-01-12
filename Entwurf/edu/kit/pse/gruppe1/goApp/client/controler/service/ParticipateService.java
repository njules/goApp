package edu.kit.pse.gruppe1.goApp.client.controler.service;

import edu.kit.pse.gruppe1.goApp.client.model.*;

/**
 * This Service provides methods to handle a users reaction towards an event
 */
public class ParticipateService {

	/**
	 * The user is added to the event as a participant this includes starting a Timer for the Notification Broadcast. This method is used if the user wants to participate and enters this decision in the GroupActivity
	 * @param event the event which the user want to participate in. User and event have to be in the same group
	 * @param user the user who wants to participate and is in the same group as the event
	 * @return true, if method was successful, otherwise false
	 */
	private boolean accept(Event event, User user) {
		// TODO - implement ParticipateService.accept
		throw new UnsupportedOperationException();
	}

	/**
	 * the connection between the user and the event is deleted
	 * @param event the event which the user doesn't want to join
	 * @param user the user who doesn't want to participate
	 * @return true, if method was successful, otherwise false
	 */
	private boolean reject(Event event, User user) {
		// TODO - implement ParticipateService.reject
		throw new UnsupportedOperationException();
	}

}