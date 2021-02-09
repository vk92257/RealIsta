package com.example.myapplication.clientsprofile.clients_activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.pojo.Client_booking_detailsPojo;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.TimeAgo2;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Objects;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Clients_ViewFullJobDetails extends AppCompatActivity {

    private static final String TAG = "Clients_ViewFullJobDetails";

    private ScrollView scrolltop;
    private TextView invitationtype;
    private CircularImageView profileimage;
    private TextView modelname;
    private TextView title;
    private TextView castingcompany;
    private TextView time;
    private TextView jobtype;
    private TextView description;
    private TextView projectinformation;
    private TextView typeoflook;
    private TextView jobduration;
    private TextView jobrate;
    private TextView shootingdates;

    private TextView shootinglocation;
    private TextView gender;
    private TextView agerange;

    private TextView height;
    private TextView weight;
    private TextView ethnicity;

    private TextView eyecolor;
    private TextView hairlength;
    private TextView haircolor;
    private TextView specialskills;

    private LinearLayout attachments_ll;
    private ListView attachments;
    private LinearLayout cardbottom;

    private AppCompatButton endcontractbutton;
    private Client_booking_detailsPojo client_booking_detailsPojo;
    private ArrayAdapter arrayAdapter;


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
        setContentView(R.layout.clients_viewfulljobdetails);
        client_booking_detailsPojo = getIntent().getParcelableExtra("talentdetail");
        intiview();
        setView(client_booking_detailsPojo);

    }


    private void intiview() {

        scrolltop = findViewById(R.id.scrolltop);
        invitationtype = findViewById(R.id.invitationtype);
        profileimage = findViewById(R.id.profileimage);
        modelname = findViewById(R.id.modelname);
        title = findViewById(R.id.title);
        castingcompany = findViewById(R.id.castingcompany);
        time = findViewById(R.id.time);
        jobtype = findViewById(R.id.jobtype);
        description = findViewById(R.id.description);
        projectinformation = findViewById(R.id.projectinformation);
        typeoflook = findViewById(R.id.typeoflook);
        jobduration = findViewById(R.id.jobduration);
        jobrate = findViewById(R.id.jobrate);
        shootingdates = findViewById(R.id.shootingdates);
        shootinglocation = findViewById(R.id.shootinglocation);
        gender = findViewById(R.id.gender);
        agerange = findViewById(R.id.agerange);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        ethnicity = findViewById(R.id.ethnicity);
        eyecolor = findViewById(R.id.eyecolor);
        hairlength = findViewById(R.id.hairlength);
        haircolor = findViewById(R.id.haircolor);
        specialskills = findViewById(R.id.specialskills);
        attachments_ll = findViewById(R.id.attachments_ll);
        attachments = findViewById(R.id.attachments);
        cardbottom = findViewById(R.id.cardbottom);
        endcontractbutton = findViewById(R.id.endcontractbutton);

    }


    private void setView(Client_booking_detailsPojo client_booking_detailsPojo) {
        Log.d(TAG, "setView: status-> "+client_booking_detailsPojo.getStatus().equals("3"));

        if(client_booking_detailsPojo.getStatus().equals("3")){
            invitationtype.setText("CONTRACT HAS ENDED");
            endcontractbutton.setEnabled(true);
            endcontractbutton.setClickable(false);
            endcontractbutton.setAlpha((float) 0.5);

        }
        Glide.with(this).
                load(client_booking_detailsPojo.getProfile_img())
                .thumbnail(0.5f)
                .error(R.mipmap.ic_launcher_r)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileimage);
        modelname.setText(client_booking_detailsPojo.getName());
        title.setText(client_booking_detailsPojo.getJobTitle());
        castingcompany.setText(client_booking_detailsPojo.getJobTitle());
        TimeAgo2 timeAgo2 = new TimeAgo2();
        Log.d(TAG, "setView: time get-> "+client_booking_detailsPojo.getCreatedAt());
        time.setText("Posted "+timeAgo2.covertTimeToText(client_booking_detailsPojo.getCreatedAt()));

        Log.e(TAG, "setView: jobtype -> "+client_booking_detailsPojo.getJobType());


        jobtype.setText(jsonarrayconvert((ArrayList<String>)client_booking_detailsPojo.getJobType()));
        description.setText(client_booking_detailsPojo.getAboutProject());

        projectinformation.setText(jsonarrayconvert((ArrayList<String>)client_booking_detailsPojo.getJobType()));
        typeoflook.setText(jsonarrayconvert((ArrayList<String>) client_booking_detailsPojo.getSkills()));
        jobduration.setText(client_booking_detailsPojo.getJobDuration());
        jobrate.setText("$"+client_booking_detailsPojo.getFinal_price()+" per day");
        shootingdates.setText(client_booking_detailsPojo.getPerformanceFromDate()
                + " - " + client_booking_detailsPojo.getPerformanceToDate());
        shootinglocation.setText(client_booking_detailsPojo.getPerformanceLocation());
        gender.setText(client_booking_detailsPojo.getGenderType());
        agerange.setText(client_booking_detailsPojo.getRoleAgeMin() + " - " + client_booking_detailsPojo.getRoleAgeMax());
        height.setText(client_booking_detailsPojo.getHeightFeet() + " feet " +
                client_booking_detailsPojo.getHeightInches() + " inches");
        weight.setText(client_booking_detailsPojo.getWeight() + " pounds");
        ethnicity.setText(jsonarrayconvert((ArrayList<String>)client_booking_detailsPojo.getEthnicity()));
        eyecolor.setText(jsonarrayconvert((ArrayList<String>)client_booking_detailsPojo.getEyeColor()));
        hairlength.setText(jsonarrayconvert((ArrayList<String>)client_booking_detailsPojo.getHairLength()));
        haircolor.setText(jsonarrayconvert((ArrayList<String>)client_booking_detailsPojo.getHairColor()));
        specialskills.setText(jsonarrayconvert((ArrayList<String>)client_booking_detailsPojo.getSkills()));

        if (!client_booking_detailsPojo.getAdditionalAttachment().isEmpty()) {
            attachments_ll.setVisibility(View.VISIBLE);
            ArrayList<String> linked = new ArrayList<>(client_booking_detailsPojo.getAdditionalAttachment());

            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(Objects.requireNonNull(this), R.layout.listview_attachments_item, linked);
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

    public void viewprofile(View view) {
        Intent intent = new Intent(this,View_proposal_userProfile.class);
        intent.putExtra("user_id",client_booking_detailsPojo.getTalentId());
        startActivity(intent);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }

    public void gotoendcontract(View view){
        Intent intent = new Intent(Clients_ViewFullJobDetails.this,Clients_EndContact.class);
        intent.putExtra("contract_id",client_booking_detailsPojo.getContractId());
        startActivity(intent);
        finish();
    }

}
