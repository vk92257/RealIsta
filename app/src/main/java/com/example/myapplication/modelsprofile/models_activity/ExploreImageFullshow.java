package com.example.myapplication.modelsprofile.models_activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.Adapter.GalleryViewPagerAdapter;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_activity.View_proposal_userProfile;
import com.example.myapplication.pojo.ImageViewSinglefullsize;
import com.example.myapplication.utils.OnSwipeTouchListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ExploreImageFullshow extends AppCompatActivity {
    private CircularImageView profileimage;
    private TextView fullname;
    private TextView professiontitle;
    private ImageView selectedimage;
    private ImageViewSinglefullsize datafullimage;
    private TextView viewprofile;
    private static int postion;
    private String TAG="View_proposal_userProfile";
    private ViewPager viewPager;
    private GalleryViewPagerAdapter galleryViewPagerAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
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
            setUpViewPager();
        }

        /** sushil change 12/17/2020*/
//        if(getIntent().getStringExtra("postionClick") != null){
//            postion
//        }
    }

    private void setData(){
        showProfileImage(datafullimage.getProfileimage());
        showHdImage(datafullimage.getViewimage());
        setProfessionTitle(datafullimage.getProfessiontitle());
        setFullName(datafullimage.getFullname());
//        addSwipeLeftRight();
        if (datafullimage.getData() != null){
            postion = datafullimage.getPostion();
//            addSwipeLeftRight();
        }
    }

    private void setUpViewPager() {
        Log.e(TAG, "setUpViewPager: "+datafullimage.getPostion());
        viewPager = findViewById(R.id.gallery_view_pager);
        galleryViewPagerAdapter = new GalleryViewPagerAdapter(datafullimage,ExploreImageFullshow.this);
        viewPager.setAdapter(galleryViewPagerAdapter);
        viewPager.setCurrentItem(datafullimage.getPostion());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e(TAG, "onPageScrolled: "+position );

                    onScroll(position);
            }

            @Override
            public void onPageSelected(int position) {
//                Log.e(TAG, "onPageSelected: "+position );
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.e(TAG, "onPageScrollStateChanged: "+state );
            }
        });
    }

    private void onScroll(int position) {
        postion = position;
        showProfileImage(datafullimage.getData().get(position).getProfile_img());
        setProfessionTitle(datafullimage.getData().get(position).getProffesion());
        setFullName(datafullimage.getData().get(position).getName());
        showHdImage(datafullimage.getData().get(position).getHd_images());
    }

    private void setFullName(String fullName) {
        fullname.setText(fullName);
    }

    private void setProfessionTitle(String profession) {
        professiontitle.setText(profession);
    }

    private void showHdImage(String url) {
        Glide.with(this).load(url)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( selectedimage);
    }
    private void showProfileImage(String url){
        Glide.with(this).load(Uri.parse(url))
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( profileimage);
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

    private void swipeLeft() {
//        Toast.makeText(ExploreImageFullshow.this, "right", Toast.LENGTH_SHORT).show();
        if (postion!=datafullimage.getData().size()-1){
            postion++;
            showProfileImage(datafullimage.getData().get(postion).getProfile_img());
            setProfessionTitle(datafullimage.getData().get(postion).getProffesion());
            setFullName(datafullimage.getData().get(postion).getName());
            showHdImage(datafullimage.getData().get(postion).getHd_images());

        }
    }
    private void swipeRight() {
//        Toast.makeText(ExploreImageFullshow.this, "left", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "swipeRight: postion "+postion);
        if (postion!=0){
            postion--;
            Log.d(TAG, "swipeRight: postion now ->"+postion);
            showProfileImage(datafullimage.getData().get(postion).getProfile_img());
            setProfessionTitle(datafullimage.getData().get(postion).getProffesion());
            setFullName(datafullimage.getData().get(postion).getName());
            showHdImage(datafullimage.getData().get(postion).getHd_images());
        }
    }

    public void back(View view){
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
    public void viewprofile(View view){
        Intent intent = new Intent(this, View_proposal_userProfile.class);
        intent.putExtra("pojoall_proposal",datafullimage);

        /**sushil change 12/17/2020*/
        intent.putExtra("user_id",datafullimage.getData().get(postion).getUser_id());



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
