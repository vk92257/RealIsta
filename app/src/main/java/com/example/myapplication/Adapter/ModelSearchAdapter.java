package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_activity.View_proposal_userProfile;
import com.example.myapplication.pojo.GetModelData;

import java.util.ArrayList;

public class ModelSearchAdapter extends RecyclerView.Adapter<ModelSearchAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private ArrayList<GetModelData> list;
    private ArrayList<GetModelData> filterList;
    private String TAG="ModelSearchAdapter";

    public ModelSearchAdapter(Context context, ArrayList<GetModelData> list) {
        this.context = context;
        this.list = list;
        this.filterList = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_searchmodels_item,parent,false);
        return new MyViewHolder(view) ;
   }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GetModelData getModelData = filterList.get(position);
        if (getModelData!= null){
            holder.modelName.setText(getModelData.getName());
            holder.modelProfession.setText(getModelData.getProffesion()+" | "+getModelData.getGender());
            holder.modelLocation.setText(getModelData.getCity()+", "+getModelData.getState()+" | "+getModelData.getCountry());
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
        return filterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString  = charSequence.toString();
                    if (charString.isEmpty()){
                        filterList = list;
                    }else{
                        ArrayList<GetModelData> listToFilter = new ArrayList<>();
                        for (GetModelData data : list){
                            if (data.getName().toLowerCase().contains(charString.toLowerCase())){
                                    listToFilter.add(data);
                                Log.d(TAG, "performFiltering: " + data.getSkill());
                                }else{
                                for (String skill : data.getSkill())
                                    if (skill.toLowerCase().contains(charString.toLowerCase())){
                                        listToFilter.add(data);
                                        Log.d(TAG, "performFiltering: " + data.getSkill());
                                    }
                            }
//                            else if (data.getSkill().contains(charString)){
//                                            listToFilter.add(data);
//                                        Log.d(TAG, "performFiltering: " + data.getSkill());
//                                }
                        }
                        filterList = listToFilter;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filterList;
                    return filterResults ;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    filterList = (ArrayList<GetModelData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

   static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView modelName ,modelProfession,modelLocation,skills,pastWork,totalImages;
        ImageView profileImage;
        CardView root;
    public MyViewHolder(@NonNull View itemView) {
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
