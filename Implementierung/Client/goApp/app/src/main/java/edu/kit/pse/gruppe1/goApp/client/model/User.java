package edu.kit.pse.gruppe1.goApp.client.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * An user describes an user of the goApp.
 */
public class User extends BaseObservable implements Parcelable {

    //TODO more attributes?
    /**
     * The Id is used to identify each user and is therefore unique.
     */
    private int id;
    /**
     * The name of an user is selectable by the user and can also be changed.
     */
    private String name;

    /**
     * @param id   The Id of the user.
     * @param name The name of the user.
     */
    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected User(Parcel in) {
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

    public int getId() {
        return id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyChange();
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