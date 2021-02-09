package com.example.myapplication.clientsprofile.clients_fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_activity.Client_profile_update_activity;
import com.example.myapplication.pojo.GetClientData;
import com.example.myapplication.pojo.GetModelData;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.pojo.locationpojo;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.BottomSheettl;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyProfile extends Fragment {

    private CircularImageView profileimage;
    private TextView fullname;
    private TextView location;
    private ImageView menuoption;
    public static final String TAG = "MyProfile";
    private Activity mActivity;
    private StringRequest login_client_request;
    private TextView editText;
    private  GetClientData getClientData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clients_myprofilefragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
//        initCallApi();
        Gson gson = new Gson();
        String clientdata_detail = SharedPreferanceManager.getInstance(getContext()).get_CLIENT_userInformation();
         getClientData = gson.fromJson(clientdata_detail, GetClientData.class);
        setViews(getClientData);
    }


    private void initview(View view) {
        profileimage = view.findViewById(R.id.profileimage);
        fullname = view.findViewById(R.id.name);
        location = view.findViewById(R.id.location);
        menuoption = view.findViewById(R.id.optionsmenu);
        editText = view.findViewById(R.id.edit);
        menuoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheettl bottomSheettl = new BottomSheettl();
                assert getFragmentManager() != null;
                bottomSheettl.show(getFragmentManager(), "MOdel");

            }
        });



    }

//    private void initCallApi() {
//        if (mActivity == null) {
//            return;
//        }
//        Log.d(TAG, "initCallApi: called");
//        login_client_request = new StringRequest(Request.Method.GET,
//                ApiConstant.CLIENT_GET_PROFILE,
//                (Response.Listener<String>) response -> {
//                    Log.d(TAG, "initCallApi: response-> " + response);
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
//                            JSONObject clientdetail = jsonObject.getJSONObject(ConstantString.DETAIL_TAG);
////                            Log.d(TAG, "initCallApi: data ->"+clientdetail.getString(ConstantString.ID)
////                                    +" name "+clientdetail.getString(ConstantString.EMAIL)+
////                                    "gender "+clientdetail.getString(ConstantString.GENDER));
//                            GetClientData clientData = new GetClientData(
//                                    clientdetail.getString(ConstantString.USERID),
//                                    clientdetail.getString(ConstantString.NAME),
//                                    clientdetail.getString(ConstantString.EMAIL),
//                                    clientdetail.getString(ConstantString.GENDER),
//                                    clientdetail.getString(ConstantString.GET_CLIENT_MOBILE),
//                                    clientdetail.getString(ConstantString.GET_CLIENT_COUNTRY),
//                                    clientdetail.getString(ConstantString.GET_CLIENT_STATE),
//                                    clientdetail.getString(ConstantString.GET_CLIENT_CITY),
//                                    clientdetail.getString(ConstantString.PROFILE_IMG),
//                                    clientdetail.getString(ConstantString.GET_CLIENT_CREATED_AT),
//                                    clientdetail.getString(ConstantString.GET_CLIENT_UPDATED_AT)
//                            );
//
//                            setViews(clientData);
//                        } else {
//                            Log.d(TAG, "initCallApi: error found in client fetch ");
//                        }
//
//
//                    } catch (JSONException e) {
//                        Log.d(TAG, "initCallApi: " + e);
//                        e.printStackTrace();
//                    }
//                },
//                eror -> {
//                    Log.d(TAG, "initCallApi: error-> " + eror.toString());
//                    eror.printStackTrace();
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> login_token = new HashMap<>();
//                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(getContext()).getUserData();
//                Log.d(TAG, "getHeaders: token value -> " + logindata.getToken());
//                login_token.put("Authorization", "Bearer " + logindata.getToken());
//                return login_token;
//            }
//        };
//        VolleySingleton.getInstance(getContext()).addToRequestQueue(login_client_request);
//    }

    @Override
    public void onStop() {
        super.onStop();
        if (login_client_request != null) {
            login_client_request.cancel();
        }

    }

    @SuppressLint("SetTextI18n")
    private void setViews(GetClientData clientData) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: getclientData-> "+getClientData.getName());
                Intent intent = new Intent(getActivity(), Client_profile_update_activity.class);
                intent.putExtra("ClilentPotiledetail",getClientData);
                startActivity(intent);


            }
        });


        Log.d(TAG, "setViews: clientData-> " + clientData);
//        Log.d(TAG, "setViews: fullname-> "+clientData.getName());
        String name = clientData.getName();
        fullname.setText(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
        locationpojo state = clientData.getState();
        locationpojo country = clientData.getCountry();
        Log.d(TAG, "setViews: state-> " + state + " country-> " + country);
        if (state != null && !TextUtils.isEmpty(state.getLocation_name())  &&
                !state.getLocation_name().equalsIgnoreCase("null") ) {
            location.setText(state.getLocation_name().substring(0, 1).toUpperCase() + state.getLocation_name().substring(1).toLowerCase() + ", "
                    + country.getLocation_name().substring(0, 1).toUpperCase() + country.getLocation_name().substring(1).toLowerCase());
        } else {
            location.setText(country.getLocation_name().substring(0, 1).toUpperCase() + country.getLocation_name().substring(1).toLowerCase());
        }

        Glide.with(mActivity)
                .load(clientData.getProfile_img())
                .into(profileimage);


    }

}

















