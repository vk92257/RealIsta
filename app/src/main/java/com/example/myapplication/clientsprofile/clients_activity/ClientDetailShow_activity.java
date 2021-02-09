package com.example.myapplication.clientsprofile.clients_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.R;
import com.example.myapplication.pojo.GetClientData;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.pojo.locationpojo;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ClientDetailShow_activity extends AppCompatActivity {

    private FrameLayout profile_image;
    private CircularImageView profile;
    private TextView username;
    private TextView location_id;
    private TextView mobile_no;
    private TextView email;
    private TextView gender_id;
    private StringRequest login_client_request;
    private static final String TAG ="ClientDetailshow_activity" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_client_detail_show_activity);
        initview();
        volleyCallfordetail();
    }


    private void initview() {
        profile_image = findViewById(R.id.profile_image);
        profile = findViewById(R.id.profile);
        username = findViewById(R.id.username);
        location_id = findViewById(R.id.location_id);
        mobile_no = findViewById(R.id.mobile_no);
        email = findViewById(R.id.email);
        gender_id = findViewById(R.id.gender_id);

    }

    public void backbutton(View view){
        finish();
    }

    @Override
    protected void onStop() {
        login_client_request.cancel();
        super.onStop();
    }

    @SuppressLint("SetTextI18n")
    private void setdetails(GetClientData clientData){
        Glide.with(this).load(clientData.getProfile_img())
                .thumbnail(0.5f)
                .error(R.drawable.ic_broken_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(profile);
        String name = clientData.getName();
        username.setText(name.substring(0,1).toUpperCase()+name.substring(1));
        mobile_no.setText(clientData.getMobile());
        email.setText(clientData.getEmail());
        gender_id.setText(clientData.getGender());

        locationpojo countrylocation = clientData.getCountry();
        locationpojo statelocation = clientData.getState();
        locationpojo citylocation = clientData.getCity();

        if(statelocation.getLocation_name() != null && !statelocation.getLocation_name().toLowerCase().equals("null")){
            location_id.setText(statelocation.getLocation_name() + " | " +countrylocation.getLocation_name());
        }
        else{
            location_id.setText(countrylocation.getLocation_name());
        }


    }


    private void volleyCallfordetail() {

        String userid = getIntent().getStringExtra("user_id");
        login_client_request = new StringRequest(Request.Method.POST,
                ApiConstant.GET_CLINE_DETAILS,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                            JSONObject clientdetail = jsonObject.getJSONObject(ConstantString.DETAIL_TAG);
                            GetClientData clientData = new GetClientData(
                                    clientdetail.getString(ConstantString.USERID),
                                    clientdetail.getString(ConstantString.NAME),
                                    clientdetail.getString(ConstantString.EMAIL),
                                    clientdetail.getString(ConstantString.GENDER),
                                    clientdetail.getString(ConstantString.GET_CLIENT_MOBILE),
                                    convertlocationpojo(clientdetail.getJSONObject(ConstantString.GET_CLIENT_COUNTRY)),
                                    convertlocationpojo(clientdetail.getJSONObject(ConstantString.GET_CLIENT_STATE)),
                                    convertlocationpojo(clientdetail.getJSONObject(ConstantString.GET_CLIENT_CITY)),
                                    clientdetail.getString(ConstantString.PROFILE_IMG),
                                    clientdetail.getString(ConstantString.GET_CLIENT_CREATED_AT),
                                    clientdetail.getString(ConstantString.GET_CLIENT_UPDATED_AT)
                            );

                            setdetails(clientData);


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },error -> {
            Log.d(TAG, "initCallApi: error-> " + error.toString());
            error.printStackTrace();

            if (error instanceof NetworkError) {
                showvaldationError("Network Error please check internet connection", R.raw.onboard);
            } else if (error instanceof ServerError) {
                //handle if server error occurs with 5** status code
                showvaldationError("Server side error", R.raw.onboard);
            } else if (error instanceof AuthFailureError) {
                //handle if authFailure occurs.This is generally because of invalid credentials
                showvaldationError("please check your credentials ", R.raw.onboard);
            } else if (error instanceof ParseError) {
                //handle if the volley is unable to parse the response data.
                showvaldationError(" Unable to parse the response data ", R.raw.onboard);
            } else if (error instanceof NoConnectionError) {
                //handle if no connection is occurred
                showvaldationError(" No Connection to server ", R.raw.onboard);
            } else if (error instanceof TimeoutError) {
                showvaldationError("Time out error Please restart the app  ", R.raw.onboard);
                //handle if socket time out is occurred.
            }
        }){


            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(ClientDetailShow_activity.this).getUserData();
                Log.d(TAG, "getHeaders: token value -> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> userid_call= new HashMap<>();
                    userid_call.put("id",userid);
                return userid_call;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(login_client_request);
    }
    private locationpojo convertlocationpojo(JSONObject location) {
        locationpojo locationpojo = new locationpojo();
        try {
            locationpojo.setLocationid(location.getString("id"));
            locationpojo.setLocation_name(location.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return locationpojo;
    }

    private void showvaldationError(String msg, int errorimage) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg, errorimage);
        bottomSheet_for_error.setCancelable(false);
//        bottomSheet_for_error.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        bottomSheet_for_error.setlottiimage(errorimage);
        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");
    }
}