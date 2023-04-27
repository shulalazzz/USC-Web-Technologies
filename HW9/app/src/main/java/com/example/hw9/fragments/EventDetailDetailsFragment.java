package com.example.hw9.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hw9.R;

import org.json.JSONObject;

public class EventDetailDetailsFragment extends Fragment {

    View view;
    Bundle bundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_detail_details, container, false);
        setupView();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = getView();
        bundle = getArguments();
    }

    public void setupView() {
        if (this.bundle != null) {
            if (this.bundle.containsKey("date")) {
                TextView dateText = view.findViewById(R.id.event_detail_date_text);
                dateText.setText(this.bundle.getString("date"));

            } else {
                view.findViewById(R.id.event_detail_date).setVisibility(View.GONE);
            }
            if (this.bundle.containsKey("time")) {
                TextView timeText = view.findViewById(R.id.event_detail_time_text);
                timeText.setText(this.bundle.getString("time"));
            }
            else {
                view.findViewById(R.id.event_detail_time).setVisibility(View.GONE);
            }
            if (this.bundle.containsKey("status")) {
                String status = this.bundle.getString("status");
                TextView statusText = view.findViewById(R.id.event_detail_ticket_status_text);
                CardView statusCard = view.findViewById(R.id.event_detail_ticket_status_card);
                switch (status) {
                    case "onsale":
                        statusCard.setCardBackgroundColor(getResources().getColor(R.color.text_green));
                        statusText.setText("On Sale");
                        break;
                    case "offsale":
                        statusCard.setCardBackgroundColor(getResources().getColor(R.color.red));
                        statusText.setText("Off Sale");
                        break;
                    case "cancelled":
                        statusCard.setCardBackgroundColor(getResources().getColor(R.color.black));
                        statusText.setText("Cancelled");
                        break;
                    case "postponed":
                        statusCard.setCardBackgroundColor(getResources().getColor(R.color.orange));
                        statusText.setText("Postponed");
                        break;
                    case "rescheduled":
                        statusCard.setCardBackgroundColor(getResources().getColor(R.color.orange));
                        statusText.setText("Rescheduled");
                        break;
                }

            } else {
                view.findViewById(R.id.event_detail_ticket_status).setVisibility(View.GONE);
            }
            if (this.bundle.containsKey("venueName")) {
                TextView venueText = view.findViewById(R.id.event_detail_venue_text);
                venueText.setText(this.bundle.getString("venueName"));
                venueText.post(new Runnable() {
                    @Override
                    public void run() {
                        venueText.setSelected(true);
                    }
                });
            } else {
                view.findViewById(R.id.event_detail_venue).setVisibility(View.GONE);
            }
            if (this.bundle.containsKey("priceRanges")) {
                TextView priceText = view.findViewById(R.id.event_detail_price_text);
                priceText.setText(this.bundle.getString("priceRanges"));
                priceText.post(new Runnable() {
                    @Override
                    public void run() {
                        priceText.setSelected(true);
                    }
                });
            } else {
                view.findViewById(R.id.event_detail_price).setVisibility(View.GONE);
            }
            if (this.bundle.containsKey("url")) {
                String url = this.bundle.getString("url");
                TextView urlText = view.findViewById(R.id.event_detail_buy_ticket_text);
                SpannableString content = new SpannableString(url);
                content.setSpan(new android.text.style.URLSpan(url), 0, content.length(), 0);
                urlText.setText(content);
                urlText.post(new Runnable() {
                    @Override
                    public void run() {
                        urlText.setSelected(true);
                    }
                });
                urlText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url));
                        startActivity(browserIntent);
                    }
                });
            } else {
                view.findViewById(R.id.event_detail_buy_ticket_text).setVisibility(View.GONE);
            }
            if (this.bundle.containsKey("seatmap")) {
                Glide.with(view).load(this.bundle.getString("seatmap")).into((ImageView) view.findViewById(R.id.event_detail_seatmap));
            } else {
                view.findViewById(R.id.event_detail_seatmap).setVisibility(View.GONE);
            }
            if (this.bundle.containsKey("genres")) {
                TextView genreText = view.findViewById(R.id.event_detail_genre_text);
                genreText.setText(this.bundle.getString("genres"));
                genreText.post(new Runnable() {
                    @Override
                    public void run() {
                        genreText.setSelected(true);
                    }
                });
            } else {
                view.findViewById(R.id.event_detail_genre).setVisibility(View.GONE);
            }
            if (this.bundle.containsKey("artists")) {
                TextView artistText = view.findViewById(R.id.event_detail_artist_text);
                artistText.setText(this.bundle.getString("artists"));
                artistText.post(new Runnable() {
                    @Override
                    public void run() {
                        artistText.setSelected(true);
                    }
                });
            } else {
                view.findViewById(R.id.event_detail_artist).setVisibility(View.GONE);
            }
        }

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        System.out.println("from event detail details fragment resume");
//        System.out.println(this.bundle);
////        setupView();
//    }


}