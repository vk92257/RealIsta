package com.example.myapplication.modelsprofile.models_activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.myapplication.Adapter.ModelsMultipleimageAdapter;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.Location_Avtivity;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_activity.Clients_CreateAccount;
import com.example.myapplication.modelsprofile.models_selectoptions.Languages_selectActivity;
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
import com.google.gson.GsonBuilder;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.quickblox.users.model.QBUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Models_createportfolioActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 5000;

    private int diable = 0;
    private int startdisable = 1;
    private int citydisable = 1;
    private String id;
    private TextView m_currentCity;
    private TextView m_currentstate;
    private TextView m_country;
    private EditText m_age;
    private EditText m_profession;
    private EditText m_bio;
    private EditText m_pro_journey;
    private String TAG = "Models_createportfolioActivity";
    private String gender = "Male";
    private String ihavepassport = "No passport";
    private EditText minage;
    private EditText maxage;
    private EditText m_feet;
    private EditText m_feetinches;
    private EditText m_bodywaist;
    private EditText m_bodyhip;
    private EditText m_chest;
    private EditText youtubeurl;
    private TextView m_bodytype;
    private TextView m_ethnicitytv;
    private TextView languagestxtview;
    private ImageView tick, profileimg;
    private RecyclerView imageholderRv;
    private List<Uri> imageurlsList;
    private TextView maleclick;
    private TextView femaleclick;
    private TextView yespassport;
    private TextView nopassport;
    private TextView skillstxt;
    private TextView bodytypetxt;
    private TextView ethnicitytxt;
    private ModelsMultipleimageAdapter modelimageadapter;
    private ArrayList<String> langarray;
    private ArrayList<String> langarraycheck;
    private ArrayList<String> skillarray;
    private ArrayList<String> bodytypearray;
    private ArrayList<String> ethnicityarray;
    private String havepassport = "i have Passport";
    private Uri profileimageURI = null;
    private String imageuri;
    private ArrayList<String> arrayofimage;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private View processmodelcreate;
    private LinearLayout modelcreateview;
    private LoginTimesaveData loginTimesaveData;
    private TextView msg;




    private StringRequest fetchcountry;

    private LottieAnimationView progresscreate;

    private locationpojo countrypojo;
    private locationpojo statepojo;
    private locationpojo citrypojo;

    private LottieAlertDialog lottieAlertDialog;
    private LinearLayout languagelayout,skilllayout;


    private ArrayList<locationpojo> locationpojos_array;

    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.models_createportfolio);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();


//        id = String.valueOf(getIntent().getStringExtra(ConstantString.USERID));
            id = getIntent().getStringExtra(ConstantString.USERID);

        if (getIntent().getParcelableExtra("Modellogindata") != null) {
            Log.d(TAG, "onCreate: dataEnter sharedpreference-> ");
            loginTimesaveData = getIntent().getParcelableExtra("Modellogindata");
        }

        initviews();
//        Log.e("checkuserModel", "onCreate: "+user );
        progresscreate.setAnimation(R.raw.profile_changeing);
        Log.d(TAG, "onCreate: id==> " + id);
        initfirebase();
    }

    /**
     * initilize views
     */
    private void initviews() {
        arrayofimage = new ArrayList<>();
        skillarray = new ArrayList<>();
        langarray = new ArrayList<>();
        bodytypearray = new ArrayList<>();
        ethnicityarray = new ArrayList<>();
        imageurlsList = new ArrayList<>();
        langarraycheck = new ArrayList<>();

        m_currentCity = findViewById(R.id.city);
        m_currentstate = findViewById(R.id.state);
        m_country = findViewById(R.id.country);
        m_age = findViewById(R.id.age);
        m_profession = findViewById(R.id.professionaltitle1);
        m_bio = findViewById(R.id.bio);
        m_pro_journey = findViewById(R.id.professionaljourney);
        minage = findViewById(R.id.minage);
        maxage = findViewById(R.id.maxage);
        m_feet = findViewById(R.id.feet);
        tick = findViewById(R.id.tick);
        m_feetinches = findViewById(R.id.inches);
        m_bodywaist = findViewById(R.id.waist);
        m_bodyhip = findViewById(R.id.hip);
        m_chest = findViewById(R.id.chest);
        youtubeurl = findViewById(R.id.youtubevideo);
        m_bodytype = findViewById(R.id.bodytypetv);
        m_ethnicitytv = findViewById(R.id.ethnicitytv);
        imageholderRv = findViewById(R.id.rv);
        profileimg = findViewById(R.id.profile);
        languagestxtview = findViewById(R.id.languagestv);
        skillstxt = findViewById(R.id.skillstv);
        maleclick = findViewById(R.id.male);
        femaleclick = findViewById(R.id.female);
        yespassport = findViewById(R.id.yespassport);
        nopassport = findViewById(R.id.nopassport);
        processmodelcreate = findViewById(R.id.progModelCreate);
        modelcreateview = findViewById(R.id.putmodeldetail);
        msg = findViewById(R.id.msg);
        progresscreate = findViewById(R.id.progresscreate);

        tick.setVisibility(View.GONE);
        languagelayout = findViewById(R.id.languagelayout);
        skilllayout = findViewById(R.id.skilllayout);
        setLocation();
    }


    private void setLocation() {

        m_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "country setLocation: diable-> " + diable);
                countrypojo = new locationpojo();
                statepojo = new locationpojo();
                citrypojo = new locationpojo();

                callCountryList(ApiConstant.COUNTRY_LIST, 0, "Select Country");
                m_currentstate.setText("");
                m_currentCity.setText("");
                m_country.setText("");


            }
        });

        m_currentstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statepojo = new locationpojo();
                citrypojo = new locationpojo();

                Log.d(TAG, " state setLocation: diable-> " + startdisable);
                if (countrypojo != null && !TextUtils.isEmpty(countrypojo.getLocation_name())) {

                    callCountryList(ApiConstant.STATE_LIST, 1, "Select State");
                    m_currentCity.setText("");
                    m_currentstate.setText("");
//                        startdisable = 1;

                } else {
                    Toast.makeText(Models_createportfolioActivity.this, R.string.errorselectcountry, Toast.LENGTH_SHORT).show();
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
                    m_currentCity.setText("");
                    citydisable = 1;

                } else {
                    Toast.makeText(Models_createportfolioActivity.this, R.string.errorselectstatefirst, Toast.LENGTH_SHORT).show();
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
                    showvaldationError(error.toString());
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

            Intent intent = new Intent(Models_createportfolioActivity.this,
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


    public void languagesselect(View view) {
        Intent intent = new Intent(this, Languages_selectActivity.class);
        String[] data = getResources().getStringArray(R.array.language);
        Log.d(TAG, "languagesselect: data string -> " + Arrays.toString(data));
        if (langarray != null && !langarray.isEmpty()) {
            intent.putExtra("checkdata", langarray);
        }
        intent.putExtra("data", data);
        intent.putExtra(ConstantString.CODETEXT, ConstantString.LANGCODE);
        intent.putExtra(ConstantString.SELECT_TOP_TXT, "Language");
        startActivityForResult(intent, ConstantString.LANGCODE);

    }

    public void skillsselect(View view) {
        Intent intent = new Intent(this, Languages_selectActivity.class);
        String[] data = getResources().getStringArray(R.array.skill);
        Log.d(TAG, "languagesselect: data string -> " + Arrays.toString(data));
        if (skillarray != null && !skillarray.isEmpty()) {
            intent.putExtra("checkdata", skillarray);
        }
        intent.putExtra("data", data);
        intent.putExtra(ConstantString.CODETEXT, ConstantString.SKILLCODE);
        intent.putExtra(ConstantString.SELECT_TOP_TXT, "skill");
        startActivityForResult(intent, ConstantString.SKILLCODE);
    }

    public void bodytype(View view) {
        Intent intent = new Intent(this, Languages_selectActivity.class);
        String[] data = getResources().getStringArray(R.array.bodytype);
        Log.d(TAG, "languagesselect: data string -> " + Arrays.toString(data));
        if (bodytypearray != null && !bodytypearray.isEmpty()) {
            intent.putExtra("checkdata", bodytypearray);
        }
        intent.putExtra("data", data);
        intent.putExtra(ConstantString.CODETEXT, ConstantString.BODYTYPRCODE);
        intent.putExtra(ConstantString.SELECT_TOP_TXT, "Body Type");
        startActivityForResult(intent, ConstantString.BODYTYPRCODE);
    }

    public void ethicityclicked(View view) {
        Intent intent = new Intent(this, Languages_selectActivity.class);
        String[] data = getResources().getStringArray(R.array.Ethnicity);
        Log.d(TAG, "languagesselect: data string -> " + Arrays.toString(data));
        if (ethnicityarray != null && !ethnicityarray.isEmpty()) {
            intent.putExtra("checkdata", ethnicityarray);
        }
        intent.putExtra("data", data);
        intent.putExtra(ConstantString.CODETEXT, ConstantString.ETHNICITYCODE);
        intent.putExtra(ConstantString.SELECT_TOP_TXT, "Ethnicity");
        startActivityForResult(intent, ConstantString.ETHNICITYCODE);
    }

    public void emptyllclicked(View view) {
        LinearLayoutManager HorizontalLayout;
        HorizontalLayout
                = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false);
        imageholderRv.setLayoutManager(HorizontalLayout);
        modelimageadapter = new ModelsMultipleimageAdapter((ArrayList<Uri>) imageurlsList);
        imageholderRv.setAdapter(modelimageadapter);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), ConstantString.PICK_IMAGE_MULTIPLE);
    }

    public void pickprofilepic(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), ConstantString.PICK_IMAGE_SINGLE);
    }

    public void male(View view) {
        gender = "Male";
        maleclick.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        maleclick.setTextColor(getResources().getColor(R.color.white));
        femaleclick.setTextColor(getResources().getColor(R.color.black));
        femaleclick.setBackgroundColor(getResources().getColor(R.color.background_color_selected_user_item));
    }

    public void female(View view) {
        gender = "Female";
        femaleclick.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        femaleclick.setTextColor(getResources().getColor(R.color.white));
        maleclick.setTextColor(getResources().getColor(R.color.black));
        maleclick.setBackgroundColor(getResources().getColor(R.color.background_color_selected_user_item));
    }

    public void yespassport(View view) {
        havepassport = "I have Passport";
        yespassport.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        yespassport.setTextColor(getResources().getColor(R.color.white));
        nopassport.setTextColor(getResources().getColor(R.color.black));
        nopassport.setBackgroundColor(getResources().getColor(R.color.verylightgray3));
    }

    public void nopassport(View view) {
        havepassport = "I donot have Passport";
        nopassport.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        nopassport.setTextColor(getResources().getColor(R.color.white));
        yespassport.setTextColor(getResources().getColor(R.color.black));
        yespassport.setBackgroundColor(getResources().getColor(R.color.verylightgray3));
    }

    /**
     * button click's
     */

    public void createportfolio(View view) {

        String f_city = m_currentCity.getText().toString();
        String f_state = m_currentstate.getText().toString();
        String f_county = m_country.getText().toString();
        String f_age = m_age.getText().toString();
        String f_profession = m_profession.getText().toString();
        String f_bio = m_bio.getText().toString();
        String f_pro_journey = m_pro_journey.getText().toString();
        String f_minage = minage.getText().toString();
        String f_maxage = maxage.getText().toString();
        String f_feet = m_feet.getText().toString();
        String f_feetinches = m_feetinches.getText().toString();
        String f_bodywaist = m_bodywaist.getText().toString();
        String f_bodyhip = m_bodyhip.getText().toString();
        String f_chest = m_chest.getText().toString();
        String yturl = youtubeurl.getText().toString();


        if (isValidate(f_city, f_state, f_county, f_age,
                f_profession, f_bio, f_pro_journey, f_minage, f_maxage,
                f_feet, f_feetinches, f_bodywaist, f_bodyhip, f_chest, yturl)) {
            if (InternetAccess.isOnline(this)) {
                arrayofimage.clear();
                setlogin( f_city, f_state, f_county, f_age,
                        f_profession, f_bio, f_pro_journey, f_minage, f_maxage,
                        f_feet, f_feetinches, f_bodywaist, f_bodyhip, f_chest, yturl
                );
            } else {
                showvaldationError(getResources().getString(R.string.errorinternet), R.raw.no_internet);
            }
        }

    }

    private boolean isValidate( String c_city, String c_state, String c_county,
                               String c_age, String c_profession, String c_bio, String c_pro_journey,
                               String c_minage, String c_maxage, String c_feet, String c_feetinches,
                               String c_bodywaist, String c_bodyhip, String c_chest, String c_ytburl) {

        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        Pattern compiledPattern = Pattern.compile(pattern);

        boolean isValid = compiledPattern.matcher(c_ytburl).matches();


        if (c_county.isEmpty() || countrypojo == null) {

            m_country.setBackgroundResource(R.drawable.redstrok_invalid);
            m_country.requestFocus();
            if (m_country.isFocused()) {
                showvaldationError(getResources().getString(R.string.errorCurrentCounty), R.raw.onboard);

            }
//            showvaldationError(getResources().getString(R.string.errorCurrentCounty), R.raw.onboard);
            return false;
        }

//        if (c_state.isEmpty()) {
//            m_currentstate.setError(getResources().getString(R.string.errorCurrentState));
//            m_currentstate.requestFocus();
////            showvaldationError(getResources().getString(R.string.errorCurrentState), R.raw.questionmark);
//            return false;
//        }

//        if (c_city.isEmpty()) {
//            m_currentCity.setError(getResources().getString(R.string.errorCurrentCity));
//            m_currentCity.requestFocus();
////            showvaldationError(getResources().getString(R.string.errorCurrentCity), R.raw.questionmark);
//            return false;
//        }
        if (langarray.size() <= 0) {
            languagelayout.setBackgroundResource(R.drawable.redstrok_invalid);
            languagelayout.requestFocus();
            if (languagelayout.isFocused()) {
                showvaldationError(getResources().getString(R.string.errolanguage), R.raw.onboard);

            }
            return false;
        }

        if (c_age.isEmpty()) {
            m_age.setError(getResources().getString(R.string.errorage));
            m_age.requestFocus();
//            showvaldationError(getResources().getString(R.string.errorage), R.raw.onboard);
            return false;
        }
        if (Integer.parseInt(c_age) < 6) {
            m_age.setError(getResources().getString(R.string.erroragelimit));
            m_age.requestFocus();

            return false;
        }
        if (c_profession.isEmpty()) {
            m_profession.setError(getResources().getString(R.string.errorprofession));
            m_profession.requestFocus();
//            showvaldationError(getResources().getString(R.string.errorprofession), R.raw.onboard);
            return false;
        }

        if (c_bio.isEmpty()) {
            m_bio.setError(getResources().getString(R.string.errorbio));
            m_bio.requestFocus();
//            showvaldationError(getResources().getString(R.string.errorbio), R.raw.onboard);
            return false;
        }

        if (c_pro_journey.isEmpty()) {
            m_pro_journey.setError(getResources().getString(R.string.errorprojourney));
            m_pro_journey.requestFocus();
//            showvaldationError(getResources().getString(R.string.errorprojourney), R.raw.onboard);
            return false;
        }

        if (skillarray.size() <= 0) {
            skilllayout.setBackgroundResource(R.drawable.redstrok_invalid);
            skilllayout.requestFocus();
            if (skilllayout.isFocused()) {
                showvaldationError(getResources().getString(R.string.errorskills), R.raw.onboard);

            }
            return false;
        }

        if (c_minage.isEmpty()) {
            minage.setError(getResources().getString(R.string.errormimage));
            minage.requestFocus();
//            showvaldationError(getResources().getString(R.string.errormimage), R.raw.onboard);
            return false;
        }
        if(Integer.parseInt(c_minage.trim())<6){

            minage.requestFocus();
            minage.setError(getResources().getString(R.string.erroragelimit));
            return false;
        }

        if (c_maxage.isEmpty()) {
            maxage.setError(getResources().getString(R.string.errormaxage));
            maxage.requestFocus();
//            showvaldationError(getResources().getString(R.string.errormaxage), R.raw.onboard);
            return false;
        }

        if (Integer.parseInt(c_maxage.trim())<6) {
            maxage.requestFocus();
            maxage.setError(getResources().getString(R.string.erroragelimit));
//            showvaldationError(, R.raw.onboard);
            return false;
        }



        if (c_feet.isEmpty() && c_feetinches.isEmpty()) {
            m_feet.requestFocus();
            m_feet.setError(getResources().getString(R.string.errorheightmeasurement));
            showvaldationError(getResources().getString(R.string.erroheightinfeet), R.raw.onboard);
            return false;
        }
        if (Integer.parseInt(c_feet.trim()) <=0 ) {
            m_feet.requestFocus();
            m_feet.setError(getResources().getString(R.string.errorfeetzero));
//            showvaldationError(getResources().getString(R.string.errorheightmeasurement), R.raw.onboard);
            return false;
        }

        if (Integer.parseInt(c_feetinches.trim()) >12 ) {
            m_feet.requestFocus();
            m_feet.setError(getResources().getString(R.string.errorfeetinch));
//            showvaldationError(getResources().getString(R.string.errorheightmeasurement), R.raw.onboard);
            return false;
        }
//        if (c_bodywaist.isEmpty()) {
//            showvaldationError(getResources().getString(R.string.errorphoneno), R.raw.onboard);
//            return false;
//        }
//        if (c_bodyhip.isEmpty()) {
//            showvaldationError(getResources().getString(R.string.errorphoneno), R.raw.onboard);
//            return false;
//        }
//        if (c_chest.isEmpty()) {
//            showvaldationError(getResources().getString(R.string.errorphoneno), R.raw.onboard);
//            return false;
////        }
//        if (c_ytburl.isEmpty()) {
//            youtubeurl.setError(getResources().getString(R.string.errorsyoutubeurl));
//            youtubeurl.requestFocus();
////            showvaldationError(getResources().getString(R.string.errorsyoutubeurl), R.raw.onboard);
//            return false;
//        }
        if (!TextUtils.isEmpty(c_ytburl)&& !isValid) {
            youtubeurl.setError(getResources().getString(R.string.erroryoutubeurl_invalid));
            youtubeurl.requestFocus();
            return false;
        }





        if (imageurlsList.size() < 5) {
            showvaldationError(getResources().getString(R.string.errormultipleimag), R.raw.onboard);
            return false;
        }


        if (profileimageURI == null || profileimageURI.toString().equals("")) {
            showvaldationError(getResources().getString(R.string.errorimagenotfound), R.raw.onboard);
            return false;
        }

        return true;
    }

    public void setlogin( String f_city, String f_state, String f_county, String f_age,
                         String f_profession, String f_bio, String f_pro_journey, String f_minage, String f_maxage,
                         String f_feet, String f_feetinches, String f_bodywaist, String f_bodyhip, String f_chest,
                         String yturl) {

        startprogress();
        int count = 0;
        imageurlsList = modelimageadapter.getlist();
        firebaseurlconverter(profileimageURI);

        int i;
        for (i = 0; i < imageurlsList.size(); i++) {
            Log.d(TAG, "setlogin: " + i);

//            if(imageurlsList.size() > 0){
//                msg.setText("Creating your beautiful portfolio.. ["+i+"/"+ imageurlsList.size()+"]");
//            }
//            else{
//                msg.setText("Creating your beautiful portfolio.. ");
//            }
//
//            Bitmap originalImage = null;
//            try {
//                originalImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageurlsList.get(i));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            assert originalImage != null;
//            originalImage.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
//            byte[] data = outputStream.toByteArray();
            Calendar calendar = Calendar.getInstance();
            //Returns current time in millis
            long timeMilli2 = calendar.getTimeInMillis();
            Log.d(TAG, "setlogin: profileimage url data-> " + profileimageURI);

            StorageReference riversRef = storageReference.child("profile")
                    .child("image_" + String.valueOf(timeMilli2) + ".jpg");

            riversRef.putFile(imageurlsList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "onSuccess: cooo-> ");
                    final Uri[] downloadUrl = {null};


                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(TAG, "onSuccess: uri-> " + uri.toString());
                            arrayofimage.add(uri.toString());
//                            downloadUrl[0] = uri;
                            msg.setText("Creating your beautiful portfolio.. [" + arrayofimage.size() + "/" + (imageurlsList.size()) + "]");

                            if (arrayofimage.size() == imageurlsList.size()) {


                                StringRequest loginstringRequest = new StringRequest(Request.Method.POST,
                                        ApiConstant.MODEL_PORTFOLIO,
                                        (Response.Listener<String>) response -> {
                                            Log.d(TAG, "setlogin: response=> " + response);
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {

                                                    if (loginTimesaveData != null) {
                                                        SharedPreferanceManager.getInstance(Models_createportfolioActivity.this)
                                                                .userLogin(loginTimesaveData);
                                                    }
                                                    SharedPreferanceManager.getInstance(Models_createportfolioActivity.this)
                                                            .SaveProfilImage(imageuri);

//                                                    SharedPreferanceManager.getInstance(Models_createportfolioActivity.this)
//                                                            .saveQbUser(user);

//                                                    Log.e("user00", "onSuccess: "+user );
                                                    progresscreate.setAnimation(R.raw.thumsup);
                                                    msg.setText("Your portfolio has been created successfully");


                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            changetoActivity();

                                                        }
                                                    }, SPLASH_TIME_OUT);
//

//                                                    showvaldationError(jsonObject.getString(ConstantString.RESPONSE_MESSAGE));
                                                } else {
                                                    stopprogress();
                                                    showvaldationError(jsonObject.getString(ConstantString.RESPONSE_MESSAGE));
                                                }
                                            } catch (JSONException e) {

                                                e.printStackTrace();
                                            }

                                        },
                                        error -> {
                                            stopprogress();
                                            Log.d(TAG, "setlogin: error-> " + error.getMessage());
                                            showvaldationError(error.toString());
                                            if (error instanceof NetworkError) {
                                            } else if (error instanceof ServerError) {
                                            } else if (error instanceof AuthFailureError) {
                                            } else if (error instanceof ParseError) {
                                            } else if (error instanceof NoConnectionError) {
                                            } else if (error instanceof TimeoutError) {
                                                showvaldationError("Oops. Timeout error!");
                                            } else {
                                                showvaldationError(error.toString());
                                            }
                                        }) {

                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {


                                        Gson gson = new Gson();
                                        Gson imagegson = new GsonBuilder().disableHtmlEscaping().create();

                                        Log.d(TAG, "getParams:\n id-> " + id + "\n phoneno-> " + "null " +
                                                "\n city-> " + f_city + " \n state=> " + f_state + "\n f_country-> " + f_county +
                                                "\n age-> " + f_age + " \n profession-> " + f_profession + "\n bio => " + f_bio +
                                                "\n projourney=> " + f_pro_journey + "\n minage=>  " + f_minage +
                                                "\n maxage = > " + f_maxage + "\n feet=> " + f_feet + "\n  inch-> " + f_feetinches +
                                                "\n youtube url-> " + yturl + " profile image -> " + imageuri +
                                                "\n arrayimage-> " + gson.toJson(arrayofimage) + "\n langarray -> " + gson.toJson(langarray) +
                                                "\n skillarray-> " + gson.toJson(skillarray) + "\n ethnicityaray-> " + gson.toJson(ethnicityarray) +
                                                " \n bodyarray-> " + gson.toJson(bodytypearray) + "\n gender-> " + gender + "\n passport-> " + havepassport);

                                        HashMap<String, String> loginparam = new HashMap<>();
                                        loginparam.put(ConstantString.USERID, id);


                                        Log.d(TAG, "getParams: statepojo-> "+statepojo);
                                        if (statepojo != null && statepojo.getLocationid() != null) {
                                            loginparam.put(ConstantString.CURRENT_STATE, statepojo.getLocationid());
                                        }
                                        if (countrypojo != null && countrypojo.getLocationid() != null) {
                                            loginparam.put(ConstantString.COUNTRY_NAME, countrypojo.getLocationid());
                                        }

                                        if (citrypojo != null && citrypojo.getLocationid() != null) {
                                            loginparam.put(ConstantString.CURRENT_CITY, citrypojo.getLocationid());
                                        }

                                        if(!yturl.isEmpty()){
                                            loginparam.put(ConstantString.VIDEOURL, yturl);
                                        }

                                        loginparam.put(ConstantString.AGE, f_age);
                                        loginparam.put(ConstantString.PROFESSION, f_profession);
                                        loginparam.put(ConstantString.GENERMODEL, gender);
                                        loginparam.put(ConstantString.PASSPORT, havepassport);
                                        loginparam.put(ConstantString.PRO_JOURNEY, f_pro_journey);
                                        loginparam.put(ConstantString.MINIMUMAGE, f_minage);

                                        loginparam.put(ConstantString.MAXAGE, f_maxage);
                                        loginparam.put(ConstantString.HEIGHTFEET, f_feet);
                                        loginparam.put(ConstantString.HEIGHTINCH, f_feetinches);

                                        loginparam.put(ConstantString.PROFILE_IMG, imageuri);



                                        loginparam.put(ConstantString.BODY_WAIST, f_bodywaist);
                                        loginparam.put(ConstantString.BODY_HIP, f_bodyhip);
                                        loginparam.put(ConstantString.BODY_CHEST, f_chest);


                                        loginparam.put(ConstantString.IMAGEHD, imagegson.toJson(arrayofimage));
                                        loginparam.put(ConstantString.LANGUAGE, gson.toJson(langarray));
                                        loginparam.put(ConstantString.SKILL, gson.toJson(skillarray));
                                        loginparam.put(ConstantString.ETHINICITY, gson.toJson(ethnicityarray));
                                        loginparam.put(ConstantString.BODY_TYPE, gson.toJson(bodytypearray));


                                        Log.d(TAG, "getParams: parameter-> " + loginparam);


                                        return loginparam;
                                    }
                                };


                                VolleySingleton.getInstance(Models_createportfolioActivity.this).addToRequestQueue(loginstringRequest);


                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    Log.d(TAG, "onSuccess: arrayofimage-> " + arrayofimage.size() +
                            " imageurlslist-> " + imageurlsList.size());
//                    storedata(downloadUrl.toString());


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
            });

        }
    }


    private void showvaldationError(String msg) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg);
        bottomSheet_for_error.setCancelable(false);
        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");


    }

    /**
     * end button click's
     */

    private void showvaldationError(String msg, int errorimage) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg, errorimage);
        bottomSheet_for_error.setCancelable(false);
        bottomSheet_for_error.setlottiimage(errorimage);
        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: return code-> " + requestCode + " resultcode " + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantString.LANGCODE) {
                Log.d(TAG, "onActivityResult: language");
                assert data != null;
                langarray.clear();
                langarray = (ArrayList<String>) data.getSerializableExtra("senddata");
                if (!langarray.isEmpty()) {
                    languagelayout.setBackgroundResource(R.color.verylightgray);
                    if (langarray.size() > 1) {
                        languagestxtview.setText(langarray.get(0) + " and others");
                    } else {
                        languagestxtview.setText(langarray.get(0));
                    }

                } else {
                    languagestxtview.setText("Select Language");
                }


            } else if (requestCode == ConstantString.SKILLCODE) {
                Log.d(TAG, "onActivityResult: skill");
                skillarray = (ArrayList<String>) data.getSerializableExtra("senddata");
                assert skillarray != null;
                if (!skillarray.isEmpty()) {
                    skilllayout.setBackgroundResource(R.color.verylightgray2);
                    if (skillarray.size() > 1) {
                        skillstxt.setText(skillarray.get(0) + " and others");
                    } else {
                        skillstxt.setText(skillarray.get(0));
                    }

                } else {
                    skillstxt.setText("Select skill");
                }
            } else if (requestCode == ConstantString.BODYTYPRCODE) {
                Log.d(TAG, "onActivityResult: bodytype");

                langarraycheck.clear();
                bodytypearray = (ArrayList<String>) data.getSerializableExtra("senddata");
                langarraycheck = bodytypearray;
                assert bodytypearray != null;
                if (!bodytypearray.isEmpty()) {
                    if (bodytypearray.size() > 1) {
                        m_bodytype.setText(bodytypearray.get(0) + " and others");
                    } else {
                        m_bodytype.setText(bodytypearray.get(0));
                    }

                } else {
                    m_bodytype.setText("Select Body Type");
                }
            } else if (requestCode == ConstantString.ETHNICITYCODE) {
                Log.d(TAG, "onActivityResult: ethnicitycode");
                ethnicityarray = (ArrayList<String>) data.getSerializableExtra("senddata");
                assert ethnicityarray != null;
                Log.d(TAG, "onActivityResult:ethnicityarray->  " + ethnicityarray);
                if (!ethnicityarray.isEmpty()) {
                    if (ethnicityarray.size() > 1) {
                        m_ethnicitytv.setText(ethnicityarray.get(0) + " and others");
                    } else {
                        m_ethnicitytv.setText(ethnicityarray.get(0));
                    }

                } else {
                    m_ethnicitytv.setText("Select Ethnicity");
                }
            } else if (requestCode == ConstantString.PICK_IMAGE_MULTIPLE) {
                assert data != null;
                if (data.getClipData() != null) {
                    ClipData multipleclip = data.getClipData();
                    Log.d(TAG, "onActivityResult: " + multipleclip.getItemCount());
                    for (int i = 0; i < multipleclip.getItemCount(); i++) {
                        ClipData.Item item = multipleclip.getItemAt(i);
                        Log.d(TAG, "onActivityResult: " + item.getUri());
                        imageurlsList.add(item.getUri());
                    }

                } else {
                    Uri Profilepic = data.getData();
                    imageurlsList.add(Profilepic);
                }
                modelimageadapter.notifyDataSetChanged();
            } else if (requestCode == ConstantString.PICK_IMAGE_SINGLE) {
                if (data != null) {
                    Uri ProfilePic = data.getData();
                    Log.d(TAG, "onActivityResult: image uri " + ProfilePic);
                    tick.setVisibility(View.VISIBLE);
                    profileimageURI = ProfilePic;
                    Glide.with(this)
                            .load(ProfilePic)
                            .into(profileimg);
//                    profileimg.setImageURI(ProfilePic);
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
                            m_currentstate.setText(cntry_data.getLocation_name());
                            citydisable = 0;
                            startdisable = 0;

                        } else if (dcsc == 2) {
                            citrypojo = cntry_data;
                            m_currentCity.setText(cntry_data.getLocation_name());
                            citydisable = 0;
                        } else {
                            m_country.setText(cntry_data.getLocation_name());
                            countrypojo = cntry_data;
                            diable = 0;
                            startdisable = 0;
                            m_country.setClickable(true);
                            m_country.setBackgroundResource(R.color.verylightgray2);;

                        }

                    }

                }
            }
        } else {
            Log.d(TAG, "onActivityResult: out side not click -------");
//            countrypojo = null;
//            statepojo = null;
//            citrypojo = null;
            diable = 0;
            startdisable = 1;
            citydisable = 1;
//            diable = 0;
//            if(startdisable == 0){
//                startdisable = 1;
//            }
//            else{
//                startdisable = 0;
//            }
//
//            if( citydisable == 0){
//                citydisable = 1;
//            }
//            else{
//                citydisable = 1;
//            }

        }
    }


    /**
     * firebase
     */

    private void firebaseurlconverter(Uri imageuri) {
        Bitmap originalImage = null;
        try {
            originalImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assert originalImage != null;
        originalImage.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        byte[] data = outputStream.toByteArray();
        Calendar calendar = Calendar.getInstance();
        //Returns current time in millis
        long timeMilli2 = calendar.getTimeInMillis();
        StorageReference reference = storageReference.child("profile")
                .child("image_" + String.valueOf(timeMilli2) + ".jpg");

        reference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: second");
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override

                    public void onSuccess(Uri uri) {

                        storedata(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                })
                ;

//                imageuri = downloadUrl.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    private void changetoActivity() {
//        Toast.makeText(this,"Please login... ",Toast.LENGTH_SHORT).show();
//        finish();
//        Intent intent = new Intent(this,Model_HomeActivity.class);
        Intent intent = new Intent(this, FetchAll_ModelData.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void storedata(String imagesend) {
        imageuri = imagesend;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initfirebase() {

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }


    private void startprogress() {

        processmodelcreate.setVisibility(View.VISIBLE);
        modelcreateview.setVisibility(View.GONE);
    }

    private void stopprogress() {
        modelcreateview.setVisibility(View.VISIBLE);
        processmodelcreate.setVisibility(View.GONE);
    }

}
















