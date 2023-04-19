package com.example.hw9.modules;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hw9.R;

import java.util.List;

public class ArtistItemViewAdapter extends RecyclerView.Adapter<ArtistItemViewAdapter.ArtistItemViewHolder> {

    Context context;
    List<ArtistItem> artistItems;
    View view;

    // ViewHolder
    public static class ArtistItemViewHolder extends RecyclerView.ViewHolder {

        ImageView artistImageView;
        TextView artistNameView;
        TextView followersView;
        TextView spotifyUrlView;
        ProgressBar popularityProgressBar;
        TextView popularityText;
        ImageView albumImageView1;
        ImageView albumImageView2;
        ImageView albumImageView3;

        public ArtistItemViewHolder(@NonNull View itemView) {
            super(itemView);

            artistImageView = itemView.findViewById(R.id.artist_image);
            artistNameView = itemView.findViewById(R.id.artist_name);
            followersView = itemView.findViewById(R.id.followers);
            spotifyUrlView = itemView.findViewById(R.id.spotify_url);
            popularityProgressBar = itemView.findViewById(R.id.popularity_progressBar);
            popularityText = itemView.findViewById(R.id.popularity_text);
            albumImageView1 = itemView.findViewById(R.id.album_image1);
            albumImageView2 = itemView.findViewById(R.id.album_image2);
            albumImageView3 = itemView.findViewById(R.id.album_image3);
        }
    }

    // Constructor
    public ArtistItemViewAdapter(Context context, List<ArtistItem> artistItems) {
        this.context = context;
        this.artistItems = artistItems;
    }

    // Create ViewHolder
    @NonNull
    @Override
    public ArtistItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.artist_item, parent, false);
        return new ArtistItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ArtistItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.artistNameView.setText(artistItems.get(position).getName());
        holder.artistNameView.post(new Runnable() {
            @Override
            public void run() {
                holder.artistNameView.setSelected(true);
            }
        });
        Glide.with(context).load(artistItems.get(position).getImgUrl()).into(holder.artistImageView);
        String followers = formatFollowers(Integer.parseInt(artistItems.get(position).getFollowers()));
        holder.followersView.setText(followers + " Followers");
        holder.followersView.post(new Runnable() {
            @Override
            public void run() {
                holder.followersView.setSelected(true);
            }
        });
        String text = "Check out on Spotify";
        SpannableString content = new SpannableString(text);
        content.setSpan(new android.text.style.URLSpan(text), 0, content.length(), 0);
        holder.spotifyUrlView.setText(content);
        holder.spotifyUrlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = artistItems.get(position).getSpotifyUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url));
                view.getContext().startActivity(browserIntent);
            }
        });

        holder.popularityProgressBar.setProgress(Integer.parseInt(artistItems.get(position).getPopularity()));
        holder.popularityText.setText(artistItems.get(position).getPopularity());
        Glide.with(context).load(artistItems.get(position).getAlbums().get(0)).into(holder.albumImageView1);
        Glide.with(context).load(artistItems.get(position).getAlbums().get(1)).into(holder.albumImageView2);
        Glide.with(context).load(artistItems.get(position).getAlbums().get(2)).into(holder.albumImageView3);
    }

    @Override
    public int getItemCount() {
        return artistItems.size();
    }

    @SuppressLint("DefaultLocale")
    public static String formatFollowers(int value) {
        if (value >= 1000000) {
            double millions = value / 1000000.0;
            return String.format("%.1fM", millions);
        } else if (value >= 1000) {
            double thousands = value / 1000.0;
            return String.format("%.0fK", thousands);
        } else {
            return String.valueOf(value);
        }
    }


}
