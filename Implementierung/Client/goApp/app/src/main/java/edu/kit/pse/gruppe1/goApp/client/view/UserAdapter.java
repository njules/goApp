package edu.kit.pse.gruppe1.goApp.client.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.databinding.UserViewBinding;
import edu.kit.pse.gruppe1.goApp.client.model.User;

/**
 * Created by Tobias on 20.01.2017.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private User[] dataset;

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        private UserViewBinding binding;

        public UserViewHolder(UserViewBinding b) {
            super(b.getRoot());
            binding = b;
            binding.executePendingBindings();
        }
        public UserViewBinding getBinding() {
            return binding;
        }
    }

    public UserAdapter(User[] userDataset) {
        dataset = userDataset;
    }
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.user_view, parent, false);
        return new UserViewHolder(binding);
        /** create a new view
         View v = LayoutInflater.from(parent.getContext())
         .inflate(R.layout.group_view, parent, false);

         ViewHolder vh = new ViewHolder((TextView) v);
         return vh;*/
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final User user = dataset[position];
        holder.getBinding().setUser(user);
        holder.getBinding().executePendingBindings();
    }

    public User getItem(int position){
        return dataset[position];
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }
}
