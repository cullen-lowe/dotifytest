package com.dotify.music.dotify;

import java.util.ArrayList;

public class Playlist{

    private String name;
    private ArrayList<Song> songs;

    //constructor
    public Playlist(String name){
        this.name = name;
        songs = new ArrayList<>();
    }

    //returns arraylist of playlist's songs
    public ArrayList<Song> getSongs(){
        return songs;
    }

    public void addSong(Song s){
        songs.add(s);
    }

    public void removeSong(Song s){
        songs.remove(s);
    }

    public void changePlaylistName(String name){
        this.name = name;
    }

}
