package pse.goApp.client.model;

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

	/**
	 * Sets the time of the event.
	 * @param time The new time of the event.
	 */
	public synchronized void setTime(Time time) {
		this.time = time;
	}

	/**
	 * Sets the name of the event.
	 * @param name The new name of the event.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the time of the event.
	 * @return The time of the event.
	 */
	public Time getTime() {
		return this.time;
	}

	/**
	 * Returns the name of the event.
	 * @return The name of the event.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the Id of the event
	 * @return The Id of the event.
	 */
	public int getId() {
		return this.id;
	}

}