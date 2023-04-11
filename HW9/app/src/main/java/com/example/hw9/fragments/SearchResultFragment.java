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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchResultFragment extends Fragment {

    View view;
    RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_result, container, false);
        queue = Volley.newRequestQueue(requireContext());
        if (getArguments() == null) {
            return view;
        }
        Bundle passingData = getArguments();
        String searchUrl = passingData.getString("searchUrl");
        System.out.println(searchUrl);

        LinearLayout searchResultHeader = view.findViewById(R.id.search_result_header);
        searchResultHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_searchResultFragment_to_searchFragment, passingData);
            }
        });

        sendToBackendSearch(searchUrl);


        return view;
    }

    public void sendToBackendSearch(String searchUrl) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, searchUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            displaySearchResult(jsonObject);
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

    public void displaySearchResult(JSONObject resultData) {

    }


}