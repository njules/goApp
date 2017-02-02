package edu.kit.pse.gruppe1.goApp.client.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.databinding.NewEventViewBinding;
import edu.kit.pse.gruppe1.goApp.client.databinding.RequestUserViewBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.User;

/**
 * Created by Tobias on 27.01.2017.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private User[] dataset;
    private final ItemClickListener itemClickListener;

    public RequestAdapter(User[] userDataset, ItemClickListener icl) {
        dataset = userDataset;
        itemClickListener = icl;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RequestUserViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.request_user_view, parent, false);
        return new RequestViewHolder(binding, itemClickListener);
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        final User user = dataset[position];
        holder.getBinding().setUser(user);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }

    public User getItem(int position) {
        return dataset[position];
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RequestUserViewBinding getBinding() {
            return binding;
        }

        private RequestUserViewBinding binding;
        private final ItemClickListener itemClickListener;

        public RequestViewHolder(RequestUserViewBinding b, ItemClickListener icl) {
            super(b.getRoot());
            binding = b;
            binding.executePendingBindings();
            itemClickListener = icl;
            itemView.findViewById(R.id.accept_member).setOnClickListener(this);
            itemView.findViewById(R.id.reject_member).setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClicked(getAdapterPosition(),view);
        }
        //private final Listener;
    }
}
