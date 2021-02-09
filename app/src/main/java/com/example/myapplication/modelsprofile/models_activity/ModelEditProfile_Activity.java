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
import com.example.myapplication.Adapter.AdapterDataforimage;
import com.example.myapplication.Adapter.ModelsMultipleimageAdapter;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.Location_Avtivity;
import com.example.myapplication.R;
import com.example.myapplication.modelsprofile.models_selectoptions.Languages_selectActivity;
import com.example.myapplication.pojo.GetModelData;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.pojo.locationpojo;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.InternetAccess;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.regex.Pattern;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

// Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
public class ModelEditProfile_Activity extends AppCompatActivity {

    private int diable = 0;
    private int startdisable = 1;
    private int citydisable = 1;
    private int link_change= 0;
    private GetModelData getModelData;
    private String id;
    private EditText m_mobileno;
    private TextView m_currentCity;
    private TextView m_currentstate;
    private TextView m_country;
    private EditText m_age;
    private EditText m_profession;
    private EditText m_bio;
    private EditText m_pro_journey;
    private String TAG = "ModelsEditProfile_Activity";
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
    private RecyclerView imageholderRv, imageholdershow;
    private ArrayList<String> imageurlsList;
    private ArrayList<String> newimageutlList;
    private TextView maleclick;
    private TextView femaleclick;
    private TextView yespassport;
    private TextView nopassport;
    private TextView skillstxt;
    private TextView bodytypetxt;
    private TextView ethnicitytxt;
    private ModelsMultipleimageAdapter modelimageadapter;
    private ArrayList<String> langarray;
    private ArrayList<String> skillarray;
    private ArrayList<String> bodytypearray;
    private ArrayList<String> ethnicityarray;
    private String havepassport = "i have Passport";
    private Uri profileimageURI = null;
    private FloatingActionButton editbutton;

    private String imageuri;
    private ArrayList<String> arrayofimage;
    private ArrayList<String> newArraylistimage;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private View processmodelcreate;
    private LinearLayout modelcreateview;
    private AdapterDataforimage imageAdapter;
    private AdapterDataforimage newrecyclerAdapter;
    private ArrayList<String> Completearrayofimage;
    private StringRequest login_client_request;
    private GetModelData ModelData;
    private TextView msg_uploading;


    private locationpojo countrypojo;
    private locationpojo statepojo;
    private locationpojo citrypojo;

    private StringRequest fetchcountry;

    private LottieAlertDialog lottieAlertDialog;

    private LinearLayout languagelayout, skilllayout,
            bodytypelayout, ethnicitylayout;

    private ArrayList<locationpojo> locationpojos_array;


    @Override
    protected void onStop() {
        super.onStop();
        if (login_client_request != null) {
            login_client_request.cancel();

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.models_editprofile);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        initviews();
        Bundle data = getIntent().getExtras();
        assert data != null;
        if (data.getParcelable("modelData") != null) {
            getModelData = data.getParcelable("modelData");
            assert getModelData != null;
            id = getModelData.getUser_id();
            countrypojo = getModelData.getCountry_pojo();
            statepojo = getModelData.getState_pojo();
            citrypojo = getModelData.getCity_pojo();
            setData(getModelData);
        }


        initfirebase();
        newrecyclerAdapter = new AdapterDataforimage(this, new ArrayList<>());

    }

    private void initviews() {
        newArraylistimage = new ArrayList<>();
        arrayofimage = new ArrayList<>();
        Completearrayofimage = new ArrayList<>();
        m_mobileno = findViewById(R.id.mobile);
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
        imageholdershow = findViewById(R.id.rv_uploaded);
        editbutton = findViewById(R.id.edit);
        msg_uploading = findViewById(R.id.msg);

        languagelayout = findViewById(R.id.languagelayout);
        skilllayout = findViewById(R.id.skilllayout);
        bodytypelayout = findViewById(R.id.bodytypelayout);
        ethnicitylayout = findViewById(R.id.ethnicitylayout);

//        tick.setVisibility(View.GONE);
        setLocation();

    }

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
                    Toast.makeText(ModelEditProfile_Activity.this, R.string.errorselectcountry, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ModelEditProfile_Activity.this, R.string.errorselectstatefirst, Toast.LENGTH_SHORT).show();
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

            Intent intent = new Intent(ModelEditProfile_Activity.this,
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


    public void back(View view) {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }


    @SuppressLint("SetTextI18n")
    private void setData(GetModelData modelData) {
        Log.e(TAG, "\n\n\n\n\n\n\n\nsetData: dataset-> for edit "+modelData+"\n\n\n\n\n\n" );

        Log.e("editdeatail", "setData: ---> "+modelData.getBody_bio() );
        if (modelData.getGender().toLowerCase().equals("male")) {
            maleselect();
        } else {
            femaleselect();
        }

        if (ihavepassport.toLowerCase().equals(ihavepassport)) {
            yespassport_select();
        } else {
            nopassword_select();
        }


        imageholderRv.setVisibility(View.GONE);
        msg_uploading.setText(getResources().getText(R.string.Updatingyouprofile));
        langarray = modelData.getLanguage();
        skillarray = modelData.getSkill();
        bodytypearray = modelData.getBody_type();
        ethnicityarray = modelData.getEthnicity();

        Log.d(TAG, "setData: response -> " + modelData.toString());
        Log.d(TAG, "setData: modelData-> chest-> " + modelData.getChest() +
                " modelData -> waist-> " + modelData.getWaist() +
                " modelData-> hip-> " + modelData.getHip());
        if (modelData.getChest() != null && !modelData.getChest().equals("") && !modelData.getChest().toLowerCase().equals("null")) {
            m_chest.setText(modelData.getChest());
        } else {
            m_chest.setHint("NA");
        }


        if (modelData.getWaist() != null && !modelData.getWaist().equals("") && !modelData.getWaist().toLowerCase().equals("null")) {
            m_bodywaist.setText(modelData.getWaist());
        } else {
            m_bodywaist.setHint("NA");
        }

        if (modelData.getHip() != null && !modelData.getHip().equals("") && !modelData.getHip().toLowerCase().equals("null")) {
            m_bodyhip.setText(modelData.getHip());
        } else {
            m_bodyhip.setHint("NA");
        }

//        profileimageURI = Uri.parse(modelData.getProfile_img());
//        profileimageURI = Uri.parse(modelData.getProfile_img());
        imageurlsList = modelData.getHd_images();
        imageuri = modelData.getProfile_img();
        profileimageURI = Uri.parse(modelData.getProfile_img());
        Log.d(TAG, "setData: imageurlsList-> " + imageurlsList +
                " modelDataimage-> " + modelData.getHd_images());
        Log.d(TAG, "setData: profileimg" + profileimg + " imageprofile-> " + modelData.getProfile_img());

        Glide.with(this)
                .load(modelData.getProfile_img()).error(R.mipmap.ic_launcher_r)
                .into(profileimg);
        LinearLayoutManager HorizontalLayout;
        HorizontalLayout
                = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false);

        imageAdapter = new AdapterDataforimage(this, imageurlsList);
        imageholdershow.setLayoutManager(HorizontalLayout);

        imageholdershow.setAdapter(imageAdapter);
        m_mobileno.setText(modelData.getMobile());
        m_currentCity.setText(citrypojo.getLocation_name());
        m_currentstate.setText(statepojo.getLocation_name());
        m_country.setText(countrypojo.getLocation_name());

        Log.d(TAG, "setData: size langauge-> " + modelData.getLanguage().size());
        if (modelData.getLanguage().size() > 1) {
            languagestxtview.setText(modelData.getLanguage().get(0) + " and others ");
        } else {
            languagestxtview.setText(modelData.getLanguage().get(0));
        }


        m_age.setText(modelData.getAge());
        m_profession.setText(modelData.getProffesion());
        if(modelData.getPersonal_bio()!=null && !modelData.getPersonal_bio().equalsIgnoreCase(" ")
        && !modelData.getPersonal_bio().equalsIgnoreCase("null")){
            m_bio.setText(modelData.getPersonal_bio());
        }
        else{
            m_bio.setHint("NA");
        }

        m_pro_journey.setText(modelData.getPersonal_journey());


        if (modelData.getSkill().size() > 1) {
            skillstxt.setText(modelData.getSkill().get(0) + " and others ");

        } else {
            skillstxt.setText(modelData.getSkill().get(0));
        }

        minage.setText(modelData.getRole_age_min());
        maxage.setText(modelData.getRole_age_max());
        m_feet.setText(modelData.getHeight_feet());
        m_feetinches.setText(modelData.getHeight_feet());

        if (modelData.getBody_type().size() != 0) {
            if (modelData.getBody_type().size() > 1) {
                m_bodytype.setText(modelData.getBody_type().get(0) + " and others ");
            } else {
                m_bodytype.setText(modelData.getBody_type().get(0));
            }
        }


        Log.d(TAG, "setData: ethnicitysize is " + modelData.getEthnicity().size() + " ethnicity is " + modelData.getEthnicity());
        if (modelData.getEthnicity().size() != 0) {
            if (modelData.getEthnicity().size() > 1) {
                m_ethnicitytv.setText(modelData.getEthnicity().get(0) + " and others ");
            } else  {
                m_ethnicitytv.setText(modelData.getEthnicity().get(0));
            }
        } else {
            m_ethnicitytv.setText("Select Ethnicity");
        }

        if(modelData.getVideo_url() !=null &&
                !modelData.getVideo_url().equalsIgnoreCase("")
        && !modelData.getVideo_url().equalsIgnoreCase("null")){
            youtubeurl.setText(modelData.getVideo_url());
        }
        else{
            youtubeurl.setHint("NA");
        }

        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), ConstantString.PICK_IMAGE_SINGLE);
            }
        });

    }

    /**
     * onclick funtions
     */
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
        intent.putExtra("data", data);
        if (skillarray != null && !skillarray.isEmpty()) {
            intent.putExtra("checkdata", skillarray);
        }
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

    public void yespassport(View view) {
        yespassport_select();
    }

    private void yespassport_select() {
        havepassport = "i have Passport";
        yespassport.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        nopassport.setBackgroundColor(getResources().getColor(R.color.verylightgray3));
        nopassport.setTextColor(getResources().getColor(R.color.black));
        yespassport.setTextColor(getResources().getColor(R.color.background_color_selected_user_item));
    }


    public void nopassport(View view) {

        nopassword_select();
    }

    private void nopassword_select() {
        havepassport = "i donot have Passport";
        nopassport.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        yespassport.setBackgroundColor(getResources().getColor(R.color.verylightgray3));
        yespassport.setTextColor(getResources().getColor(R.color.black));
        nopassport.setTextColor(getResources().getColor(R.color.background_color_selected_user_item));
    }

    public void updateportfolio(View view) {
        String f_mobile = m_mobileno.getText().toString();
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


        if (isValidate(f_mobile, f_city, f_state, f_county, f_age,
                f_profession, f_bio, f_pro_journey, f_minage, f_maxage,
                f_feet, f_feetinches, f_bodywaist, f_bodyhip, f_chest, yturl)) {
            if (InternetAccess.isOnline(this)) {
                arrayofimage.clear();
                setlogin(f_mobile, f_city, f_state, f_county, f_age,
                        f_profession, f_bio, f_pro_journey, f_minage, f_maxage,
                        f_feet, f_feetinches, f_bodywaist, f_bodyhip, f_chest, yturl
                );
            } else {
                showvaldationError(getResources().getString(R.string.errorinternet), R.raw.onboard);
            }
        }
    }


    /**
     * end onclick
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: return code-> " + requestCode + " resultcode " + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantString.LANGCODE) {
                languagelayout.setBackgroundResource(R.color.verylightgray2);
                Log.d(TAG, "onActivityResult: language");
                assert data != null;
                langarray = (ArrayList<String>) data.getSerializableExtra("senddata");
                if (!langarray.isEmpty()) {
                    languagelayout.setBackgroundResource(0);
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
                skilllayout.setBackgroundResource(R.color.verylightgray2);
                skillarray = (ArrayList<String>) data.getSerializableExtra("senddata");
                assert skillarray != null;
                if (!skillarray.isEmpty()) {
                    skilllayout.setBackgroundResource(0);
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
                bodytypearray = (ArrayList<String>) data.getSerializableExtra("senddata");
                assert bodytypearray != null;
                if (!bodytypearray.isEmpty()) {
                    bodytypelayout.setBackgroundResource(0);
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
                if (!ethnicityarray.isEmpty()) {
                    ethnicitylayout.setBackgroundResource(0);
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
                Log.d(TAG, "onActivityResult: data.getClipData-> " + data.getClipData());
                if (data.getClipData() != null) {
                    ClipData multipleclip = data.getClipData();
                    Log.d(TAG, "onActivityResult: multipleclipsize -> " + multipleclip.getItemCount());

                    for (int i = 0; i < multipleclip.getItemCount(); i++) {
                        ClipData.Item item = multipleclip.getItemAt(i);
                        Log.d(TAG, "onActivityResult: " + item.getUri());
//                        imageurlsList.add(item.getUri().toString());
                        newArraylistimage.add(item.getUri().toString());

                    }


//                    imageAdapter.notifyDataSetChanged();
                } else {

                    Uri ProfilePic = data.getData();
                    Log.d(TAG, "onActivityResult: single image pic in multiple image-> "
                            + ProfilePic.toString());
                    newArraylistimage.add(ProfilePic.toString());

                }
                if (newArraylistimage.size() > 0) {
                    imageholderRv.setVisibility(View.VISIBLE);
                    newrecyclerAdapter = new AdapterDataforimage(this, newArraylistimage);
                    imageholderRv.setAdapter(newrecyclerAdapter);

                    LinearLayoutManager HorizontalLayout;
                    HorizontalLayout
                            = new LinearLayoutManager(
                            this,
                            LinearLayoutManager.HORIZONTAL,
                            false);
                    imageholderRv.setLayoutManager(HorizontalLayout);
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
                        link_change =1;
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

    private void firebaseurlconverter(Uri imageuri, String f_mobile,
                                      String f_city, String f_state, String f_county, String f_age,
                                      String f_profession, String f_bio, String f_pro_journey, String f_minage,
                                      String f_maxage, String f_feet, String f_feetinches, String f_bodywaist, String f_bodyhip, String f_chest, String yturl) {
        Log.e(TAG, "firebaseurlconverter: single convert uri-> " + imageuri);
//        Bitmap originalImage = null;
//        try {
//            originalImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        assert originalImage != null;
//        originalImage.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
//        byte[] data = outputStream.toByteArray();
        Calendar calendar = Calendar.getInstance();
        //Returns current time in millis
        long timeMilli2 = calendar.getTimeInMillis();
        StorageReference reference = storageReference.child("profile")
                .child("image_" + String.valueOf(timeMilli2) + ".jpg");

        if(link_change == 1) {


            reference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "onSuccess: second");
                    Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override

                        public void onSuccess(Uri uri) {
                            Log.d(TAG, "onSuccess: profile image uri-> " + uri);
                            storedata(uri.toString());
//                            sendvollyrequest(f_mobile, f_city, f_state, f_county, f_age,
//                                    f_profession, f_bio, f_pro_journey, f_minage, f_maxage,
//                                    f_feet, f_feetinches, f_bodywaist, f_bodyhip, f_chest,
//                                    yturl);
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
        else{
            sendvollyrequest(f_mobile, f_city, f_state, f_county, f_age,
                    f_profession, f_bio, f_pro_journey, f_minage, f_maxage,
                    f_feet, f_feetinches, f_bodywaist, f_bodyhip, f_chest,
                    yturl);
        }
    }

    public void storedata(String imagesend) {
        Log.e(TAG, "storedata: imageurl-> " + imagesend);
        imageuri = imagesend;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initfirebase() {

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


    }


    private boolean isValidate(String c_mobile, String c_city, String c_state, String c_county,
                               String c_age, String c_profession, String c_bio, String c_pro_journey,
                               String c_minage, String c_maxage, String c_feet, String c_feetinches,
                               String c_bodywaist, String c_bodyhip, String c_chest, String c_ytburl) {
        Log.d(TAG, "sushil edit ->\n isValidate:id=> " + id + " \n c_mobile-> " + c_mobile + " \n language-> " +
                langarray + " \n skill-> " + skillarray + " \n imageurlList-> "
                + imageurlsList + "\n profileimage-> " + " \n c_city-> " + c_city + " \n c_state-> " +
                c_state + " \n c_country-> " + c_county + " \n c_age-> " + c_age + " \n c_profession-> " + c_profession
                + "\n c_bio " + c_bio + " \n c_pro_jour-> " + c_pro_journey + " \n c_minage-> " + c_minage
                + "\n c_maxage-> " + c_maxage + " \n c_feet-> " + c_feet + " c_feetinc-> " + c_feetinches
                + "\n c_bodywait -> " + c_bodywaist + " c_bodyhip-> " + c_bodyhip + " c_chest-> " + c_chest +
                "\n c_ytube -> " + c_ytburl + " \n");


        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        Pattern compiledPattern = Pattern.compile(pattern);

        boolean isValid = compiledPattern.matcher(c_ytburl).matches();

        if (profileimageURI == null || profileimageURI.toString().equals("")) {
            showvaldationError(getResources().getString(R.string.errorimagenotfound), R.raw.onboard);
            return false;
        }


        Log.e(TAG, "isValidate: imagesize " + imageurlsList.size() + " image set " + newArraylistimage.size());
        if (imageurlsList.size() + newArraylistimage.size() < 5) {
            showvaldationError(getResources().getString(R.string.errormultipleimag), R.raw.onboard);
            return false;
        }


        if (c_mobile.isEmpty()) {
            m_mobileno.setError(getResources().getString(R.string.errorphoneno));
            m_mobileno.requestFocus();
//            showvaldationError(getResources().getString(R.string.errorphoneno), R.raw.questionmark);
            return false;
        }
        if (!android.util.Patterns.PHONE.matcher(c_mobile).matches()) {
            m_mobileno.setError(getResources().getString(R.string.errorphonenovalidation));
            m_mobileno.requestFocus();
            return false;
        }
        if (c_county.isEmpty() || countrypojo == null) {
            m_country.setError(getResources().getString(R.string.errorCurrentCounty));
            m_country.requestFocus();
//            showvaldationError(getResources().getString(R.string.errorCurrentCounty), R.raw.onboard);
            return false;
        }

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
            minage.setError(getResources().getString(R.string.erroridealageisreq));
            minage.requestFocus();
//            showvaldationError(getResources().getString(R.string.errormimage), R.raw.onboard);
            return false;
        }

        if (Integer.parseInt(c_minage.trim()) < 6) {

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

        if (Integer.parseInt(c_maxage.trim()) < 6) {
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

        if (Integer.parseInt(c_feet.trim()) <= 0) {
            m_feet.requestFocus();
            m_feet.setError(getResources().getString(R.string.errorfeetzero));
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
//        }

//        if (bodytypearray.size() <= 0) {
//            bodytypelayout.setBackgroundResource(R.drawable.redstrok_invalid);
//            bodytypelayout.requestFocus();
//            if(bodytypelayout.isFocused()){
//                showvaldationError(getResources().getString(R.string.errorjobtype), R.raw.onboard);
//
//            }
//            return false;
//        }

//        if (ethnicityarray.size() <= 0) {
//            ethnicitylayout.setBackgroundResource(R.drawable.redstrok_invalid);
//            ethnicitylayout.requestFocus();
//            if(ethnicitylayout.isFocused()){
//                showvaldationError(getResources().getString(R.string.errorethinicity), R.raw.onboard);
//            }
//            return false;
//        }

//        if (c_ytburl.isEmpty()) {
//            youtubeurl.setError(getResources().getString(R.string.errorsyoutubeurl));
//            youtubeurl.requestFocus();
////            showvaldationError(getResources().getString(R.string.errorsyoutubeurl), R.raw.onboard);
//            return false;
//        }
//        if (!isValid) {
//            youtubeurl.setError(getResources().getString(R.string.erroryoutubeurl_invalid));
//            youtubeurl.requestFocus();
//            return false;
//        }


        return true;
    }


    public void setlogin(String f_mobile, String f_city, String f_state, String f_county, String f_age,
                         String f_profession, String f_bio, String f_pro_journey, String f_minage, String f_maxage,
                         String f_feet, String f_feetinches, String f_bodywaist, String f_bodyhip, String f_chest,
                         String yturl) {


        Log.d(TAG, "setlogin: startprogress--> profileiomageUri-> " + profileimageURI);
        startprogress();
        int count = 0;

        Log.d(TAG, "setlogin: imageurlList-> " + imageurlsList + " imageuri fetch -> " + imageuri);
        if (profileimageURI == null) {
            imageuri = getModelData.getProfile_img();
            Log.d(TAG, "setlogin: if null imageuri-> " + imageuri);
        }


        Log.d(TAG, "setlogin: newrecyclerAdapter-> " + newrecyclerAdapter.getItemCount());
        if (newrecyclerAdapter.getItemCount() > 0) {
            Log.e(TAG, "setlogin: newrecclerAdapter.getlist not null " + newrecyclerAdapter.getList());
//            imageurlsList = newrecyclerAdapter.getList();

            newArraylistimage = newrecyclerAdapter.getList();
            for (int i = 0; i < newArraylistimage.size(); i++) {
//                msg_uploading.setText("Uploading new images ["+i+"/"+newArraylistimage.size()+"]");
//                Log.d(TAG, "setlogin:data->  " + !newArraylistimage.contains(imageurlsList.get(i)));
//                Bitmap originalImage = null;
//                try {
////                    Log.d(TAG, "setlogin: imageuri-> " + newArraylistimage.get(i));
//                    originalImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(newArraylistimage.get(i)));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                assert originalImage != null;
//                originalImage.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
//                byte[] data = outputStream.toByteArray();
                Calendar calendar = Calendar.getInstance();
                //Returns current time in millis
                long timeMilli2 = calendar.getTimeInMillis();
                Log.d(TAG, "setlogin: profileimage url data-> " + profileimageURI);

                StorageReference riversRef = storageReference.child("profile")
                        .child("image_" + String.valueOf(timeMilli2) + ".jpg");

                riversRef.putFile(Uri.parse(newArraylistimage.get(i))).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: cooo-> ");
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d(TAG, "onSuccess: uri-> " + uri.toString());
                                arrayofimage.add(uri.toString());
                                Log.d("firesushil", "onSuccess: arryofimage size-> " + arrayofimage.size() +
                                        "imageurlslist -> " + newArraylistimage.size());
                                if (arrayofimage.size() == newArraylistimage.size()) {
                                    firebaseurlconverter(profileimageURI, f_mobile, f_city, f_state, f_county, f_age,
                                            f_profession, f_bio, f_pro_journey, f_minage, f_maxage,
                                            f_feet, f_feetinches, f_bodywaist, f_bodyhip, f_chest,
                                            yturl);

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.getMessage());
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

        } else {
            Log.d("firesushil", "setlogin: newrecclerAdapter.getlist  null " + newrecyclerAdapter.getList());
            firebaseurlconverter(profileimageURI, f_mobile,
                    f_city, f_state, f_county, f_age, f_profession, f_bio,
                    f_pro_journey, f_minage, f_maxage, f_feet, f_feetinches, f_bodywaist, f_bodyhip, f_chest, yturl);
        }

    }

    private void sendvollyrequest(String f_mobile, String f_city, String f_state, String f_county, String f_age,
                                  String f_profession, String f_bio, String f_pro_journey, String f_minage, String f_maxage,
                                  String f_feet, String f_feetinches, String f_bodywaist, String f_bodyhip, String f_chest,
                                  String yturl) {

        StringRequest loginstringRequest = new StringRequest(Request.Method.POST,
                ApiConstant.MODEL_PORTFOLIO,
                (Response.Listener<String>) response -> {
                    Log.d(TAG, "setlogin: response=> " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {

//                            stopprogress();
////                            Toast.makeText(this, jsonObject.getString(ConstantString.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
//                            showvaldationError(jsonObject.getString(ConstantString.RESPONSE_MESSAGE), R.raw.successnew);
                            initCallApi(jsonObject.getString(ConstantString.RESPONSE_MESSAGE));

                        } else {
                            stopprogress();
                            showvaldationError(jsonObject.getString(ConstantString.RESPONSE_MESSAGE), R.raw.onboard);

                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                },
                error -> {
                    stopprogress();
                    Log.d(TAG, "setlogin: error-> " + error.getMessage());
                    showvaldationError(error.toString(), R.raw.onboard);
                    if (error instanceof NetworkError) {
                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                    } else if (error instanceof ParseError) {
                    } else if (error instanceof NoConnectionError) {
                    } else if (error instanceof TimeoutError) {
                        showvaldationError("Oops. Timeout error!", R.raw.onboard);
                    } else {
                        showvaldationError(error.toString(), R.raw.onboard);
                    }
                    finish();
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Gson gson = new Gson();
                Gson imagegson = new GsonBuilder().disableHtmlEscaping().create();

                Log.d(TAG, "getParams:\n id-> " + id + "\n phoneno-> " + f_mobile +
                        "\n city-> " + f_city + " \n state=> " + f_state + "\n f_country-> " + f_county +
                        "\n age-> " + f_age + " \n profession-> " + f_profession + "\n bio => " + f_bio +
                        "\n projourney=> " + f_pro_journey + "\n minage=>  " + f_minage +
                        "\n maxage = > " + f_maxage + "\n feet=> " + f_feet + "\n  inch-> " + f_feetinches +
                        "\n youtube url-> " + yturl + " profile image -> " + imageuri +
                        "\n arrayimage-> " + gson.toJson(arrayofimage) + "\n langarray -> " + gson.toJson(langarray) +
                        "\n skillarray-> " + gson.toJson(skillarray) + "\n ethnicityaray-> " + gson.toJson(ethnicityarray) +
                        " \n bodyarray-> " + gson.toJson(bodytypearray) + "\n gender-> " + gender + "\n passport-> " + havepassport);

                HashMap<String, String> loginparam = new HashMap<>();
                if (statepojo != null) {
                    loginparam.put(ConstantString.CURRENT_STATE, statepojo.getLocationid());
                }
                if (countrypojo != null) {
                    loginparam.put(ConstantString.COUNTRY_NAME, countrypojo.getLocationid());
                }

                if (citrypojo != null) {
                    loginparam.put(ConstantString.CURRENT_CITY, citrypojo.getLocationid());
                }
                loginparam.put(ConstantString.USERID, id);
                loginparam.put(ConstantString.PHONE_NO, f_mobile);
                loginparam.put(ConstantString.AGE, f_age);
                loginparam.put(ConstantString.PROFESSION, f_profession);
                loginparam.put(ConstantString.GENERMODEL, gender);
                loginparam.put(ConstantString.PASSPORT, havepassport);
                loginparam.put(ConstantString.APP_BIO, f_bio);
                loginparam.put(ConstantString.PRO_JOURNEY, f_pro_journey);
                loginparam.put(ConstantString.MINIMUMAGE, f_minage);

                loginparam.put(ConstantString.MAXAGE, f_maxage);
                loginparam.put(ConstantString.HEIGHTFEET, f_feet);
                loginparam.put(ConstantString.HEIGHTINCH, f_feetinches);
                loginparam.put(ConstantString.VIDEOURL, yturl);
                loginparam.put(ConstantString.PROFILE_IMG, imageuri);
                loginparam.put(ConstantString.WEIGHT, "23");
                loginparam.put(ConstantString.DETAILBIO, f_bio);
                loginparam.put(ConstantString.HOURLY_RATE, "500");
                loginparam.put(ConstantString.BODY_WAIST, f_bodywaist);
                loginparam.put(ConstantString.BODY_HIP, f_bodyhip);
                loginparam.put(ConstantString.BODY_CHEST, f_chest);
                loginparam.put(ConstantString.APPEAR_PROF, "jlkafsdjlkasjdflkasjhdfl");


                if (imageAdapter != null && imageAdapter.getItemCount() > 0) {
                    Completearrayofimage = imageAdapter.getList();
                    if (arrayofimage.size() > 0) {
                        Completearrayofimage.addAll(arrayofimage);
                    }

                } else if (arrayofimage.size() > 0) {
                    Completearrayofimage.addAll(arrayofimage);
                }

                loginparam.put(ConstantString.IMAGEHD, imagegson.toJson(Completearrayofimage));
                loginparam.put(ConstantString.LANGUAGE, gson.toJson(langarray));
                loginparam.put(ConstantString.SKILL, gson.toJson(skillarray));
                loginparam.put(ConstantString.ETHINICITY, gson.toJson(ethnicityarray));
                loginparam.put(ConstantString.BODY_TYPE, gson.toJson(bodytypearray));


                Log.d(TAG, "getParams: parameter-> " + loginparam);


                return loginparam;
            }
        };
        VolleySingleton.getInstance(ModelEditProfile_Activity.this).addToRequestQueue(loginstringRequest);
    }


    private void initCallApi(String successmessage) {

        Log.d(TAG, "initCallApi: called");
        login_client_request = new StringRequest(Request.Method.GET,
                ApiConstant.MODEL_GET_PROFILE,
                (Response.Listener<String>) response -> {
                    Log.d(TAG, "initCallApi: \n\n\n response-> " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                            JSONObject modeldetail = jsonObject.getJSONObject(ConstantString.DETAIL_TAG);

                            ArrayList<String> languagelist = new ArrayList<>();
                            JSONArray langaray = modeldetail.getJSONArray(ConstantString.GET_TALENT_LANGUAGE);
                            for (int i = 0; i < langaray.length(); i++) {
                                languagelist.add(langaray.get(i).toString());
                            }

                            ArrayList<String> skilllist = new ArrayList<>();
                            JSONArray skillarray = modeldetail.getJSONArray(ConstantString.GET_TALENT_SKILL);
                            for (int i = 0; i < skillarray.length(); i++) {
                                skilllist.add(skillarray.get(i).toString());
                            }

                            ArrayList<String> bodytype = new ArrayList<>();
                            JSONArray bodyaray = modeldetail.getJSONArray(ConstantString.GET_TALENT_BODY_TYPE);
                            for (int i = 0; i < bodyaray.length(); i++) {
                                bodytype.add(bodyaray.get(i).toString());
                            }

                            ArrayList<String> ethicitytype = new ArrayList<>();
                            JSONArray etharay = modeldetail.getJSONArray(ConstantString.GET_TALENT_ETHNICITY);

                            for (int i = 0; i < etharay.length(); i++) {
                                ethicitytype.add(etharay.get(i).toString());
                            }

                            ArrayList<String> imagetype = new ArrayList<>();
                            JSONArray imagehd = modeldetail.getJSONArray(ConstantString.GET_TALENT_HD_IMAGE);
                            for (int i = 0; i < imagehd.length(); i++) {
                                imagetype.add(imagehd.get(i).toString());
                            }


                            ModelData = new GetModelData(
                                    modeldetail.getString(ConstantString.USERID),
                                    modeldetail.getString(ConstantString.NAME),
                                    modeldetail.getString(ConstantString.EMAIL),
                                    modeldetail.getString(ConstantString.GET_TALENT_MOBILE),
                                    modeldetail.getString(ConstantString.GET_TALENT_AGE),
                                    modeldetail.getString(ConstantString.GET_TALENT_PROFFESION),
                                    modeldetail.getString(ConstantString.GENDER),
                                    modeldetail.getString(ConstantString.GET_TALENT_PASSPORT),
                                    modeldetail.getString(ConstantString.GET_TALENT_PERSONALBIO),
                                    modeldetail.getString(ConstantString.GET_TALENT_PERSONAL_JOURNEY),
                                    modeldetail.getString(ConstantString.GET_TALENT_HOULY_RATE),
                                    modeldetail.getString(ConstantString.GET_TALENT_ROLE_AGEMIN),
                                    modeldetail.getString(ConstantString.GET_TALENT_ROLE_AGEMAX),
                                    modeldetail.getString(ConstantString.GET_TALENT_HEIGHT_FEET),
                                    modeldetail.getString(ConstantString.GET_TALENT_HEIGHT_INC),
                                    skilllist,
                                    imagetype,
                                    modeldetail.getString(ConstantString.PROFILE_IMG),
                                    modeldetail.getString(ConstantString.GET_TALENT_WEIGHT),
                                    modeldetail.getString(ConstantString.GET_TALENT_VIDEO_URL),
                                    languagelist,
                                    bodytype,
                                    ethicitytype,
                                    modeldetail.getString(ConstantString.GET_TALENT_CREATEAT),
                                    modeldetail.getString(ConstantString.GET_TALENT_UPDATEDAT)
                            );

                            ModelData.setWaist(modeldetail.getString(ConstantString.GET_TAlENT_WAIST));
                            ModelData.setHip(modeldetail.getString(ConstantString.GET_TALENT_HIP));
                            ModelData.setChest(modeldetail.getString(ConstantString.GET_TALENT_CHEST));

                            ModelData.setCountry_pojo(convertlocationpojo(modeldetail.getJSONObject(ConstantString.GET_TALENT_COUNTRY)));
                            ModelData.setState_pojo(convertlocationpojo(modeldetail.getJSONObject(ConstantString.GET_TALENT_STATE)));
                            ModelData.setCity_pojo(convertlocationpojo(modeldetail.getJSONObject(ConstantString.GET_TALENT_CITY)));


                            Log.e(TAG, "initCallApi: modelData-> " + modeldetail.getString(ConstantString.PROFILE_IMG));
                            SharedPreferanceManager.getInstance(this).clear_model_previoudDetail();
                            Gson modelgson = new Gson();
                            SharedPreferanceManager.getInstance(this)
                                    .save_model_userInformation(modelgson.toJson(ModelData));
                            changetoActivity(successmessage);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                eror -> {
                    Log.d(TAG, "initCallApi: error-> " + eror.toString());

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(ModelEditProfile_Activity.this).getUserData();
                Log.d(TAG, "getHeaders: token value-> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(login_client_request);

    }


    private void changetoActivity(String successmessage) {


//Toast.makeText(this, jsonObject.getString(ConstantString.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
//        showvaldationError(successmessage, R.raw.successnew);
        Toast.makeText(this, "Portfolio updated successfully", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }},1000);

//        Intent intent = new Intent(this, Model_HomeActivity.class);
//        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//        startActivity(intent);
    }

    private void showvaldationError(String msg, int errorimage) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg, "", errorimage);
        bottomSheet_for_error.setCancelable(false);
//        bottomSheet_for_error.setlottiimage(errorimage);

        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");
    }

    private void showvaldationError2(String msg, int errorimage, String btnmsg) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg, btnmsg, errorimage);
        bottomSheet_for_error.setCancelable(false);

        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");
    }

    private void startprogress() {

        processmodelcreate.setVisibility(View.VISIBLE);
//        modelcreateview.setVisibility(View.GONE);
    }

    private void stopprogress() {
//        modelcreateview.setVisibility(View.VISIBLE);
        processmodelcreate.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }
}
