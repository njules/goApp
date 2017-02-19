package edu.kit.pse.gruppe1.goApp.server.database.management;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

//Tests just work with hibernate.cfg.xml configured and an available Database 
@RunWith(Suite.class)
@SuiteClasses({ DatabaseInitializerTest.class, EventDeletionTimerTest.class,
        EventManagementTest.class, EventUserManagementTest.class, GroupManagementTest.class,
        GroupUserManagementTest.class, ThreadTest.class, UserManagementTest.class,
        RequestManagementTest.class, LocationDeletionTimerTest.class })
public class AllDatabaseManagementTests {

}
