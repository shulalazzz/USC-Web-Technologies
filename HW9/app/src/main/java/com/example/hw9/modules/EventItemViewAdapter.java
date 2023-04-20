package com.example.hw9.modules;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hw9.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.List;

public class EventItemViewAdapter extends RecyclerView.Adapter<EventItemViewAdapter.EventItemViewHolder> {

    Context context;
    List<EventItem> eventItems;
    View view;
    OnEventListener mOnEventListener;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;


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
        this.sharedPreferences = context.getSharedPreferences("eventPreference", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.gson = new Gson();
    }


    @NonNull
    @Override
    public EventItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new EventItemViewHolder(view, mOnEventListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
        String eventID = eventItems.get(position).getId();
        if (this.sharedPreferences.contains(eventID)) {
            holder.heartIconView.setImageResource(R.mipmap.ic_heart_filled_foreground);
            holder.heartClicked = true;
        } else {
            holder.heartIconView.setImageResource(R.mipmap.ic_heart_outline_foreground);
            holder.heartClicked = false;
        }
        holder.heartIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("heart clicked");
                holder.heartClicked = !holder.heartClicked;
                String eventName = eventItems.get(position).getEventName();
                if (holder.heartClicked) {
                    holder.heartIconView.setImageResource(R.mipmap.ic_heart_filled_foreground);
                    String eventJson = gson.toJson(eventItems.get(position));
                    editor.putString(eventID, eventJson);
                    editor.apply();
                    Snackbar.make(view, eventName + " added to favorites", Snackbar.LENGTH_SHORT).show();
                } else {
                    holder.heartIconView.setImageResource(R.mipmap.ic_heart_outline_foreground);
                    editor.remove(eventID);
                    editor.apply();
                    Snackbar.make(view, eventName + " removed from favorites", Snackbar.LENGTH_SHORT).show();
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
