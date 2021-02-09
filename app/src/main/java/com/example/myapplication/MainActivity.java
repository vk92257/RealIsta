package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.clientsprofile.clients_fragment.Client_login;
import com.example.myapplication.utils.ConstantString;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity {
    private String loginAs ;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginAs = getIntent().getAction();
        setuplogin(loginAs);
    }

    private void setuplogin(String loginAs){
        if(loginAs.equals(ConstantString.CLIENT_LOGIN)){
            setFreagment(new Client_login());
        }
        else{
            setFreagment(new Client_login());
        }
    }

    private void setFreagment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fadein,R.anim.fadeout);
        fragmentTransaction.replace(R.id.fragment_container,fragment).commit();

    }
}