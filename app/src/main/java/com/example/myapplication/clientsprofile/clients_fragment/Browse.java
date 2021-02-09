package com.example.myapplication.clientsprofile.clients_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Adapter.ModelSearchAdapter;
import com.example.myapplication.Adapter.PopularModelsAdapter;
import com.example.myapplication.Adapter.RecommendedModelsAdapter;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_activity.TopFemaleModels;
import com.example.myapplication.clientsprofile.clients_activity.TopMaleModels;
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

public class Browse extends Fragment {
    private NestedScrollView defaultLayout;
    private ShimmerRecyclerView rv_RecommendedModels;
    private ShimmerRecyclerView rv_PopularModels;
    private RecyclerView rv_SearchModels;
    private RecommendedModelsAdapter recommendedModelsAdapter;
    private PopularModelsAdapter popularModelsAdapter;
    private ModelSearchAdapter modelSearchAdapter;
    ArrayList<GetModelData> modelData ;
    private CardView maleModel , femaleModel;
    private String TAG="Browse";
    private StringRequest fetch_TopFemaleModels;
    private EditText search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.searchmodels_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findviews(view);
        rv_integration(view);
        vollyerccall_forFindJob_list();
//        addingData();

        maleModel = view.findViewById(R.id.malemodel);
        femaleModel = view.findViewById(R.id.femalemodel);
        maleModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TopMaleModels.class));

            }
        });
        femaleModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TopFemaleModels.class));

            }
        });
    }

    private void findviews(View view) {
        defaultLayout = view.findViewById(R.id.defaultlayout);
        search = view.findViewById(R.id.search);
            serarchModel(view);
    }

    private void serarchModel(View view) {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    rv_serachModelsIntegration(view);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rv_SearchModels.setVisibility(View.VISIBLE);
                defaultLayout.setVisibility(View.GONE);
                modelSearchAdapter.getFilter().filter(charSequence);
                if (TextUtils.isEmpty(charSequence)){
                    defaultLayout.setVisibility(View.VISIBLE);
                    rv_SearchModels.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                modelSearchAdapter.getFilter().filter(editable.toString());
            }
        });

    }
    private void rv_serachModelsIntegration(View view){
       rv_SearchModels =  view.findViewById(R.id.rv);
        rv_SearchModels.setLayoutManager(new LinearLayoutManager(getActivity()));
        modelSearchAdapter = new ModelSearchAdapter(getActivity(),modelData);
        rv_SearchModels.setAdapter(modelSearchAdapter);
        rv_SearchModels.setHasFixedSize(true);
    }

    private void rv_integration(View view) {
        // Top Recommended modes
        rv_RecommendedModels = view.findViewById(R.id.rv_artist);
        modelData = new ArrayList<>();
        rv_RecommendedModels.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recommendedModelsAdapter = new RecommendedModelsAdapter(modelData,getActivity());
        rv_RecommendedModels.setAdapter(recommendedModelsAdapter);
        rv_RecommendedModels.setHasFixedSize(true);

        // Top Popular Models
        rv_PopularModels = view.findViewById(R.id.rv_popularartists);
        rv_PopularModels.setLayoutManager(new LinearLayoutManager(getActivity()));
        popularModelsAdapter = new PopularModelsAdapter(modelData,getActivity());
        rv_PopularModels.setAdapter(popularModelsAdapter);
        rv_PopularModels.setHasFixedSize(true);
          }
    /**
     * Api call for getting the list of top female models
     */
    public void vollyerccall_forFindJob_list(){
       rv_PopularModels.showShimmer();
       rv_RecommendedModels.showShimmer();
       Log.d(TAG, "vollyerccall_forjob_list: ");
        fetch_TopFemaleModels = new StringRequest(Request.Method.GET,
                ApiConstant.RECOMMENDED_TALENTS,
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
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(getActivity()).getUserData();
                Log.d(TAG, "getHeaders: for  value-> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(fetch_TopFemaleModels);
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
           recommendedModelsAdapter.notifyDataSetChanged();
            rv_RecommendedModels.hideShimmer();
            popularModelsAdapter.notifyDataSetChanged();
            rv_PopularModels.hideShimmer();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showvaldationError(String msg) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg);
        bottomSheet_for_error.setCancelable(false);
        bottomSheet_for_error.show(getActivity().getSupportFragmentManager(), "error bottom");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(fetch_TopFemaleModels!= null){
            fetch_TopFemaleModels.cancel();
        }
    }
}
