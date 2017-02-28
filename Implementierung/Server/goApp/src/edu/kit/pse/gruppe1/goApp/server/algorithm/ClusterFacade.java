package edu.kit.pse.gruppe1.goApp.server.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math4.ml.clustering.Cluster;
import org.apache.commons.math4.ml.clustering.Clusterer;
import org.apache.commons.math4.ml.clustering.DBSCANClusterer;
import org.apache.commons.math4.ml.clustering.DoublePoint;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Location;

/**
 * This class is facade for the algortihm, requests the events locations from the database and calls
 * the methods of the Clusterer and the CentralPointAlgo. It also generates Points from the given
 * coordinates, because the Algorithm need them.
 */
public class ClusterFacade {

    private CentralPointAlgo algorithm;
    private Clusterer<DoublePoint> clusterer;
    private EventManagement management;

    /**
     * default constructor with simpleCentral-Algorithm and DBSCAN
     */
    public ClusterFacade() {
        this.algorithm = new SimpleCentral();
        clusterer = new DBSCANClusterer<DoublePoint>(0.001, 1);
        this.management = new EventManagement();
    }

    /**
     * Constructor with DBSCAN, CentralPointAlgo selectable.
     * 
     * @param algorithm
     *            the algorithm that should be used
     */
    public ClusterFacade(CentralPointAlgo algorithm) {
        this.algorithm = algorithm;
        this.clusterer = new DBSCANClusterer<DoublePoint>(0.001, 1);
        this.management = new EventManagement();
    }

    /**
     * Constructor with SimpleCentralAlgo, Clusterer selectable.
     * 
     * @param clusterer
     *            the clusterer
     */
    public ClusterFacade(Clusterer<DoublePoint> clusterer) {
        this.algorithm = new SimpleCentral();
        this.clusterer = clusterer;
        this.management = new EventManagement();
    }

    /**
     * Clusterer and midpoint-algorithm selectable.
     * 
     * @param algorithm
     *            the algorithm that should be used
     * @param clusterer
     *            the clusterer
     */
    public ClusterFacade(Clusterer<DoublePoint> clusterer, CentralPointAlgo algorithm) {
        this.algorithm = algorithm;
        this.clusterer = clusterer;
        this.management = new EventManagement();
    }

    /**
     * 
     * @param algorithm
     *            the algorithm that should be used
     */
    public void setAlgorithm(CentralPointAlgo algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * 
     * @param clusterer
     *            the clusterer
     */
    public void setClusterer(Clusterer<DoublePoint> clusterer) {
        this.clusterer = clusterer;
    }

    /**
     * This method fetches all locations from the database which belong to the event and calls the
     * cluster algorithm to get it clustered. After that it calls the MidpointAlgo to get the
     * midpoints from the calculated clusters.
     * 
     * @return central points of the calculated clusters
     * @param event
     *            List of points which should clustered and which central point should be
     *            calculated.
     */
    public List<DoublePoint> getClusteredCentralPoints(Event event) {

        int eventId = event.getEventId();

        List<DoublePoint> locations = getEventsLocations(eventId);
        List<? extends Cluster<DoublePoint>> clusters = getClusters(locations);

        List<DoublePoint> result = new ArrayList<DoublePoint>();

        for (Cluster<DoublePoint> c : clusters) {

            if (c.getPoints().size() >= 2) {

                result.add(getCenter(c));
            }

        }

        return result;

    }

    /**
     * This class fetches the locations from the event, and builds a list of DoublePoint.class
     * objects
     */
    private List<DoublePoint> getEventsLocations(int eventId) {

        List<Location> list = management.getUserLocations(eventId);
        List<DoublePoint> locations = new ArrayList<DoublePoint>(0);
        DoublePoint userPoint;

        for (Location userLocation : list) {

            if (!(userLocation == null)) {

                userPoint = new DoublePoint(
                        new double[] {userLocation.getLongitude(), userLocation.getLatitude() }); // Build
                                                                                                   // a
                                                                                                   // DoublePoint
                                                                                                   // from
                                                                                                   // the
                                                                                                   // Location
                locations.add(userPoint);
            }

        }

        return locations;

    }

    /**
     *
     * Method to directly call the Clusterer
     * 
     * @param points
     *            of points which should be clustered
     * @return list of clusters
     */
    public List<? extends Cluster<DoublePoint>> getClusters(List<DoublePoint> points) {

        return clusterer.cluster(points);

    }

    /**
     * Method to directly call the midpoint algorithm
     * 
     * @param cluster
     *            which should be calculated
     * @return midpoint
     */
    public DoublePoint getCenter(Cluster<DoublePoint> cluster) {
        return algorithm.calculateCentralPoint(cluster);
    }

    /**
     * Method which calls the getClusteredCentralPoints method and converts the DoublePoints into
     * Locations
     * 
     * @param event
     *            Event whose locations should be calculated
     * @return List of clustered midpoints
     *
     */
    public List<Location> getClusteredLocations(Event event) {
        List<DoublePoint> pointList = getClusteredCentralPoints(event);
        List<Location> locations = new ArrayList<Location>();

        for (DoublePoint point : pointList) {

            locations.add(new Location(point.getPoint()[0], point.getPoint()[1], ""));

        }

        return locations;
    }
}
