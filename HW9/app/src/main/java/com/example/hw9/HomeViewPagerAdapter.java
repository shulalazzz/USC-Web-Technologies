package com.example.hw9;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hw9.fragments.FavoritesFragment;
import com.example.hw9.fragments.HostSearchFragment;
import com.example.hw9.fragments.SearchFragment;

public class HomeViewPagerAdapter extends FragmentStateAdapter {

    public HomeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new FavoritesFragment();
        }
        return new HostSearchFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
