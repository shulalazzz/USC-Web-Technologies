package com.example.hw9;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hw9.fragments.FavoritesFragment;
import com.example.hw9.fragments.SearchFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new FavoritesFragment();
        }
        return new SearchFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
