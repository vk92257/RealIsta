package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_activity.Clients_ViewFullJobDetails;
import com.example.myapplication.modelsprofile.models_activity.Model_client_view_details;
import com.example.myapplication.modelsprofile.models_activity.Models_ViewJobInvitation;
import com.example.myapplication.pojo.Client_booking_detailsPojo;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class Client_Booking_Adapter  extends RecyclerView.Adapter<Client_Booking_Adapter.TalentDetail_ViewHolder> {

    private ArrayList<Client_booking_detailsPojo> client_booking_detailsPojos_arr;
    private Context context ;
    private String TAG = "Client_Booking_Adapter";

    public Client_Booking_Adapter(ArrayList<Client_booking_detailsPojo> client_booking_detailsPojos_arr, Context context) {
        this.client_booking_detailsPojos_arr = client_booking_detailsPojos_arr;
        this.context = context;
    }

    @NonNull
    @Override
    public TalentDetail_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TalentDetail_ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_models_contracts_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TalentDetail_ViewHolder holder, int position) {
        int status = Integer.parseInt(client_booking_detailsPojos_arr.get(position).getStatus());



      if(status == 1){
            holder.invitationtype.setBackgroundResource(R.drawable.gradientbackblue);
            holder.invitationtype.setText("BOOKED");
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context, Model_client_view_details.class);
//                    intent.putExtra("jobid",model_allContract_pojosarry.get(position).jobid);
//                    intent.putExtra("profileimage_client",model_allContract_pojosarry.get(position).getProfile_img());
//                    context.startActivity(intent);
//                    context.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                }
            });

        }
        else if(status == 3){
            holder.invitationtype.setBackgroundResource(R.drawable.gradientbackred);
            holder.invitationtype.setText("ENDED");
        }


        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Clients_ViewFullJobDetails.class);

                intent.putExtra("talentdetail",client_booking_detailsPojos_arr.get(position));
                context.startActivity(intent);

            }
        });


        Log.d(TAG, "onBindViewHolder:profile image->  "+
                client_booking_detailsPojos_arr.get(position).getProfile_img());

        Glide.with(context).load(client_booking_detailsPojos_arr.get(position).getProfile_img())
                .thumbnail(0.5f)
                .error(R.mipmap.ic_launcher_r)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.profileimage);

        holder.title.setText(client_booking_detailsPojos_arr.get(position).getName());
        holder.clientname.setText(client_booking_detailsPojos_arr.get(position).getJobTitle());
        holder.invitationmessage.setVisibility(View.GONE);


        holder.clientname.setText(
                client_booking_detailsPojos_arr.get(position).getJobTitle());
        holder.rate.setText(" $"+client_booking_detailsPojos_arr
                .get(position).getFinal_price());
        holder.duration.setText(client_booking_detailsPojos_arr.get(position).getJobDuration());
//            holder.shootlocation.setText(model_allContract_pojosarry.get(position).getPerformance_location());
        holder.casting.setText(client_booking_detailsPojos_arr.get(position).getClientInformation());
        holder.productname.setText(client_booking_detailsPojos_arr.get(position).getProductName());


    }

    @Override
    public int getItemCount() {
        Log.d("data", "getItemCount: "+client_booking_detailsPojos_arr.size());
        return client_booking_detailsPojos_arr.size();
    }

    static class TalentDetail_ViewHolder extends RecyclerView.ViewHolder{
        TextView invitationtype;
        LinearLayout root;
        CircularImageView profileimage;
        TextView title;
        TextView clientname;
        TextView casting;
        TextView invitationmessage;
        TextView rate;
        TextView duration;
        TextView shootlocation;
        TextView productname;
        public TalentDetail_ViewHolder(@NonNull View itemView) {
            super(itemView);
            invitationtype = itemView.findViewById(R.id.invitationtype);
            root = itemView.findViewById(R.id.root);
            profileimage = itemView.findViewById(R.id.profileimage);
            title = itemView.findViewById(R.id.title);
            clientname = itemView.findViewById(R.id.clientname);
            casting = itemView.findViewById(R.id.casting);
            invitationmessage = itemView.findViewById(R.id.invitationmessage);
            rate = itemView.findViewById(R.id.rate);
            duration = itemView.findViewById(R.id.duration);
            shootlocation = itemView.findViewById(R.id.shootinglocation);
            productname = itemView.findViewById(R.id.productname);
        }
    }
}
