package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.junit.Assert.assertNotNull;

import java.sql.Time;
import java.sql.Timestamp;

import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public class EventManagementTest {

    @Test
    public void testAdd() {
        String name = "testn";
        Location location = new Location(0, 0, name);
        User user = new UserManagement().add(name, 1);
        assertNotNull(user);
        Group group = new GroupManagement().add(name, user.getUserId());
        assertNotNull(group);
        Timestamp time = new Timestamp(0);
        EventManagement management = new EventManagement();

        Event event = management.add(name, location, time, user.getUserId(), group.getGroupId());
        assertNotNull(event);
    }

}
