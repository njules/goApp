package edu.kit.pse.gruppe1.goApp.client.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

/**
 * An event is created by a user within a specific group.
 */
public class Event implements Parcelable {
    /**
     * The Id is used to identify each event and is therefore unique.
     */
    private int id;
    /**
     * The name of an event is given by the creator.
     */
    private String name;
    /**
     * The time of an event tells when the event is starting and set by the creator of the event.
     */
    private Timestamp time;
    private User creator;
    private Location location;

    /**
     * @param id       The Id of the event.
     * @param name     The name of the event.
     * @param time     The time of the event.
     * @param location The Location of the event.
     * @param creator  the User who created the event.
     */
    public Event(int id, String name, Timestamp time, Location location, User creator) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.location = location;
        this.creator = creator;
    }

    /**
     * @param in the Parcel that creates the Event.
     */
    private Event(Parcel in) {
        id = in.readInt();
        name = in.readString();
        time = new Timestamp(in.readLong());
        location = in.readParcelable(Location.class.getClassLoader());
        creator = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    /**
     * @return the Id of the Event.
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name of the Event.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the timestamp of the Event.
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * @return the User who created the Event.
     */
    public User getCreator() {
        return creator;
    }


    /**
     * @return the location of the Event.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param name the new name of the event.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param time the new timestamp of the Event.
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(id);
        out.writeString(name);
        out.writeLong(time.getTime());
        out.writeParcelable(location, i);
        out.writeParcelable(creator, i);
    }

}