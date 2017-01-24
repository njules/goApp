package edu.kit.pse.gruppe1.goApp.server.algorithm;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventUserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math4.ml.clustering.*;

/**
 * This class is facade for the algortihm, requests the events locations from the database and calls
 * the methods of the Clusterer and the CentralPointAlgo. It also generates Points from the given
 * coordinates, because the Algorithm need them.
 */
public class ClusterFacade {

  /**
   * This method fetches all locations from the database which belong to the event and calls the
   * cluster algorithm to get it clustered. After that it calls the MidpointAlgo to get the
   * midpoints from the calculated clusters.
   * 
   * @return central points of the calculated clusters
   * @param event
   *          List of points which should clustered and which central point should be calculated.
   */
  public List<DoublePoint> getClusteredCentralPoints(Event event) {
    int eventId = event.getEventId();

    List<DoublePoint> locations = getEventsLocations(eventId);
    List<Cluster<DoublePoint>> clusters = getClusters(locations);

    CentralPointAlgo algorithm = new SimpleCentral();

    
    
    List<DoublePoint> result = new ArrayList<DoublePoint>(0);
    for (Cluster<DoublePoint> c : clusters) {
      
      if(!c.equals(null)) {
        result.add(algorithm.calculateCentralPoint(c));
      }
    }
    
    return result;

  }

  private List<DoublePoint> getEventsLocations(int eventId) {

    EventUserManagement management = new EventUserManagement();
    List<User> list = management.getUsers(eventId);
    List<DoublePoint> locations = new ArrayList<DoublePoint>(0);
    DoublePoint userPoint;
    Location userLocation;

    for (User user : list) {

      userLocation = user.getLocation();
      if (!user.getLocation().equals(null)) {

        userPoint = new DoublePoint(
            new double[] { userLocation.getLongitude(), userLocation.getLatitude() }); // Build a
                                                                                       // DoublePoint
                                                                                       // from the
                                                                                       // Location
        locations.add(userPoint);
      }

    }

    return locations;

  }

  private List<Cluster<DoublePoint>> getClusters(List<DoublePoint> points) {

    DBSCANClusterer<DoublePoint> clusterer = new DBSCANClusterer<DoublePoint>(0.1, 2);

    return clusterer.cluster(points);

  }

}