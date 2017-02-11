package edu.kit.pse.gruppe1.goApp.client.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.kit.pse.gruppe1.goApp.client.R;

import edu.kit.pse.gruppe1.goApp.client.databinding.GroupViewBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tobias on 20.01.2017.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private List<Group> dataset;
    private final ItemClickListener itemClickListener;

    public static class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private GroupViewBinding binding;
        private final ItemClickListener itemClickListener;

        public GroupViewHolder(GroupViewBinding b, ItemClickListener icl) {
            super(b.getRoot());
            binding = b;
            binding.executePendingBindings();
            itemClickListener = icl;
            itemView.setOnClickListener(this);
        }

        public GroupViewBinding getBinding() {
            return binding;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClicked(getAdapterPosition(), view);
        }
    }

    public GroupAdapter(Group[] groupDataset, ItemClickListener icl) {
        dataset = new ArrayList<>(Arrays.asList(groupDataset));
        itemClickListener = icl;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GroupViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.group_view, parent, false);
        return new GroupViewHolder(binding, itemClickListener);
        /** create a new view
         View v = LayoutInflater.from(parent.getContext())
         .inflate(R.layout.group_view, parent, false);

         ViewHolder vh = new ViewHolder((TextView) v);
         return vh;*/
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        final Group group = dataset.get(position);
        holder.getBinding().setGroup(group);
        holder.getBinding().executePendingBindings();
    }

    public Group getItem(int position) {
        return dataset.get(position);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void deleteItem(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }
}
