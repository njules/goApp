package edu.kit.pse.gruppe1.goApp.client.view;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.service.EventService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.LocationService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.UtilService;
import edu.kit.pse.gruppe1.goApp.client.databinding.EventInfoActivityBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.Location;
import edu.kit.pse.gruppe1.goApp.client.model.User;

import java.util.Arrays;

/**
 * The EventActivity displays all information about an Event including a dynamic Map.
 * It also starts the EventService to let the user interact with the server.
 */
public class EventActivity extends AppCompatActivity implements OnMapReadyCallback {
    private EventInfoActivityBinding binding;
    private RecyclerView participantRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private UserAdapter userAdapter;
    private GoogleMap googleMap;
    private MarkerOptions marker;

    private Event event;

    private ResultReceiver receiver;
    private EventService eventService;


    /**
     * This method creates an intent to start this exact Activity.
     * The method needs to be static because the Activity does not exist when the method is called.
     *
     * @param activity the activity currently running.
     * @param event    the event the user wants information about.
     */
    public static void start(Activity activity, Event event) {
        Intent intent = new Intent(activity, EventActivity.class);
        intent.putExtra(UtilService.EVENT, event);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = getIntent().getParcelableExtra(UtilService.EVENT);
        binding = DataBindingUtil.setContentView(this, R.layout.event_info_activity);
        binding.setEvent(event);
        Toolbar groupToolbar = (Toolbar) findViewById(R.id.event_info_toolbar);
        setSupportActionBar(groupToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        receiver = new ResultReceiver();
        eventService = new EventService();

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(EventService.RESULT_GET));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(LocationService.RESULT_MY_LOCATION));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(LocationService.RESULT_LOCATION));

        participantRecyclerView = (RecyclerView) findViewById(R.id.participants_recycler_view);
        participantRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        participantRecyclerView.setLayoutManager(linearLayoutManager);

        eventService.getEvent(this, event.getId());
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        marker = new MarkerOptions();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET};
            ActivityCompat.requestPermissions(this, permissions, 0);
            return;
        }
        googleMap.setMyLocationEnabled(true);

        Intent intent = new Intent(this, LocationService.class);
        intent.setAction(LocationService.ACTION_MY_LOCATION);
        this.startService(intent);
    }

    /**
     * The ResultReceiver evaluates return messages from earlier started Services.
     */
    private class ResultReceiver extends BroadcastReceiver {
        private LatLng positionEvent;

        @Override
        public void onReceive(Context context, Intent intent) {
            // If the intent shows any kind of error the user will be notified.
            if (intent.getStringExtra(UtilService.ERROR) != null) {
                Toast.makeText(getApplicationContext(), intent.getStringExtra(UtilService.ERROR), Toast.LENGTH_SHORT).show();
                return;
            }
            switch (intent.getAction()) {
                // loads the participants of the event in the userAdapter.
                case EventService.RESULT_GET:
                        User[] participants = (User[]) intent.getParcelableArrayExtra(UtilService.USERS);
                        User[] startedParticipants = (User[]) intent.getParcelableArrayExtra(UtilService.STARTED_USERS);
                        User[] allParticipants;

                        if (startedParticipants == null) {
                            allParticipants = participants;
                        } else if (participants == null) {
                            allParticipants = startedParticipants;
                        } else {
                            allParticipants = Arrays.copyOf(startedParticipants, startedParticipants.length + participants.length);
                            System.arraycopy(participants, 0, allParticipants, startedParticipants.length, participants.length);
                        }
                        userAdapter = new UserAdapter(allParticipants);
                        participantRecyclerView.setAdapter(userAdapter);
                    break;
                // Moves the Map to the Users Location.
                case LocationService.RESULT_MY_LOCATION:
                    positionEvent = new LatLng(event.getLocation().getLongitude(), event.getLocation().getLatitude());
                    googleMap.addMarker(new MarkerOptions().title(event.getLocation().getName()).position(positionEvent));
                    android.location.Location location = intent.getParcelableExtra(UtilService.LOCATION);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                    break;
                // Refreshes the markers on the map, representing the Group Locations.
                case LocationService.RESULT_LOCATION:
                    Log.i("Location", "Location received");
                    if(intent.getParcelableArrayExtra(UtilService.LOCATIONS)!=null) {
                        Event intentEvent = intent.getParcelableExtra(UtilService.EVENT);
                        if(intentEvent.getId()==event.getId()) {
                            googleMap.clear();
                            positionEvent = new LatLng(event.getLocation().getLongitude(), event.getLocation().getLatitude());
                            googleMap.addMarker(new MarkerOptions().title(event.getLocation().getName()).position(positionEvent));
                            Location[] locations = (Location[]) intent.getParcelableArrayExtra(UtilService.LOCATIONS);
                            for (int i = 0; i < locations.length; i++) {
                                LatLng position = new LatLng(locations[i].getLongitude(), locations[i].getLatitude());
                                googleMap.addMarker(new MarkerOptions().title(locations[i].getName()).position(position));
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}