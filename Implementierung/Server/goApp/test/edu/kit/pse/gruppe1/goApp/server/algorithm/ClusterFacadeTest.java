package edu.kit.pse.gruppe1.goApp.server.algorithm;

import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.*;

import org.junit.Before;

import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math4.ml.clustering.DBSCANClusterer;
import org.apache.commons.math4.ml.clustering.DoublePoint;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Location;

public class ClusterFacadeTest {

    private ClusterFacade facade = new ClusterFacade(new DBSCANClusterer<DoublePoint>(2,1));
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
        } catch (NoSuchFieldException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }

    @Test
    public void testGetClusteredCentralPoints() {

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
        
        
        List<Location> result = facade.getClusteredLocations(event);

        
       //manually calculated
       double xFirstDifference = result.get(0).getLongitude() - 34.0000000275;
       double yFirstDifference = result.get(0).getLatitude() - 33.2505025;
       double xSecondDifference = result.get(1).getLongitude() - 1.000005;
       double ySecondDifference = result.get(1).getLatitude() - 2.0;
       

        assertTrue(xFirstDifference <= 0.0001 && xFirstDifference >= -0.0001);
        assertTrue(yFirstDifference <= 0.0001 && yFirstDifference >= -0.0001);
        assertTrue(xSecondDifference <= 0.0001 && xSecondDifference >= -0.0001);
        assertTrue(ySecondDifference <= 0.0001 && ySecondDifference >= -0.0001);
         
        
    }
    
    @Test
    public void testGetPointsByMultiDBSCAN() {
        ArrayList<Location> locationList = new ArrayList<Location>();
        locationList.add(new Location(1.0, 1.0, "test"));          
        locationList.add(new Location(1.01, 1.01, "test"));    
        locationList.add(new Location(0.0, 0.0, "test"));    
        locationList.add(new Location(0.99, 0.99, "test"));    
        locationList.add(new Location(34.01, 33.01, "test"));    
        locationList.add(new Location(34.00, 33.01, "test"));

        doReturn(testId).when(event).getEventId();
        doReturn(locationList).when(management).getUserLocations(testId);
        when(management.getUserLocations(testId)).thenReturn(locationList);
        
        
        List<DoublePoint> result = facade.getPointsByMultiDBSCAN(event);

        
       //manually calculated
       double xFirstDifference = result.get(1).getPoint()[0] - 34.005;
       double yFirstDifference = result.get(1).getPoint()[1] - 33.01;
       double xSecondDifference = result.get(0).getPoint()[0] - 1.0;
       double ySecondDifference = result.get(0).getPoint()[1] - 1.0;
       

        assertTrue(xFirstDifference <= 0.0001 && xFirstDifference >= -0.0001);
        assertTrue(yFirstDifference <= 0.0001 && yFirstDifference >= -0.0001);
        assertTrue(xSecondDifference <= 0.0001 && xSecondDifference >= -0.0001);
        assertTrue(ySecondDifference <= 0.0001 && ySecondDifference >= -0.0001);
         
    }
    
    
    @Test
    public void testGetPointsByMultiDBSCANThird() {
        ArrayList<Location> locationList = new ArrayList<Location>();
        locationList.add(new Location(0.0, 0.0, "test"));          
        locationList.add(new Location(20.0, 20.0, "test"));    
        locationList.add(new Location(10.0, 10.0, "test"));    
        locationList.add(new Location(1.0, 1.0, "test"));    
        locationList.add(new Location(34.01, 33.01, "test"));    
     

        doReturn(testId).when(event).getEventId();
        doReturn(locationList).when(management).getUserLocations(testId);
        when(management.getUserLocations(testId)).thenReturn(locationList);
        
        
        List<Location> result = facade.getLocationsByMultiDBSCAN(event);

        
       //manually calculated
       
       double xFirstDifference = result.get(0).getLongitude() - 0.5;
       double yFirstDifference = result.get(0).getLatitude() - 0.5;
       

        assertTrue(xFirstDifference <= 0.0001 && xFirstDifference >= -0.0001);
        assertTrue(yFirstDifference <= 0.0001 && yFirstDifference >= -0.0001);
        
    }
    
    
    
    
    @Test
    public void testGetPointsByMultiDBSCANSecond() {
        ArrayList<Location> locationList = new ArrayList<Location>();
        locationList.add(new Location(0.0, 0.0, "test"));          
        locationList.add(new Location(1.0, 0.0, "test"));    
        locationList.add(new Location(2.0, 0.0, "test"));    
           
     

        doReturn(testId).when(event).getEventId();
        doReturn(locationList).when(management).getUserLocations(testId);
        when(management.getUserLocations(testId)).thenReturn(locationList);
        
        
        List<DoublePoint> result = facade.getPointsByMultiDBSCAN(event);

        
       //manually calculated
       
       double xFirstDifference = result.get(0).getPoint()[0] - 1;
       double yFirstDifference = result.get(0).getPoint()[1];
       

        assertTrue(xFirstDifference <= 0.0001 && xFirstDifference >= -0.0001);
        assertTrue(yFirstDifference <= 0.0001 && yFirstDifference >= -0.0001);
        
    }
    
    
    @Test
    public void testConvertDoublePointsToLocations() {
        
       double[] p1 = {1,2};
       double[] p2 = {3,4};
       
       DoublePoint point1 = new DoublePoint(p1);
       DoublePoint point2 = new DoublePoint(p2);
       
       
       ClusterFacade testClusterer = new ClusterFacade(new DBSCANClusterer<>(2, 1), new SimpleCentral());
       List<DoublePoint> pointList = new ArrayList<DoublePoint>(); 
       
       pointList.add(point1);
       pointList.add(point2);
       List<Location> locationList = testClusterer.convertDoublePointsToLocations(pointList);
       
       Location location1 = locationList.get(0);
       Location location2 = locationList.get(1);
       
       Location firstLocationTest = new Location(1,2,null);
       Location secondLocationTest = new Location(3,4,null);
       
       assertTrue(location1.equals(firstLocationTest));
       assertTrue(location2.equals(secondLocationTest));
       
       
        
    }
    
    @Test
    public void testDefaultConstructor() {
        facade = new ClusterFacade();
        facade.setAlgorithm(new SimpleCentral());
        facade.setClusterer(new DBSCANClusterer<DoublePoint>(2, 1));
        setUp();
        testGetClusteredCentralPoints();
        
    }
    
    @Test
    public void testSecondConstructor() {
        facade = new ClusterFacade(new SimpleCentral());
        
        facade.setClusterer(new DBSCANClusterer<DoublePoint>(2, 1));
        setUp();
        testGetClusteredCentralPoints();
        
    }
}
