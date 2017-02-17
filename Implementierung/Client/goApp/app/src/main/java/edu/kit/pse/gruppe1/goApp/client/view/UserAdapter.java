package edu.kit.pse.gruppe1.goApp.client.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.databinding.UserViewBinding;
import edu.kit.pse.gruppe1.goApp.client.model.User;

/**
 * This adapter provides a binding from an data set of users to views that are displayed within a RecyclerView.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private User[] dataset;

    /**
     * Creates the UserAdapter.
     *
     * @param userDataset contains the Users to display.
     */
    public UserAdapter(User[] userDataset) {
        dataset = userDataset;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.user_view, parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final User user = dataset[position];
        holder.getBinding().setUser(user);
        holder.getBinding().executePendingBindings();
    }

    /**
     * Gets a single item of the data set.
     *
     * @param position the position of the User to get.
     * @return the User displayed at the given position.
     */
    public User getItem(int position) {
        return dataset[position];
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private UserViewBinding binding;

        /**
         * Creates the UserViewHolder.
         *
         * @param b the binding of the UserViewHolder.
         */
        public UserViewHolder(UserViewBinding b) {
            super(b.getRoot());
            binding = b;
            binding.executePendingBindings();
        }

        /**
         * Returns the UserViewBinding.
         *
         * @return the binding of the UserViewHolder.
         */
        public UserViewBinding getBinding() {
            return binding;
        }
    }
}
