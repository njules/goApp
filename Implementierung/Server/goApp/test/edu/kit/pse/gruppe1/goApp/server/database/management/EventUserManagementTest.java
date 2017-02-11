package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.Participant;
import edu.kit.pse.gruppe1.goApp.server.model.Status;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public class EventUserManagementTest {
    private Event createdEvent;
    private String eventName = "event name";
    private String userName = "user name";
    private Location location;
    private User createdUser;
    private Group group;
    private Timestamp timestamp;
    private int googleId;

    @Before
    public void setUp() {
        location = new Location(0, 0, "location");
        createdUser = new UserManagement().add(userName, googleId);
        assertThat(createdUser, is(notNullValue()));
        group = new GroupManagement().add("group", createdUser.getUserId());
        assertThat(group, is(notNullValue()));
        timestamp = new Timestamp(0);
        createdEvent = new EventManagement().add(eventName, location, timestamp,
                createdUser.getUserId(), group.getGroupId());
        assertThat(new EventManagement().getEvent(createdEvent.getEventId()), is(notNullValue()));

    }

    @After
    public void tearDown() {
        new EventManagement().delete(createdEvent.getEventId());
        new GroupManagement().delete(group.getGroupId());
        new UserManagement().delete(createdUser.getUserId());
    }

    @Test
    public void testAddUser() {
        User userTmp = new UserManagement().add(userName + "usertmp", googleId + 1);
        assertThat(new EventUserManagement().addUser(createdEvent.getEventId(), userTmp.getUserId(),
                Status.INVITED), is(true));
        assertThat(new EventManagement().getEvent(createdEvent.getEventId())
                .getParticipant(userTmp.getUserId()), is(notNullValue()));
        assertThat(new UserManagement().getUser(userTmp.getUserId()).getParticipations().size(),
                is(1));
        new UserManagement().delete(userTmp.getUserId());
    }

    @Test
    public void testUpdateStatus() {
        Status newStatus = Status.STARTED;
        assertThat(new EventUserManagement().getEvents(createdUser.getUserId()).size(), is(1));
        assertThat(new EventUserManagement()
                .getParticipant(createdEvent.getEventId(), createdUser.getUserId()).getStatus(),
                is(Status.PARTICIPATE.getValue()));
        assertThat(new EventUserManagement().updateStatus(createdEvent.getEventId(),
                createdUser.getUserId(), newStatus), is(true));

        assertThat(new EventUserManagement().getEvents(createdUser.getUserId()).size(), is(1));
        assertThat(new EventUserManagement()
                .getParticipant(createdEvent.getEventId(), createdUser.getUserId()).getStatus(),
                is(newStatus.getValue()));
    }

    @Test
    public void testGetParticipant() {
        Participant participant = new EventUserManagement()
                .getParticipant(createdEvent.getEventId(), createdUser.getUserId());
        assertThat(participant, is(notNullValue()));
    }

    @Test
    public void testGetParticipants() {
        List<Participant> participants = new EventUserManagement()
                .getParticipants(createdEvent.getEventId());
        assertThat(participants, is(notNullValue()));
        assertThat(participants.size(), is(1));
        assertThat(participants.get(0).getEvent().getEventId(), is(createdEvent.getEventId()));
        assertThat(participants.get(0).getUser().getUserId(), is(createdUser.getUserId()));
    }

    @Test
    public void testDeleteParamsEventIdUserId() {
        assertThat(new EventUserManagement().getUsers(createdEvent.getEventId()).size(), is(1));
        assertThat(new EventUserManagement().getEvents(createdUser.getUserId()).size(), is(1));

        assertThat(new EventUserManagement().delete(createdEvent.getEventId(),
                createdUser.getUserId()), is(true));
        assertThat(new EventUserManagement().getParticipant(createdEvent.getEventId(),
                createdUser.getUserId()), is(nullValue()));
        assertThat(new EventManagement().getEvent(createdEvent.getEventId()), is(notNullValue()));
        assertThat(new UserManagement().getUser(createdUser.getUserId()), is(notNullValue()));
        assertThat(new GroupManagement().getGroup(group.getGroupId()), is(notNullValue()));

        assertThat(new EventUserManagement().getUsers(createdEvent.getEventId()).size(), is(0));
        assertThat(new EventUserManagement().getEvents(createdUser.getUserId()).size(), is(0));
    }

    @Test
    public void testDeleteParamsParticipantId() {
        assertThat(new EventUserManagement().getUsers(createdEvent.getEventId()).size(), is(1));
        assertThat(new EventUserManagement().getEvents(createdUser.getUserId()).size(), is(1));

        assertThat(
                new EventUserManagement().delete(
                        createdEvent.getParticipant(createdUser.getUserId()).getParticipantID()),
                is(true));
        assertThat(new EventUserManagement().getParticipant(createdEvent.getEventId(),
                createdUser.getUserId()), is(nullValue()));
        assertThat(new EventManagement().getEvent(createdEvent.getEventId()), is(notNullValue()));
        assertThat(new UserManagement().getUser(createdUser.getUserId()), is(notNullValue()));
        assertThat(new GroupManagement().getGroup(group.getGroupId()), is(notNullValue()));
        assertThat(new EventUserManagement().getUsers(createdEvent.getEventId()).size(), is(0));
        assertThat(new EventUserManagement().getEvents(createdUser.getUserId()).size(), is(0));
    }

    @Test
    public void testGetUsers() {
        List<User> users = new EventUserManagement().getUsers(createdEvent.getEventId());
        assertThat(users, is(notNullValue()));
        assertThat(users.size(), is(1));
        assertThat(users.get(0).getUserId(), is(createdUser.getUserId()));
    }

    @Test
    public void testGetUserByStatus() {
        User userTmp = new UserManagement().add(userName + "usertmp", googleId + 1);
        assertThat(new EventUserManagement().addUser(createdEvent.getEventId(), userTmp.getUserId(),
                Status.STARTED), is(true));
        assertThat(new UserManagement().getUser(userTmp.getUserId()), is(notNullValue()));

        List<User> users = new EventUserManagement().getUserByStatus(Status.PARTICIPATE,
                createdEvent.getEventId());
        assertThat(users, is(notNullValue()));
        assertThat(users.size(), is(1));
        assertThat(users.get(0).getUserId(), is(createdUser.getUserId()));

        users = new EventUserManagement().getUserByStatus(Status.STARTED,
                createdEvent.getEventId());
        assertThat(users, is(notNullValue()));
        assertThat(users.size(), is(1));
        assertThat(users.get(0).getUserId(), is(userTmp.getUserId()));

        new UserManagement().delete(userTmp.getUserId());
    }

    @Test
    public void testGetEventsByStatus() {
        Event eventTmp = new EventManagement().add(eventName + "eventTmp",
                new Location(0, 0, "locationTmp"), new Timestamp(0), createdUser.getUserId(),
                group.getGroupId());
        assertThat(new EventUserManagement().updateStatus(eventTmp.getEventId(),
                createdUser.getUserId(), Status.STARTED), is(true));
        assertThat(new EventManagement().getEvent(eventTmp.getEventId()), is(notNullValue()));

        List<Event> events = new EventUserManagement().getEventsByStatus(Status.INVITED,
                group.getGroupId(), createdUser.getUserId());
        assertThat(events, is(notNullValue()));
        assertThat(events.size(), is(0));

        events = new EventUserManagement().getEventsByStatus(Status.PARTICIPATE, group.getGroupId(),
                createdUser.getUserId());
        assertThat(events, is(notNullValue()));
        assertThat(events.size(), is(1));
        assertThat(events.get(0).getEventId(), is(createdEvent.getEventId()));

        events = new EventUserManagement().getEventsByStatus(Status.STARTED, group.getGroupId(),
                createdUser.getUserId());
        assertThat(events, is(notNullValue()));
        assertThat(events.size(), is(1));
        assertThat(events.get(0).getEventId(), is(eventTmp.getEventId()));

        new EventManagement().delete(eventTmp.getEventId());
    }

    @Test
    public void testGetEvents() {
        List<Event> events = new EventUserManagement().getEvents(createdUser.getUserId());
        assertThat(events, is(notNullValue()));
        assertThat(events.size(), is(1));
        assertThat(events.get(0).getEventId(), is(createdEvent.getEventId()));
    }

}
