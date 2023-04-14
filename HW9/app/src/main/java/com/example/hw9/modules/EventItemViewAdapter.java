package com.example.hw9.modules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hw9.R;

import java.util.List;

public class EventItemViewAdapter extends RecyclerView.Adapter<EventItemViewAdapter.EventItemViewHolder> {

    Context context;
    List<EventItem> eventItems;
    View view;

    public static class EventItemViewHolder extends RecyclerView.ViewHolder {

        ImageView eventImageView;
        TextView eventNameView;
        TextView venueNameView;
        TextView genreView;
        TextView eventDateView;
        TextView eventTimeView;
        ImageView heartIconView;

        public EventItemViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImageView = itemView.findViewById(R.id.event_image);
            eventNameView = itemView.findViewById(R.id.event_name);
            venueNameView = itemView.findViewById(R.id.venue_name);
            genreView = itemView.findViewById(R.id.genre);
            eventDateView = itemView.findViewById(R.id.event_date);
            eventTimeView = itemView.findViewById(R.id.event_time);
            heartIconView = itemView.findViewById(R.id.heart_icon);

        }
    }

    public EventItemViewAdapter(Context context, List<EventItem> eventItems) {
        this.context = context;
        this.eventItems = eventItems;
    }


    @NonNull
    @Override
    public EventItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new EventItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventItemViewHolder holder, int position) {
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
        Glide.with(context).load(eventItems.get(position).getImgUrl()).centerCrop().into(holder.eventImageView);
    }

    @Override
    public int getItemCount() {
        return eventItems.size();
    }

    public EventItem getItem(int position) {
        return eventItems.get(position);
    }


}
