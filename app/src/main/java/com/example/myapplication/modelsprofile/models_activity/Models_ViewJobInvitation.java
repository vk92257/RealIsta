package com.example.myapplication.modelsprofile.models_activity;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Adapter.All_JobPost;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.R;
import com.example.myapplication.pojo.AcceptRejectSinglejob_detailpojo;
import com.example.myapplication.pojo.JobPostdetail_pojo;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class Models_ViewJobInvitation extends AppCompatActivity {
    private TextView title;
    private TextView invitationmessage;
    private TextView castingcompany;
    private TextView vacancies;
    private TextView time;
    private TextView jobtype;
    private TextView description;
    private TextView jobduration;
    private TextView jobrate;
    private TextView shootingdates;
    private TextView shootinglocation;
    private LinearLayout attachments_ll;
    private ListView attachments;
    private TextView hiredtext;
    private MaterialCardView reject;
    private MaterialCardView accept;
    private String Jobid;
    private String contractID;
    private StringRequest fetchjob_list;
    private AcceptRejectSinglejob_detailpojo arpojo;

    private ScrollView scrollView_jobinvitaion;
    private ShimmerFrameLayout shimmer;

    private String userid;

    private int AcceptContract = 1011;

    private int status ;

    private static final String TAG = "Models_ViewJobInvitaion";
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.models_viewjobinvitation);
        Jobid = getIntent().getStringExtra("jobid");
        contractID = getIntent().getStringExtra("contractID");
        status = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("status")));
        userid = getIntent().getStringExtra("userid");
        Log.e(TAG, "onCreate: jobid-> "+Jobid+" contractID-> "+contractID+" status-> "+status);

        LoginTimesaveData logindata = SharedPreferanceManager.getInstance(Models_ViewJobInvitation.this).getUserData();
        Log.e(TAG, "getHeaders: token value-> " + logindata.getToken());
        iniview();
        vollyerccall_forjob_Data(Jobid,contractID);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(fetchjob_list !=null){
            fetchjob_list.cancel();
        }
    }

    private void iniview() {

        title = findViewById(R.id.title);
        invitationmessage = findViewById(R.id.invitationmessage);
        castingcompany = findViewById(R.id.castingcompany);
        vacancies = findViewById(R.id.vacancies);
        time = findViewById(R.id.time);
        jobtype = findViewById(R.id.jobtype);
        description = findViewById(R.id.description);
        jobduration = findViewById(R.id.jobduration);
        jobrate = findViewById(R.id.jobrate);
        shootingdates = findViewById(R.id.shootingdates);
        shootinglocation = findViewById(R.id.shootinglocation);
        attachments_ll = findViewById(R.id.attachments_ll);
        attachments = findViewById(R.id.attachments);
        reject = findViewById(R.id.reject);
        accept = findViewById(R.id.accept);
        hiredtext = findViewById(R.id.hiredtext);
        scrollView_jobinvitaion = findViewById(R.id.booking_invitaionlayout);
        shimmer = findViewById(R.id.shimmer_view_container);

    }

    private void setView(AcceptRejectSinglejob_detailpojo jobpost) {
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);

        title.setText(jobpost.getJobTitle());

        if(status == 1){
            hiredtext.setVisibility(View.VISIBLE);
            reject.setVisibility(View.GONE);
            accept.setVisibility(View.GONE);
        }

        Log.d(TAG, "setView: job data "+jobpost);
        invitationmessage.setText(jobpost.getPosition());
        castingcompany.setText(jobpost.getClientInformation());
        vacancies.setText(jobpost.getTotalRoles());
        time.setText(jobpost.getCreatedAt());
        jobtype.setText(jobpost.getJobType());
        description.setText(jobpost.getRoleDescription());
        jobduration.setText(jobpost.getJobDuration());
        jobrate.setText("$"+jobpost.getFinalPrice()+" per day");
        shootingdates.setText(jobpost.getPerformanceFromDate()+"  -  "+jobpost.getPerformanceToDate());
        shootinglocation.setText(jobpost.getPerformanceLocation());


        if(!jobpost.getAdditionalAttachment().isEmpty()) {
            attachments_ll.setVisibility(View.VISIBLE);
            ArrayList<String> linked = new ArrayList<>(jobpost.getAdditionalAttachment());

            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(Objects.requireNonNull(this),
                    R.layout.listview_attachments_item, linked);
            attachments.setAdapter(arrayAdapter);
            attachments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(linked.get(i)));
                    startActivity(in);
                }
            });

        }else{
            attachments_ll.setVisibility(View.GONE);
        }



    }

    public void back(View view) {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void reject_contract_invitation(View view) {
        Log.d(TAG, "reject_contract_invitation: ");
        Intent intent =new Intent(this,Contracts_Accept_reject.class);
        intent.putExtra("Value",true);
        intent.putExtra("confirm",contractID);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void accept_contract_invitation(View view) {
        Intent intent =new Intent(this,Contracts_Accept_reject.class);
        intent.putExtra("value",false);
        intent.putExtra("confirm",contractID);
        intent.putExtra("userid",userid);
        startActivityForResult(intent,AcceptContract);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void vollyerccall_forjob_Data(String jobid,String contractID){
        shimmer.startLayoutAnimation();
        Log.d(TAG, "vollyerccall_forjob_list: ");
        fetchjob_list = new StringRequest(Request.Method.POST,
                ApiConstant. ACCEPTE_REJECT_CONTRACT_CLIENTFINALPRICE,
                (Response.Listener<String>) response ->{
                    Log.e(TAG, "vollyerccall_forjob_list: \n\n\n\n\n\n\n\n\n  "+response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if(!jsonObject.getBoolean(ConstantString.IS_ERROR)){
                            responsefetch(jsonObject);
                        }
                        else{
//                            joballpost_rv.hideShimmer();
                            Toast.makeText(this,
                                    jsonObject.getString(ConstantString.RESPONSE_MESSAGE),Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "vollyerccall_forjob_list: "+jsonObject.getString(ConstantString.RESPONSE_MESSAGE));
//                            nojobpostshow();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    Log.d(TAG, "initCallApi: error-> " + error.toString());
//                    showvaldationError(error.toString());
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d(TAG, "getParams: jobid-> "+jobid+" cnotractid "+contractID);
                HashMap<String,String > singlejobdetailparam = new HashMap<>();
                singlejobdetailparam.put("jobId",jobid);
                singlejobdetailparam.put("contract_id",contractID);
                return singlejobdetailparam;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(Models_ViewJobInvitation.this).getUserData();
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
            Log.d(TAG, "responsefetch: jsonobject-> "+jobpost);

//                JSONObject jobpost = (JSONObject) detailobjec.get(i);
                Log.d(TAG, "responsefetch: jobpost-> "+jobpost);

                JSONArray skillarray = jobpost.getJSONArray("skills");
                Log.d(TAG, "responsefetch: skillarray-> "+skillarray);
                ArrayList<String> skilla = new ArrayList<>();
                for(int p=0;p<skillarray.length();p++){
                    skilla.add(skillarray.get(p).toString());
                }
                JSONArray additionattach = jobpost.getJSONArray("additional_attachment");
                ArrayList<String> additionattacharray = new ArrayList<>();
                for(int p=0;p<additionattach.length();p++){
                    additionattacharray.add(additionattach.get(p).toString());
                }

                JSONArray hair_colorjson = jobpost.getJSONArray("hair_color");
                ArrayList<String> hair_colorarray = new ArrayList<>();
                for(int p=0;p<hair_colorjson.length();p++){
                    hair_colorarray.add(hair_colorjson.get(p).toString());
                }
                JSONArray eye_colorjson = jobpost.getJSONArray("eye_color");
                ArrayList<String> eye_colorarray = new ArrayList<>();
                for(int p=0;p<eye_colorjson.length();p++){
                    eye_colorarray.add(eye_colorjson.get(p).toString());
                }
                JSONArray hair_lengthjson = jobpost.getJSONArray("hair_length");
                ArrayList<String> hair_lengtharray = new ArrayList<>();
                for(int p=0;p<hair_lengthjson.length();p++){
                    hair_lengtharray.add(hair_lengthjson.get(p).toString());
                }
                JSONArray skiethnicitylljson = jobpost.getJSONArray("ethnicity");
                ArrayList<String> skiethnicityllarray = new ArrayList<>();
                for(int p=0;p<skiethnicitylljson.length();p++){
                    skiethnicityllarray.add(skiethnicitylljson.get(p).toString());
                }

//
//                JobPostdetail_pojo jobdetail =
//                        new JobPostdetail_pojo(jobpost.getString("job_id"),
//                                jobpost.getString("user_id"),
//                                jobpost.getString("name"),
//                                jobpost.getString("role_age_min"),
//                                jobpost.getString("role_age_max"),
//                                jobpost.getString("height_feet"),
//                                jobpost.getString("height_inches"),
//                                jobpost.getString("weight"),
//                                jobpost.getString("about_project"),
//                                jobpost.getString("about_personality"),
//                                jobpost.getString("job_rate_per_day"),
//                                jobpost.getString("job_title"),
//                                jobpost.getString("job_type"),
//                                jobpost.getString("total_roles"),
//                                jobpost.getString("gender_type"),
//                                jobpost.getString("product_name"),
//                                jobpost.getString("job_duration"),
//                                jobpost.getString("role_description"),
//                                jobpost.getString("performance_location"),
//                                jobpost.getString("performance_from_date"),
//                                jobpost.getString("performance_to_date"),
//                                jobpost.getString("client_information"),
//                                skilla,
//                                additionattacharray,
//                                hair_colorarray,
//                                eye_colorarray,
//                                hair_lengtharray,
//                                skiethnicityllarray,
//                                jobpost.getString("created_at"),
//                                jobpost.getString("updated_at")
//                        );


            arpojo = new AcceptRejectSinglejob_detailpojo();

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
            arpojo.setAdditionalAttachment( additionattacharray);
            arpojo.setHairColor(hair_colorarray);
            arpojo.setEyeColor( eye_colorarray);
            arpojo.setHairLength(hair_lengtharray);
            arpojo.setEthnicity( skiethnicityllarray);
            arpojo.setCreatedAt(jobpost.getString("created_at"));
            arpojo.setUpdatedAt(jobpost.getString("updated_at"));
            arpojo.setFinalPrice(jobpost.getString("final_price"));
            arpojo.setPosition(jobpost.getString("position"));



                    setView(arpojo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: this is the data resultcode-> "+resultCode+
                " AcceptContract-> "+requestCode);
        if(resultCode != RESULT_OK){
            return ;
        }

        if( requestCode == AcceptContract ){

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
