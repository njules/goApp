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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * A group is a composition of several users of the goApp, in which the users
 * can create events.
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

	public Group() {
	}

	/**
	 * 
	 * @param name
	 *            The name of the group.
	 * @param founder
	 *            The founder of the Group.
	 */
	public Group(String name, User founder) {
		this.name = name;
		this.founder = founder;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GROUP_ID")
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "group")
	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "group")
	public Set<Request> getRequests() {
		return requests;
	}

	public void setRequests(Set<Request> requests) {
		this.requests = requests;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public User getFounder() {
		return founder;
	}

	public void setFounder(User founder) {
		this.founder = founder;
	}
}