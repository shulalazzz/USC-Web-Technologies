package com.example.hw9.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hw9.R;
public class FavoritesFragment extends Fragment {
    View view;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favorites, container, false);

        textView = view.findViewById(R.id.test_text);
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setSelected(true);
            }
        });
        return view;
    }

}