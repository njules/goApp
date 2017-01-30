package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.kit.pse.gruppe1.goApp.client.R;

public class AdminFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.group_info_fragment_admin, container, false);
    }

}