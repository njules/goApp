package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Timestamp;

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

}
