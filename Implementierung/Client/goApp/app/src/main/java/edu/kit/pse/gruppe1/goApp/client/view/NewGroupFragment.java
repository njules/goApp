package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.service.GroupService;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;

/**
 * Created by Tobias on 26.01.2017.
 */

public class NewGroupFragment extends DialogFragment {
    private String response;
    private GroupService groupService;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        groupService = new GroupService();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View text = inflater.inflate(R.layout.new_group_dialog, null);

        builder.setMessage(R.string.newGroup)
                .setView(text)
                .setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText et = (EditText) text.findViewById(R.id.new_group_name);
                        if(et.getText().toString().trim().isEmpty()){
                            Log.i("Input","Empty String");
                        } else {
                            groupService.create(getActivity(), et.getText().toString(), Preferences.getUser());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
