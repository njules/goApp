package edu.kit.pse.gruppe1.goApp.server.model;

import java.sql.Date;
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
	 * The time of an event tells when the event is starting and set by the
	 * creator of the event.
	 */
	private Date time;

	private Group group;
	private User creator;
	private Set<Participant> participants;
	private Set<Location> clusterPoints;
	private Location location;

	public Event() {
	}

	public Event(String name, Location location, Date time, Group group, User creator) {
		this.name = name;
		this.location = location;
		this.time = time;
		this.group = group;
		this.creator = creator;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EVENT_ID")
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "time")
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUP_ID")
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}

	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "event")
	public Set<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(Set<Participant> participants) {
		this.participants = participants;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "cluster_points")
	public Set<Location> getClusterPoints() {
		return clusterPoints;
	}

	public void setClusterPoints(Set<Location> clusterPoints) {
		this.clusterPoints = clusterPoints;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="LOCATION_ID")
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}	
}