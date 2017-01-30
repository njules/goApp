package edu.kit.pse.gruppe1.goApp.server.database.management;

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
        assertNotNull(user);
        createdGroup = new GroupManagement().add(groupName, user.getUserId());
        assertNotNull(createdGroup);
    }

    @After
    public void tearDown() throws Exception {        
        new GroupManagement().delete(createdGroup.getGroupId());
        new UserManagement().delete(user.getUserId());
    }

    @Test
    public void testAdd() {
        assertNotNull(createdGroup);
        assertEquals(groupName, createdGroup.getName());
        assertEquals(user.getUserId(), createdGroup.getFounder().getUserId());
        assertEquals(1, createdGroup.getUsers().size());
        for (User u : createdGroup.getUsers()) {
            assertEquals(user.getUserId(), u.getUserId());
        }
    }

    @Test
    public void testGetGroup() {
        Group group = new GroupManagement().getGroup(createdGroup.getGroupId());
        assertNotNull(group);
        assertEquals(createdGroup.getName(), group.getName());        
        assertEquals(createdGroup.getFounder().getUserId(), group.getFounder().getUserId());
        assertEquals(createdGroup.getUsers().size(), group.getUsers().size());
        assertTrue(group.getUsers().containsAll(createdGroup.getUsers()));
    }

}
