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
 * A request is created whenever a user wants to join a new group and deleted as
 * soon as the founder adds or rejects the user.
 */
@Entity
@Table(name = "requestT", uniqueConstraints = @UniqueConstraint(columnNames = { "USER_ID", "GROUP_ID" }))
public class Request {

	private Integer requestId;
	private User user;
	private Group group;

	public Request() {
	}

	/**
	 * 
	 * @param user
	 *            The user who creates the request.
	 * @param group
	 *            The group which the user wants to join.
	 */
	public Request(User user, Group group) {
		this.user = user;
		this.group = group;
		throw new UnsupportedOperationException();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "REQUEST_ID", unique = true, nullable = false)
	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUP_ID", nullable = false)
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}