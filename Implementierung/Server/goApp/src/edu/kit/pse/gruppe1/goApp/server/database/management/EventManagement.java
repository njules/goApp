package edu.kit.pse.gruppe1.goApp.server.database.management;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
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
     * creates new Group and adds new entry to table adds all User from Group to Event and sets
     * Status
     * 
     * @param name
     *            Name of Event
     * @param location
     *            Location, where Event takes place
     * @param time
     *            Date and Time, when Event takes place
     * @param creatorId
     *            ID of User who created the Event
     * @param groupId
     *            ID of Group to which Event is related to
     */

    public Event add(String name, Location location, Timestamp time, int creatorId, int groupId) {
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

    public List<Location> getUserLocations(int eventId) {
        Event event = getEvent(eventId);
        if (event == null) {
            return null;
        }
        List<Location> locations = new ArrayList<>(event.getParticipants().size());
        for (Participant participant : event.getParticipants()) {
            locations.add(participant.getUser().getLocation());
        }
        return new ArrayList<Location>();
    }

    public boolean setClusterPoints(int eventId, Collection<Location> points) {
        Event event = getEvent(eventId);
        if (event == null) {
            return false;
        }
        event.setClusterPoints(new HashSet<Location>(points));
        return update(event);
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

    public void deleteOldEvents(long minutesTillDeletion) {
        long now = System.currentTimeMillis();
        long deletionTime = now - (minutesTillDeletion * 60L * 1000L);
        Timestamp time = new Timestamp(deletionTime);
        Query query = DatabaseInitializer.getFactory().openSession()
                .createQuery("delete from Event where time < :limit");
        query.setParameter("limit", time);
        query.executeUpdate();

    }
}