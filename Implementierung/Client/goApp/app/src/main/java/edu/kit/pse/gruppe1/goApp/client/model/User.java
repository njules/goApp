package edu.kit.pse.gruppe1.goApp.client.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * An user describes an user of the goApp.
 */
public class User extends BaseObservable implements Parcelable {

    /**
     * The Id is used to identify each user and is therefore unique.
     */
    private int id;
    /**
     * The name of an user is selectable by the user and can also be changed.
     */
    private String name;

    private Status status;

    private Location location;

    /**
     * @param id   The Id of the user.
     * @param name The name of the user.
     */
    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @param in the Parcel that creates the User.
     */
    User(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     * @return the Id of the User.
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name of the User.
     */
    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyChange();
    }

    /**
     * @return the Location of the User.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location the new location of the User.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return the Status of the User.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the new Status of the User.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(id);
        out.writeString(name);
    }
}