package com.dotify.music.dotify;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

public class MusicLibrary extends Fragment {

    private UserLibrary userLibrary;
    private MainActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        userLibrary = new UserLibrary();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.music_library, container, false);

        Button library = rootView.findViewById(R.id.user_library);
        Button top40 = rootView.findViewById(R.id.top_40);
        Button playlist = rootView.findViewById(R.id.user_playlist);
        Button settings = rootView.findViewById(R.id.settings);

        library.setOnClickListener((v) -> {
            Fragment userLibrary = new UserLibrary();
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

            String tag = "library";
            transaction.replace(R.id.main_feed_fragment, userLibrary, tag);
            transaction.addToBackStack(tag);

            transaction.commit();
        });

        top40.setOnClickListener((v) -> {
            System.out.println("SHOW TOP 40");
        });

        playlist.setOnClickListener((v) -> {
            System.out.println("SHOW PLAYLIST");
        });

        settings.setOnClickListener((v) -> {
            openSettingsActivity();
        });
        return rootView;
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }
}
