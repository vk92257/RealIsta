package com.example.myapplication.modelsprofile.models_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.Adapter.ExploreViewPagerAdapter;
import com.example.myapplication.Adapter.GalleryViewPagerAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Splash;
import com.example.myapplication.clientsprofile.clients_activity.Clients_ViewDetailedProposal;
import com.example.myapplication.clientsprofile.clients_activity.View_proposal_userProfile;
import com.example.myapplication.landingpage;
import com.example.myapplication.pojo.ImageViewSinglefullsize;
import com.example.myapplication.utils.OnSwipeTouchListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GalleryImageFullshow extends AppCompatActivity {
    private CircularImageView profileimage;
    private TextView fullname;
    private TextView professiontitle;
    private ImageView selectedimage;
    private ImageViewSinglefullsize datafullimage;
    private TextView viewprofile;
    private static int postion;
    private int profileGallery;
    private String TAG="GalleryImageFullShow";
    private ViewPager viewPager;
    private ExploreViewPagerAdapter galleryViewPagerAdapter;
    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getIntent().getIntExtra("profileGallery",0) != 0){
            profileGallery = getIntent().getIntExtra("profileGallery",0);
            Log.d(TAG, "onStart: profilegallery-> "+profileGallery);
        }
        else{
            profileGallery = 0;
        }
        if(profileGallery == 1){
            viewprofile.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.models_fullpictureview);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        initview();
        if(getIntent().getParcelableExtra("fullimagdetail") != null){
            datafullimage = getIntent().getParcelableExtra("fullimagdetail");
            setData();
        }
            setUpViewPager();


    }

    private void setUpViewPager() {
        viewPager = findViewById(R.id.gallery_view_pager);
        galleryViewPagerAdapter = new ExploreViewPagerAdapter(GalleryImageFullshow.this,datafullimage.getImages_hd());
        viewPager.setAdapter(galleryViewPagerAdapter);
        viewPager.setCurrentItem(postion);
    }

    private void setData(){
        Log.d(TAG, "setData: profilegallery-> "+profileGallery);
        Glide.with(this).load(Uri.parse(datafullimage.getProfileimage()))
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( profileimage);
        showHdImage(datafullimage.getViewimage());
        professiontitle.setText(datafullimage.getProfessiontitle());
        fullname.setText(datafullimage.getFullname());
        if (datafullimage.getImages_hd() != null){
            postion = datafullimage.getPostion();
            addSwipeLeftRight();
            Log.e(TAG, "setData: positionn is =  "+datafullimage.getPostion()+"Size of  array is "+datafullimage.getImages_hd().size());
        }
    }

    private void showHdImage(String url) {
        Glide.with(this).load(url)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( selectedimage);
    }

    private void addSwipeLeftRight() {


        selectedimage.setOnTouchListener(new OnSwipeTouchListener(this){


            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeTop() {
//                Toast.makeText(GalleryImageFullshow.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                swipeRight();

            }
            public void onSwipeLeft() {
                swipeLeft();

            }
            public void onSwipeBottom() {
//                Toast.makeText(GalleryImageFullshow.this, "bottom", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void swipeLeft() {

//        if (postion!=datafullimage.getImages_hd().size()-1){
//            postion++;
////            Toast.makeText(GalleryImageFullshow.this, "right", Toast.LENGTH_SHORT).show();
//            showHdImage(datafullimage.getImages_hd().get(postion));
//        }


                if (postion!=datafullimage.getImages_hd().size()-1){
                    postion++;
//            Toast.makeText(GalleryImageFullshow.this, "right", Toast.LENGTH_SHORT).show();
                    showHdImage(datafullimage.getImages_hd().get(postion));
                }

    }
    private void swipeRight() {

                if (postion!=0){
                    postion--;
//            Toast.makeText(GalleryImageFullshow.this, "left", Toast.LENGTH_SHORT).show();
                    showHdImage(datafullimage.getImages_hd().get(postion));
                }


    }

    public void back(View view){
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
    public void viewprofile(View view){
        Intent intent = new Intent(this, View_proposal_userProfile.class);
        intent.putExtra("pojoall_proposal",datafullimage);
        intent.putExtra("user_id",datafullimage.getUserId());
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        Log.e(TAG, "viewprofile: "+getIntent().getStringExtra("user_id"));
        startActivity(intent);
    }

    private void initview(){
        profileimage = findViewById(R.id.profileimage);
        fullname = findViewById(R.id.name);
        professiontitle =findViewById(R.id.professiontitle);
        selectedimage  = findViewById(R.id.image);
        viewprofile = findViewById(R.id.viewprofile);
//        viewprofile.setVisibility(View.GONE);

    }







    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
