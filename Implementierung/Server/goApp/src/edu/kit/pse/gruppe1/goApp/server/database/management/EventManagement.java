package edu.kit.pse.gruppe1.goApp.server.database.management;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.Participant;
import edu.kit.pse.gruppe1.goApp.server.model.Status;
import edu.kit.pse.gruppe1.goApp.server.model.User;

/**
 * Management for Event Table
 */
public class EventManagement implements Management {

	/**
	 * creates new Group and adds new entry to table adds all User from Group to
	 * Event and sets Status
	 * 
	 * @param name
	 *            Name of Event
	 * @param location
	 *            Location, where Event takes place
	 * @param time
	 *            Date and Time, when Event takes place
	 * @param userId
	 *            ID of User who created the Event
	 * @param groupID
	 *            ID of Group to which Event is related to
	 */
	public Event add(String name, Location location, Date time, int creatorId, int groupId) {
		User creator = new UserManagement().getUser(creatorId);
		Group group = new GroupManagement().getGroup(groupId);
		if (creator == null || group == null) {
			return null;
		}
		Event event = new Event(name, location, time, group, creator);
		Set<Participant> participants = new HashSet<>(group.getUsers().size());
		for (User user : group.getUsers()) {
			participants.add(new Participant(Status.INVITED.getValue(), event, user));
		}
		event.setParticipants(participants);
		Session session = DatabaseInitializer.getFactory().getCurrentSession();
		session.beginTransaction();
		session.save(event);
		session.getTransaction().commit();
		return event;
	}

	/**
	 * updates entry in table
	 * 
	 * @param chEvent
	 *            Event with changes
	 * @return true, if update was successful, otherwise false
	 */
	public boolean update(Event chEvent) {
		if (chEvent.getEventId() == null) {
			return false;
		}
		Session session = DatabaseInitializer.getFactory().getCurrentSession();
		session.beginTransaction();
		session.update(chEvent);
		session.getTransaction().commit();
		return true;
	}

	/**
	 * updates entry with given id - sets new name
	 * 
	 * @param eventID
	 *            ID of entry to be updated
	 * @param name
	 *            new Name of Entry
	 * @return true, if update was successfull, otherwise false
	 */
	public boolean updateName(int eventId, String name) {
		Event event = getEvent(eventId);
		if (event == null) {
			return false;
		}
		event.setName(name);
		return update(event);
	}



	/**
	 * gets Event with given eventID
	 * 
	 * @param eventID
	 *            ID of event
	 * @return matching  Event
	 */
	public Event getEvent(int eventId) {
		Session session = DatabaseInitializer.getFactory().getCurrentSession();
		session.beginTransaction();
		Event event = (Event) session.get(Event.class, eventId);
		session.getTransaction().commit();
		return event;
	}

	/**
	 * get User who created Event
	 * 
	 * @param eventID
	 *            ID of entry
	 * @return ID of User
	 */
	public User getCreator(int eventId) {
		Event event = getEvent(eventId);
		if (event == null) {
			return null;
		}
		return event.getCreator();
	}

	@Override
	public boolean delete(int eventId) {
		Event event = getEvent(eventId);
		if (event == null) {
			return false;
		}
		Session session = DatabaseInitializer.getFactory().getCurrentSession();
		session.beginTransaction();
		session.delete(event);
		session.getTransaction().commit();
		return true;
	}
}