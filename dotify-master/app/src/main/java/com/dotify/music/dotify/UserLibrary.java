package com.dotify.music.dotify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class UserLibrary extends Fragment {

    private JSONArray userLibrary;
    private TextView music_statusbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.user_library, container, false);

        music_statusbar = getActivity().findViewById(R.id.music_statusbar_fragment);

        JSONArray userLibrary = getSongs();
        if (userLibrary == null){
            rootView = ErrorDisplay.displayError("Failed to connect to server", this, R.id.main_feed_fragment);
        } else {
            this.userLibrary = userLibrary;
            GridView albumView = rootView.findViewById(R.id.library_albums);

            HashMap<String, Artist> builtLibrary = buildLibrary(userLibrary);
            ArrayList<HashMap<String, String>> adaptedArtists = new ArrayList<>();

            Set<String> keys = builtLibrary.keySet();
            for (String key : keys) {
                Artist artist = builtLibrary.get(key);
                HashMap<String, String> item = new HashMap<>();
                item.put("artist", artist.getName());
                adaptedArtists.add(item);
            }

            String[] from = {"artist"};
            int[] to = {R.id.artist_display_title};

            View finalRootView = rootView;
            SimpleAdapter adapter = new SimpleAdapter(this.getContext(), adaptedArtists, R.layout.artist_display, from, to) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view =  super.getView(position, convertView, parent);
                    TextView textView = view.findViewById(R.id.artist_display_title);
                    textView.setOnClickListener((v) -> {
                        updateViewToAlbums(finalRootView, builtLibrary.get(textView.getText()));
                    });
                    return view;
                }
            };
            albumView.setAdapter(adapter);
        }
        return rootView;
    }

    private JSONArray getSongs() {
        DatabaseRetriever music = new DatabaseRetriever();
        try {
            music.execute("GET", "getAllSongs.php");
            return music.get(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HashMap<String, Artist> buildLibrary(@NonNull JSONArray data){
        HashMap<String, Artist> discography = new HashMap<>();
        String artistName, albumName, songName, songUrl;
        int track;

        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject item = data.getJSONObject(i);
                artistName = item.getString("artist");
                albumName = item.getString("album");
                songName = item.getString("song");
                songUrl = item.getString("url");
                track = item.getInt("track");
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            Artist artist = discography.get(artistName);
            if (artist == null) artist = new Artist(artistName);
            Album album = artist.addAlbumIfEmpty(albumName);
            album.addSong(songName, songUrl, track);
            artist.updateAlbum(album);
            discography.put(artistName, artist);
        }

        return discography;
    }

    private void updateViewToAlbums(View rootView, Artist artist) {
        GridView gridView = rootView.findViewById(R.id.library_albums);
        HashMap<String, Album> discography = artist.getDiscography();
        ArrayList<HashMap<String, String>> adaptedAlbums = new ArrayList<>();

        for (String albumTitle : discography.keySet()) {
            HashMap<String, String> albums = new HashMap<>();
            Album album = discography.get(albumTitle);
            albums.put("_album", album.getTitle());
            adaptedAlbums.add(albums);
        }

        String[] from = {"_album"};
        int[] to = {R.id.artist_display_title};
        SimpleAdapter adapter = new SimpleAdapter(getContext(), adaptedAlbums, R.layout.artist_display, from, to) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.artist_display_title);
                textView.setOnClickListener((v) -> {
                    updateViewToSongs(rootView, discography.get(textView.getText()));
                });
                return view;
            }
        };
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void updateViewToSongs(View rootView, Album album) {
        GridView gridView = rootView.findViewById(R.id.library_albums);
        TreeMap<Integer, Song> songList = album.getSongs();
        ArrayList<HashMap<String, String>> adaptedSongs = new ArrayList<>();

        for (Integer id : songList.keySet()) {
            HashMap<String, String> songs = new HashMap<>();
            Song song = songList.get(id);
            songs.put("_song", song.getTitle());
            songs.put("key", id.toString());
            adaptedSongs.add(songs);
        }

        String[] from = {"_song"};
        int[] to = {R.id.artist_display_title};
        SimpleAdapter adapter = new SimpleAdapter(getContext(), adaptedSongs, R.layout.artist_display, from, to) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.artist_display_title);
                textView.setOnClickListener((v) -> playSong(songList.get(Integer.parseInt(adaptedSongs.get(position).get("key")))));
                return view;
            }
        };
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void playSong(Song song) {
        Intent intent = new Intent(getContext(), MusicPlayer.class);
        Player player = Player.getInstance();
        player.buildQueue(new Song[]{song});
        player.playNewSelected();
        startActivityForResult(intent, 10002);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10002 && resultCode == Activity.RESULT_OK) {
            Player player = Player.getInstance();
            Song song = player.getCurrentSong();
            if (song != null) {
                music_statusbar.setText(song.getArtist() + " - " + song.getTitle());
                music_statusbar.setSelected(true);
            }
        }
    }
}
