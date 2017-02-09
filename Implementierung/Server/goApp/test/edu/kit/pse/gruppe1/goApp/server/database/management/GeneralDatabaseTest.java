package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public class GeneralDatabaseTest {
    TestDatabase database = new TestDatabase();

    List<User> users;
    List<Event> events;
    List<Group> groups;

    @Before
    public void setUp() throws Exception {
        database.create(20, 7, 12);
        users = database.getUsers();
        events = database.getEvents();
        groups = database.getGroups();

    }

    @After
    public void tearDown() throws Exception {
        database.setUsers(users);
        database.setEvents(events);
        database.setGroups(groups);
        database.delete();
    }

    @Test
    public void test() {
        new UserManagement().delete(users.get(0).getUserId());
        users.delete()
        assertThat(actual, matcher);
    }

}
