package edu.kit.pse.gruppe1.goApp.server.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * An event is created by a user within a specific group.
 */
@Entity
@Table(name = "event")
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
	


	public Event() {}


	public Event(Integer id, String name, Date time) {
		this.eventId = id;
		this.name = name;
		this.time = time;
	}


	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EVENT_ID")
	public Integer getId() {
		return eventId;
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


}