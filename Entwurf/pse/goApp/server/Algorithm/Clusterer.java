package pse.goApp.server.Algorithm;

/**
 * Interface for a cluster algorithm.
 */
public abstract class Clusterer {

	/**
	 * Abstract method to calculate a cluster from a given list of points.
	 * @param list The list of points which should be clustered.
	 */
	public abstract Cluster calculateCluster(List<Point> list);

}