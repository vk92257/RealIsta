package com.example.myapplication.clientsprofile.clients_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.Complete_checkAcitivity;
import com.example.myapplication.R;
import com.example.myapplication.modelsprofile.models_activity.Models_submitProposal;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.InternetAccess;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class Clients_HiringNowActivity extends AppCompatActivity {
    private EditText invitetext;
    private EditText jobrate;
    private RadioButton accepttermsradio;
    private MaterialCardView hireartist;
    private String JObid;
    private String proposalid;
    private View progModelCreate;
    private LottieAnimationView lottieanimation;
    private TextView msg;
    private static final String TAG = "Clients_HiringNowActivity";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        JObid = getIntent().getStringExtra("jobid");
        proposalid = getIntent().getStringExtra("proposalid");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.client_hiringhimnow);
        initview();
    }

    private void initview() {
        invitetext = findViewById(R.id.invitetext);
        jobrate = findViewById(R.id.jobrate);
        accepttermsradio = findViewById(R.id.accepttermsradio);
        hireartist = findViewById(R.id.hireartist);
        progModelCreate = findViewById(R.id.progModelCreate);
        lottieanimation = findViewById(R.id.lottieanimation);
        lottieanimation.setAnimation(R.raw.lottie_send);
        msg = findViewById(R.id.msg);
        msg.setText("");
//        accepttermsradio.setChecked(false);
//        //        lottieanimation.setAnimation(R.raw.invitation);
////        lottieanimation.setAnimation("invitaion.json");
//
//        accepttermsradio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean checked = ((RadioButton) v).isChecked();
//                // Check which radiobutton was pressed
//                if (checked){
//                   ((RadioButton) v).setChecked(false);
//                }
//                else{
//                    ((RadioButton) v).setChecked(true);
//                    // Do your coding
//                }
//            }
//        });


    }


    public void back(View view) {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void hireNow(View view) {
        if (isvalidate()) {
            if (InternetAccess.isOnline(this)) {
                showprogressbar();
                volleyapicall();
            } else {
                showvaldationError(getResources().getString(R.string.errorinternet));
            }
        }

    }

    private boolean isvalidate(){

        if(jobrate.getText().toString().isEmpty()){
            showvaldationError(getResources().getString(R.string.errorjobrate));
            return false;
        }
        if(Integer.parseInt(jobrate.getText().toString().trim()) <= 0){
            showvaldationError(getResources().getString(R.string.erroramountcanbezero));
            return false;
        }


        if(!accepttermsradio.isChecked()){
            showvaldationError(getResources().getString(R.string.erroraccepttermsandcondition));
            return false;
        }


        return true;
    }

    private void volleyapicall() {
        Log.d(TAG, "volleyapicall: ");
        StringRequest proposalrequest = new StringRequest(Request.Method.POST,
                ApiConstant.HIRE_FOR_JOB,
                (Response.Listener<String>) response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d(TAG, "volleyapicall: response-> " + response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {

//                            hideprogressbar();

//                            hideprogress();    Intent intent = new Intent(Clients_HiringNowActivity.this,
//                                    Complete_checkAcitivity.class);
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            finish();
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        } else {
                            hideprogressbar();
//                            hideprogress();
                        }
                    } catch (JSONException e) {
hideprogressbar();
                    }
                },
                error -> {
//                    hideprogress();
                    hideprogressbar();
                    showvaldationError(error.toString());
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> submitproposal = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(Clients_HiringNowActivity.this).getUserData();
                Log.d(TAG, "getHeaders: token value-> " + logindata.getToken());
                submitproposal.put("Authorization", "Bearer " + logindata.getToken());
                return submitproposal;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Gson gson = new Gson();
//                Log.d(TAG, "getParams: cover_letter-> "+proposal.getText().toString().trim()+
//                        "attachment-> "+gson.toJson(attchementforproposal)+
//                        " jobId->  "+proposalid_jobid+" rate-> "+rate.getText().toString().trim());
                Log.d(TAG, "getParams: "+proposalid+" JObid-> "+JObid);
                HashMap<String, String> submitpropsalparam = new HashMap<>();
                submitpropsalparam.put("purposal_id", proposalid);
                submitpropsalparam.put("jobId", JObid);
                submitpropsalparam.put("position", invitetext.getText().toString().trim());
                submitpropsalparam.put("final_price", jobrate.getText().toString().trim());

                return submitpropsalparam;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(proposalrequest);

    }


    private void showvaldationError(String msg) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg);
        bottomSheet_for_error.setCancelable(false);
        bottomSheet_for_error.setlottiimage(R.raw.questionmark);
        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");

    }
    private void showprogressbar(){
        progModelCreate.setVisibility(View.VISIBLE);
    }

    private void hideprogressbar(){
        progModelCreate.setVisibility(View.GONE);
    }
}
