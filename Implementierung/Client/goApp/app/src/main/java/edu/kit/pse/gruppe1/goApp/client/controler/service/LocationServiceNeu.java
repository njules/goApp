package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.HTTPConnection;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.Location;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tobias on 10.02.2017.
 */

public class LocationServiceNeu extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String SERVLET = "LocationServlet";
    private static final String NAME = "LocationServiceNeu";
    public static final String RESULT_LOCATION = "resultLocation";
    public static final String ACTION_LOCATION = "Location";
    public static final String ACTION_MY_LOCATION = "myLocation";
    public static final String RESULT_MY_LOCATION = "myLocation";

    private GoogleApiClient mGoogleApiClient;
    private android.location.Location mLastLocation;
    private int refreshTime = 15000;
    private int eventLength = 3600000;
    private AlarmManager eventAlarmMgr;
    private PendingIntent eventAlarmIntent;
    private Event event;
    private boolean connected;


    public LocationServiceNeu() {
        super(NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this.getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        mGoogleApiClient.connect();
        Log.i("MyLocation", "connect()");
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (!connected) {
        }
        Intent resultIntent = new Intent();
        if (mLastLocation != null) {
            if (intent.getAction().equals(ACTION_MY_LOCATION)) {
                resultIntent.setAction(RESULT_MY_LOCATION);
                resultIntent.putExtra(UtilService.LOCATION, mLastLocation);
            } else {
                event = intent.getParcelableExtra(UtilService.EVENT);
                Location[] locations = syncLocation(event.getId());
                resultIntent.setAction(RESULT_LOCATION);
                resultIntent.putExtra(UtilService.LOCATIONS, locations);


                if (System.currentTimeMillis() + refreshTime < event.getTime().getTime() + eventLength) {
                    eventAlarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                    Intent eventIntent = new Intent(this, LocationService.class);
                    eventIntent.putExtra(UtilService.EVENT, event);
                    eventAlarmIntent = PendingIntent.getService(this, 0, eventIntent, 0);
                    eventAlarmMgr.setWindow(AlarmManager.RTC, System.currentTimeMillis() + refreshTime, refreshTime, eventAlarmIntent);
                }
            }
        }
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        connected = true;
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        connected = true;
    }

    private Location[] syncLocation(int eventId) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.LOC_NAME.toString(), Preferences.getUser().getName());
            requestJson.put(JSONParameter.LONGITUDE.toString(), mLastLocation.getLongitude());
            requestJson.put(JSONParameter.LATITUDE.toString(), mLastLocation.getLatitude());
            requestJson.put(JSONParameter.EVENT_ID.toString(), eventId);
            requestJson.put(JSONParameter.USER_ID.toString(), Preferences.getUser().getId());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.SYNC_LOC.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HTTPConnection connection = new HTTPConnection(SERVLET);
        JSONObject result = connection.sendGetRequest(requestJson.toString());
        if (UtilService.isError(result)) {
            return null;
        } else {
            return UtilService.getLocations(result);
        }
    }
}
