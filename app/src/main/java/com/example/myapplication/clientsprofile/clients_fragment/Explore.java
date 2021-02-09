package com.example.myapplication.clientsprofile.clients_fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Adapter.ExploreAdapter;
import com.example.myapplication.Adapter.SaveJobS_Adapter;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_activity.Clients_Home;
import com.example.myapplication.clientsprofile.clients_activity.Clients_SelectCriteriaExploreModels;
import com.example.myapplication.clientsprofile.clients_activity.FetchAll_ClientData;
import com.example.myapplication.pojo.Client_Explore_detailfetch;
import com.example.myapplication.pojo.Gell_all_jobs_postOPJO;
import com.example.myapplication.pojo.GetClientData;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.google.gson.Gson;
import com.todkars.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Explore extends Fragment {


    private LinearLayout titlebar;
    private TextView title;
    private TextView selectcriteria;
    private ShimmerRecyclerView rv_explore;
    private StringRequest model_client_request;
    private ArrayList<Client_Explore_detailfetch> client_explore_detailfetchesarr;
    private ExploreAdapter exploreAdapter;
    private SwipeRefreshLayout referesh_option;
    private static  final  String TAG = "ExploreFregament";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clients_exploremodelsfragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        if(model_client_request != null){
            model_client_request.cancel();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCallApi(ApiConstant.CLIENT_EXPLORE,"");
        initview(view);
    }

    private void initview(View view){
        Log.d(TAG, "initview: ");
        titlebar = view.findViewById(R.id.titlebar);
        title = view.findViewById(R.id.title);
        selectcriteria = view.findViewById(R.id.selectcriteria);
        rv_explore = view.findViewById(R.id.rv);
        referesh_option = view.findViewById(R.id.referesh_option);


        rv_explore.showShimmer();
        selectcriteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Clients_SelectCriteriaExploreModels.class);
                startActivityForResult(intent,ConstantString.CodeforExplore);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });


        referesh_option.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rv_explore.showShimmer();
                initCallApi(ApiConstant.CLIENT_EXPLORE,"");
            }
        });

    }
    private void initCallApi(String exploreApi,String searchvalue){


        model_client_request = new StringRequest(Request.Method.POST,
                exploreApi,
                (Response.Listener<String>) response ->{
                    Log.d(TAG, "initCallApi: response-> "+response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(!jsonObject.getBoolean(ConstantString.IS_ERROR)){
                            responsefetch(jsonObject);
                  }
                        else{
                            Log.d(TAG, "initCallApi: error found in client fetch ");
                        }


                    } catch (JSONException e) {
                        Log.d(TAG, "initCallApi: "+e);
                        e.printStackTrace();
                    }
                },
                eror -> {
                    Log.d(TAG, "initCallApi: error-> "+eror.toString());
                    eror.printStackTrace();
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> exploreparam = new HashMap<>();

                if(exploreApi.equals(ApiConstant.CLIENT_EXPLORE_BY_BODYTYPE)){
                    exploreparam.put("bodyType",searchvalue);
                }
                else if(exploreApi.equals(ApiConstant.CLIENT_EXPLORE_BY_GENDER)){
                    Log.e(TAG, "initCallApi: Api "+exploreApi+" Searching is "+searchvalue);
                    exploreparam.put("gender",searchvalue);
                }
                else if(exploreApi.equals(ApiConstant.CLIENT_EXPLORE_BY_EHIN)){
                    exploreparam.put("ethincity",searchvalue);
                }
                else if(exploreApi.equals(ApiConstant.CLIENT_EXPLORE_BY_SKILL)){
                    exploreparam.put("skill",searchvalue);
                }
                else {
                    exploreparam.put("","");
                }


                return exploreparam;
//                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String ,String > login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(getActivity()).getUserData();
                Log.d(TAG, "getHeaders: token value -> "+logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(model_client_request);
    }


    private void responsefetch(JSONObject jsonObject) {
        client_explore_detailfetchesarr= new ArrayList<>();

        Log.d(TAG, "responseSavefetch: response save data jobs-> "+jsonObject);

        try {
            JSONArray detailobje = jsonObject.getJSONArray(ConstantString.DETAIL_TAG);
            for(int i=0;i<detailobje.length();i++){
                JSONObject exploretalent = (JSONObject) detailobje.get(i);

//                Log.d(TAG, "save job recyclerview responsefetch: jobpost-> "+exploretalent);

                Client_Explore_detailfetch client_explore_detailfetch =
                        new Client_Explore_detailfetch();
                        client_explore_detailfetch.setUser_id(exploretalent.getString("user_id"));
                        client_explore_detailfetch.setName(exploretalent.getString("name"));
                        client_explore_detailfetch.setHd_images(exploretalent.getString("hd_images"));
                        client_explore_detailfetch.setProfile_img(exploretalent.getString("profile_img"));
                         client_explore_detailfetch.setProffesion(exploretalent.getString("proffesion"));
                        client_explore_detailfetchesarr.add(client_explore_detailfetch);
            }
            rv_explore.hideShimmer();
            if(referesh_option != null){
                referesh_option.setRefreshing(false);
            }
            GridLayoutManager manager = new GridLayoutManager(getContext(),
                    3, GridLayoutManager.VERTICAL, false);
            exploreAdapter = new ExploreAdapter(client_explore_detailfetchesarr,getContext());
//            saveJobS_adapter.SetOnclickListener(this::OnlistSingleListener);
            rv_explore.setLayoutManager(manager);
            rv_explore.setAdapter(exploreAdapter);
            exploreAdapter.notifyDataSetChanged();
            rv_explore.hideShimmer();
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode ==ConstantString.CodeforExplore ){
            String reciveapi = data.getStringExtra("searchApi");
            String searchvalue = data.getStringExtra("resulttxt");
            assert reciveapi != null;
            assert searchvalue != null;
            Log.d(TAG, "@@@@@@@@onActivityResult: api-> "+reciveapi+" searchvalue-> "+searchvalue);
            if(!reciveapi.isEmpty() && !searchvalue.isEmpty()){
                initCallApi(reciveapi,searchvalue);
                rv_explore.showShimmer();
            }


        }
    }
}
