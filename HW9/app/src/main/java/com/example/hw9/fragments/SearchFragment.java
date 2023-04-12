package com.example.hw9.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9.HomeActivity;
import com.example.hw9.R;
import com.example.hw9.modules.SuggestionsAdapter;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class SearchFragment extends Fragment {

    private AutoCompleteTextView keywordInput;
    private EditText distanceInput;
    private Spinner categoryInput;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch locationSwitch;
    private String locationGeo;
    private AutoCompleteTextView locationInput;
    private ProgressBar autoCompleteProgressBar;
    private final String backendAutoCompleteUrl = "https://csci-571-hw8-382201.wl.r.appspot.com/autocomplete/";
    private final String ipInfoApi = "https://ipinfo.io/json?token=fcee7187512c64";
    private RequestQueue queue;
    View view;
    // The array that contains the suggestions
    ArrayAdapter<String> autoCompleteAdapter;
    private Handler autocompleteDelayHandler = new Handler();
    private Runnable runnable;

    static HashMap<String, String> categoryMap = new HashMap<String, String>() {{
        put("All", "Default");
        put("Music", "Music");
        put("Sports", "Sports");
        put("Arts & Theatre", "Arts");
        put("Film", "Film");
        put("Miscellaneous", "Miscellaneous");
    }};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);

        Button searchButton = view.findViewById(R.id.search_button);
        Button clearButton = view.findViewById(R.id.clear_button);
        distanceInput = view.findViewById(R.id.distance_input);
        categoryInput = view.findViewById(R.id.category_spinner);
        locationSwitch = view.findViewById(R.id.location_switch);
        locationInput = view.findViewById(R.id.location_input);
        autoCompleteProgressBar = view.findViewById(R.id.autocomplete_progress_bar);
        initAutoComplete();
        queue = Volley.newRequestQueue(requireContext());
        restoreForm(getArguments());

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = keywordInput.getText().toString();
                String location = locationInput.getText().toString();
                boolean locationSwitchStatus = locationSwitch.isChecked();
                if (keyword.isEmpty()) {
                    Snackbar.make(view, "Keyword cannot be empty", Snackbar.LENGTH_SHORT).show();
                } else if (!locationSwitchStatus && location.isEmpty()) {
                    Snackbar.make(view, "Location cannot be empty", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (location.isEmpty()) {
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, ipInfoApi, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    locationGeo = jsonObject.getString("loc");
                                    sendToSearchResultFragment();
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Snackbar.make(view, "Error: " + error.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        queue.add(stringRequest);
                    } else {
                        location = location.trim().replaceAll("\\s+", "+");
                        String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=AIzaSyAhrUOOniYwPz_aLnuKi2M6v3DfG50oH5o";
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, googleMapUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("ZERO_RESULTS")) {
                                        Snackbar.make(view, "Location not found", Snackbar.LENGTH_SHORT).show();
                                        return;
                                    }
                                    locationGeo = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").
                                            getJSONObject("location").getString("lat") + "," +
                                            jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").
                                                    getJSONObject("location").getString("lng");
                                    sendToSearchResultFragment();
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Snackbar.make(view, "Error: " + error.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        queue.add(stringRequest);
                    }
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
            locationInput.setText("");
        });

        return view;
    }

    private void restoreForm(Bundle passingData) {
        if (passingData != null) {
            keywordInput.setText(passingData.getString("keyword"));
            distanceInput.setText(passingData.getString("distance"));
            boolean locationSwitchStatus = passingData.getBoolean("locationSwitch");
            locationSwitch.setChecked(locationSwitchStatus);
            if (locationSwitchStatus) {
                locationInput.setVisibility(View.GONE);
            } else {
                locationInput.setVisibility(View.VISIBLE);
            }
            locationInput.setText(passingData.getString("location"));
            categoryInput.setSelection(Integer.parseInt(passingData.getString("category")));
        }
    }

    private void sendToSearchResultFragment() throws JSONException {
        String backendSearchUrl = "https://csci-571-hw8-382201.wl.r.appspot.com/search/";
        Bundle outState = new Bundle();
        outState.putString("keyword", keywordInput.getText().toString());
        outState.putString("distance", distanceInput.getText().toString());
        outState.putString("category", String.valueOf(categoryInput.getSelectedItemPosition()));
        outState.putBoolean("locationSwitch", locationSwitch.isChecked());
        outState.putString("location", locationInput.getText().toString());

        JSONObject searchJson = new JSONObject();
        searchJson.put("keyword", keywordInput.getText().toString());
        searchJson.put("category", categoryMap.get(categoryInput.getSelectedItem().toString()));
        searchJson.put("distance", distanceInput.getText().toString());
        searchJson.put("location", locationGeo);
        String searchUrl = backendSearchUrl + searchJson;
        outState.putString("searchUrl", searchUrl);

        Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_searchResultFragment, outState);
    }

    private void initAutoComplete() {
        keywordInput = view.findViewById(R.id.keyword_input);
        autoCompleteAdapter = new SuggestionsAdapter(requireContext(), android.R.layout.simple_list_item_1);
        keywordInput.setAdapter(autoCompleteAdapter);
        keywordInput.setThreshold(0);
        keywordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    String autoCompleteUrl = backendAutoCompleteUrl + s;
                    autocompleteDelayHandler.removeCallbacks(runnable);
                    autoCompleteProgressBar.setVisibility(View.VISIBLE);
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            sendAutoCompleteAndUpdateAdapter(autoCompleteUrl);
                        }
                    };
                    autocompleteDelayHandler.postDelayed(runnable, 500);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void sendAutoCompleteAndUpdateAdapter(String autoCompleteUrl) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, autoCompleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                System.out.println(response);
                if (response == null || response.trim().equals("") || response.equals("null")) {
                    autoCompleteAdapter.clear();
                    autoCompleteAdapter.notifyDataSetChanged();
                    autoCompleteProgressBar.setVisibility(View.GONE);
                    return;
                }
                try {
                    JSONArray suggestions = new JSONArray(response);
                    autoCompleteAdapter.clear();
                    for (int i = 0; i < suggestions.length(); i++) {
                        autoCompleteAdapter.add(suggestions.getString(i));
                    }
                    autoCompleteAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                autoCompleteProgressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                autoCompleteProgressBar.setVisibility(View.GONE);
                Snackbar.make(view, "Error: " + error.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

}