package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.*;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

/**
 * The StartActivity offers an overview above all Groups and Requests of a User.
 * In addition the User can change his name here.
 */
public class StartActivity extends AppCompatActivity implements Communicator {
    private RecyclerView groupRecyclerView;
    private GroupAdapter groupAdapter;
    private RecyclerView.LayoutManager groupLayoutManager;

    private RecyclerView requestRecyclerView;
    private GroupAdapter requestAdapter;
    private RecyclerView.LayoutManager requestLayoutManager;

    private User user;
    private StartActivityBinding binding;
    private int deletePosition;
    private String newUserName;

    private GroupSearchService groupSearchService;
    private RequestSearchService requestSearchService;
    private RequestService requestService;
    private UserService userService;
    private ResultReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.start_activity);
        user = Preferences.getUser();
        binding.setUser(user);
        Toolbar startToolbar = (Toolbar) findViewById(R.id.start_toolbar);
        setSupportActionBar(startToolbar);
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
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(RequestService.RESULT_DELETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(UserService.RESULT_CHANGE));

        //Group Recycler View
        groupSearchService = new GroupSearchService();
        groupRecyclerView = (RecyclerView) findViewById(R.id.my_groups_recycler_view);
        groupRecyclerView.setHasFixedSize(true);
        groupLayoutManager = new LinearLayoutManager(this);
        groupRecyclerView.setLayoutManager(groupLayoutManager);
        groupSearchService.getGroupsByMember(this, user);

        //Request Recycler View
        requestSearchService = new RequestSearchService();
        requestRecyclerView = (RecyclerView) findViewById(R.id.my_requests_recycler_view);
        requestRecyclerView.setHasFixedSize(true);
        requestLayoutManager = new LinearLayoutManager(this);
        requestRecyclerView.setLayoutManager(requestLayoutManager);
        requestSearchService.getRequestsByUser(this, user);
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
        newUserName = response;
        userService.changeName(this, user, response);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    /**
     * The ResultReceiver evaluates return messages from earlier started Services.
     */
    private class ResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // If the intent shows any kind of error the user will be notified.
            if (intent.getStringExtra(UtilService.ERROR) != null) {
                Toast.makeText(getApplicationContext(), intent.getStringExtra(UtilService.ERROR), Toast.LENGTH_SHORT).show();
                return;
            }
            switch (intent.getAction()) {
                //The groups of the User are loaded into the groupAdapter.
                case GroupSearchService.RESULT_GET_BY_MEMBER:
                    if (intent.getParcelableArrayExtra(UtilService.GROUPS) == null) {
                        break;
                    }
                    groupAdapter = new GroupAdapter((Group[]) intent.getParcelableArrayExtra(UtilService.GROUPS), new ItemClickListener() {
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
                //The groups where the User requested to join are loaded into the requestAdapter.
                case RequestSearchService.RESULT_GET_BY_USER:
                    if (intent.getParcelableArrayExtra(UtilService.GROUPS) == null) {
                        break;
                    }
                    requestAdapter = new GroupAdapter((Group[]) intent.getParcelableArrayExtra(UtilService.GROUPS), new ItemClickListener() {
                        @Override
                        public void onItemClicked(int position, View view) {
                            deletePosition = position;
                            Group group = requestAdapter.getItem(position);
                            showDialog(group);
                        }
                    });
                    requestRecyclerView.setAdapter(requestAdapter);
                    break;
                //Deletes a request from the request adapter.
                case RequestService.RESULT_DELETE:
                    Toast.makeText(StartActivity.this, getString(R.string.deletedRequest), Toast.LENGTH_SHORT).show();
                    requestAdapter.deleteItem(deletePosition);
                    break;
                //Changes the displayed username.
                case UserService.RESULT_CHANGE:
                    user.setName(newUserName);
                    Preferences.setUser(user);
                    break;
                default:
                    break;
            }
        }

        /**
         * This is called when a User clicks on a Request and gives him the opportunity to delete it.
         *
         * @param group the group affiliated with the request.
         */
        private void showDialog(final Group group) {
            AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);

            builder.setMessage(R.string.deleteRequest)
                    .setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            requestService.delete(StartActivity.this, new Request(user, group));
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            builder.show();
        }
    }
}