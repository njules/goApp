package edu.kit.pse.gruppe1.goApp.server.database.management;

import java.sql.Date;

import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.model.Location;

public class EventManagementTest {

	@Test
	public void testAdd() {
		String name="testn";
		Location location=new Location(0, 0, name);
		Date time=new Date(0);
		Integer creatorId=12;
		Integer groupId=10;
		//TODO
		EventManagement management=new EventManagement();
		management.add(test, new Locateion, time, creatorId, groupID)
	}

}
