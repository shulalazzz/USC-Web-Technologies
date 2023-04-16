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
    OnEventListener mOnEventListener;


    // ViewHolder
    public static class EventItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView eventImageView;
        TextView eventNameView;
        TextView venueNameView;
        TextView genreView;
        TextView eventDateView;
        TextView eventTimeView;
        ImageView heartIconView;
        boolean heartClicked;
        OnEventListener onEventListener;

        public EventItemViewHolder(@NonNull View itemView, OnEventListener onEventListener) {
            super(itemView);
            eventImageView = itemView.findViewById(R.id.event_image);
            eventNameView = itemView.findViewById(R.id.event_name);
            venueNameView = itemView.findViewById(R.id.venue_name);
            genreView = itemView.findViewById(R.id.genre);
            eventDateView = itemView.findViewById(R.id.event_date);
            eventTimeView = itemView.findViewById(R.id.event_time);
            heartIconView = itemView.findViewById(R.id.heart_icon);
            heartClicked = false;
            this.onEventListener = onEventListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onEventListener.onEventClick(getAdapterPosition());
        }
    }

    // Interface
    public interface OnEventListener {
        void onEventClick(int position);
    }





    public EventItemViewAdapter(Context context, List<EventItem> eventItems, OnEventListener onEventListener) {
        this.context = context;
        this.eventItems = eventItems;
        this.mOnEventListener = onEventListener;
    }


    @NonNull
    @Override
    public EventItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new EventItemViewHolder(view, mOnEventListener);
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
        Glide.with(context).load(eventItems.get(position).getImgUrl()).into(holder.eventImageView);
        holder.heartIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("heart clicked");
                holder.heartClicked = !holder.heartClicked;
                if (holder.heartClicked) {
                    holder.heartIconView.setImageResource(R.mipmap.ic_heart_filled_foreground);
                } else {
                    holder.heartIconView.setImageResource(R.mipmap.ic_heart_outline_foreground);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventItems.size();
    }

    public EventItem getItem(int position) {
        return eventItems.get(position);
    }


}
