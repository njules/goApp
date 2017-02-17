package edu.kit.pse.gruppe1.goApp.client.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.kit.pse.gruppe1.goApp.client.R;

import edu.kit.pse.gruppe1.goApp.client.databinding.GroupViewBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This adapter provides a binding from an data set of groups to views that are displayed within a RecyclerView.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private List<Group> dataset;
    private final ItemClickListener itemClickListener;

    /**
     * Creates the GroupAdapter.
     *
     * @param groupDataset contains the Groups to display.
     * @param icl          defines how to react to user interaction.
     */
    public GroupAdapter(Group[] groupDataset, ItemClickListener icl) {
        dataset = new ArrayList<>(Arrays.asList(groupDataset));
        itemClickListener = icl;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GroupViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.group_view, parent, false);
        return new GroupViewHolder(binding, itemClickListener);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        final Group group = dataset.get(position);
        holder.getBinding().setGroup(group);
        holder.getBinding().executePendingBindings();
    }

    /**
     * Gets a single item of the data set.
     *
     * @param position the position of the Group to get.
     * @return the Group displayed at the given position.
     */
    public Group getItem(int position) {
        return dataset.get(position);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    /**
     * Deletes a single Group from the data set.
     *
     * @param position the position of the Group.
     */
    public void deleteItem(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    public static class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private GroupViewBinding binding;
        private final ItemClickListener itemClickListener;

        /**
         * Creates the GroupViewHolder.
         *
         * @param b   the binding of the GroupViewHolder.
         * @param icl defines how to react to user interaction.
         */
        public GroupViewHolder(GroupViewBinding b, ItemClickListener icl) {
            super(b.getRoot());
            binding = b;
            binding.executePendingBindings();
            itemClickListener = icl;
            itemView.setOnClickListener(this);
        }

        /**
         * Returns the GroupViewBinding.
         *
         * @return the binding of the GroupViewHolder.
         */
        public GroupViewBinding getBinding() {
            return binding;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClicked(getAdapterPosition(), view);
        }
    }
}
