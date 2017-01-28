package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.databinding.GroupActivityBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.Group;

import java.sql.Date;

public class GroupActivity extends AppCompatActivity implements ItemClickListener{
    private GroupActivityBinding binding;
    private Group group;
    private RecyclerView newEventRecylcerView;
    private RecyclerView acceptedEventRecyclerView;
    private LinearLayoutManager newEventLayoutManager;
    private LinearLayoutManager acceptedEventLayoutManager;
    private NewEventAdapter newEventAdapter;
    private AcceptedEventAdapter acceptedEventAdapter;

    public static void start(Activity activity, Group group) {
        Intent intent = new Intent(activity, GroupActivity.class);
        intent.putExtra("Gruppe", group);
        activity.startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = getIntent().getParcelableExtra("Gruppe");
        binding = DataBindingUtil.setContentView(this, R.layout.group_activity);
        binding.setGroup(group);
        Toolbar groupToolbar = (Toolbar) findViewById(R.id.group_toolbar);
        setSupportActionBar(groupToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        newEventAdapter = new NewEventAdapter(fillDataset(),this);
        newEventRecylcerView.setAdapter(newEventAdapter);
        acceptedEventAdapter = new AcceptedEventAdapter(fillDataset(),this);
        acceptedEventRecyclerView.setAdapter(acceptedEventAdapter);
    }

    private Event[] fillDataset() {
        Event[] events = new Event[20];
        for (int i = 0; i < 20; i++) {
            events[i] = new Event(i, "name"+i,new Date(100000*i));
        }
        return events;
    }

    @Override
    public void onItemClicked(int position) {
    }
}