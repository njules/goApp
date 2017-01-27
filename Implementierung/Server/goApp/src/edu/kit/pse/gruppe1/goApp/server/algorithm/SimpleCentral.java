package edu.kit.pse.gruppe1.goApp.server.algorithm;

import java.util.EmptyStackException;
import java.util.List;

import org.apache.commons.math4.ml.clustering.*;

/**
 * This class implements the CentralPointAlgo interface so it calculates the central point of a cluster.
 */
public class SimpleCentral extends CentralPointAlgo {

	@Override
	public DoublePoint calculateCentralPoint(Cluster<DoublePoint> cluster) {
		
	  if(cluster.getPoints().isEmpty()) {
	    throw new IllegalArgumentException("Cluster is empty");
	  }
	  
	  List<DoublePoint> list = cluster.getPoints();
		double longitude = 0;
		double latitude = 0;
		for(DoublePoint p: list) {
		  longitude += p.getPoint()[0];
		  latitude += p.getPoint()[1];
		}
		
		longitude /= list.size();
		latitude /= list.size();
		
		DoublePoint result = new DoublePoint(new double[]{longitude, latitude});
		
		return result;
		
	}
}