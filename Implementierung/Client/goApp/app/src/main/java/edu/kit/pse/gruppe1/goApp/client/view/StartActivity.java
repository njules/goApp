package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.databinding.StartActivityBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.User;


public class StartActivity extends AppCompatActivity{
    private RecyclerView groupRecyclerView;
    private RecyclerView.Adapter groupAdapter;
    private RecyclerView.LayoutManager groupLayoutManager;

    private RecyclerView requestRecyclerView;
    private RecyclerView.Adapter requestAdapter;
    private RecyclerView.LayoutManager requestLayoutManager;

    private User user;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.start_activity);
        user = new User(17, "Maxi");
        binding.setUser(user);
        Toolbar startToolbar = (Toolbar) findViewById(R.id.start_toolbar);
        setSupportActionBar(startToolbar);
    }

    //TODO: Wieder löschen nur zum Testzweck
    private Group[] fillGroupDataset() {
        Group[] groups = new Group[20];
        for (int i = 0; i < 20; i++) {
            groups[i] = new Group(i, "name" + i);
        }
        return groups;
    }

    //TODO: Wieder löschen nur zum Testzweck
    private Group[] fillDataset() {
        Group[] groups = new Group[3];
        groups[0] = new Group(23, "Test");
        groups[1] = new Group(243, "Beispiel");
        groups[2] = new Group(123, "Penis");
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
        groupRecyclerView = (RecyclerView) findViewById(R.id.my_groups_recycler_view);
        groupRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        groupLayoutManager = new LinearLayoutManager(this);
        groupRecyclerView.setLayoutManager(groupLayoutManager);
        // specify an adapter
        groupAdapter = new GroupAdapter(fillGroupDataset());
        groupRecyclerView.setAdapter(groupAdapter);

        requestRecyclerView = (RecyclerView) findViewById(R.id.my_requests_recycler_view);
        requestRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        groupLayoutManager = new LinearLayoutManager(this);
        requestRecyclerView.setLayoutManager(groupLayoutManager);
        // specify an adapter
        groupAdapter = new GroupAdapter(fillDataset());
        requestRecyclerView.setAdapter(groupAdapter);
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
                fragmentTransaction.add(fragment, "Test");
                fragmentTransaction.commit();
                return true;
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}