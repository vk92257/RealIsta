package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_activity.Clients_Home;
import com.example.myapplication.clientsprofile.clients_activity.Clients_SignUpActivity;
import com.example.myapplication.clientsprofile.clients_activity.FetchAll_ClientData;
import com.example.myapplication.modelsprofile.models_activity.FetchAll_ModelData;
import com.example.myapplication.modelsprofile.models_activity.Model_HomeActivity;
import com.example.myapplication.utils.BlurTransition;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private KenBurnsView kenBurnsView;
    private static String TAG = "Splash";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.splash);
        initview();
        setkenButns();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "run: islogin-> "+SharedPreferanceManager.getInstance(getApplicationContext())
                        .isLoggedIn());

                if (SharedPreferanceManager.getInstance(getApplicationContext())
                        .isLoggedIn()) {

                    Log.d(TAG, "run: "+SharedPreferanceManager
                            .getInstance(getApplicationContext()) + " RoleWith "+ SharedPreferanceManager
                            .getInstance(getApplicationContext()).Rolewith());

                    restoreChatSession();


                }
                else{
                    Intent intent = new Intent(Splash.this,landingpage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                }

            }
        },SPLASH_TIME_OUT);
    }


    /**restore chat session*/
    private void restoreChatSession() {
        if(SharedPreferanceManager
                .getInstance(getApplicationContext()).Rolewith().equals(ConstantString.MODEL_LOGIN)){
            Intent intent = new Intent(Splash.this, FetchAll_ModelData.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();

        }
        else{
            Intent intent = new Intent(Splash.this, FetchAll_ClientData.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();
        }

    }

    private void initview(){
        kenBurnsView =findViewById(R.id.wallpaper_iv);
    }
    private void setkenButns(){
//        @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = getResources().getDrawable(R.drawable.model9);
        Glide.with(this).load(R.drawable.model9)
                .apply(bitmapTransform(new BlurTransition(this)))
                .into(kenBurnsView);

//        .setImageDrawable(drawable);
        AccelerateDecelerateInterpolator adi = new AccelerateDecelerateInterpolator();
        RandomTransitionGenerator generator = new RandomTransitionGenerator(2000, adi);
        kenBurnsView.setTransitionGenerator(generator);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}