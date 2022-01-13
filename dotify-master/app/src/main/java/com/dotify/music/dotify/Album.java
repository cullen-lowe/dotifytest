package com.dotify.music.dotify;


import java.util.Map;
import java.util.TreeMap;

public class Album {
    private String title;
    private String artist;
    private String date;
    private TreeMap<Integer, Song> songs;

    public Album(String artist, String title) {
        this.artist = artist;
        this.title = title;
        songs = new TreeMap<>();
    }

    public TreeMap<Integer, Song> getSongs() {
        return songs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
    }

    public void addSong(String songName, String url, int id) {
        songs.put(id, new Song(artist, title, songName, url));
    }

    public void print(){
        System.out.println("Album Title: " + title);
        System.out.println("Album Artist: " + artist);
    }
}
