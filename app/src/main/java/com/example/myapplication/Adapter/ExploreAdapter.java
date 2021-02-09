package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.modelsprofile.models_activity.ExploreImageFullshow;
import com.example.myapplication.modelsprofile.models_activity.GalleryImageFullshow;
import com.example.myapplication.pojo.Client_Explore_detailfetch;
import com.example.myapplication.pojo.ImageViewSinglefullsize;

import java.util.ArrayList;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ExploreImageViewHolder>  {

    private ArrayList<Client_Explore_detailfetch> cexplorefetchs;
    private Context context ;
    private String TAG = "ExploreAdapter";

    public ExploreAdapter(ArrayList<Client_Explore_detailfetch> cexplorefetchs, Context context) {
        this.cexplorefetchs = cexplorefetchs;
        this.context = context;
    }

    @NonNull
    @Override
    public ExploreImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExploreImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_image,parent,false));
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull ExploreImageViewHolder holder, int position) {
        Glide.with(context).load(Uri.parse(cexplorefetchs.get(position).getHd_images()))
                .thumbnail(0.5f)
                .error(R.mipmap.ic_launcher_r)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExploreImageFullshow.class);
                ImageViewSinglefullsize imageViewSinglefullsize= new ImageViewSinglefullsize(
                        cexplorefetchs.get(position).getProfile_img(),
                        cexplorefetchs.get(position).getHd_images(),
                        cexplorefetchs.get(position).getName(),
                        cexplorefetchs.get(position).getProffesion()
                );
                imageViewSinglefullsize.setPostion(position);
                imageViewSinglefullsize.setData(cexplorefetchs);
                imageViewSinglefullsize.setUserId(cexplorefetchs.get(position).getUser_id());
                Log.e(TAG, "viewprofile: adapter "+imageViewSinglefullsize.getUserId());
                intent.putExtra("fullimagdetail", imageViewSinglefullsize);

                /** sushil change 12/17/2020*/
//                intent.putExtra("postionClick",position);

//                intent.putExtra("user_id",imageViewSinglefullsize.getUserId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cexplorefetchs.size();
    }

    static class ExploreImageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ExploreImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
