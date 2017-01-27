package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.model.Group;

public class NewGroupActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener {
    private RecyclerView groupRecyclerView;
    private GroupAdapter groupAdapter;
    private RecyclerView.LayoutManager groupLayoutManager;
    private EditText et;

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
        et = (EditText) findViewById(R.id.search_edit_text);

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.new_group_button);
        myFab.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_group_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        groupRecyclerView = (RecyclerView) findViewById(R.id.new_groups_recycler_view);
        groupRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        groupLayoutManager = new LinearLayoutManager(this);
        groupRecyclerView.setLayoutManager(groupLayoutManager);
    }

    //TODO: Wieder löschen nur zum Testzweck
    private Group[] fillGroupDataset() {
        Group[] groups = new Group[15];
        for (int i = 0; i < 15; i++) {
            groups[i] = new Group(i, "name" + i);
        }
        return groups;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                et.getText();
                //Todo Hier nach den angefragten Gruppen suchen (GroupSearchService)
                groupAdapter = new GroupAdapter(fillGroupDataset(), this);
                groupRecyclerView.setAdapter(groupAdapter);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NewGroupFragment fragment = new NewGroupFragment();
        fragmentTransaction.add(fragment, "NewGroupFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClicked(int position) {
        Group group = groupAdapter.getItem(position);
        //Todo Hier Anfrage erstellen
        String output = ""+group.getName() + R.string.request_send;
        Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
    }
}