package edu.kit.pse.gruppe1.goApp.server.algorithm;

import static org.junit.Assert.*;

import org.apache.commons.math4.ml.clustering.Cluster;
import org.apache.commons.math4.ml.clustering.DoublePoint;
import org.junit.Test;

public class SimpleCentralTest {

  @Test
  public void test() {
    Cluster<DoublePoint> cluster = new Cluster<DoublePoint>();

    cluster.addPoint(new DoublePoint(new double[] { 3.2, 5.6 }));
    cluster.addPoint(new DoublePoint(new double[] { 34, 53 }));
    cluster.addPoint(new DoublePoint(new double[] { 0, 0 }));
    cluster.addPoint(new DoublePoint(new double[] { 7, 5 }));
    cluster.addPoint(new DoublePoint(new double[] { 5.574457, 5.6 }));
    cluster.addPoint(new DoublePoint(new double[] { 5465474, 7865.8 }));

    SimpleCentral algo = new SimpleCentral();

    DoublePoint solution = algo.calculateCentralPoint(cluster);
    double[] point = solution.getPoint();
    double longitude = point[0];
    double latitude = point[1];

    assertTrue(longitude - 910920.6291 <= 0.0001 && longitude - 910920.6291 >= -0.0001);
    assertTrue(latitude - 1322.5 <= 0.0001 && latitude - 1322.5 >= -0.0001);

  }



  @Test(expected=IllegalArgumentException.class)
  public void testEmptyCluster() {
    Cluster<DoublePoint> cluster = new Cluster<DoublePoint>();
    SimpleCentral algo = new SimpleCentral();
    algo.calculateCentralPoint(cluster);
  }
}
