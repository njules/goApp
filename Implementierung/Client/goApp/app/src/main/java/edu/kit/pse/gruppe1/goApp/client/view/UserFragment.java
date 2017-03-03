package edu.kit.pse.gruppe1.goApp.client.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.service.GroupService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.UtilService;
import edu.kit.pse.gruppe1.goApp.client.databinding.GroupInfoFragmentMemberBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;
import edu.kit.pse.gruppe1.goApp.client.model.User;


/**
 * This fragment is loaded into the GroupInfoActivity when the User is not the founder of the Group.
 * It shows a regular member the other members of the Group and gives the opportunity to leave the Group.
 * It also starts the GroupService to let the user interact with the server.
 */
public class UserFragment extends Fragment implements View.OnClickListener {
    private Group group;
    private GroupInfoFragmentMemberBinding binding;
    private RecyclerView memberRecyclerView;
    private LinearLayoutManager memberLinearLayoutManager;
    private UserAdapter memberAdapter;
    private AppCompatButton leave;

    private ResultReceiver receiver;
    private GroupService groupService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = Preferences.getGroup();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.group_info_fragment_member, container, false);
        binding.setGroup(group);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        receiver = new ResultReceiver();
        groupService = new GroupService();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(GroupService.RESULT_DELETE_MEMBER));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(GroupService.RESULT_GET_MEMBERS));

        memberRecyclerView = (RecyclerView) getView().findViewById(R.id.member_recycler_view);
        memberRecyclerView.setHasFixedSize(true);
        memberLinearLayoutManager = new LinearLayoutManager(getActivity());
        memberRecyclerView.setLayoutManager(memberLinearLayoutManager);

        groupService.getMembers(getActivity(), group);


        leave = (AppCompatButton) getView().findViewById(R.id.leave_group_button);
        leave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.leave_group_button) {
            groupService.deleteMember(getActivity(), group, Preferences.getUser());
        }
    }

    /**
     * The ResultReceiver evaluates return messages from earlier started Services.
     */
    private class ResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // If the intent shows any kind of error the user will be notified.
            if (intent.getStringExtra(UtilService.ERROR) != null) {
                Toast.makeText(getActivity(), intent.getStringExtra(UtilService.ERROR), Toast.LENGTH_LONG).show();
                return;
            }
            switch (intent.getAction()) {
                // Loads the Group members in the memberAdapter.
                case GroupService.RESULT_GET_MEMBERS:
                    if (intent.getParcelableArrayExtra(UtilService.USERS) == null) {
                        break;
                    }
                    memberAdapter = new UserAdapter((User[]) intent.getParcelableArrayExtra(UtilService.USERS));
                    memberRecyclerView.setAdapter(memberAdapter);
                    break;
                // Starts the StartActivity since the User is not longer a member of the Group.
                case GroupService.RESULT_DELETE_MEMBER:
                    StartActivity.start(getActivity());
                    break;
                default:
                    break;
            }
        }
    }
}