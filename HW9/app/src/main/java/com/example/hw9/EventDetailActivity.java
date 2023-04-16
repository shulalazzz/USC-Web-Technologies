package com.example.hw9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hw9.modules.EventDetailViewPagerAdapter;
import com.example.hw9.modules.EventItem;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class EventDetailActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    EventDetailViewPagerAdapter eventDetailViewPagerAdapter;
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

    public void initActivity() {
        EventItem eventItem = (EventItem) getIntent().getSerializableExtra("eventItem");
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setColorFilter(getResources().getColor(R.color.text_green));
        TextView eventTitle = findViewById(R.id.event_title);
        eventTitle.setText(eventItem.getEventName());

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