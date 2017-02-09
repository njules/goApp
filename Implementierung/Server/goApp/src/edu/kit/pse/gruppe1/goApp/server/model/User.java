package edu.kit.pse.gruppe1.goApp.server.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * An user describes an user of the goApp.
 */
@Entity
@Table(name = "userT", uniqueConstraints = @UniqueConstraint(columnNames = { "GOOGLE_ID" }))
public class User {

    /**
     * The Id is used to identify each user and is therefore unique.
     */
    private Integer userId;
    private String googleId;
    /**
     * The name of an user is selectable by the user and can also be changed.
     */
    private String name;
    private Location location;
    private Set<Request> requests;
    private Set<Group> groups;
    private Set<Group> foundedGroups;
    private Set<Event> createdEvents;
    private Set<Participant> participations;

    public User() {
    }

    /**
     * 
     * @param id
     *            The Id of the user.
     * @param name
     *            The name of the user.
     */
    public User(String googleId, String name) {
        this.googleId = googleId;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", unique = true, nullable = false)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "GOOGLE_ID", nullable = false)
    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "founder")
    public Set<Group> getFoundedGroups() {
        return foundedGroups;
    }

    public void setFoundedGroups(Set<Group> foundedGroups) {
        this.foundedGroups = foundedGroups;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    public Set<Event> getCreatedEvents() {
        return createdEvents;
    }

    public void setCreatedEvents(Set<Event> createdEvents) {
        this.createdEvents = createdEvents;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    public Set<Participant> getParticipations() {
        return participations;
    }

    public void setParticipations(Set<Participant> participations) {
        this.participations = participations;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LOCATION_ID")
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }
}