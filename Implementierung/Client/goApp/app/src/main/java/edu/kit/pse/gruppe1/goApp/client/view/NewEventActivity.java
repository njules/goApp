package edu.kit.pse.gruppe1.goApp.client.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.service.*;
import edu.kit.pse.gruppe1.goApp.client.databinding.NewEventActivityBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.Location;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * The NewEventActivity allows the User to create a new Event.
 * It provides a Map, a DatePicker and a TimePicker for the User to do so.
 * It also starts the EventService to let the user interact with the server.
 */
public class NewEventActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private NewEventActivityBinding binding;
    private Location location;
    private TimePicker timepicker;
    private DatePicker datepicker;
    private EditText en;
    private EditText el;
    private Timestamp timestamp;
    private EventService eventService;
    private LocationService locationService;
    private ResultReceiver receiver;

    private GoogleMap googleMap;
    private MarkerOptions marker;

    /**
     * This method creates an intent to start this exact Activity.
     * The method needs to be static because the Activity does not exist when the method is called.
     *
     * @param activity the activity currently running.
     */
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, NewEventActivity.class);
        activity.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.new_event_activity);
        Toolbar groupToolbar = (Toolbar) findViewById(R.id.new_event_toolbar);
        setSupportActionBar(groupToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        en = (EditText) findViewById(R.id.new_event_name);
        el = (EditText) findViewById(R.id.new_event_location);

        en.setImeOptions(EditorInfo.IME_ACTION_DONE);
        el.setImeOptions(EditorInfo.IME_ACTION_DONE);


        timepicker = (TimePicker) findViewById(R.id.time_picker);
        timepicker.setIs24HourView(true);
        datepicker = (DatePicker) findViewById(R.id.date_picker);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_event_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

        eventService = new EventService();
        locationService = new LocationService();
        receiver = new ResultReceiver();

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(EventService.RESULT_CREATE));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(LocationService.RESULT_MY_LOCATION));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.make_event:
                if (location == null) {
                    Toast.makeText(this, getString(R.string.noLocation), Toast.LENGTH_SHORT).show();
                    return true;
                } else if (en.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.nameMissing), Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    location.setName(el.getText().toString());
                }

                /* timepicker.getCurrentHour() and timepicker.getCurrentMinute() are used here although they are deprecated since API level 23.
                 * But because our App is supposed to work on all devices with API level 19 or higher we need to use these methods.*/
                String timeString = datepicker.getDayOfMonth() + "-" + (datepicker.getMonth() + 1) + "-" + datepicker.getYear() + " " + timepicker.getCurrentHour() + ":" + timepicker.getCurrentMinute();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy HH:mm");
                try {
                    timestamp = new Timestamp(sdf.parse(timeString).getTime());
                    if (timestamp.after(new Timestamp(System.currentTimeMillis()))) {
                        eventService.create(this, en.getText().toString(), location, Preferences.getUser(), timestamp, Preferences.getGroup());
                    } else if (timestamp.before(new Timestamp(System.currentTimeMillis()))) {
                        Toast.makeText(this, getString(R.string.wrongTime), Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        Log.i("Timer", "Time Error");
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMapLongClickListener(this);
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

    @Override
    public void onMapLongClick(LatLng latLng) {
        location = new Location(latLng.longitude, latLng.latitude, el.getText().toString());
        googleMap.clear();
        marker.position(latLng);
        marker.title(location.getName());
        googleMap.addMarker(marker);
    }


    /**
     * The ResultReceiver evaluates return messages from earlier started Services.
     */
    private class ResultReceiver extends BroadcastReceiver {
        private AlarmManager notifyAlarmMgr;
        private PendingIntent notifyAlarmIntent;
        private AlarmManager eventAlarmMgr;
        private PendingIntent eventAlarmIntent;
        private int beforeEvent = 900000; // = 15 minutes

        @Override
        public void onReceive(Context context, Intent intent) {
            // If the intent shows any kind of error the user will be notified.
            if (intent.getStringExtra(UtilService.ERROR) != null) {
                Toast.makeText(getApplicationContext(), intent.getStringExtra(UtilService.ERROR), Toast.LENGTH_SHORT).show();
                return;
            }
            switch (intent.getAction()) {
                /* If a user created an event this case sets up a notification shortly before the Event is starting and starts the LocationService at the right time.
                * Afterwards the GroupActivity is starting.*/
                case EventService.RESULT_CREATE:
                    Event event = intent.getParcelableExtra(UtilService.EVENT);
                    Toast.makeText(NewEventActivity.this, "Neues Event erstellt", Toast.LENGTH_SHORT).show();

                    if (new Timestamp(event.getTime().getTime() - beforeEvent).before(new Timestamp(System.currentTimeMillis()))) {
                        Intent locationIntent = new Intent(context, LocationService.class);
                        locationIntent.setAction(LocationService.ACTION_LOCATION);
                        locationIntent.putExtra(UtilService.EVENT, intent.getParcelableExtra(UtilService.EVENT));
                        context.startService(locationIntent);
                        GroupActivity.start(NewEventActivity.this);
                        break;
                    }
                    notifyAlarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent notifyIntent = new Intent(context, NotificationService.class);
                    notifyIntent.putExtra(UtilService.GROUP, Preferences.getGroup());
                    notifyAlarmIntent = PendingIntent.getService(context, 0, notifyIntent, 0);
                    //900000 is 15 mins in millis
                    notifyAlarmMgr.set(AlarmManager.RTC_WAKEUP, event.getTime().getTime() - beforeEvent, notifyAlarmIntent);

                    Log.i("MainThread", Thread.currentThread().getId() + "");

                    eventAlarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent eventIntent = new Intent(context, LocationService.class);
                    eventIntent.setAction(LocationService.ACTION_LOCATION);
                    eventIntent.putExtra(UtilService.EVENT, event);
                    eventAlarmIntent = PendingIntent.getService(context, 0, eventIntent, 0);
                    eventAlarmMgr.setExact(AlarmManager.RTC, event.getTime().getTime() - beforeEvent, eventAlarmIntent);

                    GroupActivity.start(NewEventActivity.this);
                    break;
                // Moves the Map to the Users Location.
                case LocationService.RESULT_MY_LOCATION:
                    android.location.Location location = intent.getParcelableExtra(UtilService.LOCATION);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                    break;
                default:
                    break;
            }
        }
    }
}