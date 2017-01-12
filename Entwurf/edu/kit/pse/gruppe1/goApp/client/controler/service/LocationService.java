package edu.kit.pse.gruppe1.goApp.client.controler.service;

import edu.kit.pse.gruppe1.goApp.client.model.*;

/**
 * This Service is in charge of synchronizing the Users and the Group Location.
 */
public class LocationService {

	/**
	 * sends the clients current location to the server and updates the group location of the event on the client. This method is started at the specific time and is performed periodically
	 * @param user the user who's location is updated
	 * @param event the event which's group locations are returned
	 * @return true, if method was successful, otherwise false
	 */
	private boolean syncLocation(User user, Event event) {
		// TODO - implement LocationService.syncLocation
		throw new UnsupportedOperationException();
	}

}