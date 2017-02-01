package edu.kit.pse.gruppe1.goApp.server.algorithm;

import java.util.List;

import org.apache.commons.math4.ml.clustering.Cluster;
import org.apache.commons.math4.ml.clustering.DoublePoint;

/**
 * This class implements the CentralPointAlgo interface so it calculates the central point of a
 * cluster.
 */
public class ImportantMidPointCentral extends CentralPointAlgo {

    private DoublePoint eventLocation;

    /*
     * Constructor which also got the events location;
     */
    public ImportantMidPointCentral(DoublePoint eventLocation) {
        this.eventLocation = eventLocation;
    }

    /*
     * calculates the Midpoint of a cluster an a eventsLocation. The clusters midpoint is weighted
     * twice.
     */
    @Override
    public DoublePoint calculateCentralPoint(Cluster<DoublePoint> cluster) {

        if (cluster.getPoints().isEmpty()) {
            throw new IllegalArgumentException("Cluster is empty");
        }

        List<DoublePoint> list = cluster.getPoints();
        double longitude = 0;
        double latitude = 0;
        for (DoublePoint p : list) {
            longitude += p.getPoint()[0];
            latitude += p.getPoint()[1];
        }

        longitude += (1 / 2) * list.size() * eventLocation.getPoint()[0];
        latitude += (1 / 2) * list.size() * eventLocation.getPoint()[1];

        longitude /= (3 / 2) * list.size();
        latitude /= (3 / 2) * list.size();

        DoublePoint result = new DoublePoint(new double[] { longitude, latitude });

        return result;

    }
}