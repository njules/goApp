package edu.kit.pse.gruppe1.goApp.client.view;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import edu.kit.pse.gruppe1.goApp.client.databinding.StartActivityBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;
import edu.kit.pse.gruppe1.goApp.client.model.Request;
import edu.kit.pse.gruppe1.goApp.client.model.User;


public class StartActivity extends AppCompatActivity implements Communicator {
    private RecyclerView groupRecyclerView;
    private GroupAdapter groupAdapter;
    private RecyclerView.LayoutManager groupLayoutManager;

    private RecyclerView requestRecyclerView;
    private GroupAdapter requestAdapter;
    private RecyclerView.LayoutManager requestLayoutManager;

    private User user;
    private StartActivityBinding binding;

    private GroupSearchService groupSearchService;
    private RequestSearchService requestSearchService;
    private RequestService requestService;
    private UserService userService;

    private ResultReceiver receiver;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.start_activity);
        user = Preferences.getUser(); //Stattdessen wird der User in der LoginActivity geholt
        binding.setUser(user);
        Toolbar startToolbar = (Toolbar) findViewById(R.id.start_toolbar);
        setSupportActionBar(startToolbar);
    }


    //TODO: Wieder löschen nur zum Testzweck
    private Group[] fillGroupDataset() {
        Group[] groups = new Group[20];
        groups[0] = new Group(0, "Test", new User(12948, "Sven"));
        for (int i = 1; i < 20; i++) {
            groups[i] = new Group(i, "name" + i, user);
        }
        return groups;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiver = new ResultReceiver();
        userService = new UserService();
        requestService = new RequestService();
        //reigster Reveicer to revceive the services answers
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(GroupSearchService.RESULT_GET_BY_MEMBER));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(RequestSearchService.RESULT_GET_BY_USER));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(RequestService.RESULT_REJECT));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(UserService.RESULT_CHANGE));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(GroupService.RESULT_CREATE));
        //Group Recycler View
        groupSearchService = new GroupSearchService();
        groupRecyclerView = (RecyclerView) findViewById(R.id.my_groups_recycler_view);
        groupRecyclerView.setHasFixedSize(true);
        groupLayoutManager = new LinearLayoutManager(this);
        groupRecyclerView.setLayoutManager(groupLayoutManager);
        //receiver.onReceive(this,new Intent(GroupSearchService.RESULT_GET_BY_MEMBER));
        groupSearchService.getGroupsByMember(this, user); //TODO doesnt work
        // Todo: Test if Groups come back
        //Request Recycler View
        requestSearchService = new RequestSearchService();
        requestRecyclerView = (RecyclerView) findViewById(R.id.my_requests_recycler_view);
        requestRecyclerView.setHasFixedSize(true);
        requestLayoutManager = new LinearLayoutManager(this);
        requestRecyclerView.setLayoutManager(requestLayoutManager);
        receiver.onReceive(this,new Intent(RequestSearchService.RESULT_GET_BY_USER));
        //requestSearchService.getRequestsByUser(this, user); //TODO doesnt work
        //Todo: Test if Requests come back
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_name:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ChangeNameFragment fragment = new ChangeNameFragment();
                fragmentTransaction.add(fragment, "ChangeNameFragment");
                fragmentTransaction.commit();
                return true;
            case R.id.action_search:
                NewGroupActivity.start(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void respond(String response) {
        user.setName(response);
        Preferences.setUser(user);
        String output = getString(R.string.changeName) + " " + user.getName();
        //userService.changeName(this, user, response);
        //Todo hier Benutzernamen ändern

        Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, StartActivity.class);
        activity.startActivity(intent);
    }

    private class ResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case GroupSearchService.RESULT_GET_BY_MEMBER:
                    Log.i("GroupSearch",intent.toString());
                    if (intent.getParcelableArrayExtra("groups") == null){break;}
                    groupAdapter = new GroupAdapter((Group[])intent.getParcelableArrayExtra("groups"), new ItemClickListener() {
                    //groupAdapter = new GroupAdapter(fillGroupDataset(), new ItemClickListener() {
                        @Override
                        public void onItemClicked(int position, View view) {
                            Group group = groupAdapter.getItem(position);
                            Preferences.setGroup(group);
                            GroupActivity.start(StartActivity.this);
                        }
                    });
                    groupRecyclerView.setAdapter(groupAdapter);
                    break;
                case RequestSearchService.RESULT_GET_BY_USER:
                    //requestAdapter = new GroupAdapter((Group[]) intent.getParcelableArrayExtra("groups"), new ItemClickListener() {
                    requestAdapter = new GroupAdapter(fillGroupDataset(), new ItemClickListener() {
                        @Override
                        public void onItemClicked(int position, View view) {
                            Group group = requestAdapter.getItem(position);
                            //requestService.reject(StartActivity.this, new Request(user, group));
                            //TODO reject request in RequestService
                        }
                    });
                    requestRecyclerView.setAdapter(requestAdapter);
                    break;
                case RequestService.RESULT_REJECT:
                    if (intent.getBooleanExtra("ERROR", false)) {
                        Toast.makeText(StartActivity.this,"Anfrage abgebrochen",Toast.LENGTH_SHORT).show();
                        //requestAdapter.deleteItem()
                        //TODO when to delete the request
                    }
                    //TODO error?? Service shcickt anfangs requestIntent zurück
                    break;
                case UserService.RESULT_CHANGE:
                    if (intent.getBooleanExtra("ERROR", false)) {
                        Toast.makeText(StartActivity.this,"Name geändert",Toast.LENGTH_SHORT).show();
                        //user.setName();
                        //TODO when to change the name
                    }
                    break;


                //TODO default
            }
        }
    }

}