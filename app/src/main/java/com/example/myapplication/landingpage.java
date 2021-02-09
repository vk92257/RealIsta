package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.bumptech.glide.Glide;
import com.example.myapplication.clientsprofile.clients_activity.Clients_CreateJobPost;
import com.example.myapplication.clientsprofile.clients_activity.Clients_LoginActivity;
import com.example.myapplication.modelsprofile.models_activity.Model_HomeActivity;
import com.example.myapplication.modelsprofile.models_activity.Model_SignUpActivity;
import com.example.myapplication.modelsprofile.models_activity.Models_LoginActivity;
import com.example.myapplication.utils.BlurTransition;
import com.example.myapplication.utils.ConstantString;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class landingpage extends AppCompatActivity {

    private KenBurnsView kenBurnsView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landingpage);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        initview();
        setkenButns();

    }

    public void initview() {
        kenBurnsView = findViewById(R.id.wallpaper_iv);
    }

    private void setkenButns() {
//        @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = getResources().getDrawable(R.drawable.model9);
////        drawable.setAlpha(40);
////        kenBurnsView.setImageDrawable(drawable);
        Glide.with(this).load(R.drawable.model9)
                .apply(bitmapTransform(new BlurTransition(this)))
                .into(kenBurnsView);
        AccelerateDecelerateInterpolator adi = new AccelerateDecelerateInterpolator();
        RandomTransitionGenerator generator = new RandomTransitionGenerator(2000, adi);
        kenBurnsView.setTransitionGenerator(generator);
    }

    public void iwanttohire(View view) {
        Intent intent = new Intent(this, Clients_LoginActivity.class);
        intent.setAction(ConstantString.CLIENT_LOGIN);
        startActivity(intent);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);


    }

    public void iwanttowork(View view) {
        Intent intent = new Intent(this, Models_LoginActivity.class);
        intent.setAction(ConstantString.MODEL_LOGIN);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);


    }

}