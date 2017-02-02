package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.HTTPConnection;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.*;
import org.json.JSONException;
import org.json.JSONObject;

import static android.location.Criteria.ACCURACY_HIGH;

/**
 * This Service is in charge of synchronizing the Users and the Group Location.
 */
public class LocationService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String ACTION_GET = "GET";
    private static final String SERVLET = "LocationServlet";
    // minimum time interval between location updates, in milliseconds
    private static final long MIN_TIME = 3600;
    // minimum distance between location updates, in meters
    private static final float MIN_DISTANCE = 100;
    private static final String ACTION_USER_LOCATION = "USER_LOCATION";

    private GoogleApiClient mGoogleApiClient;
    private User user;
    private int eventId;

    /**
     * sends the clients current location to the server and updates the group location of the event on the client. This method is started at the specific time and is performed periodically
     *
     * @param user  the user who's location is updated
     * @param event the event which's group locations are returned
     * @return true, if method was successful, otherwise false
     */
    public void syncLocation(Context context, User user, Event event) {
        this.user = user;
        eventId = event.getId();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    public void stopSyncLocation() {
//TODO implement Methode
        mGoogleApiClient.disconnect();
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
    public void onConnected(@Nullable Bundle bundle) {
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
         //   return;
        //}
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            user.setLocation(new edu.kit.pse.gruppe1.goApp.client.model.Location(mLastLocation.getLatitude(),mLastLocation.getLongitude(),"Me"));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}