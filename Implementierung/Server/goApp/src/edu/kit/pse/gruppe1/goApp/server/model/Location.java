package edu.kit.pse.gruppe1.goApp.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The location is used to display something on the Map, whatever it is an user or the center of the group.
 */
@Entity
@Table(name = "locationT")
public class Location {
	
	
	private Integer locationId;

	/**
	 * The longitude value of the position.
	 */
	private Double longitude;
	/**
	 * The latitude value of the position.
	 */
	private Double latitude;
	/**
	 * The name of a location helps the user to identify if the location is a center point of the group or the original meeting point.
	 */
	private String name;

	public Location(){}
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
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LOCATION_ID")
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	@Column(name = "longitude")
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	@Column(name = "latitude")
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	

}