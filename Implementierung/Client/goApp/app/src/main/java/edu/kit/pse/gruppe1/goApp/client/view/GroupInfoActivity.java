package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.databinding.GroupInfoActivityBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Group;

public class GroupInfoActivity extends AppCompatActivity {
    private Group group;
    private GroupInfoActivityBinding binding;

    public static void start(Activity activity, Group group) {
        Intent intent = new Intent(activity, GroupInfoActivity.class);
        intent.putExtra("Gruppe", group);
        activity.startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = getIntent().getParcelableExtra("Gruppe");
        binding = DataBindingUtil.setContentView(this, R.layout.group_info_activity);
        binding.setGroup(group);
        Toolbar groupInfoToolbar = (Toolbar) findViewById(R.id.group_info_toolbar);
        setSupportActionBar(groupInfoToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UserFragment fragment = new UserFragment();
        fragmentTransaction.add(R.id.Fragment_Container ,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_info_menu, menu);
        return true;
    }
}