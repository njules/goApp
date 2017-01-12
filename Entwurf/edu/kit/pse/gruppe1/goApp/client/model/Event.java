package edu.kit.pse.gruppe1.goApp.client.model;

/**
 * An event is created by a user within a specific group.
 */
public class Event {

	/**
	 * The Id is used to identify each event and is therefore unique.
	 */
	private int id;
	/**
	 * The name of an event is given by the creator.
	 */
	private String name;
	/**
	 * The time of an event tells when the event is starting and set by the creator of the event.
	 */
	private Time time;

	/**
	 * 
	 * @param id The Id of the event.
	 * @param name The name of the event.
	 * @param time The time of the event.
	 */
	public Event(int id, String name, Time time) {
		// TODO - implement Event.Event
		throw new UnsupportedOperationException();
	}

}