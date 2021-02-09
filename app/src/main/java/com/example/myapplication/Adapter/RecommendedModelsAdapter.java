package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
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

public class RecommendedModelsAdapter extends RecyclerView.Adapter<RecommendedModelsAdapter.LocationViewHolder> {

    private ArrayList<GetModelData> modelData;

    private Context context;

    public RecommendedModelsAdapter(ArrayList<GetModelData> modelData , Context context) {
        this.modelData = modelData;
        this.context = context;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocationViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_recommendedmodels_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
           GetModelData getModelData = modelData.get(position);
            holder.modelName.setText(getModelData.getName());
            holder.modelProfession.setText(getModelData.getProffesion());
            Glide.with(context).load(getModelData.getProfile_img())
                .thumbnail(0.5f)
                .error(R.mipmap.ic_launcher_r)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.modelImage);

        holder.root.setOnClickListener(View ->{
            Intent intent = new Intent(context, View_proposal_userProfile.class);
            intent.putExtra("user_id",getModelData.getUser_id());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return modelData.size();
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView modelName ,modelProfession;
        ImageView modelImage;
        CardView root ;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

           modelName= itemView.findViewById(R.id.name);
           modelProfession  = itemView.findViewById(R.id.profession);
           modelImage = itemView.findViewById(R.id.image);
            root = itemView.findViewById(R.id.card);
        }
    }
}
