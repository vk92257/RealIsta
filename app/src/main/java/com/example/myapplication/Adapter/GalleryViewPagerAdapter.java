package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.annimon.stream.Objects;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_activity.View_proposal_userProfile;
import com.example.myapplication.modelsprofile.models_activity.ExploreImageFullshow;
import com.example.myapplication.pojo.ImageViewSinglefullsize;

import java.util.ArrayList;

import static com.example.myapplication.Adapter.GalleryViewAdapter.TAG;

public class GalleryViewPagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<String> images ;
    LayoutInflater layoutInflater;
    ImageViewSinglefullsize imageViewSinglefullsize ;

    public GalleryViewPagerAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public GalleryViewPagerAdapter(ImageViewSinglefullsize datafullimage, ExploreImageFullshow exploreImageFullshow) {
        this.context = exploreImageFullshow;
        this.imageViewSinglefullsize = datafullimage;
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // inflating the item.xml
        View itemView = layoutInflater.inflate(R.layout.explore_image_scroll, container, false);
        // referencing the image view from the item.xml file
        ImageView imageView = (ImageView) itemView.findViewById(R.id.image);
        // setting the image in the imageView
        Glide.with(context).load(Uri.parse(imageViewSinglefullsize.getData().get(position).getHd_images()))
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( imageView);
        // Adding the View
        Objects.requireNonNull(container).addView(itemView);
        return itemView;
    }

    @Override
    public int getCount() {
        return imageViewSinglefullsize.getData().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return  view == ((LinearLayout) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
