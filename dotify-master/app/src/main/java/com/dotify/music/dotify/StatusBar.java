package com.dotify.music.dotify;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class StatusBar extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.status_bar, container, false);
        Button newFeedBtn = rootView.findViewById(R.id.newsfeed_btn);
        Button libraryBtn = rootView.findViewById(R.id.library_btn);
        Button settingsBtn = rootView.findViewById(R.id.settings_btn);

        newFeedBtn.setOnClickListener(v -> openFeed(R.id.newsfeed_fragment, new NewsFeed()));
        libraryBtn.setOnClickListener(v -> openFeed(R.id.library_feed, new MusicLibrary()));
        settingsBtn.setOnClickListener(v -> openFeed(R.id.alarm_feed, new AlarmFeed()));

        return rootView;
    }

    void openFeed(int fragmentId, Fragment newFragment) {
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        Fragment fragment = fragmentManager.findFragmentById(fragmentId);
        if (fragment == null || !fragment.isVisible()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.main_feed_fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
