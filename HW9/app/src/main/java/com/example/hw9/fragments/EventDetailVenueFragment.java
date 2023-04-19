package com.example.hw9.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class EventDetailVenueFragment extends Fragment {

    View view;
    Bundle bundle;
    String backendVenueUrl = "https://csci-571-hw8-382201.wl.r.appspot.com/venue/";
    RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = getView();
        bundle = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_event_detail_venue, container, false);
        initFragment();
        sendToBackendVenue();
        return view;
    }
    
    public void initFragment() {
        queue = Volley.newRequestQueue(requireContext());
    }

    public void setupView(JSONObject venueObject) {
        if (venueObject == null) {
            Log.d("Venue Fragment", "Venue object is null");
            return;
        }
        try {
            if (venueObject.has("name")) {
                TextView venueName = view.findViewById(R.id.event_venue_name_text);
                venueName.setText(venueObject.getString("name"));
                venueName.post(new Runnable() {
                    @Override
                    public void run() {
                        venueName.setSelected(true);
                    }
                });
            }
            else {
                view.findViewById(R.id.event_venue_name).setVisibility(View.GONE);
            }
            if (venueObject.has("address") && venueObject.getJSONObject("address").has("line1")) {
                TextView venueAddress = view.findViewById(R.id.event_venue_address_text);
                venueAddress.setText(venueObject.getJSONObject("address").getString("line1"));
                venueAddress.post(new Runnable() {
                    @Override
                    public void run() {
                        venueAddress.setSelected(true);
                    }
                });
            }
            else {
                view.findViewById(R.id.event_venue_address).setVisibility(View.GONE);
            }
            List<String> venueCityState = new ArrayList<>();
            if (venueObject.has("city") && venueObject.getJSONObject("city").has("name")) {
                venueCityState.add(venueObject.getJSONObject("city").getString("name"));
            }
            if (venueObject.has("state") && venueObject.getJSONObject("state").has("name")) {
                venueCityState.add(venueObject.getJSONObject("state").getString("name"));
            }
            if (venueCityState.size() > 0) {
                TextView venueCityStateText = view.findViewById(R.id.event_venue_city_state_text);
                venueCityStateText.setText(String.join(", ", venueCityState));
                venueCityStateText.post(new Runnable() {
                    @Override
                    public void run() {
                        venueCityStateText.setSelected(true);
                    }
                });
            }
            else {
                view.findViewById(R.id.event_venue_city_state).setVisibility(View.GONE);
            }
            if (venueObject.has("boxOfficeInfo") && venueObject.getJSONObject("boxOfficeInfo").has("phoneNumberDetail")) {
                TextView venuePhone = view.findViewById(R.id.event_venue_contact_text);
                venuePhone.setText(venueObject.getJSONObject("boxOfficeInfo").getString("phoneNumberDetail"));
                venuePhone.post(new Runnable() {
                    @Override
                    public void run() {
                        venuePhone.setSelected(true);
                    }
                });
            }
            else {
                view.findViewById(R.id.event_venue_contact).setVisibility(View.GONE);
            }
            if (venueObject.has("boxOfficeInfo") && venueObject.getJSONObject("boxOfficeInfo").has("openHoursDetail")) {
                TextView venueOpenHours = view.findViewById(R.id.event_venue_open_hours_text);
                venueOpenHours.setText(venueObject.getJSONObject("boxOfficeInfo").getString("openHoursDetail"));
                venueOpenHours.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (venueOpenHours.getMaxLines() == 3) {
                            venueOpenHours.setMaxLines(100);
                        }
                        else {
                            venueOpenHours.setMaxLines(3);
                        }
                    }
                });
            }
            else {
                view.findViewById(R.id.event_venue_open_hours).setVisibility(View.GONE);
            }
            if (venueObject.has("generalInfo") && venueObject.getJSONObject("generalInfo").has("generalRule")) {
                TextView venueGeneralRule = view.findViewById(R.id.event_venue_general_rules_text);
                venueGeneralRule.setText(venueObject.getJSONObject("generalInfo").getString("generalRule"));
                venueGeneralRule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (venueGeneralRule.getMaxLines() == 3) {
                            venueGeneralRule.setMaxLines(100);
                        }
                        else {
                            venueGeneralRule.setMaxLines(3);
                        }
                    }
                });
            }
            else {
                view.findViewById(R.id.event_venue_general_rules).setVisibility(View.GONE);
            }
            if (venueObject.has("generalInfo") && venueObject.getJSONObject("generalInfo").has("childRule")) {
                TextView venueChildRule = view.findViewById(R.id.event_venue_child_rules_text);
                venueChildRule.setText(venueObject.getJSONObject("generalInfo").getString("childRule"));
                venueChildRule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (venueChildRule.getMaxLines() == 3) {
                            venueChildRule.setMaxLines(100);
                        }
                        else {
                            venueChildRule.setMaxLines(3);
                        }
                    }
                });
            }
            else {
                view.findViewById(R.id.event_venue_child_rules).setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void sendToBackendVenue() {
        String venueName = bundle.getString("venueName");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, backendVenueUrl + venueName, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setupView(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

}