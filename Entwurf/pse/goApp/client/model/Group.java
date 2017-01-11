package pse.goApp.client.model;

import java.util.*;

/**
 * A group is a composition of several users of the goApp, in which the users can create events.
 */
public class Group {

	Collection<Event> events;
	Collection<Request> requests;
	/**
	 * The Id is used to identify each group and is therefore unique.
	 */
	private int id;
	/**
	 * The name of a group is given by the founder and can be changed.
	 */
	private String name;

	/**
	 * 
	 * @param id The Id of the group.
	 * @param name The name of the Group.
	 */
	public Group(int id, String name) {
		// TODO - implement Group.Group
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the name of the group.
	 * @param name The new name of the group.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the group.
	 * @return The name of the group.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the Id of the Group.
	 * @return The Id of the Group.
	 */
	public int getId() {
		return this.id;
	}

}