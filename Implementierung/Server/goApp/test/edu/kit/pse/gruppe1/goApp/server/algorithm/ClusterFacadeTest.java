package edu.kit.pse.gruppe1.goApp.server.algorithm;

import edu.kit.pse.gruppe1.goApp.server.database.management.*;
import edu.kit.pse.gruppe1.goApp.server.model.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math4.ml.clustering.Cluster;
import org.apache.commons.math4.ml.clustering.Clusterer;
import org.apache.commons.math4.ml.clustering.DBSCANClusterer;
import org.apache.commons.math4.ml.clustering.DoublePoint;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;


public class ClusterFacadeTest {

  
    
    
    @Test 
    public void testOne() {
        
        
        EventUserManagement mockedManager = Mockito.mock(EventUserManagement.class);
        Event event = Mockito.mock(Event.class);
        ClusterFacade facade = Mockito.mock(ClusterFacade.class);
        
        
        
        MockitoAnnotations.initMocks(this);
        ArrayList<DoublePoint> list = new ArrayList<DoublePoint>();
        list.add(new DoublePoint(new double[] {34,33}));
        list.add(new DoublePoint(new double[] {34.3,33.001}));
        list.add(new DoublePoint(new double[] {0.0,0.1}));
        list.add(new DoublePoint(new double[] {1.0001,2}));
        list.add(new DoublePoint(new double[] {1.00,2.003}));
        list.add(new DoublePoint(new double[] {34.001,34.00001}));
        list.add(new DoublePoint(new double[] {34,34.002}));
        
        
        
        doReturn(0).when(event).getEventId();
        doReturn(list).when(facade).getEventsLocations(0);
        
        when(facade.getClusteredCentralPoints(event)).thenCallRealMethod();
        
        List<Cluster<DoublePoint>> clusters = new DBSCANClusterer<DoublePoint>(40,2).cluster(list);
        doReturn(clusters).when(facade).getClusters(list);
        
        for(Cluster<DoublePoint> c : clusters) {            
        
        doReturn(new SimpleCentral().calculateCentralPoint(c)).when(facade).getCenter(c);
        
        }
        
       
        
        List<DoublePoint> result = facade.getClusteredCentralPoints(event);
        
        double xFirstDifference = result.get(0).getPoint()[0] - 34.07525;
        double xSecondDifference = result.get(1).getPoint()[0] - 0.6667;
        double yFirstDifference = result.get(0).getPoint()[1] - 33.5007525;
        double ySecondDifference = result.get(1).getPoint()[1] - 1.3676667;   
        
        
        assertTrue( xFirstDifference <= 0.0001 && xFirstDifference >= -0.0001);
        assertTrue( yFirstDifference <= 0.0001 && yFirstDifference >= -0.0001);
        assertTrue( xSecondDifference <= 0.0001 && xSecondDifference >= -0.0001);
        assertTrue( ySecondDifference <= 0.0001 && ySecondDifference >= -0.0001);
    }
}
