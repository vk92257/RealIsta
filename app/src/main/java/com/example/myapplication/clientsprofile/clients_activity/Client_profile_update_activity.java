package com.example.myapplication.clientsprofile.clients_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.Location_Avtivity;
import com.example.myapplication.R;
import com.example.myapplication.modelsprofile.models_activity.ModelEditProfile_Activity;
import com.example.myapplication.pojo.GetClientData;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.pojo.locationpojo;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.InternetAccess;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class Client_profile_update_activity extends AppCompatActivity {


    private int diable = 0;
    private int startdisable = 1;
    private int citydisable = 1;
    private int link_change = 0;

    private String id;

    private String gender = "Male";
    private TextView maleclick;
    private TextView femaleclick;
    private TextView m_currentCity;
    private TextView m_currentstate;
    private TextView m_country;
    private EditText mobile;
    private Uri profileimageURI = null;
    private ImageView profileimg;
    private View createprogress;
    private NestedScrollView scroll_editText;
private TextView msg;
private LottieAnimationView lottieanimation;
    private locationpojo countrypojo;
    private locationpojo statepojo;
    private locationpojo citrypojo;


    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


    private StringRequest fetchcountry;
    private StringRequest login_client_request;

    private LottieAlertDialog lottieAlertDialog;
    private ArrayList<locationpojo> locationpojos_array;
    private GetClientData getClientData;

    private static final String TAG = "Client_profile_update_activity";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_client_profile_update_activity);
        initfirebase();
        initviews();
        Bundle data = getIntent().getExtras();
        Log.d(TAG, "onCreate: data.getparcelable " + data.getParcelable("ClilentPotiledetail"));
        assert data != null;
        if (data.getParcelable("ClilentPotiledetail") != null) {
            getClientData = data.getParcelable("ClilentPotiledetail");

            if (getClientData != null) {
                id = getClientData.getUser_id();
            }

            assert getClientData != null;
            countrypojo = getClientData.getCountry();
            statepojo = getClientData.getState();
            citrypojo = getClientData.getCity();
            setData(getClientData);
        }
    }


    public void initviews() {

        m_country = findViewById(R.id.country);
        m_currentCity = findViewById(R.id.city);
        m_currentstate = findViewById(R.id.state);

        mobile = findViewById(R.id.mobile);

        profileimg = findViewById(R.id.profile);

        maleclick = findViewById(R.id.male);
        femaleclick = findViewById(R.id.female);
        scroll_editText = findViewById(R.id.scroll_editText);
        createprogress  = findViewById(R.id.progModelCreate);
        msg = findViewById(R.id.msg);
        lottieanimation = findViewById(R.id.lottieanimation);
        lottieanimation.setAnimation(R.raw.yoga);
        msg.setText("Updating your profile ");

        setLocation();
    }


    private void setData(GetClientData getClientData) {
        mobile.setText(getClientData.getMobile());
        Glide.with(this)
                .load(getClientData.getProfile_img()).error(R.drawable.boss)
                .into(profileimg);

        if (getClientData.getGender().toLowerCase().equals("male")) {
            maleselect();
        } else {
            femaleselect();
        }

        if (!TextUtils.isEmpty(getClientData.getCountry().getLocation_name())) {
            m_country.setText(getClientData.getCountry().getLocation_name());
        }

        if (!TextUtils.isEmpty(getClientData.getState().getLocation_name())) {
            m_currentstate.setText(getClientData.getState().getLocation_name());
        }

        if (!TextUtils.isEmpty(getClientData.getCity().getLocation_name())) {
            m_currentCity.setText(getClientData.getCity().getLocation_name());
        }


    }

    public void back(View view) {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void pickprofilepic(View view) {
        pickimage();
    }

    public void Editprofileicon(View view) {
        pickimage();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateportfolio(View view) {
        if (isvalidate()) {
            if (InternetAccess.isOnline(this)) {

                volleyPostRequest();


            } else {
                showvaldationError(getResources().getString(R.string.errorinternet), R.raw.onboard);
            }
        }
    }

    private void pickimage() {
        @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), ConstantString.PICK_IMAGE_SINGLE);
    }

    public void male(View view) {
        maleselect();
    }

    private void maleselect() {
        gender = "Male";
        maleclick.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        femaleclick.setBackgroundColor(getResources().getColor(R.color.verylightgray3));
        femaleclick.setTextColor(getResources().getColor(R.color.black));
        maleclick.setTextColor(getResources().getColor(R.color.background_color_selected_user_item));
    }

    public void female(View view) {
        femaleselect();
    }

    private void femaleselect() {
        gender = "Female";
        femaleclick.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        maleclick.setBackgroundColor(getResources().getColor(R.color.verylightgray3));
        maleclick.setTextColor(getResources().getColor(R.color.black));
        femaleclick.setTextColor(getResources().getColor(R.color.background_color_selected_user_item));
    }


    /**
     * Country/state/city login
     */
    private void setLocation() {

        m_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "country setLocation: diable-> " + diable);


                callCountryList(ApiConstant.COUNTRY_LIST, 0, "Select Country");


            }
        });

        m_currentstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d(TAG, " state setLocation: diable-> " + startdisable);
                if (countrypojo != null && !TextUtils.isEmpty(countrypojo.getLocation_name())) {

                    callCountryList(ApiConstant.STATE_LIST, 1, "Select State");
//                        startdisable = 1;

                } else {
                    Toast.makeText(Client_profile_update_activity.this, R.string.errorselectcountry, Toast.LENGTH_SHORT).show();
                }


            }
        });


        m_currentCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, " city setLocation: diable-> " + citydisable);
                if (statepojo != null && !TextUtils.isEmpty(statepojo.getLocation_name())) {

//                            callCountryList();
                    callCountryList(ApiConstant.CITY_LIST, 2, "Select City");
                    citydisable = 1;

                } else {
                    Toast.makeText(Client_profile_update_activity.this, R.string.errorselectstatefirst, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void callCountryList(String apifetch, int csc, String titlename) {
        Log.d("data", "vollyerccall_forjob_list: apifetch-> " + apifetch + " csc-> " + csc +
                "    country -> " + countrypojo);
        if (csc == 1) {
            Log.d("data", "callCountryList: " + countrypojo.getLocationid());
        }
        lottieAlertDialog = new LottieAlertDialog.Builder(this, DialogTypes.TYPE_QUESTION)
                .setTitle("Loading")
                .setDescription("Please Wait")
                .build();
        lottieAlertDialog.setCancelable(false);
        lottieAlertDialog.show();

        fetchcountry = new StringRequest(Request.Method.POST,
                apifetch,
                (Response.Listener<String>) response -> {
                    Log.d(TAG, "vollyerccall_forjob_list: " + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                            responseCountryFetch(jsonObject, csc, titlename);
                        } else {
                            lottieAlertDialog.dismiss();
                            Log.d(TAG, "vollyerccall_forjob_list: " + jsonObject.getString(ConstantString.RESPONSE_MESSAGE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d(TAG, "initCallApi: error-> " + error.toString());
                    lottieAlertDialog.dismiss();
                    showvaldationError(error.toString(), R.raw.onboard);
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> locationamp = new HashMap<>();
//                locationamp.put("country_id", countrypojo.getLocationid());
                if (countrypojo != null) {
//                    locationamp.put("country_id", countrypojo.getLocationid());
                    Log.d(TAG, "getParams: countrypojo-> " + countrypojo.getLocationid() + " csc==2 " + csc);
                }


                if (csc == 1) {
                    locationamp.put("country_id", countrypojo.getLocationid());
                } else if (csc == 2) {
                    Log.d(TAG, "getParams: state_id-> " + statepojo.getLocationid());
                    locationamp.put("state_id", statepojo.getLocationid());
                } else {
                    locationamp.put("id", "");
                }
                return locationamp;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(fetchcountry);

    }

    private void responseCountryFetch(JSONObject jsonObject, int csc, String titlename) {

        locationpojos_array = new ArrayList<>();
        try {
            JSONArray detailobje = jsonObject.getJSONArray(ConstantString.DETAIL_TAG);
            for (int i = 0; i < detailobje.length(); i++) {
                JSONObject jobpost = (JSONObject) detailobje.get(i);

                Log.d(TAG, "responsefetch: jobpost-> " + jobpost);
                locationpojo locationpojo = new locationpojo(
                        jobpost.getString("id"),
                        jobpost.getString("name")
                );
                locationpojos_array.add(locationpojo);
            }

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {

            Intent intent = new Intent(Client_profile_update_activity.this,
                    Location_Avtivity.class);
            intent.putExtra("location", locationpojos_array);
            intent.putExtra("title_name", titlename);
            intent.putExtra("csc", csc);
            startActivityForResult(intent, ConstantString.location_code);
            lottieAlertDialog.dismiss();

//                }
//            },SPLASH_TIME_OUT);
//

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private boolean isvalidate() {

//        if (mobile.isEmpty()) {
//            showvaldationError(getResources().getString(R.string.errorphoneno), R.raw.questionmark);
//            return false;
//        }
//        if (!android.util.Patterns.PHONE.matcher(mobile).matches()) {
//            showvaldationError(getResources().getString(R.string.errorphonenovalidation), R.raw.questionmark);
//            return false;
//        }
//        if (fcurrentcity.isEmpty()) {
//            showvaldationError(getResources().getString(R.string.errorCurrentCity), R.raw.onboard);
//            return false;
//        }
//        if (fcurrentstate.isEmpty()) {
//            showvaldationError(getResources().getString(R.string.errorCurrentState), R.raw.onboard);
//            return false;
//        }
        if (TextUtils.isEmpty(m_country.getText()) && m_currentCity.getText().toString().equalsIgnoreCase("null")) {
            showvaldationError(getResources().getString(R.string.errorCurrentCounty), R.raw.questionmark);
            return false;
        }

        if ( profileimg.getResources() == null ) {
            showvaldationError(getResources().getString(R.string.errorimagenotfound), R.raw.questionmark);
            return false;
        }


        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == ConstantString.location_code) {
            Log.d(TAG, "onActivityResult: data-> " + data);
            if (data != null) {

                locationpojo cntry_data = data.getParcelableExtra("singlecountry");
                int dcsc = data.getIntExtra("csc", 0);
                Log.d(TAG, "onActivityResult: cntry_data-> " + cntry_data + "" +
                        " dcsc=> " + dcsc);


                if (cntry_data != null) {
                    if (dcsc == 1) {
                        statepojo = cntry_data;
                        m_currentCity.setText("");
                        m_currentstate.setText(cntry_data.getLocation_name());
                        citydisable = 0;
                        startdisable = 0;

                    } else if (dcsc == 2) {
                        citrypojo = cntry_data;
                        m_currentCity.setText(cntry_data.getLocation_name());
                        citydisable = 0;
                    } else {
                        m_country.setText(cntry_data.getLocation_name());
                        m_currentstate.setText("");
                        m_currentCity.setText("");
                        countrypojo = cntry_data;
                        diable = 0;
                        startdisable = 0;

                        m_country.setClickable(true);

                    }

                }

            }
        } else if (requestCode == ConstantString.PICK_IMAGE_SINGLE) {
            if (data != null) {
                Uri ProfilePic = data.getData();
                Log.e(TAG, "onActivityResult: image uri " + ProfilePic);
//                    tick.setVisibility(View.VISIBLE);
                profileimageURI = ProfilePic;
                Glide.with(this)
                        .load(ProfilePic)
                        .into(profileimg);
                link_change = 1;
//                    profileimg.setImageURI(ProfilePic);
            }
        }


    }


    private void initfirebase() {

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


    }


    /**
     * updated profile Api Call
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void volleyPostRequest() {
        startprogress();
//        Log.d(TAG, "volleyPostRequest: uri-> " + imageuri);

//           byte [] encodeByte=Base64.decode(imageuri, Base64.DEFAULT);
//        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//        Log.d(TAG, "volleyPostRequest: bitmap imageuri-> "+bitmap.toString());
        if (link_change == 1) {
            Bitmap originalImage = null;
            try {
                originalImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),profileimageURI );
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            assert originalImage != null;
            originalImage.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            byte[] data = outputStream.toByteArray();


            Log.d(TAG, "volleyPostRequest:data uri--> " + Arrays.toString(data));
            Calendar calendar = Calendar.getInstance();
            //Returns current time in millis
            long timeMilli2 = calendar.getTimeInMillis();
            StorageReference riversRef = storageReference.child("profile")
                    .child("image_" + String.valueOf(timeMilli2) + ".jpg");

            riversRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "onSuccess: ");
//                final Uri[] downloadUrl = {null};
                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            volleycallforupdate(uri.toString());
                        }
                    }).

                            addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: success data");
                                }
                            });

//                Log.d(TAG, "onSuccess: downloadurl-> " + downloadUrl[0]);


                }
            }).

                    addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        stopprogress();
                            Log.d(TAG, "onFailure: e=> " + e.getMessage());
                        }
                    });
        } else if (link_change == 0) {

            volleycallforupdate(getClientData.getProfile_img());

        }


    }


    /***Update Client Api data volleycall*/

    private void volleycallforupdate(String uri) {
        StringRequest portfoliorequest = new StringRequest(Request.Method.POST,
                ApiConstant.CLIENT_PORTFOLIO,
                (Response.Listener<String>) response -> {
                    try {
                        Log.d(TAG, "onSuccess: response==> " + response);
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
//                                            progresscreate.setAnimation(R.raw.dance_party);
//                                            msg.setText(getResources().getText(R.string.createaccount));
//                                            Toast.makeText(Clients_CreateAccount.this, R.string.createaccount,
//                                                    Toast.LENGTH_SHORT).show();


//                                            if (dataforsaveSharepreference != null) {
//                                                SharedPreferanceManager.getInstance(getApplicationContext())
//                                                        .userLogin(dataforsaveSharepreference);
//                                            }
                            initCallApi();


//                                            showvaldationError(jsonObject.getString(ConstantString.RESPONSE_MESSAGE),R.raw.animation);


                        } else {
                                            stopprogress();
                            showvaldationError(jsonObject.getString(ConstantString.RESPONSE_MESSAGE), R.raw.onboard);
                        }

                    } catch (JSONException e) {
                                        stopprogress();
                        e.printStackTrace();
                    }

                },
                error ->

                {
                                    stopprogress();
                    Log.d(TAG, "error " + error.getMessage() + " error to sting " + error.toString());
                    showvaldationError(error.getMessage(), R.raw.onboard);
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> portfoliodata = new HashMap<>();
                Log.e(TAG, "getParams: userid-> " + id + "" +
                        "Phone no " + " country-> " + countrypojo.getLocation_name() + " currentstate-> " + statepojo.getLocation_name() +
                        " currentcity -> " + citrypojo.getLocation_name() + " imageurl-> " + profileimageURI);
                portfoliodata.put(ConstantString.USERID, id);
                portfoliodata.put(ConstantString.PHONE_NO, mobile.getText().toString().trim());

                if (countrypojo != null && !countrypojo.getLocation_name().trim().toLowerCase().equals("null")) {
                    portfoliodata.put(ConstantString.COUNTRY_NAME, countrypojo.getLocationid());
                }

                if (statepojo != null && !TextUtils.isEmpty(statepojo.getLocation_name())) {
                    portfoliodata.put(ConstantString.CURRENT_STATE, statepojo.getLocationid());
                }

                if (citrypojo != null && !TextUtils.isEmpty(citrypojo.getLocation_name())) {
                    portfoliodata.put(ConstantString.CURRENT_CITY, citrypojo.getLocationid());
                }


                portfoliodata.put(ConstantString.GENDER, gender);

                portfoliodata.put(ConstantString.PROFILE_IMG, uri);

                return portfoliodata;
            }
        };
        VolleySingleton.getInstance(Client_profile_update_activity.this).

                addToRequestQueue(portfoliorequest);
    }


    /**
     * Fetch all new data api
     */
    private void initCallApi() {

//        Log.d(TAG, "initCallApi: called");
        login_client_request = new StringRequest(Request.Method.GET,
                ApiConstant.CLIENT_GET_PROFILE,
                (Response.Listener<String>) response -> {
                    Log.d(TAG, "initCallApi: response-> " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                            JSONObject clientdetail = jsonObject.getJSONObject(ConstantString.DETAIL_TAG);
//                            Log.d(TAG, "initCallApi: data ->"+clientdetail.getString(ConstantString.ID)
//                                    +" name "+clientdetail.getString(ConstantString.EMAIL)+
//                                    "gender "+clientdetail.getString(ConstantString.GENDER));
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

//                            setViews(clientData);
                            Gson modelgson = new Gson();
                            SharedPreferanceManager.getInstance(this)
                                    .save_client_userInformation(modelgson.toJson(clientData));
//                            Intent intent = new Intent(Client_profile_update_activity.this,
//                                    Clients_Home.class);
//                            Log.d(TAG, "setlogin:inside id-> "+succesobject.getInt(ConstantString.ID));

                            changeActivity();
                        } else {
                            Log.d(TAG, "initCallApi: error found in client fetch ");
                        }


                    } catch (JSONException e) {
                        Log.d(TAG, "initCallApi: " + e);
                        e.printStackTrace();
                    }
                },
                error -> {
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


                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(Client_profile_update_activity.this).getUserData();
                Log.d(TAG, "getHeaders: token value -> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(login_client_request);
    }

    private  void changeActivity(){

//                            Log.d(TAG, "setlogin:inside id-> "+succesobject.getInt(ConstantString.ID));
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }

    private void startprogress() {
        createprogress.setVisibility(View.VISIBLE);
        scroll_editText.setVisibility(View.GONE);
    }

    private void stopprogress() {
        createprogress.setVisibility(View.GONE);
        scroll_editText.setVisibility(View.VISIBLE);
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
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg, "", errorimage);
        bottomSheet_for_error.setCancelable(false);
//        bottomSheet_for_error.setlottiimage(errorimage);

        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }


}