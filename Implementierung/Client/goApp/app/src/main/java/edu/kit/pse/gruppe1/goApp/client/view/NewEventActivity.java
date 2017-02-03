package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.service.EventService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.NotificationService;
import edu.kit.pse.gruppe1.goApp.client.databinding.NewEventActivityBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.Location;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NewEventActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private NewEventActivityBinding binding;
    private Location location;
    private TimePicker timepicker;
    private DatePicker datepicker;
    private EditText en;
    private EditText el;
    private Timestamp timestamp;

    GoogleMap googleMap;
    MarkerOptions marker;


    private ResultReceiver receiver;

    public static void start (Activity activity){
        Intent intent = new Intent(activity, NewEventActivity.class);
        activity.startActivity(intent);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.new_event_activity);
        Toolbar groupToolbar = (Toolbar) findViewById(R.id.new_event_toolbar);
        setSupportActionBar(groupToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        en = (EditText)findViewById(R.id.new_event_name);
        el = (EditText)findViewById(R.id.new_event_location);

        timepicker = (TimePicker)findViewById(R.id.time_picker);
        timepicker.setIs24HourView(true);
        datepicker = (DatePicker)findViewById(R.id.date_picker);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_event_menu, menu);
        return true;
    }

    @Override
    protected void onStart(){
        super.onStart();
        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
        receiver = new ResultReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(EventService.RESULT_CREATE));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.make_event:
                if(location == null){
                    Toast.makeText(this, getString(R.string.noLocation), Toast.LENGTH_SHORT).show();
                    return true;
                }
                else{
                    location.setName(el.getText().toString());
                }


                String timeString = datepicker.getDayOfMonth() +"-"+ (datepicker.getMonth()+1) +"-"+ datepicker.getYear() +" "+ timepicker.getCurrentHour() +":"+ timepicker.getCurrentMinute();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm");
                try {
                    timestamp = new Timestamp(sdf.parse(timeString).getTime());
                    Log.i("ourTime", timestamp.toString());
                    Log.i("sysTime", new Timestamp(System.currentTimeMillis()).toString());
                    if(timestamp.after(new Timestamp(System.currentTimeMillis()))){
                        //TODO hier mit Event Service neues event erstellen.
                    } else {
                        Toast.makeText(this, getString(R.string.wrongTime), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMapLongClickListener(this);
        marker = new MarkerOptions();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        location = new Location(latLng.longitude, latLng.latitude, el.getText().toString());
        googleMap.clear();
        marker.position(latLng);
        marker.title(location.getName());
        googleMap.addMarker(marker);
    }

    private class ResultReceiver extends BroadcastReceiver {
        private AlarmManager notifyAlarmMgr;
        private PendingIntent notifyAlarmIntent;

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case EventService.RESULT_CREATE:
                    if (intent.getBooleanExtra("ERROR", false)) {
                        Toast.makeText(NewEventActivity.this,"Neues Event erstellt",Toast.LENGTH_SHORT).show();

                        notifyAlarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                        Intent notifyIntent = new Intent(context, NotificationService.class);
                        notifyAlarmIntent = PendingIntent.getService(context, 0, notifyIntent, 0);
                        notifyAlarmMgr.set(AlarmManager.RTC_WAKEUP, timestamp.getTime(), notifyAlarmIntent);

                        GroupActivity.start(NewEventActivity.this);
                    }
                    break;
                    //TODO default
            }
        }
    }
}