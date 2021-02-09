package com.example.myapplication.modelsprofile.models_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.pojo.FetchAllJob_pojo;
import com.example.myapplication.pojo.Gell_all_jobs_postOPJO;
import com.example.myapplication.utils.TimeAgo2;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Single_JobDetail_Activity extends AppCompatActivity {

    private ScrollView scrollViewtop;
    private TextView title;
    private TextView castingcompany;
    private TextView vacancies;
    private TextView time;
    private TextView projectinformation;
    private TextView typeoflook;
    private TextView agerange;
    private TextView height;
    private TextView weight;
    private TextView ethnicity;
    private TextView eyecolor;
    private TextView hairlength;
    private TextView haircolor;
    private TextView specialskills;
    private TextView gender;
    private TextView jobtype;
    private TextView description;
    private TextView jobduration;
    private TextView jobrate;
    private TextView shootingdates;
    private TextView shootinglocation;
    private LinearLayout attachments_ll;
    private ListView attachments;
    private LinearLayout cardbottom;
    private MaterialCardView submitted, alreadysubmit;
    private static int CALLBACK_CODE = 12345;


    private FetchAllJob_pojo singlejob;
    private static final String TAG = "Single_jobDetail";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.models_viewfulljobdetails);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        initview();
        Log.d(TAG, "onCreate: 1 -> " + getIntent().getParcelableExtra("gellAllJobsPostOPJOS"));
        Log.d(TAG, "onCreate: 2 -> " + getIntent().getParcelableArrayListExtra("gellAllJobsPostOPJOS"));
        Log.d(TAG, "onCreate: 3 -> " + getIntent().getShortArrayExtra("gellAllJobsPostOPJOS"));
        if (getIntent().getParcelableExtra("Single_job") != null) {
            singlejob = getIntent().getParcelableExtra("Single_job");
            setView();
        } else {
            Toast.makeText(this, "Nothing to show sorry please add ", Toast.LENGTH_SHORT).show();
        }

    }

    private void initview() {
        scrollViewtop = findViewById(R.id.scrolltop);
        title = findViewById(R.id.title);
        castingcompany = findViewById(R.id.castingcompany);
        vacancies = findViewById(R.id.vacancies);
        time = findViewById(R.id.time);
        projectinformation = findViewById(R.id.projectinformation);
        typeoflook = findViewById(R.id.typeoflook);
        agerange = findViewById(R.id.agerange);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        ethnicity = findViewById(R.id.ethnicity);

        eyecolor = findViewById(R.id.eyecolor);
        hairlength = findViewById(R.id.hairlength);
        haircolor = findViewById(R.id.haircolor);
        specialskills = findViewById(R.id.specialskills);
        gender = findViewById(R.id.gender);
        jobtype = findViewById(R.id.jobtype);
        description = findViewById(R.id.description);
        jobduration = findViewById(R.id.jobduration);

        jobrate = findViewById(R.id.jobrate);
        shootingdates = findViewById(R.id.shootingdates);
        shootinglocation = findViewById(R.id.shootinglocation);
        attachments_ll = findViewById(R.id.attachments_ll);
        attachments = findViewById(R.id.attachments);
        cardbottom = findViewById(R.id.cardbottom);

        alreadysubmit = findViewById(R.id.alreadysubmit);
        submitted = findViewById(R.id.submitted);


    }


    @SuppressLint("SetTextI18n")
    private void setView() {
//
        Log.d(TAG, "setView: singlejob-> " + singlejob.getStatus());
        if (singlejob.getStatus().equals("1")) {

            submitted.setVisibility(View.GONE);
            alreadysubmit.setVisibility(View.VISIBLE);
        } else {
            submitted.setVisibility(View.VISIBLE);
            alreadysubmit.setVisibility(View.GONE);
        }


        title.setText(singlejob.getJobTitle());
        castingcompany.setText(singlejob.getClientInformation());
        vacancies.setText("Need to hire " + singlejob.getTotalRoles() + " artist(s)");
        Log.e("cliop", "setView: "+singlejob.getCreatedAt());
        TimeAgo2 timeAgo2 = new TimeAgo2();
//        holder.time.setText(timeAgo2.covertTimeToText(datestart));
        time.setText("Posted " + timeAgo2.covertTimeToText(singlejob.getCreatedAt()));
        projectinformation.setText(singlejob.getRoleDescription());
//        typeoflook.setText(singlejob.get);
        agerange.setText(singlejob.getRoleAgeMin() + "yrs - " + singlejob.getRoleAgeMax() + " yrs");
        height.setText(singlejob.getHeightFeet() + " feet " + singlejob.getHeightInches() + " inches");
        weight.setText(singlejob.getWeight() + " pounds");
        ethnicity.setText((jsonarrayconvert((ArrayList<String>) singlejob.getEthnicity())));
        eyecolor.setText((jsonarrayconvert((ArrayList<String>) singlejob.getEyeColor())));
        hairlength.setText((jsonarrayconvert((ArrayList<String>) singlejob.getHairLength())));
        haircolor.setText((jsonarrayconvert((ArrayList<String>) singlejob.getHairColor())));
        typeoflook.setText(singlejob.getAboutPersonality());

        specialskills.setText((jsonarrayconvert((ArrayList<String>) singlejob.getEthnicity())));
        gender.setText(singlejob.getGenderType());
        jobtype.setText((jsonarrayconvert((ArrayList<String>) singlejob.getJobType())));
        description.setText(singlejob.getRoleDescription());

        jobduration.setText(singlejob.getJobDuration());
        jobrate.setText("$" + singlejob.getJobRatePerDay() + " per day");
        shootingdates.setText(singlejob.getPerformanceFromDate() + "  -  " + singlejob.getPerformanceToDate());
        shootinglocation.setText(singlejob.getPerformanceLocation());
        if (!singlejob.getAdditionalAttachment().isEmpty() && singlejob.getAdditionalAttachment().get(0) != null) {
            attachments_ll.setVisibility(View.VISIBLE);
            ArrayList<String> linked = new ArrayList<>();
            linked.addAll(singlejob.getAdditionalAttachment());

            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.listview_attachments_item, linked);
            attachments.setAdapter(arrayAdapter);
            attachments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(linked.get(i)));
                    startActivity(in);
                }
            });
        } else {
            attachments_ll.setVisibility(View.GONE);
        }

    }


    public void back(View view) {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }

    public void cardbottom(View view) {
//        finish();
        Log.d(TAG, "cardbottom: jobid-> " + singlejob.getId() + " price-> " + singlejob.getJobRatePerDay());
        Intent intent = new Intent(this, Models_submitProposal.class);
        intent.putExtra("client_budget", singlejob.getJobRatePerDay());
        intent.putExtra("Client_job_duration", singlejob.getJobDuration());
        intent.putExtra("propsalid", singlejob.getId());
        Log.d(TAG, "cardbottom: jobid-> " + singlejob.getId());
//        startActivity(intent);
        startActivityForResult(intent, CALLBACK_CODE);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);


    }


    private StringBuilder jsonarrayconvert(ArrayList<String> jsondata) {
        StringBuilder locationstring = new StringBuilder();

        for (int i = 0; i < jsondata.size(); i++) {

            locationstring.append(jsondata.get(i));
            if (i < jsondata.size() - 1) {
                locationstring.append(", ");
            }
        }

        if (locationstring.length() <= 0) {
            locationstring.append("NA");
            return locationstring;
        }
        return locationstring;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if(requestCode == CALLBACK_CODE){
            finish();
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
