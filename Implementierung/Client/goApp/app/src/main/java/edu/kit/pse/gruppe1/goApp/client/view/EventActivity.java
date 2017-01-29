package edu.kit.pse.gruppe1.goApp.client.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.Location;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback {
    Event event;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_info_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()))
                .title(getString(R.string.event_location)));
        for (int i = 0; i < event.getClusterPoints().size(); i++) {
           // googleMap.addMarker(new MarkerOptions()
           //         .position(new LatLng(event.getClusterPoints(), event.getLocation().getLongitude()))
            //        .title(getString(R.string.event_location)));
        }
    }
}