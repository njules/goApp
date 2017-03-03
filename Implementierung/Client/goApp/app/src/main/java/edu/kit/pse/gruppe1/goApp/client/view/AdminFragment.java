package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.view.*;
import android.widget.Toast;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.service.GroupService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.RequestSearchService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.RequestService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.UtilService;
import edu.kit.pse.gruppe1.goApp.client.databinding.GroupInfoFragmentAdminBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;
import edu.kit.pse.gruppe1.goApp.client.model.Request;
import edu.kit.pse.gruppe1.goApp.client.model.User;


/**
 * This fragment is loaded into the GroupInfoActivity when the User is the founder of the Group.
 * It shows the founder all relevant information about the Group.
 * It also starts the GroupService, the RequestService and the RequestSearchService to let the user interact with the server.
 */
public class AdminFragment extends Fragment implements ItemClickListener, View.OnClickListener {

    private GroupInfoFragmentAdminBinding binding;
    private Group group;
    private RecyclerView memberRecyclerView;
    private RecyclerView requestRecyclerView;
    private LinearLayoutManager memberLinearLayoutManager;
    private LinearLayoutManager requestLinearLayoutManager;
    private RequestAdapter requestAdapter;
    private MemberAdapter memberAdapter;
    private AppCompatButton delete;

    private int memberPosition;
    private int requestPosition;
    private User newMember;

    private RequestService requestService;
    private GroupService groupService;
    private RequestSearchService requestSearchService;
    private ResultReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = Preferences.getGroup();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflats the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.group_info_fragment_admin, container, false);
        binding.setGroup(group);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        receiver = new ResultReceiver(getActivity());
        requestSearchService = new RequestSearchService();
        groupService = new GroupService();
        requestService = new RequestService();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(GroupService.RESULT_GET_MEMBERS));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(RequestSearchService.RESULT_GET_BY_GROUP));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(RequestService.RESULT_ACCEPT));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(RequestService.RESULT_REJECT));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(GroupService.RESULT_SET_NAME));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(GroupService.RESULT_DELETE));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(GroupService.RESULT_DELETE_MEMBER));

        memberRecyclerView = (RecyclerView) getView().findViewById(R.id.member_recycler_view);
        memberRecyclerView.setHasFixedSize(true);
        memberLinearLayoutManager = new LinearLayoutManager(getActivity());
        memberRecyclerView.setLayoutManager(memberLinearLayoutManager);

        requestRecyclerView = (RecyclerView) getView().findViewById(R.id.request_recycler_view);
        requestRecyclerView.setHasFixedSize(true);
        requestLinearLayoutManager = new LinearLayoutManager(getActivity());
        requestRecyclerView.setLayoutManager(requestLinearLayoutManager);

        requestSearchService.getRequestsByGroup(getActivity(), group);
        groupService.getMembers(getActivity(), group);

        delete = (AppCompatButton) getView().findViewById(R.id.delete_group_button);
        delete.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.group_info_admin_menu, menu);
    }


    @Override
    public void onItemClicked(int position, View view) {
        switch (view.getId()) {
            case R.id.accept_member:
                newMember = requestAdapter.getItem(position);
                requestPosition = position;
                requestService.accept(getActivity(), new Request(newMember, group));
                break;
            case R.id.reject_member:
                requestPosition = position;
                requestService.reject(getActivity(), new Request(requestAdapter.getItem(position), group));
                break;
            case R.id.delete_member:
                memberPosition = position;
                if (memberAdapter.getItem(position).getId() != Preferences.getUser().getId()) {
                    groupService.deleteMember(getActivity(), group, memberAdapter.getItem(position));
                } else {
                    Toast.makeText(getActivity(), getString(R.string.DeleteError), Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_name) {
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ChangeNameFragment fragment = new ChangeNameFragment();
            fragmentTransaction.add(fragment, "ChangeNameFragment");
            fragmentTransaction.commit();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.delete_group_button) {
            groupService.delete(getActivity(), group);
        }
    }

    /**
     * The ResultReceiver evaluates return messages from earlier started Services.
     */
    private class ResultReceiver extends BroadcastReceiver {

        Activity activity;

        public ResultReceiver(Activity activity){
            super();
            this.activity = activity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // If the intent shows any kind of error the user will be notified.
            if (intent.getStringExtra(UtilService.ERROR) != null) {
                Toast.makeText(context, intent.getStringExtra(UtilService.ERROR), Toast.LENGTH_SHORT).show();
                return;
            }
            switch (intent.getAction()) {
                // Loads the Group members in the memberAdapter.
                case GroupService.RESULT_GET_MEMBERS:
                    if (intent.getParcelableArrayExtra(UtilService.USERS) == null) {
                        break;
                    }
                    memberAdapter = new MemberAdapter((User[]) intent.getParcelableArrayExtra(UtilService.USERS), AdminFragment.this);
                    memberRecyclerView.setAdapter(memberAdapter);
                    break;
                // Loads all Requests connected with the Group in the requestAdapter.
                case RequestSearchService.RESULT_GET_BY_GROUP:
                    if (intent.getParcelableArrayExtra(UtilService.USERS) == null) {
                        break;
                    }
                    requestAdapter = new RequestAdapter((User[]) intent.getParcelableArrayExtra(UtilService.USERS), AdminFragment.this);
                    requestRecyclerView.setAdapter(requestAdapter);
                    break;
                // Deletes a member from the memberAdapter.
                case GroupService.RESULT_DELETE_MEMBER:
                    memberAdapter.deleteItem(memberPosition);
                    break;
                // Starts the StartActivity since the Group does not longer exist.
                case GroupService.RESULT_DELETE:
                    StartActivity.start(activity);
                    break;
                // Inserts a new member into the memberAdapter and deletes the related request from the requestAdapter.
                case RequestService.RESULT_ACCEPT:
                    memberAdapter.insertItem(requestAdapter.getItem(requestPosition));
                    requestAdapter.delete(requestPosition);
                    break;
                // Deletes a request from the requestAdapter.
                case RequestService.RESULT_REJECT:
                    requestAdapter.delete(requestPosition);
                    break;
                // Changes the name of the displayed Group.
                case GroupService.RESULT_SET_NAME:
                    String name = intent.getStringExtra(UtilService.NAME);
                    group.setName(name);
                    Preferences.setGroup(group);
                    binding.setGroup(group);
                    break;
                default:
                    break;
            }
        }
    }
}