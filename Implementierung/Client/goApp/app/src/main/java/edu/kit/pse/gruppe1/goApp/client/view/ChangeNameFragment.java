package edu.kit.pse.gruppe1.goApp.client.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;
import edu.kit.pse.gruppe1.goApp.client.R;

/**
 * This class is used whenever a User- or  Group name should be changed.
 */
public class ChangeNameFragment extends DialogFragment {
    private Communicator communicator;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View text = inflater.inflate(R.layout.change_name_dialog, null);
        communicator = (Communicator) getActivity();

        builder.setMessage(R.string.changeName)
                .setView(text)
                .setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText et = (EditText) text.findViewById(R.id.changeName);
                        String response = et.getText().toString();
                        if (response.trim().isEmpty()) {
                            Toast.makeText(getActivity(), getString(R.string.retryName), Toast.LENGTH_SHORT).show();
                            onCreateDialog(savedInstanceState);
                        } else {
                            communicator.respond(response);
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
