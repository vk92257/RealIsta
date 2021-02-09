package com.example.myapplication.chatModule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.VideoView;

import com.example.myapplication.R;

public class View_ChatVideo_fullScreen extends AppCompatActivity {
    private VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__chat_video_full_screen);
        initview();
    }

    private void initview(){
        videoView = findViewById(R.id.video_player);



    }
}