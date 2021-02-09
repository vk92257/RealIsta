package com.example.myapplication.clientsprofile.clients_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class JobTypeSelect_option extends AppCompatActivity {
    private String selectoption;
    private RadioButton stillphoto,videoshoot,bothvalue;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.jobtypeselect_radio);
        intiview();
        if (getIntent().getStringExtra("data") != null) {
            selectoption = getIntent().getStringExtra("data");
            setinitview();
        }

        setdata();
    }

    public void back(View view){
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    private void intiview() {
        stillphoto = findViewById(R.id.stillphoto);
        videoshoot = findViewById(R.id.videoshoot);
        bothvalue = findViewById(R.id.both);
    }

    private void setinitview() {
        if (selectoption.toLowerCase().equals("still photoshoot")) {

            stillphoto.setChecked(true);
        } else if (selectoption.toLowerCase().equals("video shoot")) {
            videoshoot.setChecked(true);
        } else if(selectoption.toLowerCase().equals("both")) {
            bothvalue.setChecked(true);
        }
    }


    private void setdata() {
        stillphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stillphoto.setChecked(true);
                videoshoot.setChecked(false);
                bothvalue.setChecked(false);
                Intent intent = new Intent();
                intent.putExtra("senddata", "Still Photoshoot");
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }
        });


        videoshoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stillphoto.setChecked(false);
                videoshoot.setChecked(true);
                bothvalue.setChecked(false);
                Intent intent = new Intent();
                intent.putExtra("senddata", "Video shoot");
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });


        bothvalue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stillphoto.setChecked(false);
                videoshoot.setChecked(false);
                bothvalue.setChecked(true);
                Intent intent = new Intent();
                intent.putExtra("senddata", "Both");
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
    }
}
