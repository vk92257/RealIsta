package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.pojo.GetModelData;
import com.example.myapplication.pojo.Model_allContract_pojo;

import java.util.ArrayList;

public class ModelsBookingAdapter extends RecyclerView.Adapter<ModelsBookingAdapter.LocationViewHolder> {

    private ArrayList<Model_allContract_pojo> ModelData;
    private Context context;

    public ModelsBookingAdapter(ArrayList<Model_allContract_pojo> ModelData, Context context) {

        this.ModelData = ModelData;
        this.context = context;

    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocationViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_clients_contracts_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
//            holder.modelName.setText(name.get(position));
//            holder.modelProfession.setText(profession.get(position));
////            holder.modelName.setText(name.get(position));

    }

    @Override
    public int getItemCount() {
        return ModelData.size();
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
//        TextView modelName ,modelProfession;
//        ImageView modelImage;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

//           modelName= itemView.findViewById(R.id.name);
//           modelProfession  = itemView.findViewById(R.id.profession);
//           modelImage = itemView.findViewById(R.id.image);

        }
    }
}
