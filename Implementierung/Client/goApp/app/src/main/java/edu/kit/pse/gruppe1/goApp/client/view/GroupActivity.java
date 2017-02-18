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

import java.sql.Timestamp;

/**
 * The GroupActivity shows every upcoming Event of the Group to the User.
 * It also starts the GroupService and the ParticipateService to let the user interact with the server.
 */
public class GroupActivity extends AppCompatActivity implements View.OnClickListener {
    private GroupActivityBinding binding;
    private Group group;
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

    /**
     * This method creates an intent to start this exact Activity.
     * The method needs to be static because the Activity does not exist when the method is called.
     *
     * @param activity the activity currently running.
     */
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, GroupActivity.class);
        activity.startActivity(intent);
    }

    @Override
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
        newEventLayoutManager = new LinearLayoutManager(this);
        newEventLayoutManager.setOrientation(newEventLayoutManager.HORIZONTAL);
        newEventRecylcerView.setLayoutManager(newEventLayoutManager);

        acceptedEventRecyclerView = (RecyclerView) findViewById(R.id.accepted_event_recycler_view);
        acceptedEventRecyclerView.setHasFixedSize(true);
        acceptedEventLayoutManager = new LinearLayoutManager(this);
        acceptedEventLayoutManager.setOrientation(acceptedEventLayoutManager.HORIZONTAL);
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

    /**
     * The ResultReceiver evaluates return messages from earlier started Services.
     */
    private class ResultReceiver extends BroadcastReceiver {
        private AlarmManager notifyAlarmMgr;
        private PendingIntent notifyAlarmIntent;
        private AlarmManager eventAlarmMgr;
        private PendingIntent eventAlarmIntent;
        private int beforeEvent = 900000;

        @Override
        public void onReceive(Context context, Intent intent) {
            // If the intent shows any kind of error the user will be notified.
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
                        accept(context,(Event)intent.getParcelableExtra(UtilService.EVENT));
                    } else if (resultStatus == Status.REJECTED.getValue()) {
                        newEventAdapter.deleteItem(deletePosition);
                    } else if (resultStatus == Status.STARTED.getValue()) {
                        EventActivity.start(GroupActivity.this, (Event) intent.getParcelableExtra(UtilService.EVENT));
                    }
                    break;
                default:
                    break;
            }
        }

        /**
         * This method deletes an Event from the newEventAdapter and adds it to the acceptedEventAdapter instead.
         * The method also takes care of setting up a notification shortly before the Event is starting and starts the LocationService at the right time.
         *
         * @param context is needed to start Services out of this method.
         */
        private void accept(Context context,Event intentEvent) {
            newEventAdapter.deleteItem(deletePosition);
            if (acceptedEventAdapter == null) {
                Log.i("GroupActivity", intentEvent.getName());
                Event[] accEvents = {intentEvent};
                acceptedEventAdapter = new AcceptedEventAdapter(accEvents, new ItemClickListener() {
                    @Override
                    public void onItemClicked(int position, View view) {
                        Event event = acceptedEventAdapter.getItem(position);
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
                Log.i("GroupActivity", intentEvent.getName());
                acceptedEventAdapter.insertItem(intentEvent);
            }

            if (new Timestamp(intentEvent.getTime().getTime() - beforeEvent).before(new Timestamp(System.currentTimeMillis()))) {
                Intent locationIntent = new Intent(context, LocationService.class);
                locationIntent.setAction(LocationService.ACTION_LOCATION);
                locationIntent.putExtra(UtilService.EVENT, intentEvent);
                context.startService(locationIntent);
                return;
            } else {

                notifyAlarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent notifyIntent = new Intent(context, NotificationService.class);
                notifyIntent.putExtra(UtilService.GROUP, Preferences.getGroup());
                notifyAlarmIntent = PendingIntent.getService(context, 0, notifyIntent, 0);
                notifyAlarmMgr.set(AlarmManager.RTC_WAKEUP, intentEvent.getTime().getTime() - beforeEvent, notifyAlarmIntent);

                eventAlarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent eventIntent = new Intent(context, LocationService.class);
                eventIntent.putExtra(UtilService.EVENT, intentEvent);
                eventAlarmIntent = PendingIntent.getService(context, 0, eventIntent, 0);
                eventAlarmMgr.setExact(AlarmManager.RTC, intentEvent.getTime().getTime() - beforeEvent, eventAlarmIntent);
            }
        }


        /**
         * This method loads the events in the right Adapters.
         *
         * @param intent the intent broadcast by the GroupService.
         */
        private void loadEvents(Intent intent) {
            if (intent.getParcelableArrayExtra(UtilService.ACCEPTED_EVENTS) != null) {
                acceptedEventAdapter = new AcceptedEventAdapter((Event[]) intent.getParcelableArrayExtra(UtilService.ACCEPTED_EVENTS), new ItemClickListener() {
                    @Override
                    public void onItemClicked(int position, View view) {
                        Event event = acceptedEventAdapter.getItem(position);
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