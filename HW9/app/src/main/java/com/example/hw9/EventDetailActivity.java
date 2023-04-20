package com.example.hw9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.hw9.fragments.EventDetailArtistFragment;
import com.example.hw9.fragments.EventDetailDetailsFragment;
import com.example.hw9.fragments.EventDetailVenueFragment;
import com.example.hw9.modules.EventDetailViewPagerAdapter;
import com.example.hw9.modules.EventItem;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

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

public class EventDetailActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    EventDetailDetailsFragment eventDetailDetailsFragment;
    EventDetailArtistFragment eventDetailArtistFragment;
    EventDetailVenueFragment eventDetailVenueFragment;
    EventDetailViewPagerAdapter eventDetailViewPagerAdapter;
    JSONObject eventObject;
    String backendEventDetailUrl = "https://csci-571-hw8-382201.wl.r.appspot.com/event/";
    Bundle detailsBundle = new Bundle();
    Bundle artistBundle = new Bundle();
    Bundle venueBundle = new Bundle();
    RelativeLayout progressBarContainer;
    LinearLayout eventDetailContainer;
    ImageView facebookIcon;
    String facebookLink;
    ImageView twitterIcon;
    String twitterLink;
    int[] tabIcons = {
            R.drawable.info_icon,
            R.drawable.artist_icon,
            R.drawable.venue_icon
    };
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        initActivity();

    }

    public void processResponseData() {
        if (this.eventObject != null) {
            try {
                if (this.eventObject.has("dates")) {
                    if (this.eventObject.getJSONObject("dates").has("start") && this.eventObject.getJSONObject("dates").getJSONObject("start").has("localDate")) {
                        String dateString = this.eventObject.getJSONObject("dates").getJSONObject("start").getString("localDate");
                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        DateFormat outputFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);
                        String outputDate = outputFormat.format(Objects.requireNonNull(inputFormat.parse(dateString)));
                        detailsBundle.putString("date", outputDate);
                        if (eventObject.getJSONObject("dates").getJSONObject("start").has("localTime")) {
                            String time = this.eventObject.getJSONObject("dates").getJSONObject("start").getString("localTime");
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormatTime = new SimpleDateFormat("HH:mm:ss");
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormatTime = new SimpleDateFormat("h:mm a");
                            String outputTime = outputFormatTime.format(Objects.requireNonNull(inputFormatTime.parse(time)));
                            detailsBundle.putString("time", outputTime);
                        }
                        if (this.eventObject.getJSONObject("dates").has("status")) {
                            String status = this.eventObject.getJSONObject("dates").getJSONObject("status").getString("code");
                            detailsBundle.putString("status", status);
                        }
                    }
                }
                if (this.eventObject.has("_embedded") && this.eventObject.getJSONObject("_embedded").has("venues")) {
                    String venueName = this.eventObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                    detailsBundle.putString("venueName", venueName);
                    venueBundle.putString("venueName", venueName);
                }
                if (this.eventObject.has("priceRanges")) {
                    String minPrice = this.eventObject.getJSONArray("priceRanges").getJSONObject(0).getString("min");
                    String maxPrice = this.eventObject.getJSONArray("priceRanges").getJSONObject(0).getString("max");
                    String currency = this.eventObject.getJSONArray("priceRanges").getJSONObject(0).getString("currency");
                    detailsBundle.putString("priceRanges", String.format("%s - %s (%s)", minPrice, maxPrice, currency));
                }
                if (this.eventObject.has("url")) {
                    String url = this.eventObject.getString("url");
                    detailsBundle.putString("url", url);
                    facebookLink = String.format("https://www.facebook.com/sharer/sharer.php?u=%s&amp;src=sdkpreparse", url);
                    twitterLink = String.format("https://twitter.com/intent/tweet?text=Check out %s on Ticketmaster! %s&hashtags=hashtag1,hashtag2", this.eventObject.getString("name"), url);
                }
                if (this.eventObject.has("seatmap")) {
                    String seatmap = this.eventObject.getJSONObject("seatmap").getString("staticUrl");
                    detailsBundle.putString("seatmap", seatmap);
                }
                if (this.eventObject.has("classifications")) {
                    List<String> genres = new ArrayList<>();
                    JSONObject classifications = this.eventObject.getJSONArray("classifications").getJSONObject(0);
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
                    detailsBundle.putString("genres", genreString);
                }
                if (this.eventObject.has("_embedded") && this.eventObject.getJSONObject("_embedded").has("attractions")) {
                    JSONArray attractions = this.eventObject.getJSONObject("_embedded").getJSONArray("attractions");
                    ArrayList<String> artists = new ArrayList<>();
                    ArrayList<String> musicArtists = new ArrayList<>();
                    for (int i = 0; i < attractions.length(); i++) {
                        if (attractions.getJSONObject(i).has("classifications") && attractions.getJSONObject(i).getJSONArray("classifications").getJSONObject(0)
                                .has("segment") && attractions.getJSONObject(i).getJSONArray("classifications").getJSONObject(0).getJSONObject("segment")
                                .has("name") && attractions.getJSONObject(i).getJSONArray("classifications").getJSONObject(0).getJSONObject("segment")
                                .getString("name").contains("Music")) {
                            musicArtists.add(attractions.getJSONObject(i).getString("name"));
                        }
                        artists.add(attractions.getJSONObject(i).getString("name"));
                    }
                    artistBundle.putStringArrayList("artistNames", musicArtists);
                    String artistString = String.join(" | ", artists);
                    detailsBundle.putString("artists", artistString);
                }
                sendDataToFragments();
            }
            catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void sendDataToFragments() {
        this.eventDetailDetailsFragment.setArguments(detailsBundle);
        this.eventDetailArtistFragment.setArguments(artistBundle);
        this.eventDetailVenueFragment.setArguments(venueBundle);
        eventDetailViewPagerAdapter.notifyDataSetChanged();
    }

    public void sendToBackendEventDetail(String eventId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = backendEventDetailUrl + eventId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    eventObject = new JSONObject(response);
//                    System.out.println(eventObject);
                    processResponseData();
                    progressBarContainer.setVisibility(View.GONE);
                    eventDetailContainer.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }

    public void initActivity() {
        EventItem eventItem = (EventItem) getIntent().getSerializableExtra("eventItem");
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setColorFilter(getResources().getColor(R.color.text_green));
        TextView eventTitle = findViewById(R.id.event_title);
        eventTitle.setText(eventItem.getEventName());
        ImageView heartIcon = findViewById(R.id.heart_icon);
        progressBarContainer = findViewById(R.id.event_detail_progress_bar_container);
        eventDetailContainer = findViewById(R.id.event_detail_container);
        facebookIcon = findViewById(R.id.facebook_icon);
        twitterIcon = findViewById(R.id.twitter_icon);
        sharedPreferences = getSharedPreferences("eventPreference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.contains(eventItem.getId())) {
            heartIcon.setImageResource(R.mipmap.ic_heart_filled_foreground);
        }
        else {
            heartIcon.setImageResource(R.mipmap.ic_heart_outline_foreground);
        }
        facebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookLink));
                startActivity(intent);
            }
        });

        twitterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterLink));
                startActivity(intent);
            }
        });

        heartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.contains(eventItem.getId())) {
                    heartIcon.setImageResource(R.mipmap.ic_heart_outline_foreground);
                    editor.remove(eventItem.getId());
                    editor.apply();
                    Snackbar.make(view, eventItem.getEventName() + " removed from favorites", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    heartIcon.setImageResource(R.mipmap.ic_heart_filled_foreground);
                    String eventString = gson.toJson(eventItem);
                    editor.putString(eventItem.getId(), eventString);
                    editor.apply();
                    Snackbar.make(view, eventItem.getEventName() + " added to favorites", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        eventTitle.post(new Runnable() {
            @Override
            public void run() {
                eventTitle.setSelected(true);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tabLayout = findViewById(R.id.event_detail_tab_layout);
        viewPager2 = findViewById(R.id.event_detail_view_pager);
        eventDetailViewPagerAdapter = new EventDetailViewPagerAdapter(this);
        eventDetailDetailsFragment = new EventDetailDetailsFragment();
        eventDetailArtistFragment = new EventDetailArtistFragment();
        eventDetailVenueFragment = new EventDetailVenueFragment();
        eventDetailViewPagerAdapter.addFragment(eventDetailDetailsFragment);
        eventDetailViewPagerAdapter.addFragment(eventDetailArtistFragment);
        eventDetailViewPagerAdapter.addFragment(eventDetailVenueFragment);
        viewPager2.setAdapter(eventDetailViewPagerAdapter);
        viewPager2.setOffscreenPageLimit(2);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                ((TextView) tab.getCustomView().findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.text_green));
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_icon)).setColorFilter(getResources().getColor(R.color.text_green));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.white));
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_icon)).setColorFilter(getResources().getColor(R.color.white));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position > tabLayout.getTabCount() - 1) {
                    return;
                }
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        setupTabs();

        sendToBackendEventDetail(eventItem.getId());
    }

    @SuppressLint("SetTextI18n")
    public void setupTabs() {
        for (int i = 0; i < 3; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setCustomView(R.layout.event_detail_tab_item);
            ImageView tabIcon = Objects.requireNonNull(tab.getCustomView()).findViewById(R.id.tab_icon);
            tabIcon.setImageResource(tabIcons[i]);
            TextView tabText = tab.getCustomView().findViewById(R.id.tab_text);
            if (i == 0) {
                tabText.setText("DETAILS");
            }
            else if (i == 1) {
                tabText.setText("ARTIST(S)");
            }
            else {
                tabText.setText("VENUE");
            }
            tabLayout.addTab(tab);
        }
    }


}