package com.example.hw9.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hw9.EventDetailActivity;
import com.example.hw9.R;
import com.example.hw9.modules.EventItem;
import com.example.hw9.modules.EventItemViewAdapter;
import com.example.hw9.modules.FavoriteItemViewAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FavoritesFragment extends Fragment implements FavoriteItemViewAdapter.OnEventListener{
    View view;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView favoritesRecyclerView;
    FavoriteItemViewAdapter eventItemViewAdapter;
    List<EventItem> eventItems = new ArrayList<>();
    Gson gson = new Gson();
    RelativeLayout progressBarContainer;
    RelativeLayout noResultContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favorites, container, false);
        initFragment();
        updateFavoriteList();
        displayFavoriteList();
        return view;
    }

    public void initFragment() {
        sharedPreferences = requireActivity().getSharedPreferences("eventPreference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        favoritesRecyclerView = view.findViewById(R.id.favorite_recycler_view);
        progressBarContainer = view.findViewById(R.id.search_result_progress_bar_container);
        noResultContainer = view.findViewById(R.id.no_result_container);

    }

    public void updateFavoriteList() {
        eventItems.clear();
        Map<String, ?> allEntries = sharedPreferences.getAll();
        if (allEntries.isEmpty()) {
            progressBarContainer.setVisibility(View.GONE);
            noResultContainer.setVisibility(View.VISIBLE);
            return;
        } else {
            noResultContainer.setVisibility(View.GONE);
            progressBarContainer.setVisibility(View.GONE);
        }
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
//            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            EventItem eventItem = gson.fromJson(entry.getValue().toString(), EventItem.class);
            eventItems.add(eventItem);
        }
    }

    public void displayFavoriteList() {
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventItemViewAdapter = new FavoriteItemViewAdapter(requireContext(), eventItems, this);
        eventItemViewAdapter.setNoResultsContainer(noResultContainer);
        favoritesRecyclerView.setAdapter(eventItemViewAdapter);
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        if(eventItemViewAdapter != null)
            eventItemViewAdapter.notifyDataSetChanged();
        updateFavoriteList();
    }

    @Override
    public void onEventClick(int position) {
        Intent intent = new Intent(requireContext(), EventDetailActivity.class);
        intent.putExtra("eventItem", eventItems.get(position));
        startActivity(intent);
    }

}