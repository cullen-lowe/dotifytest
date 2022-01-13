package com.dotify.music.dotify;

public class Song {

    private String title;
    private String url;
    private String artist;
    private String album;

    public Song(String artist, String album, String title, String url) {
        this.artist = artist;
        this.album = album;
        this.title = title;
        this.url = url;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
