package com.example.myapplication.clientsprofile.clients_activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_fragment.Bookings;
import com.example.myapplication.clientsprofile.clients_fragment.Browse;
import com.example.myapplication.clientsprofile.clients_fragment.Chats;
import com.example.myapplication.clientsprofile.clients_fragment.Explore;
import com.example.myapplication.clientsprofile.clients_fragment.JobPosts;
import com.example.myapplication.clientsprofile.clients_fragment.MyProfile;
import com.example.myapplication.modelsprofile.models_fragment.Model_Chats;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.NonSwipeableViewPager;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.quickblox.users.model.QBUser;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class Clients_Home extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private MeowBottomNavigation bottomnavigation;
    private FrameLayout nonSwipeableViewPager;
    private static final int JOB_POST = 1;
    private static final int BOOKING = 2;
    private static final int CHATS = 3;
    private static final int EXPLORE = 4;
    private static final int BROWSER = 5;
    private static final int MY_PROFILE = 6;
    public static final String TAG = "Client_Home";
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

//    private QBUser user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.home);

//        Log.e(TAG, "onCreate: "+(QBUser)getIntent().getSerializableExtra(ConstantString.USER_TAG));
//        if(getIntent().getSerializableExtra(ConstantString.USER_TAG) != null){
//
//            user = (QBUser) getIntent().getSerializableExtra(ConstantString.USER_TAG);
//        }
//        Log.e("Userlogin", "onCreate: "+ SharedPreferanceManager.getInstance(Clients_Home.this).getQbUser());
        intiview();
        getSupportFragmentManager().beginTransaction().replace(R.id.vp,new JobPosts()).addToBackStack(null).commit();
        initmeownavigation();
    }
    private void intiview(){
        bottomnavigation = findViewById(R.id.meow);
        nonSwipeableViewPager = findViewById(R.id.vp);
    }
    private void initmeownavigation(){
        bottomnavigation.add(new MeowBottomNavigation.Model(JOB_POST, R.drawable.ic_folder_special_black_24dp));
        bottomnavigation.add(new MeowBottomNavigation.Model(BOOKING, R.drawable.ic_business_center_black_24dp));
        bottomnavigation.add(new MeowBottomNavigation.Model(CHATS, R.drawable.ic_chat_black_24dp));
        bottomnavigation.add(new MeowBottomNavigation.Model(EXPLORE, R.drawable.ic_language_black_24dp));
        bottomnavigation.add(new MeowBottomNavigation.Model(BROWSER, R.drawable.ic_whatshot_black_24dp));
        bottomnavigation.add(new MeowBottomNavigation.Model(MY_PROFILE, R.drawable.ic_perm_identity_black_24dp));
        bottomnavigation.show(JOB_POST,true);
        bottomnavigation.setOnClickMenuListener(items -> {
            Fragment seletedfragment = null;
            switch (items.getId()){
                case JOB_POST:
                    Log.d(TAG, "invoke: "+JOB_POST);
                    seletedfragment = new JobPosts();
                    break;

                case BOOKING:
                    Log.d(TAG, "invoke: "+BOOKING);
                    seletedfragment = new Bookings();
                    break;

                case CHATS:
                    Log.d(TAG, "invoke: "+CHATS);
                    seletedfragment = new Model_Chats();
                    break;

                case EXPLORE:
                    Log.d(TAG, "invoke: "+EXPLORE);
                    seletedfragment = new Explore();
                    break;

                case BROWSER:
                    Log.d(TAG, "invoke: "+BROWSER);
                    seletedfragment = new Browse();
                    break;

                case MY_PROFILE:
                    Log.d(TAG, "invoke: "+MY_PROFILE);
                    seletedfragment = new MyProfile();
                    break;

            }
            assert seletedfragment != null;
            Log.d(TAG, "invoke: "+seletedfragment.toString());
            getSupportFragmentManager().beginTransaction().replace(R.id.vp,seletedfragment).commit();
            return null;
        });

//        bottomnavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
//            @Override
//            public Unit invoke(MeowBottomNavigation.Model items) {
//                Fragment seletedfragment = null;
//                switch (items.getId()){
//                    case JOB_POST:
//                        Log.d(TAG, "invoke: "+JOB_POST);
//                        seletedfragment = new JobPosts();
//                        break;
//
//                    case BOOKING:
//                        Log.d(TAG, "invoke: "+BOOKING);
//                        seletedfragment = new Bookings();
//                        break;
//
//                    case CHATS:
//                        Log.d(TAG, "invoke: "+CHATS);
//                        seletedfragment = new Chats();
//                        break;
//
//                    case EXPLORE:
//                        Log.d(TAG, "invoke: "+EXPLORE);
//                        seletedfragment = new Explore();
//                        break;
//
//                    case BROWSER:
//                        Log.d(TAG, "invoke: "+BROWSER);
//                        seletedfragment = new Browse();
//                        break;
//
//                    case MY_PROFILE:
//                        Log.d(TAG, "invoke: "+MY_PROFILE);
//                        seletedfragment = new MyProfile();
//                        break;
//
//                }
//                assert seletedfragment != null;
//                getSupportFragmentManager().beginTransaction().replace(R.id.vp,seletedfragment).commit();
//               return Unit.INSTANCE;
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    @Override
    public void onBackPressed()
    {
//        int count =  getSupportFragmentManager().getBackStackEntryCount();
//        Log.d(TAG, "onBackPressed: count fragment-> "+count);
//        if(count ==1){
//            getSupportFragmentManager().popBackStack();
//
//        }

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
