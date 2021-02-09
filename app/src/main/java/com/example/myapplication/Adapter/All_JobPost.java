package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.Interface.DeleteJobPostListener;
import com.example.myapplication.clientsprofile.clients_activity.Clients_ViewTheirFullJobDetailsAndProposals;
import com.example.myapplication.pojo.JobPostdetail_pojo;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import org.jivesoftware.smack.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class All_JobPost extends RecyclerView.Adapter<All_JobPost.JobPostViewHolder> {
    private ArrayList<JobPostdetail_pojo> alljobpost;
    private Context context;

    private String TAG = "All_JobPost";
    private DeleteJobPostListener deleteJobPostListener;

    public All_JobPost(ArrayList<JobPostdetail_pojo> alljobpost, Context context) {
        this.alljobpost = alljobpost;
        this.context = context;
    }
    public void setOnPostDeleteListener(DeleteJobPostListener onPostDeleteListener){
        this.deleteJobPostListener=  onPostDeleteListener;
    }

    @NonNull
    @Override
    public JobPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JobPostViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_alljobposts_item,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull JobPostViewHolder holder, int position) {
        holder.jobTitle.setText(alljobpost.get(position).getJob_title());
        holder.jobType.setText(alljobpost.get(position).getJob_type());
        holder.jobcompany.setText("Client: "+
                alljobpost.get(position).getClient_information());
        holder.jobRate.setText("Budget: $"+alljobpost.get(position).getJob_rate_per_day()+" per day");
        holder.viewjobdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Clients_ViewTheirFullJobDetailsAndProposals.class);
                intent.putExtra("Jobdetailsshow",alljobpost.get(position));
                context.startActivity(intent);
            }
        });

        holder.deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteJobPostListener.onPostDelete(position);

            }
        });
    }


    @Override
    public int getItemCount() {
        return alljobpost.size();
    }
    static class JobPostViewHolder extends RecyclerView.ViewHolder{
        TextView jobTitle;
        TextView jobType;
        TextView jobcompany;
        TextView jobRate;
        Button viewjobdetails;
        CardView card;
        ImageView deletePost;
        public JobPostViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.title);
            jobType = itemView.findViewById(R.id.type);
            jobcompany = itemView.findViewById(R.id.company);
            jobRate = itemView.findViewById(R.id.rate);
            viewjobdetails = itemView.findViewById(R.id.viewjobdetails);
            card = itemView.findViewById(R.id.card);
            deletePost =itemView.findViewById(R.id.delete_post);
        }
    }
}
