package com.example.myapplication.chatModule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.R;
import com.example.myapplication.utils.ConstantString;
import com.github.chrisbanes.photoview.PhotoView;
import com.quickblox.chat.model.QBAttachment;

import java.io.File;

public class View_ChatImage_fullScreen extends AppCompatActivity {

    private PhotoView photoView;
    private File viewimage ;
    private String type = null;
    private ProgressBar image_progress;
    private ImageView play_btn_id;
    private VideoView video_player;
    private static final String TAG = "View_chatImage_fullScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view__chat_attachment_full_screen);
        initviews();

        Log.e(TAG, "onCreate: " );
        if (getIntent().getStringExtra(ConstantString.CHAT_IMAGE_VIEW) != null) {
            Log.e(TAG, "ifcondition when true " );
            viewimage = new File(getIntent().getStringExtra(ConstantString.CHAT_IMAGE_VIEW));

        }
        else{
            Log.e(TAG, "ifcondition when false file-> "+viewimage );
        }
        if (getIntent().getStringExtra(ConstantString.ATTACHMENT_TYPE) != null) {

         type = getIntent().getStringExtra(ConstantString.ATTACHMENT_TYPE);
        }


            setImageDisplay(viewimage);

    }




    private void initviews() {
        photoView = findViewById(R.id.image);
        image_progress = findViewById(R.id.image_progress);
        play_btn_id = findViewById(R.id.play_btn_id);
        video_player = findViewById(R.id.video_player);
        play_btn_id.setVisibility(View.GONE);

    }

    public void onbackclick(View view) {
        finish();
    }



    @SuppressLint("LongLogTag")
    private void setVideodisplay(File viewimage) {
        image_progress.setVisibility(View.GONE);

        Log.e("path", "setVideodisplay: filepath-> "+viewimage.getAbsolutePath() );
        if(viewimage.exists()){
            Log.e(TAG, "setVideodisplay: file already present " );
        }
        else{
            Log.e(TAG, "setVideodisplay: file not present " );
        }

        Toast.makeText(this,"playing video",Toast.LENGTH_SHORT).show();
        Log.e(TAG, "setVideodisplay:uri "+viewimage );
//        video_player.setVideoURI(Uri.fromFile(viewimage));
        video_player.setVideoPath(viewimage.getAbsolutePath());
        Log.e(TAG, "setVideodisplay: "+video_player.isPlaying() );
//        video_player.setVideoURI(Uri.parse(viewimage));


    }

    private void setImageDisplay(File imageurl) {
        Log.e("path", "setImageDisplay: file url-> "+imageurl.getAbsolutePath() );
        Glide.with(this)
                .load(imageurl).addListener(new RequestListener<Drawable>() {
            @SuppressLint("CheckResult")
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Toast.makeText(View_ChatImage_fullScreen.this, "unable to show please check your intenet connection.", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                image_progress.setVisibility(View.GONE);


                return false;
            }
        }).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoView);
    }


}

