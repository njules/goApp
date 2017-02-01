package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public class GroupManagementTest {
    private User user;
    private Group createdGroup;
    private String groupName = "group name";

    @Before
    public void setUp() throws Exception {
        user = new UserManagement().add("user", 3);
        assertThat(user, is(notNullValue()));
        createdGroup = new GroupManagement().add(groupName, user.getUserId());
        assertThat(createdGroup, is(notNullValue()));
    }

    @After
    public void tearDown() throws Exception {
        new GroupManagement().delete(createdGroup.getGroupId());
        new UserManagement().delete(user.getUserId());
    }

    @Test
    public void testAdd() {
        assertThat(createdGroup, is(notNullValue()));
        assertThat(createdGroup.getName(), is(groupName));
        assertThat(createdGroup.getFounder().getUserId(), is(user.getUserId()));
        assertThat(createdGroup.getUsers().size(), is(1));
        for (User u : createdGroup.getUsers()) {
            assertThat(u.getUserId(), is(user.getUserId()));
        }
    }

    @Test
    public void testGetGroup() {
        Group group = new GroupManagement().getGroup(createdGroup.getGroupId());
        assertThat(group, is(notNullValue()));
        assertThat(group.getName(), is(createdGroup.getName()));
        assertThat(group.getFounder().getUserId(), is(createdGroup.getFounder().getUserId()));
        assertThat(group.getUsers().size(), is(createdGroup.getUsers().size()));
        assertThat(group.getUsers().containsAll(createdGroup.getUsers()), is(true));
    }

    @Test
    public void testDelete() {
        assertThat(new GroupManagement().delete(createdGroup.getGroupId()), is(true));
        assertThat(new GroupManagement().getGroup(createdGroup.getGroupId()), is(nullValue()));
        assertThat(new UserManagement().getUser(user.getUserId()), is(notNullValue()));
    }

    @Test
    public void testGetEvents() {
        List<Event> events = new GroupManagement().getEvents(createdGroup.getGroupId());
        assertThat(events, is(notNullValue()));
        assertThat(events.size(), is(0));
        Event event = new EventManagement().add("event name", new Location(0, 0, "location"),
                new Timestamp(0), user.getUserId(), createdGroup.getGroupId());
        events.add(event);
        createdGroup.setEvents(new HashSet<>(events));
        assertThat(new GroupManagement().update(createdGroup), is(true));
        events = new GroupManagement().getEvents(createdGroup.getGroupId());
        assertThat(events, is(notNullValue()));
        assertThat(events.size(), is(1));
        assertThat(events.get(0).getEventId(), is(event.getEventId()));
    }

    @Test
    public void testUpdate() {
        Event event = new Event("event name", new Location(1, 1, "location"), new Timestamp(0),
                createdGroup, user);
        List<Event> events = new ArrayList<>();
        events.add(event);
        createdGroup.setEvents(new HashSet<>(events));
        assertThat(new GroupManagement().update(createdGroup), is(true));
        events = new GroupManagement().getEvents(createdGroup.getGroupId());
        assertThat(events, is(notNullValue()));
        assertThat(events.size(), is(1));
        assertThat(events.get(0).getEventId(), is(event.getEventId()));
    }

    @Test
    public void testGetGroupsByName() {
        List<Group> groups = new GroupManagement().getGroupsByName("no group with this name");
        assertThat(groups, is(notNullValue()));
        assertThat(groups.size(), is(0));
        groups = new GroupManagement().getGroupsByName(groupName);
        checkListContainsCreatedGroup(groups);
        groups = new GroupManagement().getGroupsByName(groupName.substring(0, 2));
        checkListContainsCreatedGroup(groups);
        groups = new GroupManagement().getGroupsByName(groupName.substring(1, 3));
        checkListContainsCreatedGroup(groups);
    }

    private void checkListContainsCreatedGroup(List<Group> groups) {
        assertThat(groups, is(notNullValue()));
        assertThat(groups.size(), is(1));
        assertThat(groups.get(0).getGroupId(), is(createdGroup.getGroupId()));
    }

    @Test
    public void testUpdateFounder() {
        User userTmp = new UserManagement().add("user u", user.getGoogleId() + 1);
        assertThat(userTmp, is(notNullValue()));
        assertThat(new GroupManagement().updateFounder(createdGroup.getGroupId(), userTmp),
                is(true));
        assertThat(
                new GroupManagement().getGroup(createdGroup.getGroupId()).getFounder().getUserId(),
                is(userTmp.getUserId()));
        assertThat(new GroupManagement().updateFounder(createdGroup.getGroupId(), user), is(true));
        assertThat(
                new GroupManagement().getGroup(createdGroup.getGroupId()).getFounder().getUserId(),
                is(user.getUserId()));
        assertThat(new UserManagement().delete(userTmp.getUserId()), is(true));
    }

    @Test
    public void testUpdateName() {
        String newName = createdGroup.getName() + "new";
        assertThat(new GroupManagement().updateName(createdGroup.getGroupId(), newName), is(true));
        assertThat(new GroupManagement().getGroup(createdGroup.getGroupId()).getName(),
                is(newName));
    }

}
