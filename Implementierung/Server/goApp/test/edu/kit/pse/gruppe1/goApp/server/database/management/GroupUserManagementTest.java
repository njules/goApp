package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Status;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public class GroupUserManagementTest {
    private User createdUser;
    private Group createdGroup;
    private String groupName = "group name";
    private String userName = "user name";
    private int googleId = 1234;

    @Before
    public void setUp() throws Exception {
        createdUser = new UserManagement().add(userName, googleId);
        assertThat(createdUser, is(notNullValue()));
        createdGroup = new GroupManagement().add(groupName, createdUser.getUserId());
        assertThat(createdGroup, is(notNullValue()));
        assertThat(new GroupUserManagement().getGroups(createdUser.getUserId()).size(), is(1));
    }

    @After
    public void tearDown() throws Exception {
        new GroupManagement().delete(createdGroup.getGroupId());
        new UserManagement().delete(createdUser.getUserId());
    }

    @Test
    public void testAdd() {
        User userTmp = new UserManagement().add(userName + "usertmp", googleId + 1);
        assertThat(new GroupUserManagement().add(createdGroup.getGroupId(), userTmp.getUserId()),
                is(true));
        Set<User> users = new GroupManagement().getGroup(createdGroup.getGroupId()).getUsers();
        assertThat(users, is(notNullValue()));
        assertThat(users.size(), is(2));
        assertThat(users.contains(userTmp), is(true));
        assertThat(new UserManagement().getUser(userTmp.getUserId()).getGroups()
                .contains(createdGroup), is(true));

        new UserManagement().delete(userTmp.getUserId());
    }

    @Test
    public void testDelete1() {
        // should return false because createdUser is founder of createdGroup
        assertThat(new GroupUserManagement().delete(createdGroup.getGroupId(),
                createdUser.getUserId()), is(false));
        assertThat(new GroupUserManagement().getGroups(createdUser.getUserId()).size(), is(1));
    }

    @Test
    public void testDelete2() {
        User userTmp = new UserManagement().add(userName + "usertmp", googleId + 1);
        assertThat(new GroupUserManagement().add(createdGroup.getGroupId(), userTmp.getUserId()),
                is(true));
        assertThat(new GroupUserManagement().getGroups(userTmp.getUserId()).size(), is(1));
        assertThat(new GroupUserManagement().delete(createdGroup.getGroupId(), userTmp.getUserId()),
                is(true));
        assertThat(new GroupUserManagement().getGroups(userTmp.getUserId()).size(), is(0));
        new UserManagement().delete(userTmp.getUserId());
    }

    @Test
    public void testGetGroups() {
        List<Group> groups = new GroupUserManagement().getGroups(createdUser.getUserId());
        assertThat(groups, is(notNullValue()));
        assertThat(groups.size(), is(1));
        assertThat(groups.get(0).getGroupId(), is(createdGroup.getGroupId()));
    }

    @Test
    public void testGetUsers() {
        List<User> users = new GroupUserManagement().getUsers(createdGroup.getGroupId());
        assertThat(users, is(notNullValue()));
        assertThat(users.size(), is(1));
        assertThat(users.get(0).getUserId(), is(createdUser.getUserId()));
    }
}
