package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.HTTPConnection;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.Location;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tobias on 07.02.2017.
 */

public class LocationService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String SERVLET = "LocationServlet";
    private static final String NAME = "LocationService";
    public static final String RESULT_LOCATION = "resultLocation";

    private GoogleApiClient mGoogleApiClient;
    private android.location.Location mLastLocation;

    public LocationService() {
        super(NAME);
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            Event event = intent.getParcelableExtra(UtilService.EVENT);
            Location[] locations = syncLocation(event.getId());
            Intent resultIntent = new Intent();
            resultIntent.setAction(RESULT_LOCATION);
            resultIntent.putExtra(UtilService.LOCATIONS, locations);
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
            manager.sendBroadcast(resultIntent);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private Location[] syncLocation(int eventId){
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.LocationName.toString(), Preferences.getUser().getName());
            requestJson.put(JSONParameter.Longitude.toString(), mLastLocation.getLongitude());
            requestJson.put(JSONParameter.Latitude.toString(), mLastLocation.getLatitude());
            requestJson.put(JSONParameter.EventID.toString(), eventId);
            requestJson.put(JSONParameter.UserID.toString(),Preferences.getUser().getId());
            //TODO JsonParameter Methode Location
            requestJson.put(JSONParameter.Method.toString(), JSONParameter.Methods.GET_CLUSTER.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HTTPConnection connection = new HTTPConnection(SERVLET);
        JSONObject result = connection.sendGetRequest(requestJson.toString());
        try {
            //TODO else & ErroCode parameter
            if (result.getInt(JSONParameter.ErrorCode.toString()) == 0) {
                JSONArray latitude = result.getJSONArray(JSONParameter.Latitude.toString());
                JSONArray longitude = result.getJSONArray(JSONParameter.Longitude.toString());
                JSONArray name = result.getJSONArray(JSONParameter.LocationName.toString());
                Location[] locations = new Location[latitude.length()];
                for (int i = 0; i < name.length(); i++) {
                     locations[i] = new Location(
                             (double) longitude.get(i),
                             (double) latitude.get(i),
                             (String) name.get(i));
                }
                return locations;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
