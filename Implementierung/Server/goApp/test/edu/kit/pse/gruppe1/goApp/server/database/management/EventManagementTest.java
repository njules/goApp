package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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
        user = new UserManagement().add("user", 1);
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
    public void testAdd() {
        assertNotNull(createdEvent);
        assertEquals(eventName, createdEvent.getName());
        assertEquals(location.getLocationId(), createdEvent.getLocation().getLocationId());
        assertEquals(timestamp.getTime(), createdEvent.getTimestamp().getTime());
        assertEquals(user.getUserId(), createdEvent.getCreator().getUserId());
        assertEquals(group.getGroupId(), createdEvent.getGroup().getGroupId());
    }

    @Test
    public void testDelete() {
        new EventManagement().delete(createdEvent.getEventId());
        assertNull(new EventManagement().getEvent(createdEvent.getEventId()));
    }

    @Test
    public void testGetCreator() {
        assertEquals(user.getUserId(),
                new EventManagement().getCreator(createdEvent.getEventId()).getUserId());
    }

    @Test
    public void testGetEvent() {
        Event event = new EventManagement().getEvent(createdEvent.getEventId());
        assertNotNull(event);
        assertEquals(createdEvent.getName(), createdEvent.getName());
        assertEquals(createdEvent.getLocation().getLocationId(),
                event.getLocation().getLocationId());
        assertEquals(createdEvent.getTimestamp().getTime(), event.getTimestamp().getTime());
        assertEquals(createdEvent.getCreator().getUserId(), event.getCreator().getUserId());
        assertEquals(createdEvent.getGroup().getGroupId(), event.getGroup().getGroupId());
    }

    @Test
    public void testGetUserLocations() {
        List<Location> locations = new EventManagement()
                .getUserLocations(createdEvent.getEventId());
        assertEquals(locations.size(), 0);
        Location userLocation = new Location(1, 1, "userLocation");
        user.setLocation(userLocation);
        new UserManagement().update(user);
        locations = new EventManagement().getUserLocations(createdEvent.getEventId());
        System.out.println(new UserManagement().getUser(user.getUserId()).getLocation().getName());
        assertEquals(1, locations.size());
        assertEquals(userLocation.getLocationId(), locations.get(0).getLocationId());
    }

    @Test
    public void testSetClusterPoints() {
        List<Location> points = new ArrayList<>();
        Location location = new Location(1, 1, "location");
        points.add(location);
        new EventManagement().setClusterPoints(createdEvent.getEventId(), points);
        Event event = new EventManagement().getEvent(createdEvent.getEventId());
        assertEquals(points.size(), event.getClusterPoints().size());
        assertTrue(points.containsAll(event.getClusterPoints()));
    }

}
