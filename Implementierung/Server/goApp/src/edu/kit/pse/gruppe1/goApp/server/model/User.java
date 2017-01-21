package edu.kit.pse.gruppe1.goApp.server.model;

/**
 * An user describes an user of the goApp.
 */
public class User {

	/**
	 * The Id is used to identify each user and is therefore unique.
	 */
	private int id;
	/**
	 * The name of an user is selectable by the user and can also be changed.
	 */
	private String name;

	/**
	 * 
	 * @param id The Id of the user.
	 * @param name The name of the user.
	 */
	public User(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}