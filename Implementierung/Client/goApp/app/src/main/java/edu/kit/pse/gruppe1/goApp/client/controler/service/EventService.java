package edu.kit.pse.gruppe1.goApp.client.controler.service;

import java.sql.Date;

import edu.kit.pse.gruppe1.goApp.client.model.*;

/**
 * This Service provides methods to handle a single Event
 */
public class EventService {

	/**
	 * creates an event
	 * @param name name of the group chosen freely by a member of the group
	 * @param destination the location which the user choose on a map to let his event take place there (coordinates) and gave it a name (string)
	 * @param eventAdmin the user who creates the event and is a group member
	 * @param time the time when the event is going to take place. This time can not be in the past
	 * @param group the group in which the event is created and which members are all invited
	 * @return true, if method was successful, otherwise false
	 */
	private boolean create(String name, Location destination, User eventAdmin, Date time, Group group) {
		// TODO - implement EventService.create
		throw new UnsupportedOperationException();
	}

	/**
	 * gets the event from the server database
	 * @param eventID the id to identify which event to get
	 * @return an event object with attributes
	 */
	private Event getEvent(int eventID) {
		// TODO - implement EventService.getEvent
		throw new UnsupportedOperationException();
	}

	/**
	 * if the event admin whishes to change the event. Only time, destination and name can be changed
	 * @param event the new instance of the event with the wanted attributes changed which will replace the old event
	 * @return true, if method was successful, otherwise false
	 */
	private boolean change(Event event) {
		// TODO - implement EventService.change
		throw new UnsupportedOperationException();
	}

}