package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public class UserManagementTest {
    private User createdUser;
    private String userName = "user name";
    private int googleId = 1234;

    @Before
    public void setUp() throws Exception {
        createdUser = new UserManagement().add(userName, googleId);
        assertThat(createdUser, is(notNullValue()));
    }

    @After
    public void tearDown() throws Exception {
        new UserManagement().delete(createdUser.getUserId());
    }

    @Test
    public void testAdd() {
        assertThat(createdUser, is(notNullValue()));
        assertThat(createdUser.getName(), is(userName));
        assertThat(createdUser.getGoogleId(), is(googleId));
    }

    @Test
    public void testDelete() {
        assertThat(new UserManagement().delete(createdUser.getUserId()), is(true));
        assertThat(new UserManagement().getUser(createdUser.getUserId()), is(nullValue()));
    }

    @Test
    public void testGetUser() {
        User user = new UserManagement().getUser(createdUser.getUserId());
        assertThat(user, is(notNullValue()));
        assertThat(user.getGoogleId(), is(createdUser.getGoogleId()));
        assertThat(user.getUserId(), is(createdUser.getUserId()));
        assertThat(user.getName(), is(createdUser.getName()));
    }
    
    @Test
    public void testGetUserByGoogleId() {
        User user = new UserManagement().getUserByGoogleId(createdUser.getGoogleId()+1);
        assertThat(user, is(nullValue()));
        user = new UserManagement().getUserByGoogleId(createdUser.getGoogleId());
        assertThat(user, is(notNullValue()));
        assertThat(user.getGoogleId(), is(createdUser.getGoogleId()));
        assertThat(user.getUserId(), is(createdUser.getUserId()));
        assertThat(user.getName(), is(createdUser.getName()));
    }

    @Test
    public void testUpdateLocation() {
        assertThat(new UserManagement().getUser(createdUser.getUserId()).getLocation(),
                is(nullValue()));
        Location location = new Location(1, 1, "location");
        assertThat(new UserManagement().updateLocation(createdUser.getUserId(), location),
                is(true));
        assertThat(new UserManagement().getUser(createdUser.getUserId()).getLocation(),
                is(notNullValue()));
        assertThat(new UserManagement().getUser(createdUser.getUserId()).getLocation().getName(),
                is(location.getName()));
        assertThat(
                new UserManagement().getUser(createdUser.getUserId()).getLocation().getLocationId(),
                is(location.getLocationId()));
    }

    @Test
    public void testUpdate() {
        int newGoogleId = googleId + 1;
        createdUser.setGoogleId(newGoogleId);
        assertThat(new UserManagement().update(createdUser), is(true));
        assertThat(new UserManagement().getUser(createdUser.getUserId()).getGoogleId(),
                is(newGoogleId));
    }

    @Test
    public void testUpdateName() {
        String newName = userName + "new";
        assertThat(new UserManagement().updateName(createdUser.getUserId(), newName), is(true));
        assertThat(new UserManagement().getUser(createdUser.getUserId()).getName(), is(newName));
    }

}
