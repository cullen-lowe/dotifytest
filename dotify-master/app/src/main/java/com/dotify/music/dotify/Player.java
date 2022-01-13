package com.dotify.music.dotify;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

class Player {
    private static final Player ourInstance = new Player();

    private static final String SERVER_IP = DatabaseRetriever.SERVER_IP;
    private boolean songsLoaded;
    private boolean initialized;

    private MediaPlayer player;
    private ArrayList<Song> queue;
    private ListIterator<Song> listIterator;
    private Song currSong;
    private boolean playNextImmediately;

    static Player getInstance() {
        return ourInstance;
    }

    private Player() {
        songsLoaded = false;
        initialized = false;
        currSong = null;
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        queue = new ArrayList<>();
    }

    public void buildQueue(Song[] songs) {
        queue.clear();
        queue.addAll(Arrays.asList(songs));
        listIterator = queue.listIterator();
        songsLoaded = true;
        currSong = next();
    }

    public boolean hasLoadedSongs() {
        return songsLoaded;
    }

    public Song getCurrentSong() {
        return currSong;
    }

    public Song next() {
        Song nextSong = null;
        if (listIterator != null && listIterator.hasNext()) {
            nextSong = listIterator.next();
            prepareSong(nextSong);
        }
        return nextSong;
    }

    public Song previous() {
        Song prevSong = null;
        if (listIterator != null && listIterator.hasPrevious()) {
            prevSong = listIterator.previous();
            prepareSong(prevSong);
        }
        return prevSong;
    }

    private void prepareSong(Song song) {
        try {
            Log.d("MusicStream", "Streaming http://" + SERVER_IP + "/music/" + song.getUrl());
            player.stop();
            player.reset();
            initialized = false;
            player.setDataSource("http://" + SERVER_IP + "/music/" + song.getUrl());
            player.setOnPreparedListener((mp) -> {
                if (playNextImmediately) {
                    playNextImmediately = false;
                    mp.start();
                }
                initialized = true;
            });
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playNewSelected() {
        playNextImmediately = true;
    }

    public void play() {
        if (!player.isPlaying() && initialized)
            player.start();
    }

    public void pause() {
        if (player.isPlaying() && initialized)
            player.pause();
    }

    public boolean isPlaying() {
        return initialized && player.isPlaying();
    }

    public boolean setToPlay() {
        return playNextImmediately;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
