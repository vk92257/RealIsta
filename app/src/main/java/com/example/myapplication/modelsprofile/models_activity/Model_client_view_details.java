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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.pojo.AcceptRejectSinglejob_detailpojo;
import com.example.myapplication.pojo.GetClientData;
import com.example.myapplication.pojo.JobPostdetail_pojo;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.TimeAgo2;
import com.example.myapplication.utils.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class Model_client_view_details extends AppCompatActivity {

    private static final String TAG = "Model_client_view_details";
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
    private StringRequest fetchjob_list;
    private String Jobid;
    private String profileimage_client;
    private ShimmerFrameLayout shimmer;
    private FrameLayout rootLayout;
    private String contractID;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.model_client_view_details_layout);
        initviews();
        Jobid = getIntent().getStringExtra("jobid");
        contractID = getIntent().getStringExtra("contractID");
        profileimage_client = getIntent().getStringExtra("profileimage_client");
        vollyerccall_forjob_Data(Jobid);
    }

    public void back(View view) {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    private void initviews() {
        shimmer = findViewById(R.id.shimmer_view_container);
        profileimage = findViewById(R.id.profileimage);
        jobtitle = findViewById(R.id.title);
        castingcompany = findViewById(R.id.castingcompany);
        time = findViewById(R.id.time);
        jobduration = findViewById(R.id.jobduration);
        jobrate = findViewById(R.id.jobrate);
        jobtype = findViewById(R.id.jobtype);
        description = findViewById(R.id.description);
        projectinformation = findViewById(R.id.projectinformation);
        typeoflook = findViewById(R.id.typeoflook);
        shootingdates = findViewById(R.id.shootingdates);
        shootinglocation = findViewById(R.id.shootinglocation);
        agerange = findViewById(R.id.agerange);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        gender = findViewById(R.id.gender);
        ethnicity = findViewById(R.id.ethnicity);
        eyecolor = findViewById(R.id.eyecolor);
        hairlength = findViewById(R.id.hairlength);
        haircolor = findViewById(R.id.haircolor);
        specialskills = findViewById(R.id.specialskills);
        attachments_ll = findViewById(R.id.attachments_ll);
        attachments = findViewById(R.id.attachments);
        rootLayout = findViewById(R.id.main_layout);
    }

    @SuppressLint("SetTextI18n")
    private void setvalue(AcceptRejectSinglejob_detailpojo jobPostdetail_pojo) {


        Glide.with(this)
                .load(profileimage_client).error(R.mipmap.ic_launcher_r)
                .into(profileimage);
        jobtitle.setText(jobPostdetail_pojo.getJobTitle());
        castingcompany.setText("Client : " + jobPostdetail_pojo.getClientInformation());
        TimeAgo2 timeAgo2 = new TimeAgo2();
        time.setText(timeAgo2.covertTimeToText(jobPostdetail_pojo.getCreatedAt()));
        jobduration.setText(jobPostdetail_pojo.getJobDuration());
        jobrate.setText("$" + jobPostdetail_pojo.getFinalPrice() + " per day");
        jobtype.setText(jobPostdetail_pojo.getJobType());
        description.setText(jobPostdetail_pojo.getRoleDescription());
        projectinformation.setText(jobPostdetail_pojo.getAboutProject());
        typeoflook.setText(jobPostdetail_pojo.getJobType());
        shootingdates.setText(jobPostdetail_pojo.getPerformanceFromDate() + " - " +
                jobPostdetail_pojo.getPerformanceToDate());

        shootinglocation.setText(jobPostdetail_pojo.getPerformanceLocation());
        agerange.setText(jobPostdetail_pojo.getRoleAgeMin() + " - " + jobPostdetail_pojo.getRoleAgeMax());
        height.setText(jobPostdetail_pojo.getHeightFeet() + " feet " +
                jobPostdetail_pojo.getHeightInches() + " inches");

        weight.setText(jobPostdetail_pojo.getWeight() + " pounds");
        gender.setText(jobPostdetail_pojo.getGenderType());

        ethnicity.setText(jsonarrayconvert((ArrayList<String>) jobPostdetail_pojo.getEthnicity()));
        eyecolor.setText(jsonarrayconvert((ArrayList<String>) jobPostdetail_pojo.getEyeColor()));
        hairlength.setText(jsonarrayconvert((ArrayList<String>) jobPostdetail_pojo.getHairColor()));
        haircolor.setText(jsonarrayconvert((ArrayList<String>) jobPostdetail_pojo.getHairColor()));
        specialskills.setText(jsonarrayconvert((ArrayList<String>) jobPostdetail_pojo.getSkills()));
        if (!jobPostdetail_pojo.getAdditionalAttachment().isEmpty()) {
            attachments_ll.setVisibility(View.VISIBLE);
            ArrayList<String> linked = new ArrayList<>(jobPostdetail_pojo.getAdditionalAttachment());

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

        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
        rootLayout.setVisibility(View.VISIBLE);


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

    public void vollyerccall_forjob_Data(String jobid) {

        shimmer.startLayoutAnimation();
//       shimmer.startShimmer();
        Log.d(TAG, "vollyerccall_forjob_list: ");
        fetchjob_list = new StringRequest(Request.Method.POST,
                ApiConstant.ACCEPTE_REJECT_CONTRACT_CLIENTFINALPRICE,
                (Response.Listener<String>) response -> {
                    Log.d(TAG, "vollyerccall_forjob_list: ------>\n\n\n\n   " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                            responsefetch(jsonObject);
                        } else {
//                            joballpost_rv.hideShimmer();
                            Toast.makeText(this,
                                    jsonObject.getString(ConstantString.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "vollyerccall_forjob_list: " + jsonObject.getString(ConstantString.RESPONSE_MESSAGE));
//                            nojobpostshow();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    Log.d(TAG, "initCallApi: error-> " + error.toString());
//                    showvaldationError(error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d(TAG, "getParams: jobid-> " + jobid);
                HashMap<String, String> singlejobdetailparam = new HashMap<>();
                singlejobdetailparam.put("jobId", jobid);
                singlejobdetailparam.put("contract_id", contractID);
                return singlejobdetailparam;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(Model_client_view_details.this).getUserData();
                Log.d(TAG, "getHeaders: token value-> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(fetchjob_list);
    }

    private void responsefetch(JSONObject jsonObject) {
        try {
//            Log.d(TAG, "responsefetch: jsonobject-> "+ jsonObject.getJSONObject(ConstantString.DETAIL_TAG));
            JSONObject jobpost = jsonObject.getJSONObject(ConstantString.DETAIL_TAG);
            Log.d(TAG, "responsefetch: jsonobject-> " + jobpost);

//                JSONObject jobpost = (JSONObject) detailobjec.get(i);
            Log.d(TAG, "responsefetch: jobpost-> " + jobpost);

            JSONArray skillarray = jobpost.getJSONArray("skills");
            Log.d(TAG, "responsefetch: skillarray-> " + skillarray);
            ArrayList<String> skilla = new ArrayList<>();
            for (int p = 0; p < skillarray.length(); p++) {
                skilla.add(skillarray.get(p).toString());
            }
            JSONArray additionattach = jobpost.getJSONArray("additional_attachment");
            ArrayList<String> additionattacharray = new ArrayList<>();
            for (int p = 0; p < additionattach.length(); p++) {
                additionattacharray.add(additionattach.get(p).toString());
            }

            JSONArray hair_colorjson = jobpost.getJSONArray("hair_color");
            ArrayList<String> hair_colorarray = new ArrayList<>();
            for (int p = 0; p < hair_colorjson.length(); p++) {
                hair_colorarray.add(hair_colorjson.get(p).toString());
            }
            JSONArray eye_colorjson = jobpost.getJSONArray("eye_color");
            ArrayList<String> eye_colorarray = new ArrayList<>();
            for (int p = 0; p < eye_colorjson.length(); p++) {
                eye_colorarray.add(eye_colorjson.get(p).toString());
            }
            JSONArray hair_lengthjson = jobpost.getJSONArray("hair_length");
            ArrayList<String> hair_lengtharray = new ArrayList<>();
            for (int p = 0; p < hair_lengthjson.length(); p++) {
                hair_lengtharray.add(hair_lengthjson.get(p).toString());
            }
            JSONArray skiethnicitylljson = jobpost.getJSONArray("ethnicity");
            ArrayList<String> skiethnicityllarray = new ArrayList<>();
            for (int p = 0; p < skiethnicitylljson.length(); p++) {
                skiethnicityllarray.add(skiethnicitylljson.get(p).toString());
            }

            AcceptRejectSinglejob_detailpojo arpojo = new AcceptRejectSinglejob_detailpojo();

            arpojo.setUserId(jobpost.getString("user_id"));
            arpojo.setName(jobpost.getString("name"));
            arpojo.setRoleAgeMin(jobpost.getString("role_age_min"));
            arpojo.setRoleAgeMax(jobpost.getString("role_age_max"));
            arpojo.setHeightFeet(jobpost.getString("height_feet"));
            arpojo.setHeightInches(jobpost.getString("height_inches"));
            arpojo.setWeight(jobpost.getString("weight"));
            arpojo.setAboutProject(jobpost.getString("about_project"));
            arpojo.setAboutPersonality(jobpost.getString("about_personality"));
            arpojo.setJobRatePerDay(jobpost.getString("job_rate_per_day"));
            arpojo.setJobTitle(jobpost.getString("job_title"));
            arpojo.setJobType(jobpost.getString("job_type"));
            arpojo.setTotalRoles(jobpost.getString("total_roles"));
            arpojo.setGenderType(jobpost.getString("gender_type"));
            arpojo.setProductName(jobpost.getString("product_name"));
            arpojo.setJobDuration(jobpost.getString("job_duration"));
            arpojo.setRoleDescription(jobpost.getString("role_description"));
            arpojo.setPerformanceLocation(jobpost.getString("performance_location"));
            arpojo.setPerformanceFromDate(jobpost.getString("performance_from_date"));
            arpojo.setPerformanceToDate(jobpost.getString("performance_to_date"));
            arpojo.setClientInformation(jobpost.getString("client_information"));
            arpojo.setSkills(skilla);
            arpojo.setAdditionalAttachment(additionattacharray);
            arpojo.setHairColor(hair_colorarray);
            arpojo.setEyeColor(eye_colorarray);
            arpojo.setHairLength(hair_lengtharray);
            arpojo.setEthnicity(skiethnicityllarray);
            arpojo.setCreatedAt(jobpost.getString("created_at"));
            arpojo.setUpdatedAt(jobpost.getString("updated_at"));
            arpojo.setFinalPrice(jobpost.getString("final_price"));
            arpojo.setPosition(jobpost.getString("position"));

            setvalue(arpojo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
