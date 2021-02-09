package com.example.myapplication.Adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ModelsMultipleimageAdapter extends RecyclerView.Adapter<ModelsMultipleimageAdapter.ImageMultipleViewHolder> {
    private  ArrayList<Uri> mMultipleimage;
     String TAG = "ModelMultipleimageAdapter";

    public ModelsMultipleimageAdapter(ArrayList<Uri> mMultipleimage) {
        this.mMultipleimage = mMultipleimage;
    }

    @NonNull
    @Override
    public ImageMultipleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageMultipleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_gallery_item_editprofile,parent,false));
    }
    public ArrayList<Uri> getlist(){
        return this.mMultipleimage;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageMultipleViewHolder holder, int position) {
            holder.image.setImageURI(mMultipleimage.get(position));
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMultipleimage.remove(position);
                    notifyDataSetChanged();
                }
            });
    }

    @Override
    public int getItemCount() {
        return mMultipleimage.size();
    }

    static class ImageMultipleViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        ImageView remove;
        public ImageMultipleViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            remove = itemView.findViewById(R.id.remove);
        }
    }
}
