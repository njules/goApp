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
import edu.kit.pse.gruppe1.goApp.client.controler.service.LocationServiceNeu;
import edu.kit.pse.gruppe1.goApp.client.controler.service.UtilService;
import edu.kit.pse.gruppe1.goApp.client.databinding.EventInfoActivityBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.Location;
import edu.kit.pse.gruppe1.goApp.client.model.User;

import java.util.ArrayList;
import java.util.Iterator;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback {
    private EventInfoActivityBinding binding;
    private RecyclerView participantRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private UserAdapter userAdapter;
    private Event event;
    private ResultReceiver receiver;
    private EventService eventService;

    private GoogleMap googleMap;
    private MarkerOptions marker;

    public static void start(Activity activity, Event event) {
        Intent intent = new Intent(activity, EventActivity.class);
        intent.putExtra("Event", event);
        activity.startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = getIntent().getParcelableExtra("Event");
        binding = DataBindingUtil.setContentView(this,R.layout.event_info_activity);
        binding.setEvent(event);
        Toolbar groupToolbar = (Toolbar) findViewById(R.id.event_info_toolbar);
        setSupportActionBar(groupToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        eventService = new EventService();
        receiver = new ResultReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(EventService.RESULT_GET));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(LocationServiceNeu.RESULT_MY_LOCATION));

        binding.setEvent(event);
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

        Intent intent = new Intent(this, LocationServiceNeu.class);
        intent.setAction(LocationServiceNeu.ACTION_MY_LOCATION);
        this.startService(intent);
    }

    private class ResultReceiver extends BroadcastReceiver {
        private LatLng positionEvent;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(UtilService.ERROR) != null) {
                Toast.makeText(getApplicationContext(), intent.getStringExtra(UtilService.ERROR), Toast.LENGTH_LONG).show();
                return;
            }
            switch (intent.getAction()) {
                case EventService.RESULT_GET:
                    if (intent.getParcelableArrayExtra(UtilService.USERS ) != null) {
                        userAdapter = new UserAdapter((User[]) intent.getParcelableArrayExtra(UtilService.USERS));
                        participantRecyclerView.setAdapter(userAdapter);
                    }
                    break;
                case LocationServiceNeu.RESULT_MY_LOCATION:
                    positionEvent = new LatLng(event.getLocation().getLongitude(), event.getLocation().getLatitude());
                    googleMap.addMarker(new MarkerOptions().title(event.getLocation().getName()).position(positionEvent));
                    android.location.Location location = (android.location.Location)intent.getParcelableExtra(UtilService.LOCATION);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                    break;
                case LocationServiceNeu.RESULT_LOCATION:
                    googleMap.clear();
                    positionEvent = new LatLng(event.getLocation().getLongitude(), event.getLocation().getLatitude());
                    googleMap.addMarker(new MarkerOptions().title(event.getLocation().getName()).position(positionEvent));
                    Location[] locations = (Location[]) intent.getParcelableArrayExtra(UtilService.LOCATIONS);
                    for (int i = 0; i < locations.length; i++) {
                        LatLng position = new LatLng(locations[i].getLatitude(), locations[i].getLongitude());
                        googleMap.addMarker(new MarkerOptions().title(locations[i].getName()).position(position));
                    }
            }
        }
    }
}