package edu.kit.pse.gruppe1.goApp.server.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * A group is a composition of several users of the goApp, in which the users can create events.
 */
@Entity
@Table(name = "groupT")
public class Group {
  /**
   * The Id is used to identify each group and is therefore unique.
   */
  private Integer groupId;
  private User founder;
  private Set<Event> events;
  private Set<Request> requests;
  private Set<User> users;
  /**
   * The name of a group is given by the founder and can be changed.
   */
  private String name;

  /**
   * Standard constructor
   */
  public Group() {
  }

  /**
   * 
   * @param name
   *          The name of the group.
   * @param founder
   *          The founder of the group.
   */
  public Group(String name, User founder) {
    this.name = name;
    this.founder = founder;
    this.users=new HashSet<>();
    this.users.add(founder);
  }

  /**
   * @return the id of the group
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "GROUP_ID")
  public Integer getGroupId() {
    return groupId;
  }

  /**
   * @param groupId
   *          the id of the group
   */
  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  /**
   * @return the name of the group
   */
  @Column(name = "name")
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name of the group
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the events which belongs to the group
   */
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "group")
  public Set<Event> getEvents() {
    return events;
  }

  /**
   * @param events
   *          the events which belongs to the group
   */
  public void setEvents(Set<Event> events) {
    this.events = events;
  }

  /**
   * @return the requests which belongs to the group
   */
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "group")
  public Set<Request> getRequests() {
    return requests;
  }

  /**
   * @param requests
   *          the requests which belongs to the group
   */
  public void setRequests(Set<Request> requests) {
    this.requests = requests;
  }

  /**
   * @return all member of the group
   */
  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
  public Set<User> getUsers() {
    return users;
  }

  /**
   * @param users
   *          all member of the group
   */
  public void setUsers(Set<User> users) {
    this.users = users;
  }

  /**
   * @return the founder of the group
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  public User getFounder() {
    return founder;
  }

  /**
   * @param founder
   *          the founder of the group
   */
  public void setFounder(User founder) {
    this.founder = founder;
  }

  /**
   * @param userId
   *          the userId of the request
   * @return the request with the given userId
   */
  public Request getRequest(Integer userId) {
    for (Request request : requests) {
      if (request.getUser().getUserId().equals(userId)) {
        return request;
      }
    }
    return null;
  }
}