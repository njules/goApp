package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.service.EventService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.GroupService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.NotificationService;
import edu.kit.pse.gruppe1.goApp.client.databinding.GroupActivityBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.Location;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;

import java.sql.Date;

public class GroupActivity extends AppCompatActivity implements View.OnClickListener{
    private GroupActivityBinding binding;
    private Group group;
    private RecyclerView newEventRecylcerView;
    private RecyclerView acceptedEventRecyclerView;
    private LinearLayoutManager newEventLayoutManager;
    private LinearLayoutManager acceptedEventLayoutManager;
    private NewEventAdapter newEventAdapter;
    private AcceptedEventAdapter acceptedEventAdapter;

    private GroupService groupService;
    private ResultReceiver receiver;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, GroupActivity.class);
        activity.startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = Preferences.getGroup();
        binding = DataBindingUtil.setContentView(this, R.layout.group_activity);
        binding.setGroup(group);
        Toolbar groupToolbar = (Toolbar) findViewById(R.id.group_toolbar);
        setSupportActionBar(groupToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton newEventFab = (FloatingActionButton) findViewById(R.id.create_event);
        newEventFab.setOnClickListener(this);

        binding.setGroup(group);
        groupService = new GroupService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();


        newEventRecylcerView = (RecyclerView) findViewById(R.id.new_event_recycler_view);
        newEventRecylcerView.setHasFixedSize(true);
        acceptedEventRecyclerView = (RecyclerView) findViewById(R.id.accepted_event_recycler_view);
        acceptedEventRecyclerView.setHasFixedSize(true);

        newEventLayoutManager = new LinearLayoutManager(this);
        newEventLayoutManager.setOrientation(newEventLayoutManager.HORIZONTAL);
        acceptedEventLayoutManager = new LinearLayoutManager(this);
        acceptedEventLayoutManager.setOrientation(acceptedEventLayoutManager.HORIZONTAL);

        newEventRecylcerView.setLayoutManager(newEventLayoutManager);
        acceptedEventRecyclerView.setLayoutManager(acceptedEventLayoutManager);



        acceptedEventAdapter = new AcceptedEventAdapter(fillDataset(), new ItemClickListener() {
            @Override
            public void onItemClicked(int position, View view) {
                Event event = newEventAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.start_event:
                        //TODO service.
                        Log.i("GroupActivity", "go");
                        break;
                    default:
                        EventActivity.start(GroupActivity.this, event);
                        Log.i("GroupActivity", "info");
                }
            }
        });
        acceptedEventRecyclerView.setAdapter(acceptedEventAdapter);

        newEventAdapter = new NewEventAdapter(fillDataset(), new ItemClickListener() {
            @Override
            public void onItemClicked(int position, View view) {
                Event event = newEventAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.accept_event:
                        //service.
                        Log.i("GroupActivity", "accept");
                        break;
                    case R.id.reject_event:
                        //servide.
                        Log.i("GroupActivity", "reject");
                        break;
                    default:
                        EventActivity.start(GroupActivity.this, event);
                        Log.i("GroupActivity", "info");
                }
            }
        });
        newEventRecylcerView.setAdapter(newEventAdapter);

    }

    //Todo löschen, nur zum Testzweck
    private Event[] fillDataset() {
        Event[] events = new Event[20];
        for (int i = 0; i < 20; i++) {
            events[i] = new Event(i, "name" + i, new Date(100000 * i), new Location(i, i, "Random Location" + i));
        }
        return events;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_group_info:
                GroupInfoActivity.start(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        NewEventActivity.start(this);
    }

    private class ResultReceiver extends BroadcastReceiver {
        private AlarmManager notifyAlarmMgr;
        private PendingIntent notifyAlarmIntent;

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case GroupService.ACTION_GET_EVENTS:
                    if(intent.getParcelableArrayExtra("events") == null){break;}
                    Event[] groupEvents = (Event[]) intent.getParcelableArrayExtra("events");
                    newEventAdapter = new NewEventAdapter(fillDataset(), new ItemClickListener() {
                        @Override
                        public void onItemClicked(int position, View view) {
                            Event event = newEventAdapter.getItem(position);
                            switch (view.getId()) {
                                case R.id.accept_event:
                                    //service.
                                    Log.i("GroupActivity", "accept");
                                    break;
                                case R.id.reject_event:
                                    //servide.
                                    Log.i("GroupActivity", "reject");
                                    break;
                                default:
                                    EventActivity.start(GroupActivity.this, event);
                                    Log.i("GroupActivity", "info");
                            }
                        }
                    });
                    newEventRecylcerView.setAdapter(newEventAdapter);
                    break;

                    //TODO default
            }
        }
    }
}