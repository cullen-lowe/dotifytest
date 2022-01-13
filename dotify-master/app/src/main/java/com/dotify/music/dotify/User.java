package com.dotify.music.dotify;

import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String hashedPassword;

    //privacy settings TBA?

    private Playlist favorites;
    private ArrayList<Playlist> playlists;

    public User(String username){
        this.username = username;
    }
}
