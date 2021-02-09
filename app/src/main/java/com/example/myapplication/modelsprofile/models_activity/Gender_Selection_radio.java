package com.example.myapplication.modelsprofile.models_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.myapplication.R;

import java.util.ArrayList;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class Gender_Selection_radio extends AppCompatActivity {

    private ListView list;
    private RadioButton male, female, transgender;
    private String gender;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.gender_selection_radio);

        intiview();
        if (getIntent().getStringExtra("data") != null) {
            gender = getIntent().getStringExtra("data");
            setinitview();
        }

        setdata();
    }


    public void back(View view){
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    private void intiview() {
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        transgender = findViewById(R.id.transgender);
    }

    private void setinitview() {
        if (gender.toLowerCase().equals("male")) {

            male.setChecked(true);
        } else if (gender.toLowerCase().equals("female")) {
            female.setChecked(true);
        } else if(gender.toLowerCase().equals("transgender")) {
            transgender.setChecked(true);
        }
    }


    private void setdata() {
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setChecked(true);
                female.setChecked(false);
                transgender.setChecked(false);
                Intent intent = new Intent();
                intent.putExtra("senddata", "Male");
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }
        });


        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setChecked(false);
                female.setChecked(true);
                transgender.setChecked(false);
                Intent intent = new Intent();
                intent.putExtra("senddata", "Female");
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });


        transgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setChecked(false);
                female.setChecked(false);
                transgender.setChecked(true);
                Intent intent = new Intent();
                intent.putExtra("senddata", "Transgender");
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
    }
}
