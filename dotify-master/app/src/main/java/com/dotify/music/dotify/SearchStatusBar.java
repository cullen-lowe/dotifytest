package com.dotify.music.dotify;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchStatusBar extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_statusbar, container, false);
        SearchView searchView = rootView.findViewById(R.id.search_statusbar_fragment);
        ImageView settingsBtn = rootView.findViewById(R.id.search_statusbar_settings);

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    System.out.println("onQueryTextSubmit");
                    openSearchResults(searchView.getQuery().toString());
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    System.out.println("onQueryTextChange");
                    return false;
                }
            });
        }

        settingsBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        });

        return rootView;
    }

    private void openSearchResults(String query) {
        Intent intent = new Intent(getActivity(), Search.class);
        intent.putExtra("query", query);
        startActivity(intent);
    }
}
