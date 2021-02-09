package com.example.myapplication.clientsprofile.clients_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.pojo.GetClientData;
import com.example.myapplication.pojo.JobPostdetail_pojo;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.TimeAgo2;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Objects;

public class Job_Details_Fragement extends Fragment {

    private JobPostdetail_pojo jobPostdetail_pojo;
    private CircularImageView profileimage;
    private TextView jobtitle;
    private TextView castingcompany;
    private TextView time;
    private TextView jobduration;
    private TextView jobrate;
    private TextView jobtype;
    private TextView description;
    private TextView projectinformation;
    private TextView typeoflook;
    private TextView shootingdates;
    private TextView shootinglocation;
    private TextView agerange;
    private TextView height;
    private TextView weight;
    private TextView gender;
    private TextView ethnicity;
    private TextView eyecolor;
    private TextView hairlength;
    private TextView haircolor;
    private TextView specialskills;
    private LinearLayout attachments_ll;
    private ListView attachments;
    public static final String TAG = "Job_Details_Fragement";
    private TimeAgo2 timeAgo2;


    public Job_Details_Fragement(JobPostdetail_pojo jobPostdetail_pojo) {
        this.jobPostdetail_pojo = jobPostdetail_pojo;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("papa jobdetail", "onCreateView: ");
        return inflater.inflate(R.layout.clients_viewjobdetailsfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("papa jobdetail", "onViewCreated: ");
        initviews(view);
        setvalue();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("papa jobdetail", "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("papa jobdetail", "onResume: ");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("papa jobdetail", "onAttach: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("papa jobdetail", "onPause: ");
    }

    private void initviews(View view) {
        profileimage = view.findViewById(R.id.profileimage);
        jobtitle = view.findViewById(R.id.title);
        castingcompany = view.findViewById(R.id.castingcompany);
        time = view.findViewById(R.id.time);
        jobduration = view.findViewById(R.id.jobduration);
        jobrate = view.findViewById(R.id.jobrate);
        jobtype = view.findViewById(R.id.jobtype);
        description = view.findViewById(R.id.description);
        projectinformation = view.findViewById(R.id.projectinformation);
        typeoflook = view.findViewById(R.id.typeoflook);
        shootingdates = view.findViewById(R.id.shootingdates);
        shootinglocation = view.findViewById(R.id.shootinglocation);
        agerange = view.findViewById(R.id.agerange);
        height = view.findViewById(R.id.height);
        weight = view.findViewById(R.id.weight);
        gender = view.findViewById(R.id.gender);
        ethnicity = view.findViewById(R.id.ethnicity);
        eyecolor = view.findViewById(R.id.eyecolor);
        hairlength = view.findViewById(R.id.hairlength);
        haircolor = view.findViewById(R.id.haircolor);
        specialskills = view.findViewById(R.id.specialskills);
        attachments_ll = view.findViewById(R.id.attachments_ll);
        attachments = view.findViewById(R.id.attachments);

    }

    @SuppressLint("SetTextI18n")
    private void setvalue() {
        Gson gson = new Gson();
        String clientdata_detail = SharedPreferanceManager.getInstance(getContext()).get_CLIENT_userInformation();
        GetClientData getClientData = gson.fromJson(clientdata_detail, GetClientData.class);
        Glide.with(this)
                .load(getClientData.getProfile_img()).error(R.mipmap.ic_launcher_r)
                .into(profileimage);
        jobtitle.setText(jobPostdetail_pojo.getJob_title());
        castingcompany.setText("Client : " + jobPostdetail_pojo.getClient_information());
        time.setText(jobPostdetail_pojo.getPerformance_to_date());
        jobduration.setText(jobPostdetail_pojo.getJob_duration());
        jobrate.setText("$" + jobPostdetail_pojo.getJob_rate_per_day() + " per day");
        jobtype.setText(jobPostdetail_pojo.getJob_type());
        description.setText(jobPostdetail_pojo.role_description);
        projectinformation.setText(jobPostdetail_pojo.getAbout_project());
        Log.d(TAG, "setvalue: jobpostdetail_pojo job type -> "+jobPostdetail_pojo.getJob_type());
        typeoflook.setText(jobPostdetail_pojo.getJob_type());

        shootingdates.setText(jobPostdetail_pojo.getPerformance_from_date() + " - " +
                jobPostdetail_pojo.getPerformance_to_date());

        shootinglocation.setText(jobPostdetail_pojo.getPerformance_location());
        agerange.setText(jobPostdetail_pojo.getRole_age_min() + "yrs - " + jobPostdetail_pojo.role_age_max+" yrs");
        height.setText(jobPostdetail_pojo.getHeight_feet() + " feet " +
                jobPostdetail_pojo.getHeight_inches() + " inches");

        weight.setText(jobPostdetail_pojo.getWeight() + " pounds");
        gender.setText(jobPostdetail_pojo.getGender_type());

        ethnicity.setText(jsonarrayconvert((ArrayList<String>) jobPostdetail_pojo.getEthnicity()));
        eyecolor.setText(jsonarrayconvert((ArrayList<String>) jobPostdetail_pojo.getEye_color()));
        hairlength.setText(jsonarrayconvert((ArrayList<String>) jobPostdetail_pojo.getHair_length()));
        haircolor.setText(jsonarrayconvert((ArrayList<String>) jobPostdetail_pojo.getHair_color()));
        specialskills.setText(jsonarrayconvert((ArrayList<String>) jobPostdetail_pojo.getSkill()));
        Log.d(TAG, "setvalue: attachment -> "+jobPostdetail_pojo.additional_attachment.isEmpty()+
                " size-> "+jobPostdetail_pojo.additional_attachment.size());
//
         timeAgo2 = new TimeAgo2();
//        holder.time.setText(timeAgo2.covertTimeToText(datestart));


        time.setText(timeAgo2.covertTimeToText(jobPostdetail_pojo.getCreated_at()));

        if (!jobPostdetail_pojo.additional_attachment.isEmpty()) {
            attachments_ll.setVisibility(View.VISIBLE);
            ArrayList<String> linked = new ArrayList<>(jobPostdetail_pojo.additional_attachment);

            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), R.layout.listview_attachments_item, linked);
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
    public void onStop() {
        Log.d("papa jobdetail", "onStop: timeAgo= "+timeAgo2);
        if(timeAgo2 != null){
            timeAgo2 = null;
        }

        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("papa jobdetail", "onDestroy: ");
        super.onDestroy();
    }
}
