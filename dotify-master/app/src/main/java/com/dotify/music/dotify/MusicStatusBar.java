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
import android.widget.TextView;

public class MusicStatusBar extends Fragment {

    public boolean songLoaded;

    public MusicStatusBar() {
        songLoaded = false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.music_statusbar, container, false);
        TextView textView = rootView.findViewById(R.id.music_statusbar_fragment);
        textView.setOnClickListener(v -> openMusicActivity());
        return rootView;
    }

    private void openMusicActivity() {
        Intent intent = new Intent(getActivity(), MusicPlayer.class);
        startActivity(intent);
    }
}
