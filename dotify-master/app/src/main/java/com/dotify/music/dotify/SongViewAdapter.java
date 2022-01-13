package com.dotify.music.dotify;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class SongViewAdapter extends RecyclerView.Adapter<SongViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<Song> mSongs;

    public SongViewAdapter(ArrayList<Song> mSongs) {
        this.mSongs = mSongs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Log.d(TAG,"onBindViewHolder: called.");
        holder.songTitle.setText(mSongs.get(i).getTitle());
        holder.artistName.setText(mSongs.get(i).getArtist());

        holder.playButton.setOnClickListener(view -> Log.d(TAG, "onClickPlayButton: clicked on: " + mSongs.get(i).getTitle()));
        holder.likeButton.setOnClickListener(view -> Log.d(TAG, "onClickLikeButton: clicked on: " + mSongs.get(i).getTitle()));
        holder.dislikeButton.setOnClickListener(view -> Log.d(TAG, "onClickDislikeButton: clicked on: " + mSongs.get(i).getTitle()));
    }

    @Override
    public int getItemCount() {
        System.out.println("item count is " + mSongs.size());
        return mSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView songTitle;
        TextView artistName;
        ImageButton playButton;
        ImageButton likeButton;
        ImageButton dislikeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.songNameLabel);
            artistName = itemView.findViewById(R.id.artistNameLabel);
            playButton = itemView.findViewById(R.id.songPlayButton);
            likeButton = itemView.findViewById(R.id.likeButton);
            dislikeButton = itemView.findViewById(R.id.dislikeButton);
        }
    }
}
