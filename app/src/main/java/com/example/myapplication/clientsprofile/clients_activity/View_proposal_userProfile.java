package com.example.myapplication.clientsprofile.clients_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.myapplication.Adapter.GalleryImageView;
import com.example.myapplication.Adapter.GalleryViewAdapter;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.Interface.ImageSelectfull;
import com.example.myapplication.modelsprofile.models_activity.GalleryImageFullshow;
import com.example.myapplication.modelsprofile.models_activity.ModelEditProfile_Activity;
import com.example.myapplication.pojo.GetModelData;
import com.example.myapplication.pojo.ImageViewSinglefullsize;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.BottomSheettl;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class View_proposal_userProfile extends AppCompatActivity implements  ImageSelectfull {

    private int profileGallery;

    private CircularImageView profileimage;
    private TextView fullname;
    private TextView professionltitle;
    private TextView location;
    private RecyclerView rv_recyelrview;
    private TextView pro_journey;
    private TextView bio;
    private TextView idelageforrole;
    private TextView skillText;
    private TextView languagetxt;
    private TextView bodyApperancetxt;
    private TextView heighttxt;
    private TextView chesttxt;
    private TextView waisttxt;
    private TextView hiptxt;
    private TextView ethnicitytxt;
    private TextView editdata;
    private GetModelData ModelData;
    private GalleryViewAdapter imageadapter;
    private ImageView menuoptions;
    private LinearLayout galleryview;
    private TextView gallerytxt;
    private View underlinegalleryview;
    private LinearLayout portfolioview;
    private TextView portfoliotxt;
    private LinearLayout portfolioll;
    private LinearLayout galleryll;
    private View underlineportfolioview;
    private StringRequest login_client_request;
    private RecyclerView gallery_Rv;
    private ShimmerFrameLayout  shimmer;
    private LinearLayout rootLayout;
    private static final String TAG = "View_proposal_userProfile";
    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onStart() {
        super.onStart();
        initCallApi(getIntent().getStringExtra("user_id"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.client_see_talent_detail);
        intiview();
        protfolioviewopen();
        galleryview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryopen();
            }
        });
        portfolioview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                protfolioviewopen();
            }
        });
    }

    public void back(View view){
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
    private void intiview() {
        shimmer = findViewById(R.id.shimmer_view_container);
        rootLayout = findViewById(R.id.root_layout);
        menuoptions = findViewById(R.id.optionsmenu);
        fullname = findViewById(R.id.name);
        profileimage = findViewById(R.id.profileimage);
        professionltitle = findViewById(R.id.professiontitle);
        location =findViewById(R.id.location);
        rv_recyelrview = findViewById(R.id.rv_horizontal);
        pro_journey = findViewById(R.id.professionaljourney);
        bio = findViewById(R.id.bio);
        idelageforrole = findViewById(R.id.idealageforrolemodel);
        skillText = findViewById(R.id.skills);
        languagetxt = findViewById(R.id.languages);
        bodyApperancetxt = findViewById(R.id.bodyappearance);
        heighttxt = findViewById(R.id.height);
        chesttxt = findViewById(R.id.chest);
        waisttxt = findViewById(R.id.waist);
        hiptxt = findViewById(R.id.hip);
        ethnicitytxt = findViewById(R.id.ethnicity);
        galleryview = findViewById(R.id.galleryview);
        gallerytxt = findViewById(R.id.gallerytv);
        underlinegalleryview = findViewById(R.id.underlinegalleryview);
        portfolioview = findViewById(R.id.portfolioview);
        portfoliotxt = findViewById(R.id.portfoliotv);
        underlineportfolioview = findViewById(R.id.underlineportfolioview);
        portfolioll = findViewById(R.id.portfolioll);
        galleryll = findViewById(R.id.galleryll);
        gallery_Rv = findViewById(R.id.rv_images);
        findViewById(R.id.galleryview);
        findViewById(R.id.edit).setVisibility(View.GONE);

    }

    @SuppressLint("SetTextI18n")
    private void setViews(GetModelData modelData) {
        Log.d(TAG, "setViews: get in setviews -> ");
        GridLayoutManager manager = new GridLayoutManager(this,
                2, GridLayoutManager.VERTICAL, false);

//        AdapterDataforimage adapterDataforimage =
//                new AdapterDataforimage(getContext(),ModelData.getHd_images());
        GalleryImageView adapterDataforimage = new GalleryImageView(modelData.getHd_images(),this);
        adapterDataforimage.SetonimageclickListener(this);
//        adapterDataforimage.setHideImage();
        gallery_Rv.setAdapter(adapterDataforimage);
        gallery_Rv.setLayoutManager(manager);
        Log.d(TAG, "setViews: modelData.getChest-> "+modelData.getChest());
        if (modelData.getChest() != null && !modelData.getChest().equals("")  && !modelData.getChest().toLowerCase().equals("null") ){
            chesttxt.setText(modelData.getChest()+" inches");
        }
        else{
            chesttxt.setText("NA");
        }
        if (modelData.getWaist() != null && !modelData.getWaist().equals("")  && !modelData.getWaist().toLowerCase().equals("null") ) {
            waisttxt.setText(modelData.getWaist()+" inches");
        }
        else{
            waisttxt.setText("NA");
        }

        if (modelData.getHip() != null && !modelData.getHip().equals("")  && !modelData.getHip().toLowerCase().equals("null") ) {
            hiptxt.setText(modelData.getHip()+" inches");
        }else{
            hiptxt.setText("NA");
        }

        String name = modelData.getName();
        fullname.setText(name.trim().substring(0,1).toUpperCase()+name.trim().substring(1));

        Glide.with(getApplicationContext())
                .load(modelData.getProfile_img())
                .error(R.mipmap.ic_launcher_r)
                .into(profileimage);

        String proff = modelData.getProffesion();
        professionltitle.setText(proff.trim().substring(0,1)+proff.substring(1) + " | " + modelData.getGender());


           if (modelData.getState_pojo() != null &&!modelData.getState_pojo().getLocation_name().equals("null")) {
               location.setText(modelData.getState() + " | " + modelData.getCountry());
           } else {
               location.setText(modelData.getCountry());
           }

        pro_journey.setText(modelData.getPersonal_journey());
        bio.setText(modelData.getPersonal_bio());
        idelageforrole.setText(modelData.getRole_age_min() + "yrs - " + modelData.getRole_age_max() + "yrs");
        heighttxt.setText(modelData.getHeight_feet() + "ft " + modelData.getHeight_inches() + " inches");
        languagetxt.setText(jsonarrayconvert(modelData.getLanguage()));
        bodyApperancetxt.setText(jsonarrayconvert(modelData.getBody_type()));
        skillText.setText(jsonarrayconvert(modelData.getSkill()));
        ethnicitytxt.setText(jsonarrayconvert(modelData.getEthnicity()));

        imageadapter = new GalleryViewAdapter(modelData.getHd_images(),this);
        imageadapter.SetonimageclickListener(this::onclickimage);
//        imageadapter.setHideImage();
        LinearLayoutManager HorizontalLayout;
        HorizontalLayout
                = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false);
        rv_recyelrview.setLayoutManager(HorizontalLayout);
        rv_recyelrview.setAdapter(imageadapter);
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
        rootLayout.setVisibility(View.VISIBLE);

    }
    public void protfolioviewopen() {
        Log.d(TAG, "protfolioviewopen: ");
        portfolioll.setVisibility(View.VISIBLE);
        underlinegalleryview.setVisibility(View.GONE);
        underlineportfolioview.setVisibility(View.VISIBLE);
        galleryll.setVisibility(View.GONE);
        gallerytxt.setTextColor(getResources().getColor(R.color.gray1));
        portfoliotxt.setTextColor(getResources().getColor(R.color.blue));
    }

    public void galleryopen() {
        Log.d(TAG, "galleryopen: ");
        portfolioll.setVisibility(View.GONE);
        galleryview.setVisibility(View.VISIBLE);
        galleryll.setVisibility(View.VISIBLE);
        underlinegalleryview.setVisibility(View.VISIBLE);
        underlineportfolioview.setVisibility(View.GONE);
        portfoliotxt.setTextColor(getResources().getColor(R.color.gray1));
        gallerytxt.setTextColor(getResources().getColor(R.color.blue));
    }


    @Override
    public void onclickimage(int postion, String selecteimageString) {
        Intent intent = new Intent(this, GalleryImageFullshow.class);
        ImageViewSinglefullsize datafullimage = new ImageViewSinglefullsize(
                ModelData.getProfile_img(),
                selecteimageString,
                ModelData.getName(),
                ModelData.getProffesion()
        );
        datafullimage.setPostion(postion);
        datafullimage.setImages_hd(ModelData.getHd_images());
 /**sushil change 12/17/2020*/
        intent.putExtra("profileGallery",1);
        intent.putExtra("fullimagdetail",datafullimage);
        startActivity(intent);
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

    private void initCallApi(String user_id) {
        shimmer.startShimmer();
        login_client_request = new StringRequest(Request.Method.POST,
                ApiConstant.USER_GET_TALENT_DETAILS,
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


                            ModelData.setCountry(modeldetail.getString(ConstantString.GET_TALENT_COUNTRY));
                            ModelData.setState(modeldetail.getString(ConstantString.GET_TALENT_STATE));
                            ModelData.setCity(modeldetail.getString(ConstantString.GET_TALENT_CITY));
                            setViews(ModelData);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                eror -> {
                    Log.d(TAG, "initCallApi: error-> " + eror.toString());
//                    showvaldationError(eror.toString());
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> talent_detail_params= new HashMap<>();
                talent_detail_params.put("user_id",user_id);
                return talent_detail_params;
            }

            @SuppressLint("LongLogTag")
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(View_proposal_userProfile.this).getUserData();
                Log.d(TAG, "getHeaders: token value-> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(login_client_request);

        galleryll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryopen();
            }
        });
        portfolioll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                protfolioviewopen();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
