package edu.kit.pse.gruppe1.goApp.server.algorithm;

import java.awt.geom.Point2D;

import org.apache.commons.math4.ml.clustering.CentroidCluster;
import org.apache.commons.math4.ml.clustering.Cluster;
import org.apache.commons.math4.ml.clustering.DoublePoint;

import edu.kit.pse.gruppe1.goApp.server.model.Location;

/**
 * This is the Interface for calculating a central point of a cluster.
 */
public abstract class CentralPointAlgo {

  /**
   * This is the abstract method for calculating a clusters' central point.
   * 
   * @param cluster: This is the cluster which central point should be calculated.
   * @return midpoint of cluster
   */
  public abstract DoublePoint calculateCentralPoint(Cluster<DoublePoint> cluster);

}
