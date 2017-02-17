package edu.kit.pse.gruppe1.goApp.client.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
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
 * This adapter provides a binding from an data set of events to views that are displayed within a RecyclerView.
 * This adapter is used for events where the user does not yet participate.
 */

public class NewEventAdapter extends RecyclerView.Adapter<NewEventAdapter.NewEventViewHolder> {
    private List<Event> dataset;
    private final ItemClickListener itemClickListener;


    /**
     * Creates the NewEventAdapter.
     *
     * @param eventDataset contains the Events to display.
     * @param icl          defines how to react to user interaction.
     */
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

    /**
     * Gets a single item of the data set.
     *
     * @param position the position of the Event to get.
     * @return the Event displayed at the given position.
     */
    public Event getItem(int position) {
        return dataset.get(position);
    }

    /**
     * Deletes a single item from the data set.
     *
     * @param position the position of the Event to delete.
     */
    public void deleteItem(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    public static class NewEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private NewEventViewBinding binding;
        private final ItemClickListener itemClickListener;

        /**
         * Creates the NewEventViewHolder.
         *
         * @param b   the binding of the NewEventViewHolder.
         * @param icl defines how to react to user interaction.
         */
        public NewEventViewHolder(NewEventViewBinding b, ItemClickListener icl) {
            super(b.getRoot());
            binding = b;
            binding.executePendingBindings();
            itemClickListener = icl;
            itemView.findViewById(R.id.reject_event).setOnClickListener(this);
            itemView.findViewById(R.id.accept_event).setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        /**
         * Returns the NewEventViewBinding.
         *
         * @return the binding of the NewEventViewHolder.
         */
        public NewEventViewBinding getBinding() {
            return binding;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClicked(getAdapterPosition(), view);
        }
    }
}
