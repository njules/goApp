package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.location.Location;
import android.os.Bundle;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.HTTPConnection;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.*;
import org.json.JSONException;
import org.json.JSONObject;

import static android.location.Criteria.ACCURACY_HIGH;

/**
 * This Service is in charge of synchronizing the Users and the Group Location.
 */
public class LocationService implements LocationListener {

    private static final String ACTION_GET = "GET";
    private static final String SERVLET = "LocationServlet";
    // minimum time interval between location updates, in milliseconds
    private static final long MIN_TIME = 3600;
    // minimum distance between location updates, in meters
    private static final float MIN_DISTANCE = 100;
    private static final String ACTION_USER_LOCATION = "USER_LOCATION";

    private int userId = 0;
    private int eventId = 0;

    /**
     * sends the clients current location to the server and updates the group location of the event on the client. This method is started at the specific time and is performed periodically
     *
     * @param user  the user who's location is updated
     * @param event the event which's group locations are returned
     * @return true, if method was successful, otherwise false
     */
    public void syncLocation(Context context, User user, Event event) {
        userId = user.getId();
        eventId = event.getId();
        LocationManager locationManager = context.getSystemService(LocationManager.class);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(ACCURACY_HIGH);

        locationManager.requestLocationUpdates(locationManager.getBestProvider(criteria, true), MIN_TIME, MIN_DISTANCE, this);
    }

    private void getGroupLocation(int eventId) {
        JSONObject requestJson = new JSONObject();

        try {

            requestJson.put(JSONParameter.EventID.toString(), eventId);
            requestJson.put(JSONParameter.Method.toString(), ACTION_GET);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HTTPConnection connection = new HTTPConnection(SERVLET);
        JSONObject result = connection.sendGetRequest(requestJson.toString());
        try {
            //TODO else
            if (result.getInt(JSONParameter.ErrorCode.toString()) == 0) {
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.EventID.toString(), eventId);
            requestJson.put(JSONParameter.UserID.toString(), userId);
            requestJson.put(JSONParameter.Latitude.toString(), location.getLatitude());
            requestJson.put(JSONParameter.Longitude.toString(),location.getLongitude());
            requestJson.put(JSONParameter.Method.toString(), ACTION_USER_LOCATION);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HTTPConnection connection = new HTTPConnection(SERVLET);
        JSONObject result = connection.sendPostRequest(requestJson.toString());
        try {
            //TODO else
            if (result.getInt(JSONParameter.ErrorCode.toString()) == 0) {
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getGroupLocation(eventId);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}