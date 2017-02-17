package edu.kit.pse.gruppe1.goApp.client.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.databinding.RequestUserViewBinding;
import edu.kit.pse.gruppe1.goApp.client.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This adapter provides a binding from an data set of users to views that are displayed within a RecyclerView.
 * This adapter is used to display Users who have sent an request to a Group with two Buttons, to either accept them in the Group or to decline the request.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private List<User> dataset;
    private final ItemClickListener itemClickListener;

    /**
     * Creates the RequestAdapter.
     *
     * @param userDataset contains the Users to display.
     * @param icl         defines how to react to user interaction.
     */
    public RequestAdapter(User[] userDataset, ItemClickListener icl) {
        dataset = new ArrayList<>(Arrays.asList(userDataset));
        itemClickListener = icl;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RequestUserViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.request_user_view, parent, false);
        return new RequestViewHolder(binding, itemClickListener);
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        final User user = dataset.get(position);
        holder.getBinding().setUser(user);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return dataset.size();
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
     * Deletes a single User from the data set.
     *
     * @param position the position of the User.
     */
    public void delete(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    public static class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RequestUserViewBinding binding;
        private final ItemClickListener itemClickListener;

        /**
         * Creates the RequestViewHolder.
         *
         * @param b   the binding of the RequestViewHolder.
         * @param icl defines how to react to user interaction.
         */
        public RequestViewHolder(RequestUserViewBinding b, ItemClickListener icl) {
            super(b.getRoot());
            binding = b;
            binding.executePendingBindings();
            itemClickListener = icl;
            itemView.findViewById(R.id.accept_member).setOnClickListener(this);
            itemView.findViewById(R.id.reject_member).setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        /**
         * Returns the RequestViewBinding.
         *
         * @return the binding of the RequestViewHolder.
         */
        public RequestUserViewBinding getBinding() {
            return binding;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClicked(getAdapterPosition(), view);
        }
    }
}
