package com.example.myapplication.clientsprofile.clients_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.Location_Avtivity;
import com.example.myapplication.R;
import com.example.myapplication.modelsprofile.models_activity.Models_createportfolioActivity;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.pojo.locationpojo;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.InternetAccess;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.quickblox.users.model.QBUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class Clients_CreateAccount extends AppCompatActivity {

    private int diable = 0;
    private int startdisable = 1;
    private int citydisable = 1;

    private locationpojo countrypojo;
    private locationpojo statepojo;
    private locationpojo citrypojo;
    private LottieAlertDialog lottieAlertDialog;



    private TextView currentCity;
    private TextView currentState;
    private TextView country;
    private String gender;
    private ImageView bluetick;
    private TextView txtmale, txtfemale;
    private CircularImageView profile_img;
    private Uri imageuri = null;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    public static final int RESULT_LOAD_IMAGE = 101;
    private String userid;
    private View createprogress;
    private LinearLayout createlayout;
    private LottieAnimationView progresscreate;
    private TextView msg;
    private QBUser user;


    private LoginTimesaveData dataforsaveSharepreference;
    private ArrayList<locationpojo> locationpojos_array;

    private StringRequest fetchcountry;
    public static final String TAG = "Client_CreateAccount";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.clients_createaccount);
        userid = String.valueOf(getIntent().getStringExtra(ConstantString.USERID));
        Log.d(TAG, "onCreate: logintimesaveData " + getIntent().getParcelableExtra("logintimesaveData")
                + " userid => " + userid);
        if (getIntent().getParcelableExtra("logintimesaveData") != null) {
            dataforsaveSharepreference = getIntent().getParcelableExtra("logintimesaveData");
        }

        user = (QBUser) getIntent().getSerializableExtra("QBuserdata");
        Log.e("checkuser", "onCreate: "+user );
        Log.d(TAG, "onCreate: starting -> " + userid);
        initfirebase();
        initview();

    }

    /**
     * initialize  view
     */
    private void initview() {

        currentCity = findViewById(R.id.city);
        currentState = findViewById(R.id.state);
        country = findViewById(R.id.country);
        profile_img = findViewById(R.id.profile);
        txtmale = findViewById(R.id.male);
        txtfemale = findViewById(R.id.female);
        gender = "male";
        bluetick = findViewById(R.id.tick);
        bluetick.setVisibility(View.GONE);
        createprogress = findViewById(R.id.progressbarcreate);
        createlayout = findViewById(R.id.scrollcreate);
        progresscreate = findViewById(R.id.progresscreate);
        msg = findViewById(R.id.msg);

        setLocation();
        progresscreate.setAnimation(R.raw.relax);


    }

    public void portfoliomale(View view) {
        gender = "male";
        txtmale.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        txtfemale.setTextColor(getResources().getColor(R.color.black));
        txtmale.setTextColor(getResources().getColor(R.color.white));
        txtfemale.setBackgroundColor(getResources().getColor(R.color.verylightgray3));

    }

    public void portfoliofemale(View view) {
        gender = "female";
        txtfemale.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        txtfemale.setTextColor(getResources().getColor(R.color.white));
        txtmale.setTextColor(getResources().getColor(R.color.black));

        txtmale.setBackgroundColor(getResources().getColor(R.color.verylightgray3));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initfirebase() {

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


    }


    private void setLocation() {

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "country setLocation: diable-> " + diable);
                countrypojo = new locationpojo();
                statepojo = new locationpojo();
                citrypojo = new locationpojo();

                callCountryList(ApiConstant.COUNTRY_LIST, 0, "Select Country");
                currentState.setText("");
                currentCity.setText("");
                country.setText("");


            }
        });

        currentState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statepojo = new locationpojo();
                citrypojo = new locationpojo();

                Log.d(TAG, " state setLocation: diable-> " + startdisable);
                if (countrypojo != null && !TextUtils.isEmpty(countrypojo.getLocation_name())) {

                    callCountryList(ApiConstant.STATE_LIST, 1, "Select State");
                    currentCity.setText("");
                    currentState.setText("");
//                        startdisable = 1;

                } else {
                    Toast.makeText(Clients_CreateAccount.this, R.string.errorselectcountry, Toast.LENGTH_SHORT).show();
                }

            }
        });


        currentCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, " city setLocation: diable-> " + citydisable);
                if (statepojo != null && !TextUtils.isEmpty(statepojo.getLocation_name())) {

//                            callCountryList();
                    callCountryList(ApiConstant.CITY_LIST, 2, "Select City");
                    currentCity.setText("");


                } else {
                    Toast.makeText(Clients_CreateAccount.this, R.string.errorselectstatefirst, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    /**
     * createButton click
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createportfolio(View view) {
//        String fetch_mobile = mobileno.getText().toString();
        String fetch_currentcity = currentCity.getText().toString();
        String fetch_currentstate = currentState.getText().toString();
        String fetch_country = country.getText().toString();
//        Log.d(TAG, "createportfolio: " + fetch_mobile);
        Log.d(TAG, "createportfolio: " + imageuri);

        if (isvalidate( fetch_currentcity,
                fetch_currentstate, fetch_country, imageuri)) {
            if (InternetAccess.isOnline(this)) {
                volleyPostRequest(userid, fetch_currentcity,
                        fetch_currentstate, fetch_country,
                        imageuri, gender);
            } else {
                showvaldationError(getResources().getString(R.string.errorinternet), R.raw.onboard);
            }
        }

    }

    /**
     * Image icon select
     */
    public void pickprofilepic(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


    /**
     * textfields validation check
     */
    private boolean isvalidate( String fcurrentcity,
                               String fcurrentstate, String fcountry, Uri imageuri) {

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
        if (TextUtils.isEmpty(fcountry)) {
            showvaldationError(getResources().getString(R.string.errorCurrentCounty), R.raw.questionmark);
            return false;
        }

        if (imageuri == null) {
            showvaldationError(getResources().getString(R.string.errorimagenotfound), R.raw.questionmark);
            return false;
        }


        return true;
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

            Intent intent = new Intent(this, Location_Avtivity.class);
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


    /**
     * getResult from
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: resultcode-> " + requestCode + " resultcode-> " + requestCode);
        if (resultCode != RESULT_OK) {
            Log.d(TAG, "onActivityResult: resultCode not ok");
            diable = 0;
            startdisable = 1;
            citydisable = 1;

            return;
        }
        if (requestCode == RESULT_LOAD_IMAGE) {
            if (data != null) {
                Uri ProfilePic = data.getData();
                Log.d(TAG, "onActivityResult: image uri " + ProfilePic);
                assert ProfilePic != null;
                imageuri = ProfilePic;
                Glide.with(this)
                        .load(ProfilePic)
                        .into(profile_img);
//                imageuri = ProfilePic.toString();
//                profile_img.setImageURI(ProfilePic);
                bluetick.setVisibility(View.VISIBLE);
            }
        } else if (requestCode == ConstantString.location_code) {
            Log.d(TAG, "onActivityResult: data-> " + data);
            if (data != null) {
                locationpojo cntry_data = data.getParcelableExtra("singlecountry");
                int dcsc = data.getIntExtra("csc", 0);
                Log.d(TAG, "onActivityResult: cntry_data-> " + cntry_data + "" +
                        " dcsc=> " + dcsc);


                if (cntry_data != null) {
                    if (dcsc == 1) {
                        statepojo = cntry_data;
                        currentState.setText(cntry_data.getLocation_name());
                        citydisable = 0;
                        startdisable = 0;

                    } else if (dcsc == 2) {
                        citrypojo = cntry_data;
                        currentCity.setText(cntry_data.getLocation_name());
                        citydisable = 0;
                    } else {
                        country.setText(cntry_data.getLocation_name());
                        countrypojo = cntry_data;
                        diable = 0;
                        startdisable = 0;
                        country.setClickable(true);

                    }

                }

            } else {
                Log.d(TAG, "onActivityResult: resultno selected data null");
            }
        }


    }

    /**
     * volley api call
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void volleyPostRequest(String userid, String currentCity, String currentState,
                                   String country, Uri imageuri, String fgender) {
        startprogress();
        Log.d(TAG, "volleyPostRequest: uri-> " + imageuri);

//           byte [] encodeByte=Base64.decode(imageuri, Base64.DEFAULT);
//        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//        Log.d(TAG, "volleyPostRequest: bitmap imageuri-> "+bitmap.toString());
        Bitmap originalImage = null;
        try {
            originalImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assert originalImage != null;
        originalImage.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
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
                final Uri[] downloadUrl = {null};
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        downloadUrl[0] = uri;
                        StringRequest portfoliorequest = new StringRequest(Request.Method.POST,
                                ApiConstant.CLIENT_PORTFOLIO,
                                (Response.Listener<String>) response -> {
                                    try {
                                        Log.d(TAG, "onSuccess: response==> " + response);
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                                            progresscreate.setAnimation(R.raw.dance_party);
                                            msg.setText(getResources().getText(R.string.createaccount));
//                                            Toast.makeText(Clients_CreateAccount.this, R.string.createaccount,
//                                                    Toast.LENGTH_SHORT).show();


                                            if (dataforsaveSharepreference != null) {
                                                SharedPreferanceManager.getInstance(getApplicationContext())
                                                        .userLogin(dataforsaveSharepreference);
                                            }

                                            SharedPreferanceManager.getInstance(Clients_CreateAccount.this)
                                                    .SaveProfilImage(imageuri.toString());
//                                            SharedPreferanceManager.getInstance(Clients_CreateAccount.this)
//                                                    .SaveProfilImage(imageuri);
                                            SharedPreferanceManager.getInstance(Clients_CreateAccount.this)
                                                    .saveQbUser(user);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent = new Intent(Clients_CreateAccount.this,
                                                            FetchAll_ClientData.class);
//                            Log.d(TAG, "setlogin:inside id-> "+succesobject.getInt(ConstantString.ID));
                                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            },1500);

//                                            showvaldationError(jsonObject.getString(ConstantString.RESPONSE_MESSAGE),R.raw.animation);



                                    } else{
                                        stopprogress();
                                        showvaldationError(jsonObject.getString(ConstantString.RESPONSE_MESSAGE), R.raw.onboard);
                                    }

                                } catch(JSONException e){
                            stopprogress();
                            e.printStackTrace();
                                        showvaldationError(e.getMessage(), R.raw.onboard);
                        }

                    },
                    error ->

                    {
                        stopprogress();
                        Log.d(TAG, "error " + error.getMessage() + " error to sting " + error.toString());
                        showvaldationError(error.getMessage(), R.raw.onboard);
                    })

                    {
                        @Override
                        protected Map<String, String> getParams () throws AuthFailureError {

                        HashMap<String, String> portfoliodata = new HashMap<>();
                        Log.e(TAG, "getParams: userid-> " + userid + "" +
                                "phoneno-> " + "null" + " country-> " + country + " currentstate-> " + currentState +
                                " currentcity -> " + currentCity + " imageurl-> " + uri);
                        portfoliodata.put(ConstantString.USERID, userid);


                        if (countrypojo != null && !countrypojo.getLocation_name().trim().toLowerCase().equals("null") ) {
                            portfoliodata.put(ConstantString.COUNTRY_NAME, countrypojo.getLocationid());
                        }

                        if (statepojo != null && !TextUtils.isEmpty(statepojo.getLocation_name())) {
                            portfoliodata.put(ConstantString.CURRENT_STATE, statepojo.getLocationid());
                        }

                        if (citrypojo != null  && !TextUtils.isEmpty(citrypojo.getLocation_name())) {
                            portfoliodata.put(ConstantString.CURRENT_CITY, citrypojo.getLocationid());
                        }


                        portfoliodata.put(ConstantString.GENDER, gender);

                        portfoliodata.put(ConstantString.PROFILE_IMG, uri.toString());

                        return portfoliodata;
                    }
                    }

                    ;
                        VolleySingleton.getInstance(Clients_CreateAccount .this).
                                addToRequestQueue(portfoliorequest);
                }
            }).

            addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure (@NonNull Exception e){
                    Log.d(TAG, "onFailure: success data");
                }
            });

                Log.d(TAG,"onSuccess: downloadurl-> "+downloadUrl[0]);


        }
    }).

    addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure (@NonNull Exception e){
            stopprogress();
            Log.d(TAG, "onFailure: e=> " + e.getMessage());
        }
    });


}


    private void showvaldationError(String msg, int errorimage) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg, errorimage);
        bottomSheet_for_error.setCancelable(false);
        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");
    }

    private void startprogress() {
        createprogress.setVisibility(View.VISIBLE);
        createlayout.setVisibility(View.GONE);
    }

    private void stopprogress() {
        createprogress.setVisibility(View.GONE);
        createlayout.setVisibility(View.VISIBLE);
    }

}


/*
  StringRequest portfoliorequest = new StringRequest(Request.Method.POST,
                                ApiConstant.CLIENT_PORTFOLIO,
                                (Response.Listener<String>) response ->{
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d(TAG, "onSuccess: response-> "+response);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                },
                                error -> {

                                }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                HashMap<String,String> portfoliodata = new HashMap<>();
                                portfoliodata.put(ConstantString.USERID,userid);
                                portfoliodata.put(ConstantString.PHONE_NO,mobilno);
                                portfoliodata.put(ConstantString.COUNTRY_NAME,country);
                                portfoliodata.put(ConstantString.CURRENT_STATE,currentState);
                                portfoliodata.put(ConstantString.CURRENT_CITY,currentCity);
                                portfoliodata.put(ConstantString.PROFILE_IMG,uri.toString());

                                return portfoliodata;
                            }
                        };
                        VolleySingleton.getInstance(Clients_CreateAccount.this).addToRequestQueue(portfoliorequest);
 */