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

public class EventDetailViewPagerAdapter extends FragmentStateAdapter {
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
        if (position == 1) {
            return new EventDetailDetailsFragment();
        }
        else if (position == 2) {
            return new EventDetailArtistFragment();
        }
        return new EventDetailVenueFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
