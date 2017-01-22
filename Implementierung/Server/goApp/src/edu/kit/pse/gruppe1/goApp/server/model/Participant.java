package edu.kit.pse.gruppe1.goApp.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * the userEvent describes the status of a participant during the event.
 */
@Entity
@Table(name = "participantT", uniqueConstraints = @UniqueConstraint(columnNames = { "USER_ID", "EVENT_ID" }))
public class Participant {

	/**
	 * the users status is either started or not started which shows if the user
	 * already departed to meet other members at the events
	 */
	private Integer participantID;
	private Integer status;
	private Event event;
	private User user;

	public Participant() {
	}

	public Participant(Integer status, Event event, User user) {
		super();
		this.status = status;
		this.event = event;
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LOCATION_ID")
	public Integer getParticipantID() {
		return participantID;
	}

	public void setParticipantID(Integer participantID) {
		this.participantID = participantID;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVENT_ID")
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}