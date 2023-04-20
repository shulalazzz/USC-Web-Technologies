package com.example.hw9.modules;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hw9.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FavoriteItemViewAdapter extends EventItemViewAdapter {
    private final List<EventItem> eventItems;
    View view;
    Context context;
    EventItemViewAdapter.OnEventListener mOnEventListener;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RelativeLayout noResultsContainer;

    public FavoriteItemViewAdapter(Context context, List<EventItem> eventItems,  EventItemViewAdapter.OnEventListener mOnEventListener) {
        super(context, eventItems, mOnEventListener);
        this.eventItems = eventItems;
        this.context = context;
        this.mOnEventListener = mOnEventListener;
        this.sharedPreferences = context.getSharedPreferences("eventPreference", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    @NonNull
    @Override
    public EventItemViewAdapter.EventItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new EventItemViewAdapter.EventItemViewHolder(view, mOnEventListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventItemViewAdapter.EventItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.eventNameView.setText(eventItems.get(position).getEventName());
        holder.eventNameView.post(new Runnable() {
            @Override
            public void run() {
                holder.eventNameView.setSelected(true);
            }
        });
        holder.venueNameView.setText(eventItems.get(position).getVenueName());
        holder.venueNameView.post(new Runnable() {
            @Override
            public void run() {
                holder.venueNameView.setSelected(true);
            }
        });
        holder.genreView.setText(eventItems.get(position).getGenre());
        holder.genreView.post(new Runnable() {
            @Override
            public void run() {
                holder.genreView.setSelected(true);
            }
        });
        holder.eventDateView.setText(eventItems.get(position).getDate());
        holder.eventTimeView.setText(eventItems.get(position).getTime());
        Glide.with(context).load(eventItems.get(position).getImgUrl()).into(holder.eventImageView);
        holder.heartIconView.setImageResource(R.mipmap.ic_heart_filled_foreground);
        String eventID = eventItems.get(position).getId();
        String eventName = eventItems.get(position).getEventName();
        holder.heartIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != RecyclerView.NO_POSITION) {
                    editor.remove(eventID);
                    editor.apply();
                    eventItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, eventItems.size());
                    Snackbar.make(view,  eventName + " removed from favorites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                if (eventItems.size() == 0) {
                    noResultsContainer.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void setNoResultsContainer(RelativeLayout noResultsContainer) {
        this.noResultsContainer = noResultsContainer;
    }

    @Override
    public int getItemCount() {
        return eventItems.size();
    }
}
