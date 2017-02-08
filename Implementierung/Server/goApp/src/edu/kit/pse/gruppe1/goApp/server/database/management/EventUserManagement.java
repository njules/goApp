package edu.kit.pse.gruppe1.goApp.server.database.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import edu.kit.pse.gruppe1.goApp.server.model.*;

/**
 * Manages Event-User Table
 */
public class EventUserManagement implements Management {

    private Participant getParticipant(int participantId) {
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        Participant participant = (Participant) session.get(Participant.class, participantId);
        session.getTransaction().commit();
        return participant;
    }

    public Participant getParticipant(int eventId, int userId) {
        Event event = new EventManagement().getEvent(eventId);
        if (event == null) {
            return null;
        }
        return event.getParticipant(userId);
    }

    public List<Participant> getParticipants(int eventId) {
        Event event = new EventManagement().getEvent(eventId);
        if (event == null) {
            return null;
        }
        return new ArrayList<>(event.getParticipants());
    }

    /**
     * adds new entry to table
     * 
     * @param eventId
     *            eventID to combine with user
     * @param userId
     *            userID to combine with Event
     */
    public boolean addUser(int eventId, int userId, Status status) {
        Event event = new EventManagement().getEvent(eventId);
        User user = new UserManagement().getUser(userId);
        if (event == null || user == null) {
            return false;
        }
        Participant participant = new Participant(status.getValue(), event, user);
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        session.save(participant);
        session.getTransaction().commit();
        return true;
    }

    /**
     * updates Status of given user and eventID
     * 
     * @param eventID
     *            eventID to upate
     * @param userID
     *            userID to update
     * @param newStatus
     *            new Status to set
     * @return true, if update was successfull, otherwise false
     */
    public boolean updateStatus(int eventId, int userId, Status newStatus) {
        Participant participant = getParticipant(eventId, userId);
        if (participant == null) {
            return false;
        }
        participant.setStatus(newStatus.getValue());
        return update(participant);
    }

    private boolean update(Participant chParticipant) {
        if (chParticipant.getParticipantID() == null) {
            return false;
        }
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        session.update(chParticipant);
        session.getTransaction().commit();
        return true;
    }

    /**
     * removes entry (with userID and eventID)
     * 
     * @param eventID
     *            eventID to remove
     * @param userID
     *            userID to remove
     * @return true, if update was successfull, otherwise false
     */
    public boolean delete(int eventId, int userId) {
        Participant participant = getParticipant(eventId, userId);
        return delete(participant);
    }

    /**
     * get all events in which given user participates
     * 
     * @param userID
     *            ID of user
     * @return List of matching Events
     */
    public List<Event> getEvents(int userId) {
        User user = new UserManagement().getUser(userId);
        if (user == null) {
            return null;
        }
        Set<Participant> participations = user.getParticipations();
        List<Event> events = new ArrayList<>(participations.size());
        for (Participant participation : participations) {
            events.add(participation.getEvent());
        }
        return events;
    }

    /**
     * get all users which participate in given Event
     * 
     * @param eventID
     *            ID of event
     * @return List of matching User
     */
    public List<User> getUsers(int eventId) {
        Event event = new EventManagement().getEvent(eventId);
        if (event == null) {
            return null;
        }
        Set<Participant> participants = event.getParticipants();
        List<User> users = new ArrayList<>(participants.size());
        for (Participant participant : participants) {
            users.add(participant.getUser());
        }
        return users;
    }

    /**
     * get all User with given Status from an Event
     * 
     * @param status
     *            status to search for
     * @param eventID
     *            ID of event to search for
     * @return List of matching Users
     */
    public List<User> getUserByStatus(Status status, int eventId) {
        Event event = new EventManagement().getEvent(eventId);
        if (event == null) {
            return null;
        }
        Set<Participant> participants = event.getParticipants();
        List<User> users = new ArrayList<>();
        for (Participant participant : participants) {
            if (Status.fromInteger(participant.getStatus()) == status) {
                users.add(participant.getUser());
            }
        }
        return users;
    }

    public List<Event> getEventsByStatus(Status status, int groupId, int userId) {
        Group group = new GroupManagement().getGroup(groupId);
        if (group == null) {
            return null;
        }
        List<Event> events = new ArrayList<>();

        for (Event event : group.getEvents()) {
            if (Status.fromInteger(event.getParticipant(userId).getStatus()) == status) {
                events.add(event);
            }
        }
        return events;
    }

    @Override
    public boolean delete(int participantId) {
        Participant participant = getParticipant(participantId);
        return delete(participant);
    }

    private boolean delete(Participant participant) {
        if (participant == null) {
            return false;
        }
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        session.delete(participant);
        session.getTransaction().commit();
        return true;
    }
}