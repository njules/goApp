package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.service.EventService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.LocationService;
import edu.kit.pse.gruppe1.goApp.client.databinding.EventInfoActivityBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.Location;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;
import edu.kit.pse.gruppe1.goApp.client.model.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback {
    private EventInfoActivityBinding binding;
    private RecyclerView participantRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private UserAdapter userAdapter;
    private Event event;
    private ResultReceiver receiver;

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
        binding.setEvent(event);
        participantRecyclerView = (RecyclerView) findViewById(R.id.participants_recycler_view);
        participantRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        participantRecyclerView.setLayoutManager(linearLayoutManager);
        userAdapter = new UserAdapter(fillDataset());
        participantRecyclerView.setAdapter(userAdapter);
        receiver = new ResultReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(EventService.RESULT_GET));
        //TODO hier getEvent von eventService starten
    }

    //TODO For Tests
    private User[] fillDataset() {
        User[] user = new User[20];
        for (int i = 0; i < 20; i++) {
            user[i] = new User(i,"Maxi"+i);
        }
        return user;
    }

    //TODO for Tests real methodes Location Service Cluster + own Location
    private void cluster(){
        ArrayList<Location> locations = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            locations.add(new Location(i*0.01,i*2,"Group"));
        }
        event.setClusterPoints(locations);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("Maps", "Map Ready");
        googleMap.addMarker(new MarkerOptions().position(new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude())).title(event.getLocation().getName()));
        cluster();
        LocationService service = new LocationService();
        Iterator<Location> iter = event.getClusterPoints().iterator();
        while(iter.hasNext()){
            Location location = iter.next();
            googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title(location.getName()));
        }
    }

    private class ResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case EventService.RESULT_GET:
                    if (intent.getBooleanExtra("ERROR", false)) {
                        userAdapter = new UserAdapter(fillDataset());
                        participantRecyclerView.setAdapter(userAdapter);
                    }
                    break;
                    //TODO default
            }
        }
    }
}