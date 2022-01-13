package com.dotify.music.dotify;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NewsFeed extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.newsfeed_display, container, false);
        ArrayList<HashMap<String, String>> articlesAdapted = new ArrayList<>();
        JSONArray articles = getArticles();

        if (articles == null) {
            return ErrorDisplay.displayError("Failed to connect server", this, R.id.main_feed_fragment);
        }


        for (int i = 0; i < articles.length(); i++) {
            try {
                HashMap<String, String> article = new HashMap<>();
                JSONObject item = articles.getJSONObject(i);
                article.put("id", item.getString("ArticleId"));
                article.put("title", item.getString("Title"));
                articlesAdapted.add(article);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        ListView articleView = rootView.findViewById(R.id.news_articles);
        SimpleAdapter adapter = new SimpleAdapter(getContext(), articlesAdapted, R.layout.article_display,
                new String[]{"title"}, new int[]{R.id.article_title}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                LinearLayout article = view.findViewById(R.id.article_box);
                ImageView imageView = view.findViewById(R.id.article_image);
                TextView textView = view.findViewById(R.id.article_title);
                article.setOnClickListener((v) -> showArticleBody(position + 1, (String) textView.getText()));
                imageView.setImageResource(R.drawable.article_placeholder);
                return view;
            }
        };
        articleView.setAdapter(adapter);

        return rootView;
    }

    private void showArticleBody(int id, String title) {
        Intent intent = new Intent(getActivity(), Article.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    private JSONArray getArticles() {
        JSONArray rv = null;
        try {
            DatabaseRetriever retriever  = new DatabaseRetriever();
            retriever.execute("GET", "getArticleTitles.php");
            rv = retriever.get(1000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        return rv;
    }
}
