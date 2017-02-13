package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.service.GroupService;
import edu.kit.pse.gruppe1.goApp.client.databinding.GroupInfoActivityBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;

public class GroupInfoActivity extends AppCompatActivity implements Communicator {
    private Group group;
    private GroupInfoActivityBinding binding;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, GroupInfoActivity.class);
        activity.startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.group_info_activity);
        group = Preferences.getGroup();
        binding.setGroup(group);

        Toolbar groupInfoToolbar = (Toolbar) findViewById(R.id.group_info_toolbar);
        setSupportActionBar(groupInfoToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (group.getFounder().getId() == Preferences.getUser().getId()) {

            AdminFragment fragment = new AdminFragment();
            fragmentTransaction.add(R.id.Fragment_Container, fragment);
            fragmentTransaction.commit();
        } else {
            UserFragment fragment = new UserFragment();
            fragmentTransaction.add(R.id.Fragment_Container, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_name:
                android.app.FragmentManager fragmentManager = getFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ChangeNameFragment fragment = new ChangeNameFragment();
                fragmentTransaction.add(fragment, "ChangeNameFragment");
                fragmentTransaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void respond(String response) {
        GroupService groupService = new GroupService();
        groupService.setName(this, group, response);
    }

}