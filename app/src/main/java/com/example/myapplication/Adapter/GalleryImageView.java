package com.example.myapplication.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.Interface.ClickdeleteAttachment;
import com.example.myapplication.clientsprofile.Interface.ImageSelectfull;
import com.example.myapplication.pojo.ImageViewSinglefullsize;

import java.net.URI;
import java.util.ArrayList;

public class GalleryImageView extends RecyclerView.Adapter<GalleryImageView.ImagviewHolder> {
    private ArrayList<String> imagearray;
    private Context context;
    private ImageSelectfull clickdeleteAttachment;

    public GalleryImageView(ArrayList<String> imagearray, Context context) {
        this.imagearray = imagearray;
        this.context = context;
    }

    public void SetonimageclickListener(ImageSelectfull clickdeleteAttachment) {
        this.clickdeleteAttachment = clickdeleteAttachment;
    }

    @NonNull
    @Override
    public ImagviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImagviewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.gallery_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImagviewHolder holder, int position) {
//            holder.imageView.setImageURI(Uri.parse(imagearray.get(position)));
        Glide.with(context).load(Uri.parse(imagearray.get(position)))
                .thumbnail(0.5f)
                .error(R.mipmap.ic_launcher_r)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
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

    static class ImagviewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImagviewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
