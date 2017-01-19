package edu.kit.pse.gruppe1.goApp.client.model;

import java.sql.Date;

/**
 * An event is created by a user within a specific group.
 */
public class Event {
    /**
     * The name of an event is given by the creator.
     */
    private String name;
    /**
     * The Id is used to identify each event and is therefore unique.
     */
    private int id;
    /**
     * The time of an event tells when the event is starting and set by the creator of the event.
     */
    private Date time;

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
        throw new UnsupportedOperationException();
    }

}