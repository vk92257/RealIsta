package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.Interface.Jobs_clickListener;
import com.example.myapplication.clientsprofile.Interface.SingleJobOnclick;
import com.example.myapplication.pojo.FetchAllJob_pojo;
import com.example.myapplication.pojo.Gell_all_jobs_postOPJO;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.TimeAgo2;
import com.example.myapplication.utils.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class FindJobs_Adapter extends RecyclerView.Adapter<FindJobs_Adapter.JObshowViewHolder>{
    private ArrayList<FetchAllJob_pojo> gellAllJobsPostOPJOS;
    private Context mcontext;
    public static final String TAG ="FindJobs_Adapter";
    public Jobs_clickListener singleJobOnclick;
    private ArrayList<String> fave_jobid;


    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;



    public  FindJobs_Adapter(ArrayList<FetchAllJob_pojo> gellAllJobsPostOPJOS, Context mcontext,
                            ArrayList<String> fave_jobid) {
        this.gellAllJobsPostOPJOS = gellAllJobsPostOPJOS;
        this.mcontext = mcontext;
        this.fave_jobid = fave_jobid;
    }
    public void SetOnclickListener(Jobs_clickListener singleJobOnclick){
        this.singleJobOnclick = singleJobOnclick;
    }

    @NonNull
    @Override
    public JObshowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JObshowViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_gethired_jobslist_item,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull JObshowViewHolder holder, int position) {
        String datestart=gellAllJobsPostOPJOS.get(position).getCreatedAt();

            holder.title.setText(gellAllJobsPostOPJOS.get(position).getJobTitle());
            holder.rate.setText("$"+gellAllJobsPostOPJOS.get(position).getJobRatePerDay()+" per day");
        holder.duration.setText(gellAllJobsPostOPJOS.get(position).getJobDuration());
        holder.gender.setText(gellAllJobsPostOPJOS.get(position).getGenderType());
        holder.desc.setText(gellAllJobsPostOPJOS.get(position).getRoleDescription());
        holder.casting.setText(gellAllJobsPostOPJOS.get(position).getClientInformation());

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
        Log.d("cliop", "onBindViewHolder: datestart-> "+datestart);

        TimeAgo2 timeAgo2 = new TimeAgo2();
        holder.time.setText(timeAgo2.covertTimeToText(datestart));

        Log.d("time------> ", "changetime-> "+changetime(datestart));
        Log.d("time------> ", "onBindViewHolder: converttimeto -> "+covertTimeToText(datestart));

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click on -> "+position);

                singleJobOnclick.onjobclick(position);
            }
        });




        if(fave_jobid.contains(gellAllJobsPostOPJOS.get(position).getId())){
            holder.fav.setImageResource(R.drawable.ic_baseline_favorite_24);
            holder.fav.setTag(R.drawable.ic_baseline_favorite_24);
        }
        else{
            holder.fav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            holder.fav.setTag(R.drawable.ic_baseline_favorite_border_24);
        }

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((Integer) holder.fav.getTag() == R.drawable.ic_baseline_favorite_border_24 ){
                    holder.fav.setImageResource(R.drawable.ic_baseline_favorite_24);
                    holder.fav.setTag(R.drawable.ic_baseline_favorite_24);
                }
                else{
                    holder.fav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    holder.fav.setTag(R.drawable.ic_baseline_favorite_border_24);
                }
                Log.d(TAG, "onClick: position-> "+position+" id-> "+gellAllJobsPostOPJOS.get(position).getId());
                volleySavejobRequest(gellAllJobsPostOPJOS.get(position).getId(),holder.fav);
                holder.fav.setClickable(false);
            }

        });

    }

    @Override
    public int getItemCount() {
        return gellAllJobsPostOPJOS.size();
    }

    static class JObshowViewHolder extends RecyclerView.ViewHolder{
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

        }
    }

    private void volleySavejobRequest(String favjob_id, FloatingActionButton favebtn){
        Log.d(TAG, "volleySavejobRequest: called");
        StringRequest favjobApi_Save = new StringRequest(StringRequest.Method.POST,
                ApiConstant.JOB_SAVE,
                (Response.Listener<String>) response ->{
                    Log.d(TAG, "volleySavejobRequest: "+response);
                    favebtn.setClickable(true);
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
//            convTime = currentTime(date);
            convTime = second + " Seconds "  + suffix;
        } else if (minute < 60) {
//            convTime =currentTime(date);
            convTime = minute + " Minutes "+suffix;
        } else if (hour < 24) {
//            convTime =currentTime(date);
            convTime = hour + " Hours "+suffix;
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
//    public String currentTime(String data){
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
//        String dateString = null;
//        try {
//            dateString = dateFormat.format(sdf.parse(data)).toString();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return  dateString;
//    }
//
//
//    public static String getTimeAgo(long time, Context ctx) {
//        if (time < 1000000000000L) {
//            //if timestamp given in seconds, convert to millis time *= 1000;
//        }
//
//        long now = getCurrentTime(ctx);
//        if (time > now || time <= 0) {
//            return null;
//        }
//
//        // TODO: localize
//
//        final long diff = now - time;
//
//        if (diff < MINUTE_MILLIS) { return "just now"; }
//        else if (diff < 2 * MINUTE_MILLIS) { return "a minute ago"; }
//        else if (diff < 50 * MINUTE_MILLIS) { return diff / MINUTE_MILLIS + " minutes ago"; }
//        else if (diff < 90 * MINUTE_MILLIS) { return "an hour ago"; }
//        else if (diff < 24 * HOUR_MILLIS) { return diff / HOUR_MILLIS + " hours ago"; } else if (diff < 48 * HOUR_MILLIS) { return "yesterday"; }
//        else { return diff / DAY_MILLIS + " days ago"; }
//    }


    private String changetime(String timedata){
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date past = format.parse(timedata);
            Date now = new Date();
            assert past != null;
            long seconds=TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
//
//          System.out.println(TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) + " milliseconds ago");
//          System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago");
//          System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago");
//          System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago");

            if(seconds<60)
            {
                return seconds+" seconds ago";
//                System.out.println(seconds+" seconds ago");
            }
            else if(minutes<60)
            {
                return minutes+" minutes ago";
//                System.out.println(minutes+" minutes ago");
            }
            else if(hours<12)
            {
                return hours+" hours ago";
//                System.out.println(hours+" hours ago");
            }
            else
            {
                return days+" days ago";
//                System.out.println(days+" days ago");
            }
        }
        catch (Exception j){
            j.printStackTrace();
        }
        return "empty";
    }




}
