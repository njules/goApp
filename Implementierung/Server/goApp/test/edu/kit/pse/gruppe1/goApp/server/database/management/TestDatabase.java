package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public class TestDatabase {
    private List<User> users;
    private List<Group> groups;
    private List<Event> events;
    private String userName = "user no. ";
    private String groupName = "group no. ";
    private String eventName = "event no. ";

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public boolean create(int numberOfUsers, int numberOfGroups, int numberOfEvents) {
        users = new ArrayList<>(numberOfUsers);
        groups = new ArrayList<>(numberOfGroups);
        events = new ArrayList<>(numberOfEvents);
        for (int i = 0; i < numberOfUsers; i++) {
            User user = new UserManagement().add(userName + i, "i");
            if (user == null) {
                return false;
            }
            users.add(user);
        }
        for (int i = 0; i < numberOfGroups; i++) {
            Group group = new GroupManagement().add(groupName + i,
                    users.get(i % numberOfUsers).getUserId());
            if (group == null) {
                return false;
            }
            groups.add(group);
        }
        for (int i = 0; i < numberOfEvents; i++) {
            Event event = new EventManagement().add(eventName + i,
                    new Location(i, i, "location of " + eventName), new Timestamp(i),
                    groups.get(i % numberOfGroups).getFounder().getUserId(),
                    groups.get(i % numberOfGroups).getGroupId());

            if (event == null) {
                return false;
            }
            events.add(event);
        }
        return false;
    }

    public void delete() {
        for (Event event : events) {
            new EventManagement().delete(event.getEventId());
        }
        for (Group group : groups) {
            new GroupManagement().delete(group.getGroupId());
        }
        for (User user : users) {
            new UserManagement().delete(user.getUserId());
        }
    }

    public boolean isEqualToDatabase() {
        List<Group> dbGroups = new GroupManagement().getGroupsByName("");
        Set<Event> events = new HashSet<>();
        assertThat(groups.containsAll(dbGroups), is(true));
        assertThat(groups.containsAll(groups), is(true));
        for (Group group : dbGroups) {
            assertThat(actual, matcher);
        }
    }

    private Event getEvent(int eventId) {
        for (Event event : events) {
            if (event.getEventId().equals(eventId)) {
                return event;
            }
        }
        return null;
    }
}
