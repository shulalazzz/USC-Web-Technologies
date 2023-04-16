package com.example.hw9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.hw9.modules.HomeViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    HomeViewPagerAdapter homeViewPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initActivity();
    }

    public void initActivity() {
        tabLayout = findViewById(R.id.home_tab_layout);
        viewPager2 = findViewById(R.id.home_view_pager);
        homeViewPagerAdapter = new HomeViewPagerAdapter(this);
        viewPager2.setAdapter(homeViewPagerAdapter);

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