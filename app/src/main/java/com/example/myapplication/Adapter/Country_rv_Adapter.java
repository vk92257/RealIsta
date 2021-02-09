package com.example.myapplication.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.Interface.SingleJobOnclick;
import com.example.myapplication.pojo.locationpojo;

import java.util.ArrayList;
import java.util.List;

public class Country_rv_Adapter extends
        RecyclerView.Adapter<Country_rv_Adapter.LocationViewHolder>
implements Filterable {

    private ArrayList<locationpojo> exampleList;
    private ArrayList<locationpojo> exampleListFull;
    private Context context;
    private SingleJobOnclick onclicksingle;
    public static final String TAG = "Country_rv_adapter ";

    public Country_rv_Adapter(ArrayList<locationpojo> locationarray, Context context) {
        this.exampleList = locationarray;
        this.context = context;
        exampleListFull = new ArrayList<>(exampleList);
    }

    public void Setoncountrylistener(SingleJobOnclick onclicksingle){
        this.onclicksingle = onclicksingle;
    }
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocationViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_text_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {

        holder.locationid.setText(exampleList.get(position).getLocation_name());
        holder.locationid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,exampleList.get(position).getLocation_name()+" selected",Toast.LENGTH_SHORT).show();
                onclicksingle.OnlistSingleListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView locationid;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            locationid = itemView.findViewById(R.id.locationid);

        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }


    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<locationpojo> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (locationpojo item : exampleListFull) {
                    if (item.getLocation_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
