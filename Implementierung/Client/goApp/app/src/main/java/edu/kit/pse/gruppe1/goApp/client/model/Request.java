package edu.kit.pse.gruppe1.goApp.client.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A request is created whenever a user wants to join a new group and deleted as soon as the founder adds or rejects the user.
 */
public class Request implements Parcelable {

    private final User user;
    private final Group group;

    /**
     * @param user  The user who creates the request.
     * @param group The group which the user wants to join.
     */
    public Request(User user, Group group) {
        this.user = user;
        this.group = group;
        throw new UnsupportedOperationException();
    }

    public Group getGroup() {
        return group;
    }

    public User getUser() {
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeParcelable(user, 0);
        out.writeParcelable(group, 0);

    }
}