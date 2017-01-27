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
@Table(name = "participantT", uniqueConstraints = @UniqueConstraint(columnNames = { "USER_ID",
    "EVENT_ID" }))
public class Participant {

  /**
   * the users status is either started or not started which shows if the user already departed to
   * meet other members at the events
   */
  private Integer participantID;
  private Integer status;
  private Event event;
  private User user;

  /**
   * Standard constructor
   */
  public Participant() {
  }

  /**
   * Constructor which initialize attributes.
   * 
   * @param status
   *          status of the participant
   * @param event
   *          event of the participant
   * @param user
   *          user of the participant
   */
  public Participant(Integer status, Event event, User user) {
  super();
  this.status = status;
  this.event = event;
  this.user = user;
  }

  /**
   * 
   * @return the id of the participant
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "PARTICIPANT_ID", unique = true, nullable = false)
  public Integer getParticipantID() {
  return participantID;
  }

  /**
   * 
   * @param participantID
   *          set the id of the participant
   */
  public void setParticipantID(Integer participantID) {
  this.participantID = participantID;
  }

  /**
   * 
   * @return the status of the participant
   */
  @Column(name = "status", nullable = false)
  public Integer getStatus() {
  return status;
  }

  /**
   * 
   * @param status
   *          the status of the participant
   */
  public void setStatus(Integer status) {
  this.status = status;
  }

  /**
   * 
   * @return the event on which the participant participate
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "EVENT_ID", nullable = false)
  public Event getEvent() {
  return event;
  }

  /**
   * 
   * @param event
   *          the event on which the participant participate
   */
  public void setEvent(Event event) {
  this.event = event;
  }

  /**
   * 
   * @return the user which participate on the event
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", nullable = false)
  public User getUser() {
  return user;
  }

  /**
   * 
   * @param user
   *          the user which participate on the event
   */
  public void setUser(User user) {
  this.user = user;
  }

}