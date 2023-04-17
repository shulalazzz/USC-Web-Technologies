package com.example.hw9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.example.hw9.fragments.EventDetailArtistFragment;
import com.example.hw9.fragments.EventDetailDetailsFragment;
import com.example.hw9.fragments.EventDetailVenueFragment;
import com.example.hw9.modules.EventDetailViewPagerAdapter;
import com.example.hw9.modules.EventItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventDetailActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    EventDetailDetailsFragment eventDetailDetailsFragment;
    EventDetailArtistFragment eventDetailArtistFragment;
    EventDetailVenueFragment eventDetailVenueFragment;
    EventDetailViewPagerAdapter eventDetailViewPagerAdapter;
    JSONObject eventObject;
    String eventString;
    String backendEventDetailUrl = "https://csci-571-hw8-382201.wl.r.appspot.com/event/";
    List<String> artistNames = new ArrayList<>();
    String venueName;
    RelativeLayout progressBarContainer;
    LinearLayout eventDetailContainer;
    int[] tabIcons = {
            R.drawable.info_icon,
            R.drawable.artist_icon,
            R.drawable.venue_icon
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        initActivity();

    }

    public void processResponseData() {
        try {
            // get artist names
            if (this.eventObject.has("_embedded") && this.eventObject.getJSONObject("_embedded").has("attractions")) {
                JSONArray attractions = this.eventObject.getJSONObject("_embedded").getJSONArray("attractions");
                for (int i = 0; i < attractions.length(); i++) {
                    JSONObject attraction = attractions.getJSONObject(i);
                    if (attraction.has("classifications") && attraction.getJSONArray("classifications").length() > 0) {
                        JSONObject classification = attraction.getJSONArray("classifications").getJSONObject(0);
                        if (classification.has("segment") && classification.getJSONObject("segment").has("name")
                                && classification.getJSONObject("segment").getString("name").contains("Music")) {
                            this.artistNames.add(attraction.optString("name", ""));
                        }
                    }
                }
            }
            // get venue name
            if (this.eventObject.has("_embedded") && this.eventObject.getJSONObject("_embedded").has("venues")) {
                JSONArray venues = this.eventObject.getJSONObject("_embedded").getJSONArray("venues");
                if (venues.length() > 0) {
                    JSONObject venue = venues.getJSONObject(0);
                    this.venueName = venue.optString("name", "");
                }
            }
            sendDataToFragments();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void sendDataToFragments() {
        Bundle detailsBundle = new Bundle();
        detailsBundle.putString("eventString", this.eventString);
        this.eventDetailDetailsFragment.setArguments(detailsBundle);
        Bundle artistBundle = new Bundle();
        artistBundle.putStringArrayList("artistNames", (ArrayList<String>) this.artistNames);
        this.eventDetailArtistFragment.setArguments(artistBundle);
        Bundle venueBundle = new Bundle();
        venueBundle.putString("venueName", this.venueName);
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
                    eventString = response;
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

        heartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add to favorite
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