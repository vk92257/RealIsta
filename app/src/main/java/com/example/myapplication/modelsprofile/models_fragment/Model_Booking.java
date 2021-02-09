package com.example.myapplication.modelsprofile.models_fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Adapter.Models_contracts_item_Adapter;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.Complete_checkAcitivity;
import com.example.myapplication.R;
import com.example.myapplication.modelsprofile.models_activity.Models_submitProposal;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.pojo.Model_allContract_pojo;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.todkars.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Model_Booking extends Fragment {

    private static final String TAG = "Model_Booking";
    private ShimmerRecyclerView rv;
    private FrameLayout titlebar;
    private TextView title;
    private ImageView menuicon;
    private StringRequest proposalrequest;
    private LinearLayout nocontractsfoundlayout;
    private SwipeRefreshLayout referesh_option;
    private ArrayList<Model_allContract_pojo> model_allContract_pojosarry;
    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener ;
    private     PowerMenu powerMenu;




    @Override
    public void onStart() {
        super.onStart();
        volleyapicall();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(proposalrequest != null){
            proposalrequest.cancel();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.allcontracts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);
    }

    private void initview(View view){
        rv = view.findViewById(R.id.rv);
        titlebar = view.findViewById(R.id.titlebar);
        title = view.findViewById(R.id.title);
        menuicon = view.findViewById(R.id.menuicon);
        nocontractsfoundlayout = view.findViewById(R.id.nocontractsfoundlayout);
        referesh_option = view.findViewById(R.id.referesh_option);
        rv.showShimmer();
        Emptylayout();
        referesh_option.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rv.showShimmer();
                volleyapicall();
            }
        });

//        menuicon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {



        onMenuItemClickListener = (position, item) -> {
//            Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            rv.showShimmer();
            volleyapicall();
            powerMenu.setSelectedPosition(position); // change selected item
            powerMenu.dismiss();
        };
         powerMenu = new PowerMenu.Builder(Objects.requireNonNull(getContext()))
                .addItem(new PowerMenuItem("Refresh", false)) // add an item.
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | LEFT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(getContext(), R.color.black))
                .setTextGravity(Gravity.CENTER)
                .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
                .setSelectedTextColor(ContextCompat.getColor(getContext(), R.color.black))
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();


        menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                powerMenu.showAsDropDown(view);
            }
        });




            }
//        });

//    }


    private void volleyapicall() {
        Log.d(TAG, "volleyapicall: ");
         proposalrequest = new StringRequest(Request.Method.GET,
                ApiConstant.MODEL_GET_ALL_CONTRACTS,
                (Response.Listener<String>) response -> {
                    try {
                        Log.d(TAG, "volleyapicall: model booking->  "+response);
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)){
                            responseContract(jsonObject);
                        }

                    } catch (JSONException e) {

                    }
                },
                error -> {
//                    hideprogress();
                    if( error instanceof NetworkError) {
                        showvaldationError("Network Error please check internet connection", R.raw.onboard);
                    } else if( error instanceof ServerError) {
                        //handle if server error occurs with 5** status code
                        showvaldationError("Server side error", R.raw.onboard);
                    } else if( error instanceof AuthFailureError) {
                        //handle if authFailure occurs.This is generally because of invalid credentials
                        showvaldationError("please check your credentials ", R.raw.onboard);
                    } else if( error instanceof ParseError) {
                        //handle if the volley is unable to parse the response data.
                        showvaldationError(" Unable to parse the response data ", R.raw.onboard);
                    } else if( error instanceof NoConnectionError) {
                        //handle if no connection is occurred
                        showvaldationError(" No Connection to server ", R.raw.onboard);
                    } else if( error instanceof TimeoutError) {
                        showvaldationError("Time out error Please restart the app  ", R.raw.onboard);
                        //handle if socket time out is occurred.
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> fetchallcontract= new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(getContext()).getUserData();
                Log.d(TAG, "getHeaders: token value-> " + logindata.getToken());
                fetchallcontract.put("Authorization", "Bearer " + logindata.getToken());
                return fetchallcontract;
            }


        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(proposalrequest);

    }

    private void responseContract(JSONObject jsonObject){
        model_allContract_pojosarry = new ArrayList<>();
        try {
            JSONArray detailobje = jsonObject.getJSONArray(ConstantString.DETAIL_TAG);

            for(int i=0;i<detailobje.length();i++){
                JSONObject jobpost = (JSONObject) detailobje.get(i);

                Model_allContract_pojo model_allContract_pojo =
                        new Model_allContract_pojo(
                                jobpost.getString("jobid"),
                                jobpost.getString("contractid"),
                                jobpost.getString("job_title"),
                                jobpost.getString("name"),
                                jobpost.getString("job_duration"),
                                jobpost.getString("performance_location"),
                                jobpost.getString("product_name"),
                                jobpost.getString("final_price"),
                                jobpost.getString("position"),
                                jobpost.getString("status"),
                                jobpost.getString("profile_img")
                        );
                model_allContract_pojo.setUserid( jobpost.getString("user_id"));
                model_allContract_pojosarry.add(model_allContract_pojo);
                Log.e("model___booking", "responseContract: " +jobpost.getString("profile_img"));
            }
            rv.hideShimmer();
            if(referesh_option != null){
                referesh_option.setRefreshing(false);
            }
            if(model_allContract_pojosarry.size() == 0){
                NoEmptylayout();
            }
            else{
                Emptylayout();
            }
            Models_contracts_item_Adapter models_contracts_item_adapter = new Models_contracts_item_Adapter(model_allContract_pojosarry,getActivity());
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
            rv.setAdapter(models_contracts_item_adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private void Emptylayout(){
        nocontractsfoundlayout.setVisibility(View.GONE);
    }
    private void NoEmptylayout(){
        nocontractsfoundlayout.setVisibility(View.VISIBLE);
        referesh_option.setVisibility(View.GONE);
    }

    private void showvaldationError(String msg, int errorimage) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg, errorimage);
        bottomSheet_for_error.setCancelable(false);
//        bottomSheet_for_error.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        bottomSheet_for_error.setlottiimage(errorimage);
        bottomSheet_for_error.show(getFragmentManager(), "error bottom");
    }
}
