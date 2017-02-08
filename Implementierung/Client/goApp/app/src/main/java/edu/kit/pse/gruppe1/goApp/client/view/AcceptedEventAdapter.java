package edu.kit.pse.gruppe1.goApp.client.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.databinding.AcceptedEventViewBinding;
import edu.kit.pse.gruppe1.goApp.client.model.Event;

/**
 * Created by Tobias on 27.01.2017.
 */

public class AcceptedEventAdapter extends RecyclerView.Adapter<AcceptedEventAdapter.AcceptedEventViewHolder> {
    private Event[] dataset;
    private final ItemClickListener itemClickListener;

    public AcceptedEventAdapter(Event[] eventDataset, ItemClickListener icl) {
        dataset = eventDataset;
        itemClickListener = icl;
    }

    @Override
    public AcceptedEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AcceptedEventViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.accepted_event_view, parent, false);
        return new AcceptedEventViewHolder(binding, itemClickListener);
    }

    @Override
    public void onBindViewHolder(AcceptedEventViewHolder holder, int position) {
        final Event event = dataset[position];
        holder.getBinding().setEvent(event);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }

    public Event getItem(int position) {
        return dataset[position];
    }

    public static class AcceptedEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public AcceptedEventViewBinding getBinding() {
            return binding;
        }

        private AcceptedEventViewBinding binding;
        private final ItemClickListener itemClickListener;

        public AcceptedEventViewHolder(AcceptedEventViewBinding b, ItemClickListener icl) {
            super(b.getRoot());
            binding = b;
            binding.executePendingBindings();
            itemClickListener = icl;
            itemView.findViewById(R.id.start_event).setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClicked(getAdapterPosition(), view);
        }
        //private final Listener;
    }
}
