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
import com.example.myapplication.modelsprofile.models_activity.Contracts_Accept_reject;
import com.example.myapplication.modelsprofile.models_activity.Model_client_view_details;
import com.example.myapplication.modelsprofile.models_activity.Models_ViewJobInvitation;
import com.example.myapplication.pojo.Model_allContract_pojo;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Models_contracts_item_Adapter extends RecyclerView.Adapter<Models_contracts_item_Adapter.Contracts_itemViewHolder>{
    private ArrayList<Model_allContract_pojo> model_allContract_pojosarry;
    private Context context;

    public Models_contracts_item_Adapter(ArrayList<Model_allContract_pojo> model_allContract_pojosarry, Context context) {
        this.model_allContract_pojosarry = model_allContract_pojosarry;
        this.context = context;
    }

    @NonNull
    @Override
    public Contracts_itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Contracts_itemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_models_contracts_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Contracts_itemViewHolder holder, int position) {
        int status = Integer.parseInt(model_allContract_pojosarry.get(position).getStatus());

        Log.e("models_contracts", "onBindViewHolder: "+model_allContract_pojosarry.get(position).getUserid() );

        if(status == 0){
            holder.invitationtype.setBackgroundResource(R.drawable.gradientbackred);
            holder.invitationtype.setText("INVITED");
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Models_ViewJobInvitation.class);
                    intent.putExtra("jobid",model_allContract_pojosarry.get(position).jobid);
                    intent.putExtra("contractID",model_allContract_pojosarry.get(position).contractid);
                    intent.putExtra("status",model_allContract_pojosarry.get(position).status);
                    intent.putExtra("userid",model_allContract_pojosarry.get(position).getUserid());
//                    intent.putExtra("userid",model_allContract_pojosarry.get(position).)
                    context.startActivity(intent);

//                    context.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                }
            });
        }
        else if(status == 1){
            holder.invitationtype.setBackgroundResource(R.drawable.gradientbackblue);
            holder.invitationtype.setText("HIRED");
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Model_client_view_details.class);
                    intent.putExtra("jobid",model_allContract_pojosarry.get(position).jobid);
                    intent.putExtra("contractID",model_allContract_pojosarry.get(position).contractid);
                    intent.putExtra("profileimage_client",model_allContract_pojosarry.get(position).getProfile_img());
                    context.startActivity(intent);
//                    context.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                }
            });

        }
        else if(status == 2){
            holder.invitationtype.setBackgroundResource(R.drawable.gradientback);
            holder.invitationtype.setText("REJECTED");
        }
        else if(status == 3){
//            holder.invitationtype.setBackground();
            holder.invitationtype.setText("CONTRACT END");
        }



        Glide.with(context).load(Uri.parse(model_allContract_pojosarry.get(position).getProfile_img()))
                .thumbnail(0.5f)
                .error(R.mipmap.ic_launcher_r)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.profileimage);

            holder.title.setText(model_allContract_pojosarry.get(position).getJob_title());
            holder.clientname.setText(model_allContract_pojosarry.get(position).getName());
            holder.invitationmessage.setText(model_allContract_pojosarry.get(position).getPosition());
            holder.clientname.setText("Booked by "+model_allContract_pojosarry.get(position).getProduct_name());
            holder.rate.setText(" $"+model_allContract_pojosarry.get(position).getFinal_price()+" per day");
            holder.duration.setText(model_allContract_pojosarry.get(position).getJob_duration());
//            holder.shootlocation.setText(model_allContract_pojosarry.get(position).getPerformance_location());
            holder.casting.setText(model_allContract_pojosarry.get(position).getName());
            holder.productname.setText(model_allContract_pojosarry.get(position).getProduct_name());
            if(status != 2){

            }

    }

    @Override
    public int getItemCount() {
        return model_allContract_pojosarry.size();
    }

    static class Contracts_itemViewHolder extends RecyclerView.ViewHolder{
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
        public Contracts_itemViewHolder(@NonNull View itemView) {
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
