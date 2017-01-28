package edu.kit.pse.gruppe1.goApp.server.model;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * An event is created by a user within a specific group.
 */
@Entity
@Table(name = "eventT")
public class Event {

    /**
     * The Id is used to identify each event and is therefore unique.
     */
    private Integer eventId;

    /**
     * The name of an event is given by the creator.
     */
    private String name;

    /**
     * The time of an event tells when the event is starting and set by the creator of the event.
     */
    private Timestamp time;

    private Group group;
    private User creator;
    private Set<Participant> participants;
    private Set<Location> clusterPoints;
    private Location location;

    /**
     * Standard constructor
     */
    public Event() {
    }

    /**
     * Constructor which initialize attributes.
     * 
     * @param name
     *            the name of the Event
     * @param location
     *            the location of the Event
     * @param time
     *            the time of the Event
     * @param group
     *            the group in which the Event is created
     * @param creator
     *            the user who created the Event
     */
    public Event(String name, Location location, Timestamp time, Group group, User creator) {
        this.name = name;
        this.location = location;
        this.time = time;
        this.group = group;
        this.creator = creator;
    }

    /**
     * 
     * @return the id of the Event
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID", unique = true, nullable = false)
    public Integer getEventId() {
        return eventId;
    }

    /**
     * 
     * @param eventId
     *            the id of the Event
     */
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    /**
     * 
     * @return the name of the Event
     */
    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name of the Event
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the time when the Event starts
     */
    @Column(name = "time", nullable = false)
    public Timestamp getTime() {
        return time;
    }

    /**
     * 
     * @param time
     *            the time when the Event starts
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }

    /**
     * 
     * @return the group in which the Event was created
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID", nullable = false)
    public Group getGroup() {
        return group;
    }

    /**
     * 
     * @param group
     *            the group in which the Event was created
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * 
     * @return the user who created the Event
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    public User getCreator() {
        return creator;
    }

    /**
     * 
     * @param creator
     *            the user who created the Event
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 
     * @return all participants of the Event
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "event")
    public Set<Participant> getParticipants() {
        return participants;
    }

    /**
     * 
     * @param participants
     *            all participants of the Event
     */
    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }

    /**
     * 
     * @return the cluster-points of the Event
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cluster_points")
    public Set<Location> getClusterPoints() {
        return clusterPoints;
    }

    /**
     * 
     * @param clusterPoints
     *            the cluster-points of the Event
     */
    public void setClusterPoints(Set<Location> clusterPoints) {
        this.clusterPoints = clusterPoints;
    }

    /**
     * 
     * @return the location of the Event
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LOCATION_ID", nullable = false)
    public Location getLocation() {
        return location;
    }

    /**
     * 
     * @param location
     *            the location of the Event
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * 
     * @param userId
     *            the userId of the participant which should be returned
     * @return null if no user with the given userId participate and otherwise the participant which
     *         should be returned
     */
    public Participant getParticipant(Integer userId) {
        for (Participant participant : participants) {
            if (participant.getUser().getUserId().equals(userId)) {
                return participant;
            }
        }
        return null;
    }

}