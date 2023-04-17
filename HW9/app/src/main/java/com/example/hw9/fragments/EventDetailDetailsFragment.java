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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hw9.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EventDetailDetailsFragment extends Fragment {

    View view;
    JSONObject event;


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
        Bundle bundle = getArguments();
        if (bundle != null) {
            String eventString = bundle.getString("eventString");
//            System.out.println(eventString);
            try {
                event = new JSONObject(eventString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setupView() {
        if (this.event != null) {
            try {
                if (this.event.has("dates")) {
                    if (this.event.getJSONObject("dates").has("start") && this.event.getJSONObject("dates").getJSONObject("start").has("localDate")) {
                        String dateString = this.event.getJSONObject("dates").getJSONObject("start").getString("localDate");
                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        DateFormat outputFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);
                        String outputDate = outputFormat.format(Objects.requireNonNull(inputFormat.parse(dateString)));
                        TextView dateText = view.findViewById(R.id.event_detail_date_text);
                        dateText.setText(outputDate);
                        if (event.getJSONObject("dates").getJSONObject("start").has("localTime")) {
                            String time = this.event.getJSONObject("dates").getJSONObject("start").getString("localTime");
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormatTime = new SimpleDateFormat("HH:mm:ss");
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormatTime = new SimpleDateFormat("h:mm a");
                            String outputTime = outputFormatTime.format(Objects.requireNonNull(inputFormatTime.parse(time)));
                            TextView timeText = view.findViewById(R.id.event_detail_time_text);
                            timeText.setText(outputTime);
                        }
                        else {
                            view.findViewById(R.id.event_detail_time).setVisibility(View.GONE);
                        }
                        if (this.event.getJSONObject("dates").has("status")) {
                            String status = this.event.getJSONObject("dates").getJSONObject("status").getString("code");
                            TextView statusText = view.findViewById(R.id.event_detail_ticket_status_text);
                            statusText.setText(status);
                            CardView statusCard = view.findViewById(R.id.event_detail_ticket_status_card);
                            switch (status) {
                                case "onsale":
                                    statusCard.setCardBackgroundColor(getResources().getColor(R.color.text_green));
                                    break;
                                case "offsale":
                                    statusCard.setCardBackgroundColor(getResources().getColor(R.color.red));
                                    break;
                                case "cancelled":
                                    statusCard.setCardBackgroundColor(getResources().getColor(R.color.black));
                                    break;
                                default:
                                    statusCard.setCardBackgroundColor(getResources().getColor(R.color.orange));
                                    break;
                            }

                        } else {
                            view.findViewById(R.id.event_detail_ticket_status).setVisibility(View.GONE);
                        }
                    }
                } else {
                    view.findViewById(R.id.event_detail_date).setVisibility(View.GONE);
                }
                if (this.event.has("_embedded") && this.event.getJSONObject("_embedded").has("venues")) {
                    String venueName = this.event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                    TextView venueText = view.findViewById(R.id.event_detail_venue_text);
                    venueText.setText(venueName);
                    venueText.post(new Runnable() {
                        @Override
                        public void run() {
                            venueText.setSelected(true);
                        }
                    });
                } else {
                    view.findViewById(R.id.event_detail_venue).setVisibility(View.GONE);
                }
                if (this.event.has("priceRanges")) {
                    String minPrice = this.event.getJSONArray("priceRanges").getJSONObject(0).getString("min");
                    String maxPrice = this.event.getJSONArray("priceRanges").getJSONObject(0).getString("max");
                    String currency = this.event.getJSONArray("priceRanges").getJSONObject(0).getString("currency");
                    TextView priceText = view.findViewById(R.id.event_detail_price_text);
                    priceText.setText(String.format("%s - %s (%s)", minPrice, maxPrice, currency));
                    priceText.post(new Runnable() {
                        @Override
                        public void run() {
                            priceText.setSelected(true);
                        }
                    });
                } else {
                    view.findViewById(R.id.event_detail_price).setVisibility(View.GONE);
                }
                if (this.event.has("url")) {
                    String url = this.event.getString("url");
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
                if (this.event.has("seatmap")) {
                    String seatmap = this.event.getJSONObject("seatmap").getString("staticUrl");
                    Glide.with(view).load(seatmap).into((ImageView) view.findViewById(R.id.event_detail_seatmap));
                } else {
                    view.findViewById(R.id.event_detail_seatmap).setVisibility(View.GONE);
                }
                if (this.event.has("classifications")) {
                    List<String> genres = new ArrayList<>();
                    JSONObject classifications = this.event.getJSONArray("classifications").getJSONObject(0);
                    if (classifications.has("segment") && classifications.getJSONObject("segment").has("name") && !classifications.getJSONObject("segment").getString("name").equals("Undefined")) {
                        genres.add(classifications.getJSONObject("segment").getString("name"));
                    }
                    if (classifications.has("genre") && classifications.getJSONObject("genre").has("name") && !classifications.getJSONObject("genre").getString("name").equals("Undefined")) {
                        genres.add(classifications.getJSONObject("genre").getString("name"));
                    }
                    if (classifications.has("subGenre") && classifications.getJSONObject("subGenre").has("name") && !classifications.getJSONObject("subGenre").getString("name").equals("Undefined")) {
                        genres.add(classifications.getJSONObject("subGenre").getString("name"));
                    }
                    if (classifications.has("type") && classifications.getJSONObject("type").has("name") && !classifications.getJSONObject("type").getString("name").equals("Undefined")) {
                        genres.add(classifications.getJSONObject("type").getString("name"));
                    }
                    if (classifications.has("subType") && classifications.getJSONObject("subType").has("name") && !classifications.getJSONObject("subType").getString("name").equals("Undefined")) {
                        genres.add(classifications.getJSONObject("subType").getString("name"));
                    }
                    String genreString = String.join(" | ", genres);
                    TextView genreText = view.findViewById(R.id.event_detail_genre_text);
                    genreText.setText(genreString);
                    genreText.post(new Runnable() {
                        @Override
                        public void run() {
                            genreText.setSelected(true);
                        }
                    });
                } else {
                    view.findViewById(R.id.event_detail_genre).setVisibility(View.GONE);
                }
                if (this.event.has("_embedded") && this.event.getJSONObject("_embedded").has("attractions")) {
                    JSONArray attractions = this.event.getJSONObject("_embedded").getJSONArray("attractions");
                    List<String> artists = new ArrayList<>();
                    for (int i = 0; i < attractions.length(); i++) {
                        artists.add(attractions.getJSONObject(i).getString("name"));
                    }
                    String artistString = String.join(" | ", artists);
                    TextView artistText = view.findViewById(R.id.event_detail_artist_text);
                    artistText.setText(artistString);
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
            catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

    }


}