package com.example.hw9.modules;


import java.util.List;

public class ArtistItem {
    String name;
    String imgUrl;
    String popularity;
    String followers;
    String spotifyUrl;
    List<String> albums;

    public ArtistItem(String name, String imgUrl, String popularity, String followers, String spotifyUrl, List<String> albums) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.popularity = popularity;
        this.followers = followers;
        this.spotifyUrl = spotifyUrl;
        this.albums = albums;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getFollowers() {
        return followers;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public List<String> getAlbums() {
        return albums;
    }
}
