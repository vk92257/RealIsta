package com.example.myapplication.modelsprofile.models_activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.etebarian.meowbottomnavigation.MeowBottomNavigationCell;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_fragment.Bookings;
import com.example.myapplication.clientsprofile.clients_fragment.Browse;
import com.example.myapplication.clientsprofile.clients_fragment.Chats;
import com.example.myapplication.clientsprofile.clients_fragment.Explore;
import com.example.myapplication.clientsprofile.clients_fragment.JobPosts;
import com.example.myapplication.clientsprofile.clients_fragment.MyProfile;
import com.example.myapplication.modelsprofile.models_fragment.Model_Booking;
import com.example.myapplication.modelsprofile.models_fragment.Model_Browse;
import com.example.myapplication.modelsprofile.models_fragment.Model_Chats;
import com.example.myapplication.modelsprofile.models_fragment.Model_GetHired;
import com.example.myapplication.modelsprofile.models_fragment.Model_Portfolio;
import com.example.myapplication.utils.NetworkChangeReceiver;
import com.example.myapplication.utils.NonSwipeableViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Model_HomeActivity extends AppCompatActivity {
    private MeowBottomNavigation bottomnavigation;
    private FrameLayout nonSwipeableViewPager;
    private static final int JOB_POST = 1;
    private static final int BOOKING = 2;
    private static final int CHATS = 3;
    private static final int EXPLORE = 4;
    private static final int BROWSER = 5;
    private static final int MY_PROFILE = 6;
    private ImageView button;

    private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.home);
        intiview();
        getSupportFragmentManager().beginTransaction().replace(R.id.vp,new Model_GetHired()).commit();
        initmeownavigation();
    }
    private void intiview(){
        button = findViewById(R.id.scroll);
        bottomnavigation = findViewById(R.id.meow);
        nonSwipeableViewPager = findViewById(R.id.vp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatingLocalBroadCast();
            }
        });
    }

    private void creatingLocalBroadCast() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent("ScrollToTop");
        localBroadcastManager.sendBroadcast(intent);

    }

    private void initmeownavigation(){

        bottomnavigation.add(new MeowBottomNavigation.Model(JOB_POST, R.drawable.ic_search_black_24dp));
        bottomnavigation.add(new MeowBottomNavigation.Model(BOOKING, R.drawable.ic_business_center_black_24dp));
        bottomnavigation.add(new MeowBottomNavigation.Model(CHATS, R.drawable.ic_chat_black_24dp));
        bottomnavigation.add(new MeowBottomNavigation.Model(BROWSER, R.drawable.ic_language_black_24dp));
        bottomnavigation.add(new MeowBottomNavigation.Model(MY_PROFILE, R.drawable.ic_perm_identity_black_24dp));
        bottomnavigation.show(JOB_POST,true);
        bottomnavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model items) {
                Fragment seletedfragment = null;
                switch (items.getId()){
                    case JOB_POST:
                        button.setVisibility(View.VISIBLE);
                        seletedfragment = new Model_GetHired();
                        break;

                    case BOOKING:
                        hideTheScroll();
                        seletedfragment = new Model_Booking();
                        break;

                    case CHATS:
                        hideTheScroll();
                        seletedfragment = new Model_Chats();
                        break;

                    case BROWSER:
                        hideTheScroll();
                        seletedfragment = new Browse();
                        break;

                    case MY_PROFILE:
                        hideTheScroll();
                        seletedfragment = new Model_Portfolio();
                        break;

                }
                assert seletedfragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.vp,seletedfragment).commit();
                return null;
            }
        });

        bottomnavigation.setOnReselectListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                return null;
            }
        });

    }
   private void hideTheScroll(){
        button.setVisibility(View.GONE);
   }
    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(
                networkChangeReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else {
            Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }
}
