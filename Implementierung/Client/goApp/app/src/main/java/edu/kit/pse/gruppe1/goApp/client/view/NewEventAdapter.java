package edu.kit.pse.gruppe1.goApp.client.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.databinding.NewEventViewBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tobias on 27.01.2017.
 */

public class NewEventAdapter extends RecyclerView.Adapter<NewEventAdapter.NewEventViewHolder> {
    private List<Event> dataset;
    private final ItemClickListener itemClickListener;

    public NewEventAdapter(Event[] eventDataset, ItemClickListener icl) {
        dataset = new ArrayList<>(Arrays.asList(eventDataset));
        itemClickListener = icl;
    }

    @Override
    public NewEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewEventViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.new_event_view, parent, false);
        return new NewEventViewHolder(binding, itemClickListener);
    }

    @Override
    public void onBindViewHolder(NewEventViewHolder holder, int position) {
        final Event event = dataset.get(position);
        holder.getBinding().setEvent(event);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public Event getItem(int position) {
        return dataset.get(position);
    }

    public void deleteItem(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    public static class NewEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public NewEventViewBinding getBinding() {
            return binding;
        }

        private NewEventViewBinding binding;
        private final ItemClickListener itemClickListener;

        public NewEventViewHolder(NewEventViewBinding b, ItemClickListener icl) {
            super(b.getRoot());
            binding = b;
            binding.executePendingBindings();
            itemClickListener = icl;
            itemView.findViewById(R.id.reject_event).setOnClickListener(this);
            itemView.findViewById(R.id.accept_event).setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
             itemClickListener.onItemClicked(getAdapterPosition(), view);
        }
        //private final Listener;
    }
}
