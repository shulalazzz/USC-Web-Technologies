package com.example.hw9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hw9.modules.EventDetailViewPagerAdapter;
import com.example.hw9.modules.EventItem;
import com.google.android.material.tabs.TabLayout;

public class EventDetailActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    EventDetailViewPagerAdapter eventDetailViewPagerAdapter;


//    https://stackoverflow.com/questions/37833495/add-iconstext-to-tablayout

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
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
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

    }
}