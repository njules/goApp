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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import android.widget.Toast;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.service.GroupSearchService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.GroupService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.RequestService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.UtilService;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;

/**
 * This Activity lets the User send requests to already existing Groups in addition to create Groups on his own.
 * It also starts the GroupSearchService, the GroupService and the RequestService to let the user interact with the server.
 */
public class NewGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView groupRecyclerView;
    private GroupAdapter groupAdapter;
    private RecyclerView.LayoutManager groupLayoutManager;
    private EditText et;
    private ResultReceiver receiver;
    private GroupSearchService groupSearchService;
    private RequestService requestService;

    /**
     * This method creates an intent to start this exact Activity.
     * The method needs to be static because the Activity does not exist when the method is called.
     *
     * @param activity the activity currently running.
     */
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
        et.setImeOptions(EditorInfo.IME_ACTION_SEARCH); // Lupe
        et.setImeActionLabel("search",3); //tried to implement search
        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(view.getId() == R.id.search_edit_text){
                    if(keyEvent.getKeyCode()==KeyEvent.KEYCODE_ENTER){
                        if (view != null) {
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        String search = et.getText().toString();
                        if(search.isEmpty()){
                            Toast.makeText(getApplicationContext(), R.string.empty_group_name, Toast.LENGTH_SHORT).show();
                        }else {
                            groupSearchService.getGroupsByName(NewGroupActivity.this, search);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
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

        groupRecyclerView = (RecyclerView) findViewById(R.id.new_groups_recycler_view);
        groupRecyclerView.setHasFixedSize(true);
        groupLayoutManager = new LinearLayoutManager(this);
        groupRecyclerView.setLayoutManager(groupLayoutManager);

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(GroupSearchService.RESULT_GET_BY_NAME));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(RequestService.RESULT_CREATE));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(GroupService.RESULT_CREATE));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                String search = et.getText().toString();
                if(search.isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.empty_group_name, Toast.LENGTH_SHORT).show();
                }else {
                    groupSearchService.getGroupsByName(this, search);
                }
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
                //The User gets feedback about his search.
                case GroupSearchService.RESULT_GET_BY_NAME:
                    if (intent.getParcelableArrayExtra(UtilService.GROUPS) == null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.NoGroup), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    groupAdapter = new GroupAdapter((Group[]) intent.getParcelableArrayExtra(UtilService.GROUPS), new ItemClickListener() {
                        @Override
                        public void onItemClicked(int position, View view) {
                            Group group = groupAdapter.getItem(position);
                            requestService.create(NewGroupActivity.this, Preferences.getUser(), group);
                        }
                    });
                    groupRecyclerView.setAdapter(groupAdapter);
                    break;
                //The User gets feedback that a request has been send.
                case RequestService.RESULT_CREATE:
                    Toast.makeText(getApplicationContext(), getString(R.string.request_send), Toast.LENGTH_SHORT).show();
                    break;
                //The User gets feedback about his new Group and the StartActivity is started.
                case GroupService.RESULT_CREATE:
                    Toast.makeText(NewGroupActivity.this, getString(R.string.groupCreated), Toast.LENGTH_SHORT).show();
                    StartActivity.start(NewGroupActivity.this);
                    break;
                default:
                    break;
            }
        }
    }
}