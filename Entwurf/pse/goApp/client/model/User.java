package pse.goApp.client.model;

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
		// TODO - implement User.User
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the name of the user.
	 * @param name The new name of the user.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the user.
	 * @return The name of the user.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the Id of the user.
	 * @return The Id of the user.
	 */
	public int getId() {
		return this.id;
	}

}