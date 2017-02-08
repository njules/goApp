package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.Toast;
import com.google.android.gms.maps.SupportMapFragment;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.service.GroupService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.UtilService;
import edu.kit.pse.gruppe1.goApp.client.databinding.GroupInfoFragmentAdminBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;
import edu.kit.pse.gruppe1.goApp.client.model.User;


public class AdminFragment extends Fragment implements ItemClickListener, View.OnClickListener{

    GroupInfoFragmentAdminBinding binding;
    Group group;
    RecyclerView memberRecyclerView;
    RecyclerView requestRecyclerView;
    LinearLayoutManager memberLinearLayoutManager;
    LinearLayoutManager requestLinearLayoutManager;
    RequestAdapter requestAdapter;
    MemberAdapter memberAdapter;
    AppCompatButton delete;

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

        memberRecyclerView = (RecyclerView) getView().findViewById(R.id.member_recycler_view);
        requestRecyclerView = (RecyclerView) getView().findViewById(R.id.request_recycler_view);
        memberRecyclerView.setHasFixedSize(true);
        requestRecyclerView.setHasFixedSize(true);
        memberLinearLayoutManager = new LinearLayoutManager(getActivity());
        requestLinearLayoutManager = new LinearLayoutManager(getActivity());
        memberRecyclerView.setLayoutManager(memberLinearLayoutManager);
        requestRecyclerView.setLayoutManager(requestLinearLayoutManager);

        requestAdapter = new RequestAdapter(fillDataset(), this);
        requestRecyclerView.setAdapter(requestAdapter);
        memberAdapter = new MemberAdapter(fillDataset(),this);
        memberRecyclerView.setAdapter(memberAdapter);
        delete = (AppCompatButton) getView().findViewById(R.id.delete_group_button);
        delete.setOnClickListener(this);
    }

    //TODO For Tests
    private User[] fillDataset() {
        User[] user = new User[20];
        for (int i = 0; i < 20; i++) {
            user[i] = new User(i,"Maxi"+i);
        }
        return user;
    }
    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.group_info_admin_menu, menu);
    }


    @Override
    public void onItemClicked(int position, View view) {
        switch (view.getId()){
            case R.id.accept_member :
                Toast.makeText(getActivity(),"Du hast " + requestAdapter.getItem(position).getName() + " hinzugefügt",Toast.LENGTH_SHORT).show();
                //TODO add member
                break;
            case R.id.reject_member :
                Toast.makeText(getActivity(),"Du hast " + requestAdapter.getItem(position).getName() + " abgelehnt" , Toast.LENGTH_SHORT).show();
                //TODO delete request
                break;
            case R.id.delete_member :
                Toast.makeText(getActivity(),"Du hast "+ memberAdapter.getItem(position).getName() + " entfernt",Toast.LENGTH_SHORT).show();
                //TODO delete Member
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.delete_group_button){
            Toast.makeText(getActivity(),"Gruppe wird gelöscht",Toast.LENGTH_SHORT).show();
            //TODO delete Group
            StartActivity.start(getActivity());
        }
        //TODO else error massage
    }

    private class ResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case GroupService.RESULT_GET_MEMBERS:
                    if (intent.getBooleanExtra(UtilService.ERROR, false)) {
                    }
                    break;
                //TODO default
            }
        }
    }
}