package edu.kit.pse.gruppe1.goApp.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The location is used to display something on the Map, whatever it is an user or the center of the
 * group.
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
     * The name of a location helps the user to identify if the location is a center point of the
     * group or the original meeting point.
     */
    private String name;

    /**
     * standart constructor
     */
    public Location() {
    }

    /**
     * constructor
     * 
     * @param longitude
     *            the longitude
     * @param latitude
     *            the latitude
     * @param name
     *            the name of the location
     */
    public Location(double longitude, double latitude, String name) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }

    /**
     * 
     * @return the id of the location
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOCATION_ID", unique = true, nullable = false)
    public Integer getLocationId() {
        return locationId;
    }

    /**
     * 
     * @param locationId
     *            the new id of the location
     */
    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    /**
     * 
     * @return the longitude of the location
     */
    @Column(name = "longitude")
    public Double getLongitude() {
        return longitude;
    }

    /**
     * 
     * @param longitude
     *            the new longitude of the location
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * 
     * @return the latitude of the location
     */
    @Column(name = "latitude")
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     *            the new latitude of the location
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return the name of the location
     */
    @Column(name = "name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *            the new name of the location
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((locationId == null) ? 0 : locationId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Location other = (Location) obj;
        if (locationId == null) {
            if (other.locationId != null)
                return false;
        } else if (!locationId.equals(other.locationId))
            return false;
        return true;
    }

}