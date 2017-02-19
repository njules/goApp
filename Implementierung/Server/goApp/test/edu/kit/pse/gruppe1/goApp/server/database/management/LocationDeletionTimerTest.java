package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.LocationDeletionTimer;
import edu.kit.pse.gruppe1.goApp.server.model.Location;

public class LocationDeletionTimerTest {

    @Test
    public void test() {
        new LocationDeletionTimer(3600);
        Location location = new Location(1, 1, "test deletion",
                new Timestamp(System.currentTimeMillis() + 1000));
        Location location2 = new Location(1, 1, "test deletion",
                new Timestamp(System.currentTimeMillis() + 10000));
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        session.save(location);
        session.save(location2);
        session.getTransaction().commit();
        session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        Location l = session.get(Location.class, location.getLocationId());
        session.getTransaction().commit();
        assertThat(l, is(notNullValue()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        l = session.get(Location.class, location.getLocationId());
        session.getTransaction().commit();
        assertThat(l, is(nullValue()));
        session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        l = session.get(Location.class, location2.getLocationId());
        session.getTransaction().commit();
        assertThat(l, is(notNullValue()));
    }

}
