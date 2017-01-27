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

public class GroupActivity extends AppCompatActivity implements ItemClickListener{
    private GroupActivityBinding binding;
    private Group group;
    private RecyclerView newEventRecylcerView;
    private LinearLayoutManager eventLayoutManager;
    private NewEventAdapter newEventAdapter;

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
        eventLayoutManager = new LinearLayoutManager(this);
        newEventRecylcerView.setLayoutManager(eventLayoutManager);
        newEventAdapter = new NewEventAdapter(fillDataset(),this);
        newEventRecylcerView.setAdapter(newEventAdapter);
    }

    private Event[] fillDataset() {
        return null;
    }

    @Override
    public void onItemClicked(int position) {

    }
}