package edu.kit.pse.gruppe1.goApp.client.model;

/**
 * A request is created whenever a user wants to join a new group and deleted as soon as the founder adds or rejects the user.
 */
public class Request {

	private final User user;
	private final Group group;

	/**
	 * 
	 * @param user The user who creates the request.
	 * @param group The group which the user wants to join.
	 */
	public Request(User user, Group group) {
		this.user = user;
		this.group = group;
		throw new UnsupportedOperationException();
	}

	public Group getGroup() {
		return group;
	}

	public User getUser() {
		return user;
	}
}