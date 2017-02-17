package edu.kit.pse.gruppe1.goApp.client.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The location is used to display something on the Map, whatever it is an user or the center of the group.
 */
public class Location implements Parcelable {
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
     * @param longitude the longitude of the Location.
     * @param latitude  the latitude of the Location.
     * @param name      the name of the Location.
     */
    public Location(double longitude, double latitude, String name) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }

    /**
     * @param in the Parcel that creates the Location.
     */
    protected Location(Parcel in) {
        longitude = in.readDouble();
        latitude = in.readDouble();
        name = in.readString();
    }

    /**
     * @return the longitude of the Location.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return the latitude of the Location.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return the name of the Location.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the new name of the Location.
     */
    public void setName(String name) {
        this.name = name;
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeDouble(longitude);
        out.writeDouble(latitude);
        out.writeString(name);
    }
}