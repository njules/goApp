package pse.goApp.client.model;

/**
 * the userEvent describes the status of a participant during the event.
 */
public class UserEvent {

	/**
	 * the users status is either started or not started which shows if the user already departed to meet other members at the events
	 */
	private boolean started;

	/**
	 * Sets the status of the user in context to this event.
	 * @param started The new status of the user.
	 */
	public void setStarted(boolean started) {
		this.started = started;
	}

	/**
	 * Returns the status of the user.
	 * @return The status of the user.
	 */
	public boolean getStarted() {
		return this.started;
	}

}