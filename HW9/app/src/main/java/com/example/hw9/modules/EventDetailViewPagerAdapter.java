package com.example.hw9.modules;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hw9.fragments.EventDetailArtistFragment;
import com.example.hw9.fragments.EventDetailDetailsFragment;
import com.example.hw9.fragments.EventDetailVenueFragment;

import java.util.ArrayList;
import java.util.List;

public class EventDetailViewPagerAdapter extends FragmentStateAdapter {

    List<Fragment> mFragmentList = new ArrayList<>();
    public EventDetailViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public EventDetailViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public EventDetailViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }
}
