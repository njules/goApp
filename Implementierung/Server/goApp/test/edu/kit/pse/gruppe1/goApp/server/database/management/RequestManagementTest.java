package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Request;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public class RequestManagementTest {
    private User createdUser;
    private User userWithRequest;
    private Request request;
    private Group createdGroup;
    private String groupName = "group name";
    private String userName = "user name";
    private String googleId = "1234";
    private int invalidId = 1010101010;

    @Before
    public void setUp() throws Exception {
        createdUser = new UserManagement().add(userName, googleId);
        assertThat(createdUser, is(notNullValue()));
        createdGroup = new GroupManagement().add(groupName, createdUser.getUserId());
        assertThat(createdGroup, is(notNullValue()));
        assertThat(new GroupUserManagement().getGroups(createdUser.getUserId()).size(), is(1));
        userWithRequest = new UserManagement().add("user with request", googleId + 1);
        assertThat(userWithRequest, is(notNullValue()));
        assertThat(
                new RequestManagement().add(createdGroup.getGroupId(), userWithRequest.getUserId()),
                is(true));
        // load new after change
        createdGroup = new GroupManagement().getGroup(createdGroup.getGroupId());
        userWithRequest = new UserManagement().getUser(userWithRequest.getUserId());
    }

    @After
    public void tearDown() throws Exception {
        new GroupManagement().delete(createdGroup.getGroupId());
        new UserManagement().delete(createdUser.getUserId());
        new UserManagement().delete(userWithRequest.getUserId());
    }

    @Test
    public void testAdd() {
        assertThat(createdGroup.getRequest(userWithRequest.getUserId()), is(notNullValue()));
        assertThat(createdGroup.getRequests().size(), is(1));
        assertThat(userWithRequest.getRequests().size(), is(1));
        assertThat(userWithRequest.getRequests().iterator().next().getGroup().getGroupId(),
                is(createdGroup.getGroupId()));
    }

    @Test
    public void testDeleteParamsRequestId() {
        assertThat(new RequestManagement().delete(invalidId), is(false));
        assertThat(
                new RequestManagement().delete(
                        createdGroup.getRequest(userWithRequest.getUserId()).getRequestId()),
                is(true));
        assertThat(new GroupManagement().getGroup(createdGroup.getGroupId()).getRequests().size(),
                is(0));
        assertThat(new UserManagement().getUser(userWithRequest.getUserId()).getRequests().size(),
                is(0));
    }

    @Test
    public void testDeleteParamsGroupIdUserId() {
        assertThat(new RequestManagement().delete(invalidId, invalidId), is(false));
        assertThat(new RequestManagement().delete(createdGroup.getGroupId(),
                userWithRequest.getUserId()), is(true));
        assertThat(new GroupManagement().getGroup(createdGroup.getGroupId()).getRequests().size(),
                is(0));
        assertThat(new UserManagement().getUser(userWithRequest.getUserId()).getRequests().size(),
                is(0));
    }

    @Test
    public void testGetRequestByGroup() {
        assertThat(new RequestManagement().getRequestByGroup(invalidId), is(nullValue()));
        List<User> users = new RequestManagement().getRequestByGroup(createdGroup.getGroupId());
        assertThat(users, is(notNullValue()));
        assertThat(users.size(), is(1));
        assertThat(users.get(0).getUserId(), is(userWithRequest.getUserId()));
    }

    @Test
    public void testGetRequestByUser() {
        assertThat(new RequestManagement().getRequestByUser(invalidId), is(nullValue()));
        List<Group> groups = new RequestManagement().getRequestByUser(userWithRequest.getUserId());
        assertThat(groups, is(notNullValue()));
        assertThat(groups.size(), is(1));
        assertThat(groups.get(0).getGroupId(), is(createdGroup.getGroupId()));
    }
}
