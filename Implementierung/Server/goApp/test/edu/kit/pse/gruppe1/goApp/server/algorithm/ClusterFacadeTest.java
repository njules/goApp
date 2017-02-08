package edu.kit.pse.gruppe1.goApp.server.algorithm;

import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.*;

import org.junit.Before;

import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.kit.pse.gruppe1.goApp.server.model.User;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math4.ml.clustering.DoublePoint;
import edu.kit.pse.gruppe1.goApp.server.database.management.EventUserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Location;

public class ClusterFacadeTest {

    private ClusterFacade facade = new ClusterFacade();
    private int testId = 214124;

    @Mock
    private Event event;
    @Mock
    private EventUserManagement management;
    @Mock
    private User u1;
    @Mock
    private User u2;
    @Mock
    private User u3;
    @Mock
    private User u4;
    @Mock
    private User u5;
    @Mock
    private User u6;
    @Mock
    private User u7;

    @Before
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
            IllegalAccessException {
        String name = "management";
        Field field = facade.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(facade, management);
    }

    @Test
    public void testOne() {

        MockitoAnnotations.initMocks(this);

        ArrayList<User> userList = new ArrayList<User>();
        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
        userList.add(u4);
        userList.add(u5);
        userList.add(u6);
        userList.add(u7);

        doReturn(testId).when(event).getEventId();
        doReturn(userList).when(management).getUsers(testId);
        doReturn(new Location(34, 33, "test")).when(u1).getLocation();
        doReturn(new Location(34.3, 33.001, "test")).when(u2).getLocation();
        doReturn(new Location(0.0, 0.1, "test")).when(u3).getLocation();
        doReturn(new Location(1.0001, 2, "test")).when(u4).getLocation();
        doReturn(new Location(1.0, 2.003, "test")).when(u5).getLocation();
        doReturn(new Location(34.001, 34.00001, "test")).when(u6).getLocation();
        doReturn(new Location(34, 34.002, "test")).when(u7).getLocation();
        

        List<DoublePoint> result = facade.getClusteredCentralPoints(event);

        double xFirstDifference = result.get(0).getPoint()[0] - 34.07525;
        double xSecondDifference = result.get(1).getPoint()[0] - 0.6667;
        double yFirstDifference = result.get(0).getPoint()[1] - 33.5007525;
        double ySecondDifference = result.get(1).getPoint()[1] - 1.3676667;

        assertTrue(xFirstDifference <= 0.0001 && xFirstDifference >= -0.0001);
        assertTrue(yFirstDifference <= 0.0001 && yFirstDifference >= -0.0001);
        assertTrue(xSecondDifference <= 0.0001 && xSecondDifference >= -0.0001);
        assertTrue(ySecondDifference <= 0.0001 && ySecondDifference >= -0.0001);
    }
}
