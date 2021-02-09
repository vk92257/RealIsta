package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.Interface.ClickdeleteAttachment;
import com.example.myapplication.clientsprofile.Interface.ImageSelectfull;
import com.example.myapplication.modelsprofile.models_activity.GalleryImageFullshow;
import com.example.myapplication.pojo.ImageViewSinglefullsize;

import java.util.ArrayList;

public class GalleryViewAdapter extends RecyclerView.Adapter<GalleryViewAdapter.GalleyImageViewAdapter>{
    private ArrayList<String > imagearray;
    private Context context;
    private ImageSelectfull clickdeleteAttachment;
    public static final String TAG = "GalleryViewAdapter";
    public GalleryViewAdapter(ArrayList<String> imagearray, Context context) {
        this.imagearray = imagearray;
        this.context = context;
    }

    public void SetonimageclickListener(ImageSelectfull clickdeleteAttachment) {
        this.clickdeleteAttachment = clickdeleteAttachment;
    }


    @NonNull
    @Override
    public GalleyImageViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_gallery_item_horizontal,parent,false);
        return new GalleyImageViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleyImageViewAdapter holder, int position) {
//           holder.gImageview.setImageURI(Uri.parse(imagearray.get(position)));
        Glide.with(context).load(Uri.parse(imagearray.get(position)))
                .thumbnail(0.5f)
                .error(R.mipmap.ic_launcher_r)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( holder.gImageview);
        holder.gImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickdeleteAttachment.onclickimage(position, imagearray.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagearray.size();
    }

    static class GalleyImageViewAdapter extends RecyclerView.ViewHolder {
        ImageView gImageview;
        public GalleyImageViewAdapter(@NonNull View itemView) {
            super(itemView);
            gImageview = itemView.findViewById(R.id.image);
        }
    }
}
