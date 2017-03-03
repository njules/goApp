package edu.kit.pse.gruppe1.goApp.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.kit.pse.gruppe1.goApp.server.algorithm.ClusterFacadeTest;
import edu.kit.pse.gruppe1.goApp.server.algorithm.ImportantMidPointCentralTest;
import edu.kit.pse.gruppe1.goApp.server.algorithm.SimpleCentralTest;
import edu.kit.pse.gruppe1.goApp.server.database.management.DatabaseInitializerTest;
import edu.kit.pse.gruppe1.goApp.server.database.management.EventDeletionTimerTest;
import edu.kit.pse.gruppe1.goApp.server.database.management.EventManagementTest;
import edu.kit.pse.gruppe1.goApp.server.database.management.EventUserManagementTest;
import edu.kit.pse.gruppe1.goApp.server.database.management.GroupManagementTest;
import edu.kit.pse.gruppe1.goApp.server.database.management.GroupUserManagementTest;
import edu.kit.pse.gruppe1.goApp.server.database.management.LocationDeletionTimerTest;
import edu.kit.pse.gruppe1.goApp.server.database.management.RequestManagementTest;
import edu.kit.pse.gruppe1.goApp.server.database.management.ThreadTest;
import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagementTest;
import edu.kit.pse.gruppe1.goApp.server.servlet.EventServletTest;
import edu.kit.pse.gruppe1.goApp.server.servlet.GroupSearchServletTest;
import edu.kit.pse.gruppe1.goApp.server.servlet.GroupServletTest;
import edu.kit.pse.gruppe1.goApp.server.servlet.LocationServletTest;
import edu.kit.pse.gruppe1.goApp.server.servlet.ParticipateServletTest;
import edu.kit.pse.gruppe1.goApp.server.servlet.RequestSearchServletTest;
import edu.kit.pse.gruppe1.goApp.server.servlet.RequestServletTest;
import edu.kit.pse.gruppe1.goApp.server.servlet.ServletUtilsTest;
import edu.kit.pse.gruppe1.goApp.server.servlet.UserServletTest;

@RunWith(Suite.class)
@SuiteClasses({ EventServletTest.class, GroupSearchServletTest.class, GroupServletTest.class,
        LocationServletTest.class, ParticipateServletTest.class, RequestServletTest.class,
        ServletUtilsTest.class, UserServletTest.class, RequestSearchServletTest.class,
        ClusterFacadeTest.class, ImportantMidPointCentralTest.class, SimpleCentralTest.class,
        DatabaseInitializerTest.class, EventDeletionTimerTest.class, LocationDeletionTimerTest.class,
        EventManagementTest.class, EventUserManagementTest.class, GroupManagementTest.class,
        GroupUserManagementTest.class, ThreadTest.class, UserManagementTest.class,
        RequestManagementTest.class })
public class AllServerTests {

}
