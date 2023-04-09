package com.example.hw9.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.hw9.HomeActivity;
import com.example.hw9.R;

public class SearchFragment extends Fragment {

    Button searchButton;
    Button clearButton;
    AutoCompleteTextView keywordInput;
    EditText distanceInput;
    Spinner categoryInput;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch locationSwitch;
    AutoCompleteTextView locationInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchButton = view.findViewById(R.id.search_button);
        clearButton = view.findViewById(R.id.clear_button);
        keywordInput = view.findViewById(R.id.keyword_input);
        distanceInput = view.findViewById(R.id.distance_input);
        categoryInput = view.findViewById(R.id.category_spinner);
        locationSwitch = view.findViewById(R.id.location_switch);
        locationInput = view.findViewById(R.id.location_input);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = keywordInput.getText().toString();
                int distance = distanceInput.getText().toString().isEmpty() ? 10 : Integer.parseInt(distanceInput.getText().toString());
                String category = categoryInput.getSelectedItem().toString();
                String location = locationInput.getText().toString();
                boolean locationSwitchStatus = locationSwitch.isChecked();
                if (keyword.isEmpty()) {
                    Toast.makeText(getContext(), "Keyword cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (!locationSwitchStatus && location.isEmpty()) {
                    Toast.makeText(getContext(), "Location cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "http://csci571-hw9-env.eba-3jzjzq2p.us-east-2.elasticbeanstalk.com/search?keyword=" + keyword + "&category=" + category + "&distance=" + distance + "&location=" + location + "&locationSwitch=" + locationSwitchStatus;
//                    Intent intent = new Intent(HomeActivity.this, SearchResultActivity.class);
//                    intent.putExtra("url", url);
//                    startActivity(intent);
                    System.out.println(keyword + " " + category + " " + distance + " " + location + " " + locationSwitchStatus);
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywordInput.setText("");
                distanceInput.setText("10");
                categoryInput.setSelection(0);
                locationSwitch.setChecked(false);
                locationInput.setText("");
            }
        });

        locationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                locationInput.setVisibility(View.GONE);
            } else {
                locationInput.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}