package edu.kit.pse.gruppe1.goApp.client.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.databinding.AcceptedEventViewBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This adapter provides a binding from an data set of events to views that are displayed within a RecyclerView.
 */

public class AcceptedEventAdapter extends RecyclerView.Adapter<AcceptedEventAdapter.AcceptedEventViewHolder> {
    private List<Event> dataset;
    private final ItemClickListener itemClickListener;

    /**
     * Creates the AcceptedEventAdapter.
     *
     * @param eventDataset contains the Events to display.
     * @param icl          defines how to react to user interaction.
     */
    public AcceptedEventAdapter(Event[] eventDataset, ItemClickListener icl) {
        dataset = new ArrayList<>(Arrays.asList(eventDataset));
        itemClickListener = icl;
    }

    @Override
    public AcceptedEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AcceptedEventViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.accepted_event_view, parent, false);
        return new AcceptedEventViewHolder(binding, itemClickListener);
    }

    @Override
    public void onBindViewHolder(AcceptedEventViewHolder holder, int position) {
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
     * Inserts a single item into the data set.
     *
     * @param event the Event to insert.
     */
    public void insertItem(Event event) {
        dataset.add(0, event);
        notifyItemInserted(0);
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    public static class AcceptedEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AcceptedEventViewBinding binding;
        private final ItemClickListener itemClickListener;

        /**
         * Creates the AcceptedEventViewHolder.
         *
         * @param b   the binding of the AcceptedEventViewHolder.
         * @param icl defines how to react to user interaction.
         */
        public AcceptedEventViewHolder(AcceptedEventViewBinding b, ItemClickListener icl) {
            super(b.getRoot());
            binding = b;
            binding.executePendingBindings();
            itemClickListener = icl;
            itemView.findViewById(R.id.start_event).setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        /**
         * Returns the AcceptedEventViewBinding.
         *
         * @return the binding to return.
         */
        public AcceptedEventViewBinding getBinding() {
            return binding;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClicked(getAdapterPosition(), view);
        }
    }

}
