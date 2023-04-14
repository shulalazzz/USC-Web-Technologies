package com.example.hw9.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9.R;
import com.example.hw9.modules.EventItem;
import com.example.hw9.modules.EventItemViewAdapter;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultFragment extends Fragment {

    View view;
    RequestQueue queue;
    RelativeLayout progressBarContainer;
    RecyclerView searchResultRecyclerView;
    Bundle passingData;
    String searchUrl;
    List<EventItem> eventItems = new ArrayList<>();
    EventItemViewAdapter eventItemViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_result, container, false);
        progressBarContainer = view.findViewById(R.id.search_result_progress_bar_container);
        searchResultRecyclerView = view.findViewById(R.id.search_result_recycler_view);
        queue = Volley.newRequestQueue(requireContext());
        if (getArguments() == null) {
            return view;
        }
        passingData = getArguments();
        searchUrl = passingData.getString("searchUrl");
        System.out.println(searchUrl);

        LinearLayout searchResultHeader = view.findViewById(R.id.search_result_header);
        searchResultHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_searchResultFragment_to_searchFragment, passingData);
            }
        });

        sendToBackendSearch();


        return view;
    }

    public void sendToBackendSearch() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, searchUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        if (response == null || response.trim().equals("") || response.equals("null")) {
                            return;
                        }
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String eventName = jsonObject.getString("eventName");
                                String dates = jsonObject.getString("dates");
                                String imgUrl = jsonObject.getString("images");
                                String genre = jsonObject.getString("genre");
                                String venue = jsonObject.getString("venue");
                                EventItem eventItem = new EventItem(eventName, venue, dates, imgUrl, id, genre);
                                eventItems.add(eventItem);
                            }
                            progressBarContainer.setVisibility(View.GONE);
                            displaySearchResult();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(view, "Error: " + error.toString(), Snackbar.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    public void displaySearchResult() {
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        eventItemViewAdapter = new EventItemViewAdapter(requireContext(), eventItems);
        searchResultRecyclerView.setAdapter(eventItemViewAdapter);
    }


}