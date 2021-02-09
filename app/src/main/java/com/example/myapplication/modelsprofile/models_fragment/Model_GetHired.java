package com.example.myapplication.modelsprofile.models_fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import com.example.myapplication.Adapter.FindJobs_Adapter;
import com.example.myapplication.Adapter.SaveJobS_Adapter;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.Interface.Empty_recyclerview_Onclick;
import com.example.myapplication.clientsprofile.Interface.Jobs_clickListener;
import com.example.myapplication.clientsprofile.Interface.SingleJobOnclick;
import com.example.myapplication.clientsprofile.clients_activity.FetchAll_ClientData;
import com.example.myapplication.modelsprofile.models_activity.FetchAll_ModelData;
import com.example.myapplication.modelsprofile.models_activity.Single_JobDetail_Activity;
import com.example.myapplication.pojo.FetchAllJob_pojo;
import com.example.myapplication.pojo.Gell_all_jobs_postOPJO;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.todkars.shimmer.ShimmerRecyclerView;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Model_GetHired extends Fragment implements View.OnClickListener, Empty_recyclerview_Onclick,
        Jobs_clickListener {

    private static final String TAG = "Model_GetHired";
    private TextView findJobs, savedJobs;
    private ShimmerRecyclerView findJobsRecyclerView, savedJobsRecyclerView;
    private View findJobsUnderLine, savedJobsUnderLine, view;
    private StringRequest fetchFindJob_list, fetchSaveJob_list, fetch_favid;
    private ArrayList<FetchAllJob_pojo> gellAllJobsPostOPJOS, getGellAllsaveJobsPostOPJOS;
    private FindJobs_Adapter findJobs_adapter;
    private SaveJobS_Adapter saveJobS_adapter;
    private LinearLayout empty_recyclerview;
    private ArrayList<String> fave_jobids;
    private RelativeLayout nojobsave;
    private SwipeRefreshLayout refresh, referesh_rv;

    private ArrayList<FetchAllJob_pojo> fetchAllJob_pojosarr;
    private int click = 0;
    private LinearLayoutManager findJobsLayoutManger;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return view = inflater.inflate(R.layout.gethired_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView();
    }

    /**
     * Adding all the views with the their Id's.
     */
    private void findView() {
        refresh = view.findViewById(R.id.referesh_option);
        findJobs = view.findViewById(R.id.findjobsviewtv);
        savedJobs = view.findViewById(R.id.savedjobstv);
        findJobsUnderLine = view.findViewById(R.id.underlinefindjobsview);
        savedJobsUnderLine = view.findViewById(R.id.underlinesavedjobs);
        findJobsRecyclerView = view.findViewById(R.id.rv);
        savedJobsRecyclerView = view.findViewById(R.id.rv_saved);
        empty_recyclerview = view.findViewById(R.id.empty_recycleview);
        nojobsave = view.findViewById(R.id.nosavejob);
        swipeToRefresh();

//        referesh_option = view.findViewById(R.id.referesh_option);
//        referesh_rv = view.findViewById( R.id.referesh_rv);
//        referesh_option.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
////                fetchFav_save();
//            }
//        });
//        referesh_rv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                fetchFav_save();
//            }
//        });
        // calling the findJobsRecyclerViewIntegration  because its the default screen
        findJobsRecyclerViewIntegration();
        findJobs.setOnClickListener(this::onClick);
        savedJobs.setOnClickListener(this::onClick);
    }

    private void swipeToRefresh() {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                findJobsRecyclerViewIntegration();
                fetchFav_save();
            }
        });

    }

    @Override
    public void onClick(View view) {
        final Animation out = new AlphaAnimation(0.5f, 1.0f);
        out.setRepeatCount(0);
        out.setDuration(2000);
        switch (view.getId()) {
            case R.id.findjobsviewtv: {
                if (fetchSaveJob_list != null) {
                    fetchSaveJob_list.cancel();
                }
                findJobs.setTextColor(getResources().getColor(R.color.colorAccent));
//                refresh.setRefreshing(true);
                refresh.setEnabled(true);
                findJobsUnderLine.setVisibility(View.VISIBLE);
                savedJobs.setTextColor(getResources().getColor(R.color.gray1));
                savedJobsUnderLine.setVisibility(View.INVISIBLE);
                findJobsRecyclerViewIntegration();
                findJobs.setAnimation(out);
//                    Toast.makeText(getActivity(), ""+findJobs.getText(), Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.savedjobstv: {
                if (fetch_favid != null) {
                    fetch_favid.cancel();
                }
                if (fetchFindJob_list != null) {
                    fetchFindJob_list.cancel();
                }

//                savedJobs.setAnimation(null);
                nojobsave.setVisibility(View.GONE);
                savedJobs.setTextColor(getResources().getColor(R.color.colorAccent));
                savedJobsUnderLine.setVisibility(View.VISIBLE);
                refresh.setRefreshing(false);
                refresh.setEnabled(false);
                findJobs.setTextColor(getResources().getColor(R.color.gray1));
                findJobsUnderLine.setVisibility(View.INVISIBLE);
                savedJobs.setAnimation(out);
                savedJobsRecyclerViewIntegration();
//                Toast.makeText(getActivity(), ""+savedJobs.getText(), Toast.LENGTH_SHORT).show();
                break;
            }
        }

    }


    /**
     * Integration of recyclerview for saved jobs
     */
    private void savedJobsRecyclerViewIntegration() {
        nojobsave.setVisibility(View.GONE);
//        refresh.setVisibility(View.GONE);
        findJobsRecyclerView.setVisibility(View.GONE);
        savedJobsRecyclerView.setVisibility(View.VISIBLE);
        savedJobsRecyclerView.showShimmer();
        vollyerccall_forSaveJob_list();


    }

    /**
     * Integration of recyclerview find jobs
     */
    private void findJobsRecyclerViewIntegration() {
        nojobsave.setVisibility(View.GONE);
//        refresh.setVisibility(View.VISIBLE);
        savedJobsRecyclerView.setVisibility(View.GONE);
        findJobsRecyclerView.setVisibility(View.VISIBLE);



//        vollyerccall_forFindJob_list();
        fetchFav_save();

    }


    /**
     * calling the Save Job list api
     */
    public void vollyerccall_forSaveJob_list() {
        getGellAllsaveJobsPostOPJOS = new ArrayList<>();
        Log.d(TAG, "vollyerccall_forjob_list: ");
        fetchSaveJob_list = new StringRequest(Request.Method.GET,
                ApiConstant.VIEW_SAVED_JOBS,
                (Response.Listener<String>) response -> {
                    Log.d(TAG, "vollyerccall_forjob_list: save job " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                            responseSavefetch(jsonObject);
                        } else {
                            Log.d(TAG, "vollyerccall_forjob_list: " + jsonObject.getString(ConstantString.RESPONSE_MESSAGE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d(TAG, "initCallApi: error-> " + error.toString());

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
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(getContext()).getUserData();
                Log.d(TAG, "getHeaders: save job api token value-> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(fetchSaveJob_list);
    }


    /**
     * calling the Find Job list api
     */

    private void fetchFav_save() {
        Log.d(TAG, "vollyerccall_forjob_list: ");
        findJobsRecyclerView.showShimmer();
        fetch_favid = new StringRequest(Request.Method.GET,
                ApiConstant.MODEL_SAVE_JOBES_FAV_USERID,
                (Response.Listener<String>) response -> {
                    Log.d(TAG, "vollyerccall_forjob_list: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                            responsefavFetch(jsonObject);
                        } else {
                            Log.d(TAG, "vollyerccall_forjob_list: " + jsonObject.getString(ConstantString.RESPONSE_MESSAGE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d(TAG, "initCallApi: error-> " + error.toString());
                    showvaldationError(error.toString(), R.raw.onboard);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(getContext()).getUserData();
                Log.d(TAG, "getHeaders: for  value-> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(fetch_favid);
    }

    public void vollyerccall_forFindJob_list(ArrayList<String> fav_userid) {
        Log.d(TAG, "vollyerccall_forjob_list: ");
        fetchFindJob_list = new StringRequest(Request.Method.GET,
                ApiConstant.GET_ALL_JOBS,
                (Response.Listener<String>) response -> {
                    Log.d(TAG, "vollyerccall_forjob_list: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                            responsefetch(jsonObject, fav_userid);
                        } else {
                            Log.d(TAG, "vollyerccall_forjob_list: " + jsonObject.getString(ConstantString.RESPONSE_MESSAGE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {


                    Log.d(TAG, "initCallApi: error-> " + error.toString());
                    if (error instanceof NetworkError) {
                        showvaldationError("Cannot connect to Internet...Please check your connection!", R.raw.onboard);
                    } else if (error instanceof ServerError) {
                        //handle if server error occurs with 5** status code
                        showvaldationError("The server could not be found. Please try again after some time!!", R.raw.onboard);
                    } else if (error instanceof AuthFailureError) {
                        //handle if authFailure occurs.This is generally because of invalid credentials
                        showvaldationError("please check your credentials ", R.raw.onboard);
                    } else if (error instanceof ParseError) {
                        //handle if the volley is unable to parse the response data.
                        showvaldationError(" Parsing error! Please try again after some time!! ", R.raw.onboard);
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
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(getContext()).getUserData();
                Log.d(TAG, "getHeaders: for  value-> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(fetchFindJob_list);
    }


    private void responsefavFetch(JSONObject jsonObject) {
        fave_jobids = new ArrayList<>();
        try {
            JSONArray detailobje = jsonObject.getJSONArray(ConstantString.DETAIL_TAG);
            for (int i = 0; i < detailobje.length(); i++) {
                JSONObject jobpost = (JSONObject) detailobje.get(i);

                Log.d(TAG, "responsefetch: jobpost-> " + jobpost);

                fave_jobids.add(jobpost.getString("id"));
            }
            vollyerccall_forFindJob_list(fave_jobids);
//            findJobs_adapter.notifyDataSetChanged();
            refresh.setRefreshing(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * response fetched from the api
     *
     * @param jsonObject jsonObject is the response  itself
     */

    private void responsefetch(JSONObject jsonObject, ArrayList<String> fave_jobids) {
        fetchAllJob_pojosarr = new ArrayList<>();

        try {
            JSONArray detailobje = jsonObject.getJSONArray(ConstantString.DETAIL_TAG);
            for (int i = 0; i < detailobje.length(); i++) {
                JSONObject jobpost = (JSONObject) detailobje.get(i);


                JSONArray skillarray = jobpost.getJSONArray("skill");
                Log.d(TAG, "responsefetch: skillarray-> " + skillarray);
                ArrayList<String> skilla = new ArrayList<>();
                for (int p = 0; p < skillarray.length(); p++) {
                    skilla.add(skillarray.get(p).toString());
                }
                JSONArray additionattach = jobpost.getJSONArray("additional_attachment");
                ArrayList<String> additionattacharray = new ArrayList<>();
                for (int p = 0; p < additionattach.length(); p++) {
                    additionattacharray.add(additionattach.get(p).toString());
                }

                JSONArray hair_colorjson = jobpost.getJSONArray("hair_color");
                ArrayList<String> hair_colorarray = new ArrayList<>();
                for (int p = 0; p < hair_colorjson.length(); p++) {
                    hair_colorarray.add(hair_colorjson.get(p).toString());
                }
                JSONArray eye_colorjson = jobpost.getJSONArray("eye_color");
                ArrayList<String> eye_colorarray = new ArrayList<>();
                for (int p = 0; p < eye_colorjson.length(); p++) {
                    eye_colorarray.add(eye_colorjson.get(p).toString());
                }
                JSONArray hair_lengthjson = jobpost.getJSONArray("hair_length");
                ArrayList<String> hair_lengtharray = new ArrayList<>();
                for (int p = 0; p < hair_lengthjson.length(); p++) {
                    hair_lengtharray.add(hair_lengthjson.get(p).toString());
                }
                JSONArray skiethnicitylljson = jobpost.getJSONArray("ethnicity");
                ArrayList<String> skiethnicityllarray = new ArrayList<>();
                for (int p = 0; p < skiethnicitylljson.length(); p++) {
                    skiethnicityllarray.add(skiethnicitylljson.get(p).toString());
                }

                JSONArray jobtypejson = jobpost.getJSONArray("job_type");
                ArrayList<String> jobtypearray = new ArrayList<>();
                for (int p = 0; p < jobtypejson.length(); p++) {
                    jobtypearray.add(jobtypejson.get(p).toString());
                }
                Log.d(TAG, "responsefetch: jobpost-> " + jobpost);


                FetchAllJob_pojo fetchAllJob_pojo = new FetchAllJob_pojo();

                fetchAllJob_pojo.setId(jobpost.getString("id"));
                fetchAllJob_pojo.setUserId(jobpost.getString("user_id"));

                fetchAllJob_pojo.setName(jobpost.getString("name"));
                fetchAllJob_pojo.setRoleAgeMin(jobpost.getString("role_age_min"));
                fetchAllJob_pojo.setRoleAgeMax(jobpost.getString("role_age_max"));
                fetchAllJob_pojo.setHeightFeet(jobpost.getString("height_feet"));
                fetchAllJob_pojo.setHeightInches(jobpost.getString("height_inches"));
                fetchAllJob_pojo.setWeight(jobpost.getString("weight"));
                fetchAllJob_pojo.setAboutProject(jobpost.getString("about_project"));
                fetchAllJob_pojo.setAboutPersonality(jobpost.getString("about_personality"));
                fetchAllJob_pojo.setJobRatePerDay(jobpost.getString("job_rate_per_day"));
                fetchAllJob_pojo.setJobTitle(jobpost.getString("job_title"));
                fetchAllJob_pojo.setJobType(jobtypearray);


                fetchAllJob_pojo.setTotalRoles(jobpost.getString("total_roles"));
                fetchAllJob_pojo.setGenderType(jobpost.getString("gender_type"));
                fetchAllJob_pojo.setProductName(jobpost.getString("product_name"));
                fetchAllJob_pojo.setJobDuration(jobpost.getString("job_duration"));
                fetchAllJob_pojo.setRoleDescription(jobpost.getString("role_description"));
                fetchAllJob_pojo.setPerformanceLocation(jobpost.getString("performance_location"));
                fetchAllJob_pojo.setPerformanceFromDate(jobpost.getString("performance_from_date"));
                fetchAllJob_pojo.setPerformanceToDate(jobpost.getString("performance_to_date"));
                fetchAllJob_pojo.setClientInformation(jobpost.getString("client_information"));
                fetchAllJob_pojo.setSkill(skilla);
                fetchAllJob_pojo.setAdditionalAttachment(additionattacharray);

                fetchAllJob_pojo.setHairColor(hair_colorarray);
                fetchAllJob_pojo.setEyeColor(eye_colorarray);
                fetchAllJob_pojo.setHairLength(hair_lengtharray);
                fetchAllJob_pojo.setEthnicity(skiethnicityllarray);
                fetchAllJob_pojo.setCreatedAt(jobpost.getString("created_at"));
                fetchAllJob_pojo.setUpdatedAt(jobpost.getString("updated_at"));
                fetchAllJob_pojo.setStatus(jobpost.getString("status"));


                fetchAllJob_pojosarr.add(fetchAllJob_pojo);

            }
//            if(referesh_rv != null){
//                referesh_rv.setRefreshing(false);
//            }
            findJobsRecyclerView.hideShimmer();
            findJobs_adapter = new FindJobs_Adapter(fetchAllJob_pojosarr, getContext(), fave_jobids);
            findJobs_adapter.SetOnclickListener(this::onjobclick);
            findJobsLayoutManger = new LinearLayoutManager(getContext());
            findJobsRecyclerView.setLayoutManager(findJobsLayoutManger);
            findJobsRecyclerView.setAdapter(findJobs_adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void responseSavefetch(JSONObject jsonObject) {


        Log.d(TAG, "responseSavefetch: response save data jobs-> " + jsonObject);
        try {
            JSONArray detailobje = jsonObject.getJSONArray(ConstantString.DETAIL_TAG);
            for (int i = 0; i < detailobje.length(); i++) {
                JSONObject jobpost = (JSONObject) detailobje.get(i);

                JSONArray skillarray = jobpost.getJSONArray("skill");
                Log.d(TAG, "responsefetch: skillarray-> " + skillarray);
                ArrayList<String> skilla = new ArrayList<>();
                for (int p = 0; p < skillarray.length(); p++) {
                    skilla.add(skillarray.get(p).toString());
                }
                JSONArray additionattach = jobpost.getJSONArray("additional_attachment");
                ArrayList<String> additionattacharray = new ArrayList<>();
                for (int p = 0; p < additionattach.length(); p++) {
                    additionattacharray.add(additionattach.get(p).toString());
                }

                JSONArray hair_colorjson = jobpost.getJSONArray("hair_color");
                ArrayList<String> hair_colorarray = new ArrayList<>();
                for (int p = 0; p < hair_colorjson.length(); p++) {
                    hair_colorarray.add(hair_colorjson.get(p).toString());
                }
                JSONArray eye_colorjson = jobpost.getJSONArray("eye_color");
                ArrayList<String> eye_colorarray = new ArrayList<>();
                for (int p = 0; p < eye_colorjson.length(); p++) {
                    eye_colorarray.add(eye_colorjson.get(p).toString());
                }
                JSONArray hair_lengthjson = jobpost.getJSONArray("hair_length");
                ArrayList<String> hair_lengtharray = new ArrayList<>();
                for (int p = 0; p < hair_lengthjson.length(); p++) {
                    hair_lengtharray.add(hair_lengthjson.get(p).toString());
                }
                JSONArray skiethnicitylljson = jobpost.getJSONArray("ethnicity");
                ArrayList<String> skiethnicityllarray = new ArrayList<>();
                for (int p = 0; p < skiethnicitylljson.length(); p++) {
                    skiethnicityllarray.add(skiethnicitylljson.get(p).toString());
                }
                JSONArray jobtypejson = jobpost.getJSONArray("job_type");
                ArrayList<String> jobtypearray = new ArrayList<>();
                for (int p = 0; p < jobtypejson.length(); p++) {
                    jobtypearray.add(jobtypejson.get(p).toString());
                }

                FetchAllJob_pojo job_pojodetails = new FetchAllJob_pojo();
                job_pojodetails.setId(jobpost.getString("id"));
                job_pojodetails.setUserId(jobpost.getString("user_id"));
                job_pojodetails.setRoleAgeMin(jobpost.getString("role_age_min"));
                job_pojodetails.setRoleAgeMax(jobpost.getString("role_age_max"));
                job_pojodetails.setHeightFeet(jobpost.getString("height_feet"));
                job_pojodetails.setHeightInches(jobpost.getString("height_inches"));
                job_pojodetails.setWeight(jobpost.getString("weight"));
                job_pojodetails.setAboutProject(jobpost.getString("about_project"));
                job_pojodetails.setAboutPersonality(jobpost.getString("about_personality"));
                job_pojodetails.setJobRatePerDay(jobpost.getString("job_rate_per_day"));
                job_pojodetails.setSkill(skilla);
                job_pojodetails.setEthnicity(skiethnicityllarray);

                job_pojodetails.setHairColor(hair_colorarray);
                job_pojodetails.setHairLength(eye_colorarray);
                job_pojodetails.setEyeColor(hair_lengtharray);
                job_pojodetails.setJobTitle(jobpost.getString("job_title"));
                job_pojodetails.setJobType(jobtypearray);
                job_pojodetails.setTotalRoles(jobpost.getString("total_roles"));
                job_pojodetails.setGenderType(jobpost.getString("gender_type"));

                job_pojodetails.setProductName(jobpost.getString("product_name"));
                job_pojodetails.setJobDuration(jobpost.getString("job_duration"));
                job_pojodetails.setRoleDescription(jobpost.getString("role_description"));
                job_pojodetails.setPerformanceLocation(jobpost.getString("performance_location"));
                job_pojodetails.setPerformanceFromDate(jobpost.getString("performance_from_date"));
                job_pojodetails.setPerformanceToDate(jobpost.getString("performance_to_date"));
                job_pojodetails.setClientInformation(jobpost.getString("client_information"));


                job_pojodetails.setAdditionalAttachment(additionattacharray);
                job_pojodetails.setCreatedAt(jobpost.getString("created_at"));
                job_pojodetails.setUpdatedAt(jobpost.getString("updated_at"));
                job_pojodetails.setStatus(jobpost.getString("status"));


//                FetchAllJob_pojo jobdetail =
//                        new FetchAllJob_pojo(
//                                jobpost.getString("id"),
//                                jobpost.getString("user_id"),
//                                jobpost.getString("role_age_min"),
//                                jobpost.getString("role_age_max"),
//                                jobpost.getString("height_feet"),
//                                jobpost.getString("height_inches"),
//                                jobpost.getString("weight"),
//                                jobpost.getString("about_project"),
//                                jobpost.getString("about_personality"),
//                                jobpost.getString("job_rate_per_day"),
//                                skilla,
//                                skiethnicityllarray,
//                                hair_colorarray,
//                                eye_colorarray,
//                                hair_lengtharray,
//                                jobpost.getString("job_title"),
//                                jobpost.getString("job_type"),
//                                jobpost.getString("total_roles"),
//                                jobpost.getString("gender_type"),
//                                jobpost.getString("product_name"),
//                                jobpost.getString("job_duration"),
//                                jobpost.getString("role_description"),
//                                jobpost.getString("performance_location"),
//                                jobpost.getString("performance_from_date"),
//                                jobpost.getString("performance_to_date"),
//                                jobpost.getString("client_information"),
//                                additionattacharray,
//                                jobpost.getString("created_at"),
//                                jobpost.getString("updated_at"),
//                                jobpost.getString("status")
//
//                        );
                getGellAllsaveJobsPostOPJOS.add(job_pojodetails);


            }
            Log.d(TAG, "responseSavefetch: " + getGellAllsaveJobsPostOPJOS);
            findJobsRecyclerView.hideShimmer();
            if (getGellAllsaveJobsPostOPJOS.isEmpty()) {
                shownojob();
            } else {
                hidenojob();
                saveJobS_adapter = new SaveJobS_Adapter(getGellAllsaveJobsPostOPJOS, getContext());
                saveJobS_adapter.SetOnclickListener(this::onjobclick);
                saveJobS_adapter.CheckOnEmpty_Recyclerview(this::value_emptycheck);
                savedJobsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                savedJobsRecyclerView.setAdapter(saveJobS_adapter);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onjobclick(int i) {
        if (click == 0) {
            Intent intent = new Intent(getContext(), Single_JobDetail_Activity.class);
//        gellAllJobsPostOPJOS
            Log.d(TAG, "onjobclick: model_getHired ");
            if (fetchAllJob_pojosarr != null) {
                Log.d(TAG, "OnlistSingleListener: gellAlljobs -> " + fetchAllJob_pojosarr.get(i));
                intent.putExtra("Single_job", fetchAllJob_pojosarr.get(i));
            }

            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            click = 1;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fetchFindJob_list != null) {
            fetchFindJob_list.cancel();
        }
        if (fetchSaveJob_list != null) {
            fetchSaveJob_list.cancel();
        }

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver,new IntentFilter("ScrollToTop"));
         click = 0;
    }

    private void showvaldationError(String msg, int res) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg, res);
        bottomSheet_for_error.setCancelable(false);
        bottomSheet_for_error.show(getActivity().getSupportFragmentManager(), "error bottom");

    }

    @Override
    public void value_emptycheck(int value) {
        if (value >= 0) {
            shownojob();
        } else {
            hidenojob();
        }
    }

    private void shownojob() {
        refresh.setRefreshing(false);
        refresh.setEnabled(false);
        nojobsave.setVisibility(View.VISIBLE);
        savedJobsRecyclerView.setVisibility(View.GONE);
    }


    private void hidenojob() {
        nojobsave.setVisibility(View.GONE);
        savedJobsRecyclerView.setVisibility(View.VISIBLE);
    }


    private BroadcastReceiver  broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            findJobsRecyclerView.smoothScrollToPosition(0);
            if(findJobs_adapter != null){
                findJobs_adapter.notifyDataSetChanged();
            }

        }
    };

}
