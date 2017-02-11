package edu.kit.pse.gruppe1.goApp.client.view;

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
import com.google.android.gms.maps.SupportMapFragment;
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


public class AdminFragment extends Fragment implements ItemClickListener, View.OnClickListener,Communicator {

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
    private String newName;

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


    public void onStart() {
        super.onStart();

        receiver = new ResultReceiver();
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
        requestRecyclerView = (RecyclerView) getView().findViewById(R.id.request_recycler_view);
        memberRecyclerView.setHasFixedSize(true);
        requestRecyclerView.setHasFixedSize(true);
        memberLinearLayoutManager = new LinearLayoutManager(getActivity());
        requestLinearLayoutManager = new LinearLayoutManager(getActivity());
        memberRecyclerView.setLayoutManager(memberLinearLayoutManager);
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
                requestService.accept(getActivity(), new Request(requestAdapter.getItem(position), group));
                newMember = requestAdapter.getItem(position);
                requestPosition = position;
                break;
            case R.id.reject_member:
                requestService.reject(getActivity(), new Request(requestAdapter.getItem(position), group));
                requestPosition = position;
                break;
            case R.id.delete_member:
                memberPosition = position;
                groupService.deleteMember(getActivity(), group, memberAdapter.getItem(position));
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_change_name){
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ChangeNameFragment fragment = new ChangeNameFragment();
            fragmentTransaction.add(fragment, "ChangeNameFragment");
            fragmentTransaction.commit();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.delete_group_button) {
            groupService.delete(getActivity(), group);
        }
        //TODO else error massage
    }

    @Override
    public void respond(String response) {
        groupService.setName(getActivity(),group,response);
        newName = response;
    }

    private class ResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(UtilService.ERROR) != null) {
                Toast.makeText(getActivity(), intent.getStringExtra(UtilService.ERROR), Toast.LENGTH_SHORT).show();
                return;
            }
            switch (intent.getAction()) {
                case GroupService.RESULT_GET_MEMBERS:
                    if (intent.getParcelableArrayExtra(UtilService.USERS) == null){break;}
                    memberAdapter = new MemberAdapter((User[]) intent.getParcelableArrayExtra(UtilService.USERS), AdminFragment.this);
                    memberRecyclerView.setAdapter(memberAdapter);
                    break;
                case RequestSearchService.RESULT_GET_BY_GROUP:
                    if (intent.getParcelableArrayExtra(UtilService.USERS) == null){break;}
                    requestAdapter = new RequestAdapter((User[]) intent.getParcelableArrayExtra(UtilService.USERS), AdminFragment.this);
                    requestRecyclerView.setAdapter(requestAdapter);
                    break;
                case GroupService.RESULT_DELETE_MEMBER:
                    memberAdapter.deleteItem(memberPosition);
                    Toast.makeText(getContext(), "Du hast " + memberAdapter.getItem(memberPosition).getName() + " entfernt", Toast.LENGTH_SHORT).show();
                    break;
                case GroupService.RESULT_DELETE:
                    Toast.makeText(getActivity(), "Gruppe wird gelöscht", Toast.LENGTH_SHORT).show();
                    StartActivity.start(getActivity());
                    break;
                case RequestService.RESULT_ACCEPT:
                    Toast.makeText(getActivity(), "Du hast " + newMember.getName() + " hinzugefügt", Toast.LENGTH_SHORT).show();
                    memberAdapter.insertItem(newMember);
                    requestAdapter.delete(requestPosition);
                    break;
                case RequestService.RESULT_REJECT:
                    Toast.makeText(getActivity(), "Du hast " + requestAdapter.getItem(requestPosition).getName() + " abgelehnt", Toast.LENGTH_SHORT).show();
                    requestAdapter.delete(requestPosition);
                    break;
                case GroupService.RESULT_SET_NAME:
                    Toast.makeText(getContext(),"Neuer Name: " + newName,Toast.LENGTH_SHORT).show();
                    Preferences.getGroup().setName(newName);
                    group = Preferences.getGroup();
                    break;

                //TODO default
            }
        }
    }
}