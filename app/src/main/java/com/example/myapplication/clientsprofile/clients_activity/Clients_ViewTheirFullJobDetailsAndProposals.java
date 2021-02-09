package com.example.myapplication.clientsprofile.clients_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Adapter.ViewPagerAdapter;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_fragment.All_Job_Proposals_Fragment;
import com.example.myapplication.clientsprofile.clients_fragment.Job_Details_Fragement;
import com.example.myapplication.pojo.JobPostdetail_pojo;
import com.google.android.material.tabs.TabLayout;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Clients_ViewTheirFullJobDetailsAndProposals extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout jobTabLayout;
    private JobPostdetail_pojo jobdetailsingle;
public static final String TAG = " Clients_viewTheirfulljobdtail";
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clients_viewfulljobdetailsandproposals);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        initvalue();
        if(getIntent().getParcelableExtra("Jobdetailsshow") != null ){
            Log.d(TAG, "onCreate: is not null jobdetails show "+getIntent().getParcelableExtra("Jobdetailsshow") );
            jobdetailsingle = getIntent().getParcelableExtra("Jobdetailsshow");
        }
        addTabs(viewPager);

    }
    private void initvalue(){
        viewPager = findViewById(R.id.vp);
        jobTabLayout = findViewById(R.id.tl);

    }
    @SuppressLint("LongLogTag")
    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Log.d(TAG, "addTabs: "+jobdetailsingle);
        adapter.addFrag(new Job_Details_Fragement(jobdetailsingle), "Job DETAILS");
        adapter.addFrag(new All_Job_Proposals_Fragment(jobdetailsingle.job_id), "ALL PROPOSALS");
        viewPager.setAdapter(adapter);
    }

    public void back(View view){
        finish();
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }
}
