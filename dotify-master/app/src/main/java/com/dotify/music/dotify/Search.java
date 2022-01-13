package com.dotify.music.dotify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    TextView searchTermLabel;
    private ArrayList<Song> mSongs = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        Bundle bundle = getIntent().getExtras();

        searchTermLabel = findViewById(R.id.searchTermString);
        searchTermLabel.setText(bundle.getString("query"));

        RecyclerView recyclerView = findViewById(R.id.searchResultsRecycleView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        SongViewAdapter adapter = new SongViewAdapter(mSongs);
        recyclerView.setAdapter(adapter);
    }

}
