package com.example.myapplication.clientsprofile.clients_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Adapter.All_JobPost;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.Interface.DeleteJobPostListener;
import com.example.myapplication.clientsprofile.clients_activity.Clients_CreateJobPost;
import com.example.myapplication.pojo.JobPostdetail_pojo;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.todkars.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JobPosts extends Fragment implements View.OnClickListener, DeleteJobPostListener {

    private final static String TAG = "JobPosts";
    private TextView newjobpostbtn;
    private ShimmerRecyclerView rvall_JOb_post,joballpost_rv;
    private StringRequest fetchjob_list;
    private ArrayList<JobPostdetail_pojo> joblistallpojo;
    private All_JobPost all_jobPost;
    private LinearLayout nojobpostsyet_tv;
    private SwipeRefreshLayout referesh_option;
    private StringRequest delete_job_post;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newjobpost:
                Intent intent = new Intent(getActivity(), Clients_CreateJobPost.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("open", "onCreateView: ");
        return inflater.inflate(R.layout.clients_allcontractsfragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("open", "onStart: ");
        vollyerccall_forjob_list();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("open", "onViewCreated: ");
        initView(view);



    }
    private void initView(View view){
        newjobpostbtn = view.findViewById(R.id.newjobpost);
        referesh_option = view.findViewById(R.id.referesh_option);
//        rvall_JOb_post = view.findViewById(R.id.rv_allcontracts);
        joballpost_rv = view.findViewById(R.id.rv_alljobposts);
        nojobpostsyet_tv = view.findViewById(R.id.nojobpostsyet);
        newjobpostbtn.setOnClickListener(this);
        referesh_option.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                vollyerccall_forjob_list();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void vollyerccall_forjob_list(){
        joblistallpojo = new ArrayList<>();
        joballpost_rv.showShimmer();
        Log.d(TAG, "vollyerccall_forjob_list: ");
        fetchjob_list = new StringRequest(Request.Method.GET,
                ApiConstant.GET_ALL_JOB_BY_CLIENT,
                (Response.Listener<String>) response ->{
                    Log.d(TAG, "vollyerccall_forjob_list: \n\n\n\n\n"+response+" \n\n\n\n\n\n\n\n\n\n");
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(!jsonObject.getBoolean(ConstantString.IS_ERROR)){
                            responsefetch(jsonObject);

                        }
                        else{
                            joballpost_rv.hideShimmer();
                            Log.d(TAG, "vollyerccall_forjob_list: "+jsonObject.getString(ConstantString.RESPONSE_MESSAGE));
                            nojobpostshow();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(getContext()).getUserData();
                Log.d(TAG, "getHeaders: token value-> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(fetchjob_list);
    }

    private void responsefetch(JSONObject jsonObject) {
        try {
//            Log.d(TAG, "responsefetch: jsonobject-> "+ jsonObject.getJSONObject(ConstantString.DETAIL_TAG));
            JSONArray detailobjec = jsonObject.getJSONArray(ConstantString.DETAIL_TAG);
            Log.d(TAG, "responsefetch: jsonobject-> "+detailobjec);
            for(int i=0;i<detailobjec.length();i++){
                JSONObject jobpost = (JSONObject) detailobjec.get(i);
                Log.d(TAG, "responsefetch: jobpost-> "+jobpost);

                JSONArray skillarray = jobpost.getJSONArray("skill");
                Log.d(TAG, "responsefetch: skillarray-> "+skillarray);
                ArrayList<String> skilla = new ArrayList<>();
                for(int p=0;p<skillarray.length();p++){
                    skilla.add(skillarray.get(p).toString());
                }
                JSONArray additionattach = jobpost.getJSONArray("additional_attachment");
                ArrayList<String> additionattacharray = new ArrayList<>();
                Log.d(TAG, "responsefetch: additionattach-> "+additionattach+" attachment size-> "+additionattach);
                for(int p=0;p<additionattach.length();p++){
                    additionattacharray.add(additionattach.get(p).toString());
                }

                JSONArray hair_colorjson = jobpost.getJSONArray("hair_color");
                ArrayList<String> hair_colorarray = new ArrayList<>();
                for(int p=0;p<hair_colorjson.length();p++){
                    hair_colorarray.add(hair_colorjson.get(p).toString());
                }
                JSONArray eye_colorjson = jobpost.getJSONArray("eye_color");
                ArrayList<String> eye_colorarray = new ArrayList<>();
                for(int p=0;p<eye_colorjson.length();p++){
                    eye_colorarray.add(eye_colorjson.get(p).toString());
                }
                JSONArray hair_lengthjson = jobpost.getJSONArray("hair_length");
                ArrayList<String> hair_lengtharray = new ArrayList<>();
                for(int p=0;p<hair_lengthjson.length();p++){
                    hair_lengtharray.add(hair_lengthjson.get(p).toString());
                }
                JSONArray skiethnicitylljson = jobpost.getJSONArray("ethnicity");
                ArrayList<String> skiethnicityllarray = new ArrayList<>();
                for(int p=0;p<skiethnicitylljson.length();p++){
                    skiethnicityllarray.add(skiethnicitylljson.get(p).toString());
                }
                Log.d(TAG, "responsefetch: postarray-> "+jobpost.getString("job_type"));


                JobPostdetail_pojo jobdetail =
                        new JobPostdetail_pojo(jobpost.getString("job_id"),
                                jobpost.getString("user_id"),
                                jobpost.getString("name"),
                                jobpost.getString("role_age_min"),
                                jobpost.getString("role_age_max"),
                                jobpost.getString("height_feet"),
                                jobpost.getString("height_inches"),
                                jobpost.getString("weight"),
                                jobpost.getString("about_project"),
                                jobpost.getString("about_personality"),
                                jobpost.getString("job_rate_per_day"),
                                jobpost.getString("job_title"),
                                jobpost.getString("job_type"),
                                jobpost.getString("total_roles"),
                                jobpost.getString("gender_type"),
                                jobpost.getString("product_name"),
                                jobpost.getString("job_duration"),
                                jobpost.getString("role_description"),
                                jobpost.getString("performance_location"),
                                jobpost.getString("performance_from_date"),
                                jobpost.getString("performance_to_date"),
                                jobpost.getString("client_information"),
                                skilla,
                                additionattacharray,
                                hair_colorarray,
                                eye_colorarray,
                                hair_lengtharray,
                                skiethnicityllarray,
                                jobpost.getString("created_at"),
                                jobpost.getString("updated_at")
                                );
                joblistallpojo.add(jobdetail);

            }
            if(referesh_option != null){
                referesh_option.setRefreshing(false);
            }

            if(joblistallpojo.isEmpty()){
                nojobpostshow();
            }
            else{
                nojobposthide();
                all_jobPost = new All_JobPost(joblistallpojo,getContext());
                all_jobPost.setOnPostDeleteListener(this::onPostDelete);
                joballpost_rv.setLayoutManager(new LinearLayoutManager(getContext()));
                joballpost_rv.setAdapter(all_jobPost);
                joballpost_rv.hideShimmer();
                all_jobPost.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void showvaldationError(String msg) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg);
        bottomSheet_for_error.setCancelable(false);
        assert getFragmentManager() != null;
        bottomSheet_for_error.show(getFragmentManager(), "error bottom");

    }

    private void nojobpostshow(){
        nojobpostsyet_tv.setVisibility(View.VISIBLE);
        referesh_option.setVisibility(View.GONE);
    }

    private void nojobposthide(){
        nojobpostsyet_tv.setVisibility(View.GONE);
        referesh_option.setVisibility(View.VISIBLE);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d("open", "onPause: ");
        if(fetchjob_list!=null){
            fetchjob_list.cancel();
        }
        if(delete_job_post!=null){
            delete_job_post.cancel();
        }
    }

    @Override
    public void onStop() {
        Log.d("open", "onStop: ");
        super.onStop();
        if(fetchjob_list!=null){
            fetchjob_list.cancel();
        }
    }
    private void deleteVolleyPostRequest(String job_id,int postion) {
        delete_job_post = new StringRequest(Request.Method.POST,
                ApiConstant.DELETE_JOB_POST,
                (Response.Listener<String>) response ->{
                    Log.d(TAG, "initCallApi: response-> "+response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(!jsonObject.getBoolean(ConstantString.IS_ERROR)){
                            joblistallpojo.remove(postion);
                            all_jobPost.notifyDataSetChanged();
                            if(joblistallpojo.isEmpty()){
                                nojobpostshow();
                            }
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
                exploreparam.put("jobId",job_id);
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(delete_job_post);
    }

    @Override
    public void onPostDelete(int position) {

        PowerMenu  powerMenu =new PowerMenu.Builder(getActivity())
                .setHeaderView(R.layout.layout_dialog_header) // header used for title
                .setFooterView(R.layout.layout_dialog_footer) // footer used for yes and no buttons
                .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setWidth(700)
                .setSelectedEffect(false)
                .build();
                powerMenu.showAtCenter(getView());

                View footer  = powerMenu.getFooterView();
        Button yes = footer.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    deleteJobPostListener.onPostDelete(position);
                deleteVolleyPostRequest(joblistallpojo.get(position).getJob_id(),position);
                powerMenu.dismiss();
            }
        });

        Button no = footer.findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                powerMenu.dismiss();
            }
        });


    }
}
