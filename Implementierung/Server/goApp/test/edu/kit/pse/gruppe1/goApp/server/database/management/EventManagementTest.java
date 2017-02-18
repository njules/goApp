package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public class EventManagementTest {
    private Event createdEvent;
    private String eventName = "event Name";
    private Location location;
    private User user;
    private Group group;
    private Timestamp timestamp;

    @Before
    public void setUp() {
        location = new Location(0, 0, "location");
        user = new UserManagement().add("user", "1");
        assertThat(user, is(notNullValue()));
        group = new GroupManagement().add("group", user.getUserId());
        assertThat(group, is(notNullValue()));
        timestamp = new Timestamp(0);
        createdEvent = new EventManagement().add(eventName, location, timestamp, user.getUserId(),
                group.getGroupId());
        assertThat(new EventManagement().getEvent(createdEvent.getEventId()), is(notNullValue()));

    }

    @After
    public void tearDown() {
        new EventManagement().delete(createdEvent.getEventId());
        new GroupManagement().delete(group.getGroupId());
        new UserManagement().delete(user.getUserId());
    }

    @Test
    public void testUpdate() {
        // Database doesn't store the milliseconds
        Timestamp newTimestamp = new Timestamp((System.currentTimeMillis() / 1000L) * 1000L);
        createdEvent.setTimestamp(newTimestamp);
        assertThat(new EventManagement().update(createdEvent), is(true));
        assertThat(
                new EventManagement().getEvent(createdEvent.getEventId()).getTimestamp().getTime(),
                is(newTimestamp.getTime()));
    }

    @Test
    public void testUpdateName() {
        String newName = "new Name";
        assertThat(new EventManagement().updateName(createdEvent.getEventId(), newName), is(true));
        assertThat(new EventManagement().getEvent(createdEvent.getEventId()).getName(),
                is(newName));
    }

    @Test
    public void testAdd() {
        assertThat(createdEvent, is(notNullValue()));
        assertThat(createdEvent.getName(), is(eventName));
        assertThat(createdEvent.getLocation().getLocationId(), is(location.getLocationId()));
        assertThat(createdEvent.getTimestamp().getTime(), is(timestamp.getTime()));
        assertThat(createdEvent.getCreator().getUserId(), is(user.getUserId()));
        assertThat(createdEvent.getGroup().getGroupId(), is(group.getGroupId()));
        assertThat(createdEvent.getParticipants().size(), is(1));
        assertThat(createdEvent.getParticipant(user.getUserId()), is(notNullValue()));
    }

    @Test
    public void testDelete() {
        assertThat(new EventManagement().delete(createdEvent.getEventId()), is(true));
        assertThat(new EventManagement().getEvent(createdEvent.getEventId()), is(nullValue()));
        assertThat(new UserManagement().getUser(user.getUserId()), is(notNullValue()));
        assertThat(new GroupManagement().getGroup(group.getGroupId()), is(notNullValue()));
    }

    @Test
    public void testGetCreator() {
        assertThat(new EventManagement().getCreator(createdEvent.getEventId()).getUserId(),
                is(user.getUserId()));
    }

    @Test
    public void testGetEvent() {
        Event event = new EventManagement().getEvent(createdEvent.getEventId());
        assertThat(event, is(notNullValue()));
        assertThat(event.getName(), is(createdEvent.getName()));
        assertThat(event.getLocation().getLocationId(),
                is(createdEvent.getLocation().getLocationId()));
        assertThat(event.getTimestamp().getTime(), is(createdEvent.getTimestamp().getTime()));
        assertThat(event.getCreator().getUserId(), is(createdEvent.getCreator().getUserId()));
        assertThat(event.getGroup().getGroupId(), is(createdEvent.getGroup().getGroupId()));
    }

    @Test
    public void testGetUserLocations() {
        List<Location> locations = new EventManagement()
                .getUserLocations(createdEvent.getEventId());
        assertThat(locations.size(), is(0));
        Location userLocation = new Location(1, 1, "userLocation");
        user.setLocation(userLocation);
        assertThat(new UserManagement().update(user), is(true));
        locations = new EventManagement().getUserLocations(createdEvent.getEventId());
        System.out.println(new UserManagement().getUser(user.getUserId()).getLocation().getName());
        assertThat(locations.size(), is(1));
        assertThat(locations.get(0).getLocationId(), is(userLocation.getLocationId()));
    }

    @Test
    public void testSetClusterPoints() {
        List<Location> points = new ArrayList<>();
        Location location = new Location(1, 1, "location");
        points.add(location);
        assertThat(new EventManagement().setClusterPoints(createdEvent.getEventId(), points),
                is(true));
        Event event = new EventManagement().getEvent(createdEvent.getEventId());
        assertThat(event.getClusterPoints().size(), is(points.size()));
        assertThat(points.containsAll(event.getClusterPoints()), is(true));
    }

}
