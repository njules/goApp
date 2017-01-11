package edu.kit.sdqweb.pse.gruppe1.goApp.server.algorithm;

import edu.kit.sdqweb.pse.gruppe1.goApp.client.model.*;

/**
 * This class is facade for the algortihm, requests the events locations from the database and calls the methods of the Clusterer and the CentralPointAlgo.
 * It also generates Points from the given coordinates, because the Algorithm need them.
 */
public class ClusterFacade {

	/**
	 * This method fetches all locations from the database which belong to the event and calls the cluster algorithm to get it clustered. After that it calls the MidpointAlgo to get the midpoints from the calculated clusters.
	 * 
	 * @return central points of the calculated clusters
	 * @param event List of points which should clustered and which central point should be calculated.
	 */
	public List<Point> getClusteredCentralPoints(Event event) {
		// TODO - implement ClusterFacade.getClusteredCentralPoints
		throw new UnsupportedOperationException();
	}

}