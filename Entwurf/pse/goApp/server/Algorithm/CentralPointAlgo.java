package pse.goApp.server.Algorithm;

/**
 * This is the Interface for calculating a central point of a cluster.
 */
public abstract class CentralPointAlgo {

	/**
	 * This is the abstract method for calculating a clusters' central point.
	 * @param cluster This is the cluster which central point should be calculated.
	 */
	public abstract Point calculateCentralPoint(Cluster cluster);

}