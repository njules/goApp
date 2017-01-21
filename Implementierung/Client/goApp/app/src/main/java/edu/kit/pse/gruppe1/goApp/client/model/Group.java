package edu.kit.pse.gruppe1.goApp.client.model;

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
        this.id = id;
        this.name = name;
		//throw new UnsupportedOperationException();
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}
}