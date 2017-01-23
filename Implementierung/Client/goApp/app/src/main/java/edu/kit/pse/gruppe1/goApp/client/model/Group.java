package edu.kit.pse.gruppe1.goApp.client.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.*;

/**
 * A group is a composition of several users of the goApp, in which the users can create events.
 */
public class Group implements Parcelable{

	Collection<Event> events;
	Collection<Request> requests;
	/**
	 * The Id is used to identify each group and is therefore unique.
	 */
	private int id;
	/**
	 * The name of a group is given by the founder and can be changed.
	 */
	private String name;
    //TODO more attributes needed?
    //private Set<Event> events;
    //private Set<Request> requests;

	/**
	 * 
	 * @param id The Id of the group.
	 * @param name The name of the Group.
	 */
	public Group(int id, String name) {
        this.id = id;
        this.name = name;
	}


	protected Group(Parcel in) {
		id = in.readInt();
		name = in.readString();
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
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