package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
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
import edu.kit.pse.gruppe1.goApp.client.controler.service.GroupSearchService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.GroupService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.RequestService;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;

public class NewGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView groupRecyclerView;
    private GroupAdapter groupAdapter;
    private RecyclerView.LayoutManager groupLayoutManager;
    private EditText et;
    private ResultReceiver receiver;
    private GroupSearchService groupSearchService;
    private RequestService requestService;

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
        receiver = new ResultReceiver();
        groupSearchService = new GroupSearchService();
        requestService = new RequestService();
        //RecyclerViews
        groupRecyclerView = (RecyclerView) findViewById(R.id.new_groups_recycler_view);
        groupRecyclerView.setHasFixedSize(true);
        groupLayoutManager = new LinearLayoutManager(this);
        groupRecyclerView.setLayoutManager(groupLayoutManager);
        //Receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(GroupSearchService.RESULT_GET_BY_NAME));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(RequestService.RESULT_CREATE));
    }

    //TODO: Wieder löschen nur zum Testzweck
    private Group[] fillGroupDataset() {
        Group[] groups = new Group[15];
        for (int i = 0; i < 15; i++) {
            groups[i] = new Group(i, "name" + i, Preferences.getUser());
        }
        return groups;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //TODO Tastatur ausblenden
                String search = et.getText().toString();
                receiver.onReceive(this,new Intent(GroupSearchService.RESULT_GET_BY_NAME));
                //groupSearchService.getGroupsByName(this,search);
                //Todo Hier nach den angefragten Gruppen suchen (GroupSearchService)
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

    private class ResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case GroupSearchService.RESULT_GET_BY_NAME:
                    //groupAdapter = new GroupAdapter((Group[])intent.getParcelableArrayExtra("groups"), new ItemClickListener() {
                    groupAdapter = new GroupAdapter(fillGroupDataset(), new ItemClickListener() {
                        @Override
                        public void onItemClicked(int position, View view) {
                            Group group = groupAdapter.getItem(position);
                            //requestService.create(NewGroupActivity.this,Preferences.getUser(),group);
                            //Todo Hier Anfrage erstellen
                            String output = "" + group.getName() + R.string.request_send;
                            Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
                        }
                    });
                    groupRecyclerView.setAdapter(groupAdapter);
                    break;
                case RequestService.RESULT_CREATE:
                    if (intent.getBooleanExtra("ERROR", false)) {
                        Toast.makeText(NewGroupActivity.this,"Anfrage gesendet",Toast.LENGTH_SHORT).show();
                        //TODO reload groups or add group to adapter
                    }
                    break;
                case GroupService.RESULT_CREATE:
                    if (intent.getBooleanExtra("ERROR", false)) {
                        Toast.makeText(NewGroupActivity.this,"Gruppe hinzugefügt",Toast.LENGTH_SHORT).show();
                        StartActivity.start(NewGroupActivity.this);
                    }
                    break;
                    //TODO reaction to errors and how to updat view
            }
        }
    }
}