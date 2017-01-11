package edu.kit.sdqweb.pse.gruppe1.goApp.server.algorithm;

import edu.kit.sdqweb.pse.gruppe1.goApp.client.model.*;

/**
 * This class is facade for the algortihm, requests the events locations from the database and calls the methods of the Clusterer and the CentralPointAlgo.
 */
public class ClusterFacade {

	/**
	 * This method calculate clusters from the given list of points and calculate the clusters' central point.
	 * 
	 * @return central points of the calculated clusters
	 * @param event List of points which should clustered and which central point should be calculated.
	 */
	public List<Point> getClusteredCentralPoints(Event event) {
		// TODO - implement ClusterFacade.getClusteredCentralPoints
		throw new UnsupportedOperationException();
	}

}