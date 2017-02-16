package edu.kit.pse.gruppe1.goApp.client.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.databinding.AcceptedEventViewBinding;
import edu.kit.pse.gruppe1.goApp.client.databinding.MemberViewBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tobias on 27.01.2017.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    private List<User> dataset;
    private final ItemClickListener itemClickListener;

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

    public void deleteItem(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    public void insertItem(User user) {
        dataset.add(user);
        notifyItemInserted(dataset.size() - 1);
    }

    public User getItem(int position) {
        return dataset.get(position);
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MemberViewBinding getBinding() {
            return binding;
        }

        private MemberViewBinding binding;
        private final ItemClickListener itemClickListener;

        public MemberViewHolder(MemberViewBinding b, ItemClickListener icl) {
            super(b.getRoot());
            binding = b;
            binding.executePendingBindings();
            itemClickListener = icl;
            itemView.findViewById(R.id.delete_member).setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClicked(getAdapterPosition(), view);
        }
        //private final Listener;
    }
}
