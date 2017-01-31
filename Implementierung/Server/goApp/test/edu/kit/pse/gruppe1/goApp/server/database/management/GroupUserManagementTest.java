package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

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

}
