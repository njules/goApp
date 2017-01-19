package edu.kit.pse.gruppe1.goApp.client.model;

/**
 * The location is used to display something on the Map, whatever it is an user or the center of the group.
 */
public class Location {

	/**
	 * The longitude value of the position.
	 */
	private double longitude;
	/**
	 * The latitude value of the position.
	 */
	private double latitude;
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
	public Location(double longitude, double latitude, String name) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.name = name;
	}

}