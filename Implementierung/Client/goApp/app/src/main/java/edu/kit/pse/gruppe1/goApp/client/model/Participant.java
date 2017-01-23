package edu.kit.pse.gruppe1.goApp.client.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * the userEvent describes the status of a participant during the event.
 */
public class Participant implements Parcelable {

    /**
     * the users status is either started or not started which shows if the user already departed to meet other members at the events
     */
    private int status;

    protected Participant(Parcel in) {
        status = in.readInt();
    }

    public static final Creator<Participant> CREATOR = new Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel in) {
            return new Participant(in);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(status);
    }
}