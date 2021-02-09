package com.example.myapplication;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class WebView_Youtube extends Activity {

    String video_id = "";
    ImageView back_btn;
    ProgressBar progressBar;
    private String html = "";
    WebView webView;
    YouTubePlayerView youTubePlayerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.webview_youtube_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//        if(getIntent().getStringExtra("youtubeurl") != null){
//            video_id =getIntent().getStringExtra("youtubeurl");
//        }

        video_id = "https://www.youtube.com/watch?v=b1dkJXjuTsg";
        Log.e("VideoUrl", "onCreate: "+video_id);
        youTubeVidoePlayer();
    }


    private void youTubeVidoePlayer() {
         youTubePlayerView = findViewById(R.id.youtube_player_view);
//        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(  new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(video_id, 0f);
            }
        });
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
                if (youTubePlayerView!= null){
                    youTubePlayerView.release();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
          if ( youTubePlayerView != null ){
              youTubePlayerView.release();
          }
          finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

