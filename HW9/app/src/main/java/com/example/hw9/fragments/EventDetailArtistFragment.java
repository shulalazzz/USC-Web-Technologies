package com.example.hw9.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9.R;
import com.example.hw9.modules.ArtistItem;
import com.example.hw9.modules.ArtistItemViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class EventDetailArtistFragment extends Fragment {

    View view;
    Bundle bundle;
    ArrayList<String> artistNames;
    List<ArtistItem> artistItems = new ArrayList<>();
    RequestQueue queue;
    int requestCount = 0;
    RelativeLayout progressBarContainer;
    RelativeLayout noResultContainer;
    String backendArtistUrl = "https://csci-571-hw8-382201.wl.r.appspot.com/spotifyArtist/";
    String backendAlbumUrl = "https://csci-571-hw8-382201.wl.r.appspot.com/spotifyAlbum/";
    RecyclerView artistRecyclerView;
    ArtistItemViewAdapter artistItemViewAdapter;

    Comparator<ArtistItem> artistItemComparator = new Comparator<ArtistItem>() {
        @Override
        public int compare(ArtistItem o1, ArtistItem o2) {
            int idx1 = artistNames.indexOf(o1.getName().toLowerCase(Locale.ROOT));
            int idx2 = artistNames.indexOf(o2.getName().toLowerCase(Locale.ROOT));
            return idx1 - idx2;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_artist, container, false);
        initFragment();
        sendToBackendArtist();
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
//                Log.e("Volley", "Finished");
                requestCount--;
                if (requestCount == 0) {
                    setupView();
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = getView();
        bundle = getArguments();
    }

    public void initFragment() {
        queue = Volley.newRequestQueue(requireContext());
        this.artistNames = bundle.getStringArrayList("artistNames");
        progressBarContainer = view.findViewById(R.id.event_artist_progress_bar_container);
        noResultContainer = view.findViewById(R.id.no_result_container);
        artistRecyclerView = view.findViewById(R.id.event_artist_recycler_view);
    }

    public void setupView() {
        progressBarContainer.setVisibility(View.GONE);
        if (artistItems.size() == 0) {
            noResultContainer.setVisibility(View.VISIBLE);
            return;
        }
        artistItems.sort(artistItemComparator);
        for (ArtistItem artistItem : artistItems) {
            System.out.println(artistItem.getName());
//            System.out.println(artistItem.getAlbums());
        }
//        System.out.println("length of artistItems: " + artistItems.size());
//        noResultContainer.setVisibility(View.VISIBLE);
        artistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        artistItemViewAdapter = new ArtistItemViewAdapter(requireContext(), artistItems);
        artistRecyclerView.setAdapter(artistItemViewAdapter);
    }

    public void sendToBackendArtist() {
        System.out.println("from send to backend artist");
        System.out.println(this.artistNames);
        if (this.artistNames == null || this.artistNames.size() == 0) {
            progressBarContainer.setVisibility(View.GONE);
            noResultContainer.setVisibility(View.VISIBLE);
            return;
        }
        this.artistNames.replaceAll(s -> s.toLowerCase(Locale.ROOT));
        for (String artistName : artistNames) {
            requestCount++;
            JsonObjectRequest artistRequest = new JsonObjectRequest(Request.Method.GET, backendArtistUrl + artistName, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String name = response.getString("name");
                                String imgUrl = response.getJSONArray("images").getJSONObject(0).getString("url");
                                String popularity = response.getString("popularity");
                                String followers = response.getJSONObject("followers").getString("total");
                                String spotifyUrl = response.getJSONObject("external_urls").getString("spotify");
                                List<String> albums = new ArrayList<>();
                                ArtistItem artistItem = new ArtistItem(name, imgUrl, popularity, followers, spotifyUrl, albums);
                                artistItems.add(artistItem);
                                requestCount++;
                                JsonArrayRequest albumRequest = new JsonArrayRequest(Request.Method.GET, backendAlbumUrl + response.getString("id"), null,
                                        new Response.Listener<JSONArray>() {
                                            @Override
                                            public void onResponse(JSONArray response) {
                                                for (int i = 0; i < response.length(); i++) {
                                                    try {
                                                        artistItem.getAlbums().add(response.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("url"));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Volley", "Error");
                                    }
                                });
                                queue.add(albumRequest);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley", error.getMessage());
                }
            });
            queue.add(artistRequest);
        }
    }
}