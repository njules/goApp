package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.databinding.GroupInfoFragmentMemberBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;
import edu.kit.pse.gruppe1.goApp.client.model.User;

public class UserFragment extends Fragment implements View.OnClickListener{

    private Group group;
    private GroupInfoFragmentMemberBinding binding;
    RecyclerView memberRecyclerView;
    LinearLayoutManager memberLinearLayoutManager;
    UserAdapter memberAdapter;
    AppCompatButton leave;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = Preferences.getGroup();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.group_info_fragment_member, container, false);
        binding.setGroup(group);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void onStart() {
        super.onStart();
        memberRecyclerView = (RecyclerView) getView().findViewById(R.id.member_recycler_view);
        memberRecyclerView.setHasFixedSize(true);
        memberLinearLayoutManager = new LinearLayoutManager(getActivity());
        memberRecyclerView.setLayoutManager(memberLinearLayoutManager);
        memberAdapter = new UserAdapter(getDataset());
        memberRecyclerView.setAdapter(memberAdapter);
        leave = (AppCompatButton) getView().findViewById(R.id.leave_group_button);
        leave.setOnClickListener(this);
    }

    private User[] getDataset() {
        User[] user = new User[5];
        for (int i = 0; i < 5; i++) {
            user[i] = Preferences.getUser();
        }
        return user;
    }

    @Override
    public void onClick(View view) {
        //TODO delete Group
        StartActivity.start(getActivity());
    }
}