package pse.goApp.client.model;

/**
 * The location is used to display something on the Map, whatever it is an user or the center of the group.
 */
public class Location {

	/**
	 * The longitude value of the position.
	 */
	private int longitude;
	/**
	 * The latitude value of the position.
	 */
	private int latitude;
	/**
	 * The name of a location helps the user to identify if the location is a center point of the group or the original meeting point.
	 */
	private String name;

	/**
	 * 
	 * @param longitude
	 * @param latitude
	 * @param name
	 */
	public Location(int longitude, int latitude, String name) {
		// TODO - implement Location.Location
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the name of a location.
	 * @param name The new name of the location.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the latitude of the location.
	 * @param latitude The new latitude value.
	 */
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	/**
	 * Sets the longitude value of the location.
	 * @param longitude The new longitude value.
	 */
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	/**
	 * Returns the name of the location.
	 * @return The name of the location.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the latitude value of the location
	 * @return The latitude value of the location.
	 */
	public int getLatitude() {
		return this.latitude;
	}

	/**
	 * Returns the longitude value of the location.
	 * @return The longitude value of the location.
	 */
	public int getLongitude() {
		return this.longitude;
	}

}