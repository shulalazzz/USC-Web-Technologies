package com.example.hw9.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class SearchFragment extends Fragment {

    Button searchButton;
    Button clearButton;
    AutoCompleteTextView keywordInput;
    EditText distanceInput;
    Spinner categoryInput;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch locationSwitch;
    AutoCompleteTextView locationInput;
    private final String backendSearchUrl = "https://csci-571-hw8-382201.wl.r.appspot.com/search/";
    private final String backendAutoCompleteUrl = "https://csci-571-hw8-382201.wl.r.appspot.com/autocomplete/";
    private final String ipInfoApi = "https://ipinfo.io/json?token=fcee7187512c64";
    RequestQueue queue;
    View view;

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

        searchButton = view.findViewById(R.id.search_button);
        clearButton = view.findViewById(R.id.clear_button);
        keywordInput = view.findViewById(R.id.keyword_input);
        distanceInput = view.findViewById(R.id.distance_input);
        categoryInput = view.findViewById(R.id.category_spinner);
        locationSwitch = view.findViewById(R.id.location_switch);
        locationInput = view.findViewById(R.id.location_input);
        queue = Volley.newRequestQueue(requireContext());

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = keywordInput.getText().toString();
                int distance = distanceInput.getText().toString().isEmpty() ? 10 : Integer.parseInt(distanceInput.getText().toString());
                String category = categoryInput.getSelectedItem().toString();
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
                                    String locationGeo = jsonObject.getString("loc");
                                    String url = backendSearchUrl + "?keyword=" + keyword + "&category=" + categoryMap.get(category) + "&distance=" + distance + "&location=" + locationGeo;
                                    sendToSearchResultFragment(url);
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
                                    String locationGeo = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").
                                            getJSONObject("location").getString("lat") + "," +
                                            jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").
                                                    getJSONObject("location").getString("lng");
                                    String url = backendSearchUrl + "?keyword=" + keyword + "&category=" + categoryMap.get(category) + "&distance=" + distance + "&location=" + locationGeo;
                                    sendToSearchResultFragment(url);
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

    public void sendToSearchResultFragment(String url) {
        Bundle result = new Bundle();
        result.putString("searchUrl", url);
//        getParentFragmentManager().setFragmentResult("searchUrlResult", result);
//        System.out.println(url);
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        searchResultFragment.setArguments(result);
        getParentFragmentManager().beginTransaction().replace(R.id.view_pager, searchResultFragment).addToBackStack(null).commit();
    }

}