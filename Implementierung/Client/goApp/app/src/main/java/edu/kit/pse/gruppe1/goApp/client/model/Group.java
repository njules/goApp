package edu.kit.pse.gruppe1.goApp.client.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.*;

/**
 * A group is a composition of several users of the goApp, in which the users can create events.
 */
public class Group extends BaseObservable implements Parcelable {

    Collection<Event> events;
    Collection<Request> requests;

    /**
     * The User who founded the group.
     */
    private User founder;
    /**
     * The Id is used to identify each group and is therefore unique.
     */
    private int id;
    /**
     * The name of a group is given by the founder and can be changed.
     */
    private String name;

    /**
     * @param id   The Id of the Group.
     * @param name The name of the Group.
     * @param user The founder of the Group.
     */
    public Group(int id, String name, User user) {
        this.id = id;
        this.name = name;
        founder = user;
    }

    /**
     * @param in the Parcel that creates the Group.
     */
    Group(Parcel in) {
        id = in.readInt();
        name = in.readString();
        founder = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    /**
     * @return the name of the Group.
     */
    @Bindable
    public String getName() {
        return name;
    }

    /**
     * @param name the new name of the Group.
     */
    public void setName(String name) {
        this.name = name;
        notifyChange();
    }

    /**
     * @return the Id of the Group.
     */
    public int getId() {
        return id;
    }

    /**
     * @return the Founder of the Group.
     */
    public User getFounder() {
        return founder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(id);
        out.writeString(name);
        out.writeParcelable(founder, i);
    }

}