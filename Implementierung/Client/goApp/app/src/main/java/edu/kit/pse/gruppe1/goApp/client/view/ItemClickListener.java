package edu.kit.pse.gruppe1.goApp.client.view;

import android.view.View;

/**
 * Interface definition for a callback to be invoked when an Adapter item is clicked.
 */

public interface ItemClickListener {
    void onItemClicked(int position, View view);
}
