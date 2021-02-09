package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class Complete_checkAcitivity extends AppCompatActivity {
    private TextView msg;

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_complete_check_acitivity);
intview();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        },SPLASH_TIME_OUT);
    }

    public void intview(){
        msg = findViewById(R.id.msg);
        msg.setText("Complete");
    }
}