package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.model.Group;

public class NewGroupActivity extends AppCompatActivity {
    private RecyclerView groupRecyclerView;
    private RecyclerView.Adapter groupAdapter;
    private RecyclerView.LayoutManager groupLayoutManager;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, NewGroupActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_group_activity);

        Toolbar newGroupToolbar = (Toolbar) findViewById(R.id.new_group_toolbar);
        setSupportActionBar(newGroupToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        groupRecyclerView = (RecyclerView) findViewById(R.id.new_groups_recycler_view);
        groupRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        groupLayoutManager = new LinearLayoutManager(this);
        groupRecyclerView.setLayoutManager(groupLayoutManager);
        // specify an adapter
        groupAdapter = new GroupAdapter(fillGroupDataset());
        groupRecyclerView.setAdapter(groupAdapter);
    }

    //TODO: Wieder löschen nur zum Testzweck
    private Group[] fillGroupDataset() {
        Group[] groups = new Group[15];
        for (int i = 0; i < 15; i++) {
            groups[i] = new Group(i, "name" + i);
        }
        return groups;
    }
}