package com.example.myapplication.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectAdapterRecyclerview  extends RecyclerView.Adapter<SelectAdapterRecyclerview.SelectItemViewHolder> {

    private ArrayList<String> selectedArray;
    @NonNull
    @Override
    public SelectItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class SelectItemViewHolder extends RecyclerView.ViewHolder{
        public SelectItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
