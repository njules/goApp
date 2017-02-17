package edu.kit.pse.gruppe1.goApp.client.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.databinding.MemberViewBinding;
import edu.kit.pse.gruppe1.goApp.client.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This adapter provides a binding from an data set of users to views that are displayed within a RecyclerView.
 * This adapter is used to display members of a Group with a button to remove them from said Group.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    private List<User> dataset;
    private final ItemClickListener itemClickListener;

    /**
     * Creates the MemberAdapter.
     *
     * @param userDataset contains the Users to display.
     * @param icl         defines how to react to user interaction.
     */
    public MemberAdapter(User[] userDataset, ItemClickListener icl) {
        dataset = new ArrayList<>(Arrays.asList(userDataset));
        itemClickListener = icl;
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MemberViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.member_view, parent, false);
        return new MemberViewHolder(binding, itemClickListener);
    }

    @Override
    public void onBindViewHolder(MemberViewHolder holder, int position) {
        final User user = dataset.get(position);
        holder.getBinding().setUser(user);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    /**
     * Deletes a single User from the data set.
     *
     * @param position the position of the User.
     */
    public void deleteItem(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Inserts a single item into the data set.
     *
     * @param user the User to insert.
     */
    public void insertItem(User user) {
        dataset.add(user);
        notifyItemInserted(dataset.size() - 1);
    }

    /**
     * Gets a single item of the data set.
     *
     * @param position the position of the User to get.
     * @return the User displayed at the given position.
     */
    public User getItem(int position) {
        return dataset.get(position);
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    public static class MemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private MemberViewBinding binding;
        private final ItemClickListener itemClickListener;

        /**
         * Creates the MemberViewHolder.
         *
         * @param b   the binding of the MemberViewHolder.
         * @param icl defines how to react to user interaction.
         */
        public MemberViewHolder(MemberViewBinding b, ItemClickListener icl) {
            super(b.getRoot());
            binding = b;
            binding.executePendingBindings();
            itemClickListener = icl;
            itemView.findViewById(R.id.delete_member).setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        /**
         * Returns the UserViewBinding.
         *
         * @return the binding of the UserViewHolder.
         */
        public MemberViewBinding getBinding() {
            return binding;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClicked(getAdapterPosition(), view);
        }
    }
}
