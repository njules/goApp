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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
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
import edu.kit.pse.gruppe1.goApp.client.controler.service.*;
import edu.kit.pse.gruppe1.goApp.client.databinding.GroupActivityBinding;
import edu.kit.pse.gruppe1.goApp.client.model.*;

import java.sql.Date;
import java.sql.Timestamp;

public class GroupActivity extends AppCompatActivity implements View.OnClickListener {
    private GroupActivityBinding binding;
    private Group group;
    private Event eventMove;
    private int deletePosition;
    private RecyclerView newEventRecylcerView;
    private RecyclerView acceptedEventRecyclerView;
    private LinearLayoutManager newEventLayoutManager;
    private LinearLayoutManager acceptedEventLayoutManager;
    private NewEventAdapter newEventAdapter;
    private AcceptedEventAdapter acceptedEventAdapter;

    private ParticipateService participateService;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        participateService = new ParticipateService();
        groupService = new GroupService();
        receiver = new ResultReceiver();

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(GroupService.RESULT_GET_EVENTS));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(ParticipateService.RESULT_STATUS));

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

        groupService.getEvents(this, group, Preferences.getUser());
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
        private AlarmManager eventAlarmMgr;
        private PendingIntent eventAlarmIntent;
        private int beforEvent = 900000;
        ;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(UtilService.ERROR) != null) {
                Toast.makeText(getApplicationContext(), intent.getStringExtra(UtilService.ERROR), Toast.LENGTH_SHORT).show();
                return;
            }
            switch (intent.getAction()) {
                case GroupService.RESULT_GET_EVENTS:
                    loadEvents(intent);
                    break;
                case ParticipateService.RESULT_STATUS:
                    int resultStatus = intent.getIntExtra(UtilService.STATUS, 0);
                    if (resultStatus == Status.PARTICIPATE.getValue()) {
                        accept(context);
                    } else if (resultStatus == Status.REJECTED.getValue()) {
                        newEventAdapter.deleteItem(deletePosition);
                    } else if (resultStatus == Status.STARTED.getValue()) {
                        EventActivity.start(GroupActivity.this, eventMove);
                    }
                    break;

                //TODO default
            }
        }

        private void accept(Context context) {
            newEventAdapter.deleteItem(deletePosition);
            if(acceptedEventAdapter==null){
                Log.i("GroupActivity", eventMove.getName());
                Event[] accEvents = {eventMove};
                acceptedEventAdapter = new AcceptedEventAdapter(accEvents,new ItemClickListener() {
                    @Override
                    public void onItemClicked(int position, View view) {
                        Event event = acceptedEventAdapter.getItem(position);
                        eventMove = event;
                        switch (view.getId()) {
                            case R.id.start_event:
                                participateService.setStatus(GroupActivity.this, event, Preferences.getUser(), Status.STARTED);
                                break;
                            default:
                                EventActivity.start(GroupActivity.this, event);
                        }
                    }
                });
                acceptedEventRecyclerView.setAdapter(acceptedEventAdapter);
            } else {
                Log.i("GroupActivity", eventMove.getName());
                acceptedEventAdapter.insertItem(eventMove);
            }

            if (new Timestamp(eventMove.getTime().getTime() - beforEvent).before(new Timestamp(System.currentTimeMillis()))) {
                Intent locationIntent = new Intent(context,LocationServiceNeu.class);
                locationIntent.setAction(LocationServiceNeu.ACTION_LOCATION);
                locationIntent.putExtra(UtilService.EVENT,eventMove);
                context.startService(locationIntent);
                return;
            } else {

                notifyAlarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent notifyIntent = new Intent(context, NotificationService.class);
                notifyIntent.putExtra(UtilService.GROUP, Preferences.getGroup());
                notifyAlarmIntent = PendingIntent.getService(context, 0, notifyIntent, 0);
                notifyAlarmMgr.set(AlarmManager.RTC_WAKEUP, eventMove.getTime().getTime() - beforEvent, notifyAlarmIntent);

                eventAlarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent eventIntent = new Intent(context, LocationServiceNeu.class);
                eventIntent.putExtra(UtilService.EVENT, eventMove);
                eventAlarmIntent = PendingIntent.getService(context, 0, eventIntent, 0);
                eventAlarmMgr.setExact(AlarmManager.RTC, eventMove.getTime().getTime() - beforEvent, eventAlarmIntent);
            }
        }


        private void loadEvents(Intent intent) {
            if (intent.getParcelableArrayExtra(UtilService.ACCEPTED_EVENTS) != null) {
                acceptedEventAdapter = new AcceptedEventAdapter((Event[]) intent.getParcelableArrayExtra(UtilService.ACCEPTED_EVENTS), new ItemClickListener() {
                    @Override
                    public void onItemClicked(int position, View view) {
                        Event event = acceptedEventAdapter.getItem(position);
                        eventMove = event;
                        switch (view.getId()) {
                            case R.id.start_event:
                                participateService.setStatus(GroupActivity.this, event, Preferences.getUser(), Status.STARTED);
                                break;
                            default:
                                EventActivity.start(GroupActivity.this, event);
                        }
                    }
                });
                acceptedEventRecyclerView.setAdapter(acceptedEventAdapter);
            }
            if (intent.getParcelableArrayExtra(UtilService.NEW_EVENTS) != null) {
                newEventAdapter = new NewEventAdapter((Event[]) intent.getParcelableArrayExtra(UtilService.NEW_EVENTS), new ItemClickListener() {
                    @Override
                    public void onItemClicked(int position, View view) {
                        Event event = newEventAdapter.getItem(position);
                        eventMove = event;
                        deletePosition = position;
                        switch (view.getId()) {
                            case R.id.accept_event:
                                participateService.setStatus(GroupActivity.this, event, Preferences.getUser(), Status.PARTICIPATE);
                                break;
                            case R.id.reject_event:
                                participateService.setStatus(GroupActivity.this, event, Preferences.getUser(), Status.REJECTED);
                                break;
                            default:
                                EventActivity.start(GroupActivity.this, event);
                        }
                    }
                });
                newEventRecylcerView.setAdapter(newEventAdapter);
            }
        }
    }
}