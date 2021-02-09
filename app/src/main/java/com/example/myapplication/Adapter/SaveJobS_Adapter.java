package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.Interface.Empty_recyclerview_Onclick;
import com.example.myapplication.clientsprofile.Interface.Jobs_clickListener;
import com.example.myapplication.clientsprofile.Interface.SingleJobOnclick;
import com.example.myapplication.modelsprofile.models_activity.Single_JobDetail_Activity;
import com.example.myapplication.pojo.FetchAllJob_pojo;
import com.example.myapplication.pojo.Gell_all_jobs_postOPJO;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SaveJobS_Adapter extends  RecyclerView.Adapter<SaveJobS_Adapter.JObshowViewHolder> {

    private ArrayList<FetchAllJob_pojo> getAllSaveJobsPostOPJOS;
    private Context mcontext;
    public static final String TAG ="FindJobs_Adapter";
    public Jobs_clickListener singleJobOnclick;
    public Empty_recyclerview_Onclick empty_recyclerview_onclick;
    public SaveJobS_Adapter(ArrayList<FetchAllJob_pojo> getAllSaveJobsPostOPJOS, Context mcontext) {
        this.getAllSaveJobsPostOPJOS = getAllSaveJobsPostOPJOS;
        this.mcontext = mcontext;
    }
    public void SetOnclickListener(Jobs_clickListener singleJobOnclick){
        this.singleJobOnclick = singleJobOnclick;
    }
    public void CheckOnEmpty_Recyclerview(Empty_recyclerview_Onclick empty_recyclerview_onclick){
        this.empty_recyclerview_onclick = empty_recyclerview_onclick;
    }

    @NonNull
    @Override
    public SaveJobS_Adapter.JObshowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JObshowViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_gethired_jobslist_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SaveJobS_Adapter.JObshowViewHolder holder, int position) {
        String datestart= getAllSaveJobsPostOPJOS.get(position).getCreatedAt();

        holder.title.setText(getAllSaveJobsPostOPJOS.get(position).getJobTitle());
        holder.rate.setText("$"+ getAllSaveJobsPostOPJOS.get(position).getJobRatePerDay()+" per day");
        holder.duration.setText(getAllSaveJobsPostOPJOS.get(position).getJobDuration());
        holder.gender.setText(getAllSaveJobsPostOPJOS.get(position).getGenderType());
        holder.desc.setText(getAllSaveJobsPostOPJOS.get(position).getRoleDescription());
        holder.casting.setText(getAllSaveJobsPostOPJOS.get(position).getClientInformation());
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
//        Calendar cal = Calendar.getInstance();
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date c = Calendar.getInstance().getTime();
//        try {
//            Log.d(TAG, "onBindViewHolder: no parese value "+sdf.parse(datestart));
//            Date d = sdf.parse(datestart);
//
//            assert d != null;
//            Log.d(TAG, "onBindViewHolder: "+(c.getTime() - d.getTime())/(24*60*60*1000));
//            String date = String.valueOf((c.getTime() - d.getTime())/(24*60*60*1000));
//            holder.time.setText(date+" days");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        holder.time.setText(covertTimeToText(datestart));

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click on -> "+position);

                singleJobOnclick.onjobclick(position);
////                singleJobOnclick.OnlistSingleListener(position);
//                Intent intent = new Intent(mcontext, Single_JobDetail_Activity.class);
//                intent.putExtra("Single_job", getAllSaveJobsPostOPJOS.get(position));
//                mcontext.startActivity(intent);
//                mcontext.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("sushil", "onClick: ");
                if(!getAllSaveJobsPostOPJOS.isEmpty() && getAllSaveJobsPostOPJOS.size() >=position) {
                    Log.d(TAG, "onClick: position-> " + position + " id-> " + getAllSaveJobsPostOPJOS.get(position).getId());

                  holder.fav.setClickable(false);
                    volleySavejobRequest(getAllSaveJobsPostOPJOS.get(position).getId(), position,holder.fav);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return getAllSaveJobsPostOPJOS.size();
    }



    static class JObshowViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        FloatingActionButton fav;
        TextView rate;
        TextView duration;
        TextView gender;
        TextView desc;
        TextView casting;
        TextView time;
        LinearLayout root;

        public JObshowViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            fav = itemView.findViewById(R.id.fav);
            rate = itemView.findViewById(R.id.rate);
            duration = itemView.findViewById(R.id.duration);
            gender = itemView.findViewById(R.id.gender);
            desc = itemView.findViewById(R.id.desc);
            casting = itemView.findViewById(R.id.casting);
            time = itemView.findViewById(R.id.time);
            root = itemView.findViewById(R.id.root);
            root.setClickable(true);

        }
    }


    private void volleySavejobRequest(String favjob_id,int postion,FloatingActionButton favbtn){
        Log.d("sushil", "volleySavejobRequest: called");
        StringRequest favjobApi_Save = new StringRequest(StringRequest.Method.POST,
                ApiConstant.JOB_SAVE,
                (Response.Listener<String>) response ->{
                    Log.d(TAG, "vollyerccall_forjob_list: "+response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(!jsonObject.getBoolean(ConstantString.IS_ERROR)){
                            Log.d(TAG, "volleySavejobRequest: geAllsaveJobsPost size-> "+getAllSaveJobsPostOPJOS.size());

                            if(!getAllSaveJobsPostOPJOS.isEmpty() && getAllSaveJobsPostOPJOS.size() >=postion) {
                                favbtn.setClickable(true);
                                getAllSaveJobsPostOPJOS.remove(postion);
                            }


                            notifyDataSetChanged();
                            empty_recyclerview_onclick.value_emptycheck(getAllSaveJobsPostOPJOS.size());
                        }
                        else{
                            Log.d(TAG, "vollyerccall_forjob_list: "+jsonObject.getString(ConstantString.RESPONSE_MESSAGE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d(TAG, "volleySavejobRequest: error-> "+error );
                    showvaldationError(error.toString());
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> favjob_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(mcontext).getUserData();
                Log.d(TAG, "getHeaders: token value-> " + logindata.getToken());
                favjob_token.put("Authorization", "Bearer " + logindata.getToken());
                return favjob_token;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> favJobAPI_param = new HashMap<>();
                favJobAPI_param.put("jobId",favjob_id);

                return favJobAPI_param;
            }
        };
        VolleySingleton.getInstance(mcontext).addToRequestQueue(favjobApi_Save);
    }
    private void showvaldationError(String msg) {
        Toast.makeText(mcontext, msg, Toast.LENGTH_SHORT).show();


    }

    public String covertTimeToText(String date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String convTime = null;
        String prefix = "";
        String suffix = "Ago";
        Date nowTime = new Date();

        Date pasTime = null;
        try {
            pasTime = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long dateDiff = nowTime.getTime() - pasTime.getTime();
        long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
        long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
        long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
        long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);
        if (second < 60) {
            convTime = currentTime(date);
//            convTime = second + " Seconds " + suffix;
        } else if (minute < 60) {
            convTime =currentTime(date);
//            convTime = minute + " Minutes "+suffix;
        } else if (hour < 24) {
            convTime =currentTime(date);
//            convTime = hour + " Hours "+suffix;
        } else if (day >= 7) {
            if (day > 360) {
                convTime = (day / 360) + " Years " + suffix;
            } else if (day > 30) {
                convTime = (day / 30) + " Months " + suffix;
            } else {
                convTime = (day / 7) + " Week " + suffix;
            }
        } else if (day < 7) {
            convTime = day+" Days "+suffix;
        }
        return convTime;
    }
    public String currentTime(String data){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        String dateString = null;
        try {
            dateString = dateFormat.format(sdf.parse(data)).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  dateString;
    }
}
