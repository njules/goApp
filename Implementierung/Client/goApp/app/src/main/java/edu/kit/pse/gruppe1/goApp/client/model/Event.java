package edu.kit.pse.gruppe1.goApp.client.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.sql.Time;
import java.util.Set;

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

    public Date getTime() {
        return time;
    }

    /**
     * The time of an event tells when the event is starting and set by the creator of the event.
     */
    private Date time;

    private Group group;
    private User creator;
    private Set<Participant> participants;
    private Set<Location> clusterPoints;
    private Location location;

    public User getCreator() {
        return creator;
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public Set<Location> getClusterPoints() {
        return clusterPoints;
    }

    public Location getLocation() {
        return location;
    }

    protected Event(Parcel in) {
        id = in.readInt();
        name = in.readString();
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(Date time) {
        this.time = time;
    }


    /**
     * @param id   The Id of the event.
     * @param name The name of the event.
     * @param time The time of the event.
     */
    public Event(int id, String name, Date time) {
        this.id = id;
        this.name = name;
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
    }
}