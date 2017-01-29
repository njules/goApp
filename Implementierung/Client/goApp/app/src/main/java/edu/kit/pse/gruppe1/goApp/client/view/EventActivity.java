package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.Location;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback {
    Event event;

    public static void start(Activity activity, Event event) {
        Intent intent = new Intent(activity, EventActivity.class);
        intent.putExtra("Event", event);
        activity.startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = getIntent().getParcelableExtra("Event");
        setContentView(R.layout.event_info_activity);
        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("Maps", "Map Ready");
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Test"));
        //googleMap.addMarker(new MarkerOptions().position(new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude())).title(getString(R.string.event_location)));
        //for (int i = 0; i < event.getClusterPoints().size(); i++) {
        // googleMap.addMarker(new MarkerOptions()
        //         .position(new LatLng(event.getClusterPoints(), event.getLocation().getLongitude()))
        //        .title(getString(R.string.event_location)));
        //}
    }
}