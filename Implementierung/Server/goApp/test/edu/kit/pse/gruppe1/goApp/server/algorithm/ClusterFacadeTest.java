package edu.kit.pse.gruppe1.goApp.server.algorithm;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static org.mockito.Mockito.*;

import org.junit.Before;

import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math4.ml.clustering.DoublePoint;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Location;

public class ClusterFacadeTest {

    private ClusterFacade facade = new ClusterFacade();
    private int testId = 214124;

    @Mock
    private Event event;
    @Mock
    private EventManagement management;

    @Before
    public void setUp() {
       
        MockitoAnnotations.initMocks(this);
        String name = "management";
        Field field;
        try {
            field = facade.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(facade, management);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
        
        
        
    }

    @Test
    public void testOne() {

        ArrayList<Location> locationList = new ArrayList<Location>();
        locationList.add(new Location(34.00, 33.00, "test"));          
        locationList.add(new Location(34.0000001, 33.00, "test"));    
        locationList.add(new Location(0.0, 0.1, "test"));    
        locationList.add(new Location(1.00001, 2.00, "test"));   
        locationList.add(new Location(1.0, 2.00, "test"));  
        locationList.add(new Location(34.00000001, 33.00001, "test"));    
        locationList.add(new Location(34.00, 34.002, "test"));

        doReturn(testId).when(event).getEventId();
        doReturn(locationList).when(management).getUserLocations(testId);
        when(management.getUserLocations(testId)).thenReturn(locationList);
        
        
        List<DoublePoint> result = facade.getClusteredCentralPoints(event);

        
       //manually calculated
       double xFirstDifference = result.get(0).getPoint()[0] - 34.0000000275;
       double yFirstDifference = result.get(0).getPoint()[1] - 33.2505025;
       double xSecondDifference = result.get(1).getPoint()[0] - 1.000005;
       double ySecondDifference = result.get(1).getPoint()[1] - 2.0;
       

        assertTrue(xFirstDifference <= 0.0001 && xFirstDifference >= -0.0001);
        assertTrue(yFirstDifference <= 0.0001 && yFirstDifference >= -0.0001);
        assertTrue(xSecondDifference <= 0.0001 && xSecondDifference >= -0.0001);
        assertTrue(ySecondDifference <= 0.0001 && ySecondDifference >= -0.0001);
         
        
    }
}
