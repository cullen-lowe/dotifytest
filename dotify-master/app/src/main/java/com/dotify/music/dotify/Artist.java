package com.dotify.music.dotify;

import java.util.HashMap;

public class Artist {

    private HashMap<String, Album> discography;
    private String name;

    public Artist(String name) {
        this.name = name;
        discography = new HashMap<>();
    }

    Album addAlbumIfEmpty(String albumName) {
        Album album = discography.get(albumName);
        if (album == null) {
            album = new Album(name, albumName);
            discography.put(albumName, album);
        }
        return album;
    }

    void updateAlbum(Album album) {
        discography.put(album.getTitle(), album);
    }

    public HashMap<String, Album> getDiscography() {
        return discography;
    }

    public String getName() {
        return name;
    }
}
