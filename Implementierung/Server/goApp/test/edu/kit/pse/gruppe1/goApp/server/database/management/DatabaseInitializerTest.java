package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.model.Location;

public class DatabaseInitializerTest {

	@Test
	public void test() {
		assertNotNull(DatabaseInitializer.getFactory());
	}

//	@Test
	public void testLocation() {		
		Session session = DatabaseInitializer.getFactory().getCurrentSession();
		Location location = null;
		location = new Location(0, 0, "testLocation");
		session.beginTransaction();
		session.save(location);
		session.getTransaction().commit();

		Integer id = location.getLocationId();
		session = DatabaseInitializer.getFactory().getCurrentSession();
		location = null;
		session.beginTransaction();
		location = (Location) session.get(Location.class, id);
		session.getTransaction().commit();		
		assertNotNull(location);		
		assertEquals("testLocation", location.getName());
	}

}
