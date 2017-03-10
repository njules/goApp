package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;

import org.hibernate.Session;
import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.LocationDeletionTimer;
import edu.kit.pse.gruppe1.goApp.server.TimerStarter;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public class LocationDeletionTimerTest {

    @Test
    public void test() {
        User user = new UserManagement().add("test", "asfdasfd");
        new LocationDeletionTimer(3600);
        Location location = new Location(1, 1, "test deletion", null);
        Location location2 = new Location(1, 1, "test deletion",
                new Timestamp(System.currentTimeMillis() + 20000));

        new UserManagement().updateLocation(user.getUserId(), location);
        location = new UserManagement().getUser(user.getUserId()).getLocation();
        location.setDeletionTime(new Timestamp(System.currentTimeMillis() + 100));
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        session.update(location);
        session.save(location2);
        session.getTransaction().commit();
        session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        Location l = session.get(Location.class, location.getLocationId());
        session.getTransaction().commit();
        assertThat(l, is(notNullValue()));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        l = session.get(Location.class, location.getLocationId());
        session.getTransaction().commit();

        assertThat(new UserManagement().getUser(user.getUserId()).getLocation(), is(nullValue()));
        assertThat(l, is(nullValue()));
        session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        l = session.get(Location.class, location2.getLocationId());
        session.getTransaction().commit();
        assertThat(l, is(notNullValue()));
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        l = session.get(Location.class, location2.getLocationId());
        session.getTransaction().commit();
        assertThat(l, is(nullValue()));
    }

    @Test
    public void testTimerStarter() {
        Location location = new Location(1, 1, "test deletion",
                new Timestamp(System.currentTimeMillis()));
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        session.save(location);
        session.getTransaction().commit();
        session = DatabaseInitializer.getFactory().getCurrentSession();
        TimerStarter t = new TimerStarter();
        session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        Location l = session.get(Location.class, location.getLocationId());
        session.getTransaction().commit();
        assertThat(l, is(notNullValue()));
        t.contextInitialized(null);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
        session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        l = session.get(Location.class, location.getLocationId());
        session.getTransaction().commit();
        assertThat(l, is(nullValue()));
        t.contextDestroyed(null);

        location = new Location(1, 1, "test deletion", new Timestamp(System.currentTimeMillis()));
        session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        session.save(location);
        session.getTransaction().commit();
        session = DatabaseInitializer.getFactory().getCurrentSession();
        session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        l = session.get(Location.class, location.getLocationId());
        session.getTransaction().commit();
        assertThat(l, is(notNullValue()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
        session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        l = session.get(Location.class, location.getLocationId());
        session.getTransaction().commit();
        assertThat(l, is(notNullValue()));
        session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        session.delete(l);
        session.getTransaction().commit();
    }

}
