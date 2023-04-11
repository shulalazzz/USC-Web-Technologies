package com.example.hw9.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.hw9.R;

public class SearchResultFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_result, container, false);

        if (getArguments() == null) {
            return view;
        }
        Bundle passingData = getArguments();
        String searchUrl = passingData.getString("searchUrl");
        System.out.println(searchUrl + "from search result fragment");

        LinearLayout searchResultHeader = view.findViewById(R.id.search_result_header);
        searchResultHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_searchResultFragment_to_searchFragment, passingData);
            }
        });


        return view;
    }
}