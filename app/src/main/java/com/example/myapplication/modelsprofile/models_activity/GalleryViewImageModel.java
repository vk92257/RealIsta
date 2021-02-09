package com.example.myapplication.modelsprofile.models_activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.Adapter.ExploreViewPagerAdapter;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_activity.View_proposal_userProfile;
import com.example.myapplication.pojo.ImageViewSinglefullsize;
import com.example.myapplication.utils.OnSwipeTouchListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GalleryViewImageModel extends AppCompatActivity {
    private CircularImageView profileimage;
    private TextView fullname;
    private TextView professiontitle;
    private ImageView selectedimage;
    private ImageViewSinglefullsize datafullimage;
    private TextView viewprofile;
    private String TAG="GalleryViewImageModel";
    private static int postion;
    private ViewPager viewPager;
    private ExploreViewPagerAdapter galleryViewPagerAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galley_image_view_model);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        initview();
        if(getIntent().getParcelableExtra("fullimagdetail") != null){
            datafullimage = getIntent().getParcelableExtra("fullimagdetail");
            setData();
            setUpViewPager();
        }


    }
    private void setUpViewPager() {
        viewPager = findViewById(R.id.gallery_view_pager);
        galleryViewPagerAdapter = new ExploreViewPagerAdapter(GalleryViewImageModel.this,datafullimage.getImages_hd());
        viewPager.setAdapter(galleryViewPagerAdapter);
        viewPager.setCurrentItem(postion);
    }
    private void setData(){
        Glide.with(this).load(Uri.parse(datafullimage.getProfileimage()))
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( profileimage);

        showHdImage(datafullimage.getViewimage());

//        Glide.with(this).load(Uri.parse(datafullimage.getViewimage()))
//                .thumbnail(0.5f)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into( selectedimage);
        professiontitle.setText(datafullimage.getProfessiontitle());
        fullname.setText(datafullimage.getFullname());
        if(datafullimage.getImages_hd() != null){
            postion = datafullimage.getPostion();
            addSwipeLeftRight();
        }

    }

    private void showHdImage(String url) {
        Glide.with(this).load(url)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( selectedimage);
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


    private void swipeLeft() {

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


    private void addSwipeLeftRight() {

        selectedimage.setOnTouchListener(new OnSwipeTouchListener(this){
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
