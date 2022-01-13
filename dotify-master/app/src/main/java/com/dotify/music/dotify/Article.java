package com.dotify.music.dotify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Article extends AppCompatActivity {

    private int id;

    public Article() {
        id = -1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article);
        WebView webView = findViewById(R.id.article_view);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            id = b.getInt("id");
            webView.loadUrl("http://" + DatabaseRetriever.SERVER_IP + "/getArticleText.php?id=" + id);
        }
    }
}
