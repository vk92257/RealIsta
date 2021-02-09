package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_activity.View_proposal_userProfile;
import com.example.myapplication.pojo.GetModelData;

import java.util.ArrayList;

public class PopularModelsAdapter extends RecyclerView.Adapter<PopularModelsAdapter.LocationViewHolder> {

    private ArrayList<GetModelData> ModelData;
    private Context context;

    public PopularModelsAdapter(ArrayList<GetModelData> ModelData, Context context) {
        this.ModelData = ModelData;
        this.context = context;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocationViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_searchmodels_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        GetModelData getModelData = ModelData.get(position);
        if (getModelData!= null){
            holder.modelName.setText(getModelData.getName());
            holder.modelProfession.setText(getModelData.getProffesion()+" | "+getModelData.getGender());
            if(getModelData.getCity().toLowerCase().equals("null")){
                if(getModelData.getState().toLowerCase().equals("null")){
                    holder.modelLocation.setText(getModelData.getCountry());
                }
                else{
                    holder.modelLocation.setText(getModelData.getState()+" | "+getModelData.getCountry());
                }

            }
            else if(getModelData.getState().toLowerCase().equals("null")){
                holder.modelLocation.setText(getModelData.getCountry());
            }
            else{
                holder.modelLocation.setText(getModelData.getCity()+", "+getModelData.getState()+" | "+getModelData.getCountry());
            }

            String skill = "";
        if (getModelData.getSkill()!= null){
            for (int i = 0 ;i<getModelData.getSkill().size();i++){
                if (i == getModelData.getSkill().size()-1){
                    skill += getModelData.getSkill().get(i);
                }else{
                    skill += getModelData.getSkill().get(i)+",";
                }
            }
        }
            holder.skills.setText(skill);
            holder.totalImages.setText(getModelData.getImgCount());
            holder.pastWork.setText(getModelData.getPersonal_journey());
            Glide.with(context).load(getModelData.getProfile_img())
                    .thumbnail(0.5f)
                    .error(R.mipmap.ic_launcher_r)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.profileImage);
            holder.root.setOnClickListener(View ->{
                Intent  intent = new Intent(context, View_proposal_userProfile.class);
                intent.putExtra("user_id",getModelData.getUser_id());
                context.startActivity(intent);
            });
        }

    }

    @Override
    public int getItemCount() {
        return ModelData.size();
    }
    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView modelName ,modelProfession,modelLocation,skills,pastWork,totalImages;
        ImageView profileImage;
        CardView root;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            modelName= itemView.findViewById(R.id.name);
            totalImages = itemView.findViewById(R.id.totalimages);
           modelProfession  = itemView.findViewById(R.id.professiontitle);
           profileImage = itemView.findViewById(R.id.profileimage);
           modelLocation = itemView.findViewById(R.id.location);
           skills = itemView.findViewById(R.id.skills);
           pastWork = itemView.findViewById(R.id.pastWork);
           root = itemView.findViewById(R.id.card);
        }
    }
}
