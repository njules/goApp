package edu.kit.pse.gruppe1.goApp.server.database.management;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DatabaseInitializerTest.class, EventDeletionTimerTest.class,
        EventManagementTest.class, EventUserManagementTest.class, GroupManagementTest.class,
        GroupUserManagementTest.class, ThreadTest.class, UserManagementTest.class,
        RequestManagementTest.class })
public class AllDatabaseManagementTests {

}
