package edu.kit.sdqweb.pse.gruppe1.goApp.server.algorithm;

/**
 * This is the Interface for calculating a central point of a cluster.
 */
public abstract class CentralPointAlgo {

	/**
	 * This is the abstract method for calculating a clusters' central point.
	 * @param cluster This is the cluster which central point should be calculated.
	 */
	public abstract Point calculateCentralPoint(CentroidCluster cluster);

}