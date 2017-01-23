package edu.kit.pse.gruppe1.goApp.server.database.management;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import edu.kit.pse.gruppe1.goApp.server.model.*;

/**
 * Manages Request Table
 */
public class RequestManagement implements Management {
	private Request getRequest(int requestId) {
		Session session = DatabaseInitializer.getFactory().getCurrentSession();
		session.beginTransaction();
		Request request = (Request) session.get(Request.class, requestId);
		session.getTransaction().commit();
		return request;
	}

	private Request getRequest(int groupId, int userId) {
		Group group = new GroupManagement().getGroup(groupId);
		if (group == null) {
			return null;
		}
		return group.getRequest(userId);
	}

	/**
	 * creates new entry
	 * 
	 * @param groupID
	 *            groupID to combine with user
	 * @param userID
	 *            userID to combine with group
	 * @return ID of new entry
	 */
	public boolean add(int groupId, int userId) {
		Group group = new GroupManagement().getGroup(groupId);
		User user = new UserManagement().getUser(userId);
		if (group == null || user == null) {
			return false;
		}
		Request request = new Request(user, group);
		Session session = DatabaseInitializer.getFactory().getCurrentSession();
		session.beginTransaction();
		session.save(request);
		session.getTransaction().commit();
		return true;
	}

	/**
	 * get all User to given Gruop
	 * 
	 * @param gruopID
	 *            ID of group
	 * @return List of matching Users
	 */
	public List<User> getRequestByGroup(int groupId) {
		Group group = new GroupManagement().getGroup(groupId);
		if (group == null) {
			return null;
		}
		List<User> users = new ArrayList<>(group.getRequests().size());
		for (Request request : group.getRequests()) {
			users.add(request.getUser());
		}
		return users;
	}

	/**
	 * get all groups to given User
	 * 
	 * @param userID
	 *            ID of User
	 * @return List of matching Groups
	 */
	public List<Group> getRequestByUser(int userId) {
		User user = new UserManagement().getUser(userId);
		if (user == null) {
			return null;
		}
		List<Group> groups = new ArrayList<>(user.getRequests().size());
		for (Request request : user.getRequests()) {
			groups.add(request.getGroup());
		}
		return groups;
	}

	/**
	 * delete entry with given groupID and userID
	 * 
	 * @param groupID
	 * @param userID
	 * @return true, if deletion was successful, otherwise false
	 */
	public boolean delete(int groupId, int userId) {
		Request request = getRequest(groupId, userId);
		return delete(request);
	}

	@Override
	public boolean delete(int requestId) {
		Request request = getRequest(requestId);
		return delete(request);
	}

	private boolean delete(Request request) {
		if (request == null) {
			return false;
		}
		Session session = DatabaseInitializer.getFactory().getCurrentSession();
		session.beginTransaction();
		session.delete(request);
		session.getTransaction().commit();
		return true;
	}

}