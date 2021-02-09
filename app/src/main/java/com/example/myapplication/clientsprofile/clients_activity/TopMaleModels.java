package com.example.myapplication.clientsprofile.clients_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Adapter.PopularModelsAdapter;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.R;
import com.example.myapplication.pojo.GetModelData;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.todkars.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class TopMaleModels extends AppCompatActivity {

    private ShimmerRecyclerView rv_maleArtists;
    private PopularModelsAdapter popularModelsAdapter;
    private ArrayList<GetModelData> modelData;
    private String TAG="TopMaleModels";
    private StringRequest fetch_TopMaleModels;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_top_male_models);

    }

    @Override
    protected void onStart() {
        super.onStart();
        rv_integration();
//        addingData();
        vollyerccall_forFindJob_list();
    }

    public void back(View view){
        finish();
    }

    private void addingData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GetModelData getModelData = new GetModelData();
                getModelData.setName("hello");
                getModelData.setProffesion("Android Developer");
                GetModelData getModelData1 = new GetModelData();
                getModelData1.setName("hello");
                getModelData1.setProffesion("Android Developer");
                modelData.add(getModelData);
                modelData.add(getModelData1);
                popularModelsAdapter.notifyDataSetChanged();
                rv_maleArtists.hideShimmer();
            }
        },2000);
        rv_maleArtists.showShimmer();
    }
    /**
     * Api call for getting the list of top female models
     */
    public void vollyerccall_forFindJob_list(){
        Log.d(TAG, "vollyerccall_forjob_list: ");
        fetch_TopMaleModels = new StringRequest(Request.Method.GET,
                ApiConstant.TOP_MALE_MODEL,
                (Response.Listener<String>) response ->{
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(!jsonObject.getBoolean(ConstantString.IS_ERROR)){
                            responsefetch(jsonObject);
                        }
                        else{
                            Log.d(TAG, "vollyerccall_forjob_list: "+jsonObject.getString(ConstantString.RESPONSE_MESSAGE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d(TAG, "initCallApi: error-> " + error.toString());
                    showvaldationError(error.toString());
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("gender","female");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(TopMaleModels.this).getUserData();
                Log.d(TAG, "getHeaders: for  value-> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(fetch_TopMaleModels);
    }

    /**
     * @param jsonObject
     * jsonObject is having all the information of the top Female models
     */
    private void responsefetch(JSONObject jsonObject) {
        Log.d(TAG, "vollyerccall_forjob_list: "+jsonObject);
        try {
            JSONArray details = jsonObject.getJSONArray("details");
            for (int i = 0;i<details.length();i++){
                JSONObject object = details.getJSONObject(i);
                GetModelData getModelData= new GetModelData();
                getModelData.setUser_id(object.getString("user_id"));
                getModelData.setName(object.getString("name"));
                getModelData.setProffesion(object.getString("proffesion"));
                getModelData.setImgCount(object.getString("imgCount"));
                getModelData.setPersonal_journey(object.getString("personal_journey"));

                JSONArray skills = object.getJSONArray("skill");
                ArrayList <String> skill= new ArrayList<>();
                for (int j = 0 ;j<skills.length();j++){
                    skill.add(skills.getString(j));
                }
                getModelData.setSkill(skill);
//                    getModelData.setSkill(object.getString("skills"));
                getModelData.setProfile_img(object.getString("profile_img"));
                getModelData.setGender(object.getString("gender"));
                getModelData.setCountry(object.getString("countryName"));
                getModelData.setState(object.getString("stateName"));
                getModelData.setCity(object.getString("cityName"));
                modelData.add(getModelData);

            }
            popularModelsAdapter.notifyDataSetChanged();
            rv_maleArtists.hideShimmer();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showvaldationError(String msg) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg);
        bottomSheet_for_error.setCancelable(false);
        bottomSheet_for_error.show(this.getSupportFragmentManager(), "error bottom");
    }



    private void rv_integration() {
        // Top Popular Models
        modelData = new ArrayList<>();
        rv_maleArtists = findViewById(R.id.rv_female_artists);
        rv_maleArtists.setLayoutManager(new LinearLayoutManager(this));
        popularModelsAdapter = new PopularModelsAdapter(modelData,this);
        rv_maleArtists.setAdapter(popularModelsAdapter);
        rv_maleArtists.setHasFixedSize(true);
        rv_maleArtists.showShimmer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fetch_TopMaleModels!= null){
            fetch_TopMaleModels.cancel();
        }
    }
}