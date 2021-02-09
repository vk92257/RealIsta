package com.example.myapplication.modelsprofile.models_fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.myapplication.Adapter.GalleryImageView;
import com.example.myapplication.Adapter.GalleryViewAdapter;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.Interface.ImageSelectfull;
import com.example.myapplication.modelsprofile.models_activity.GalleryViewImageModel;
import com.example.myapplication.modelsprofile.models_activity.ModelEditProfile_Activity;
import com.example.myapplication.pojo.GetModelData;
import com.example.myapplication.pojo.ImageViewSinglefullsize;
import com.example.myapplication.pojo.locationpojo;
import com.example.myapplication.utils.BottomSheettl;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.flipkart.youtubeview.YouTubePlayerView;
import com.flipkart.youtubeview.activity.YouTubeActivity;
import com.flipkart.youtubeview.listener.YouTubeEventListener;
import com.flipkart.youtubeview.models.ImageLoader;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Model_Portfolio extends Fragment implements View.OnClickListener, ImageSelectfull {

    private CircularImageView profileimage;
    private TextView fullname;
    private TextView professionltitle;
    private TextView location;
    private RecyclerView rv_recyelrview;
    private TextView pro_journey;
    private TextView bio;
    private TextView idelageforrole;
    private TextView skillText;
    private TextView languagetxt;
    private TextView bodyApperancetxt;
    private TextView heighttxt;
    private TextView chesttxt;
    private TextView waisttxt;
    private TextView hiptxt;
    private TextView ethnicitytxt;
    private TextView editdata;
    private GetModelData ModelData;
    private GalleryViewAdapter imageadapter;
    private ImageView menuoptions;
    private LinearLayout galleryview;
    private TextView gallerytxt;
    private View underlinegalleryview;
    private LinearLayout portfolioview;
    private TextView portfoliotxt;
    private LinearLayout portfolioll;
    private LinearLayout galleryll;
    private View underlineportfolioview;
    private StringRequest login_client_request;
    private TextView gallerytv;
    private TextView youtubevideoShow;
    private FrameLayout youTubeVideo;
    private TextView youTubeTitle;
    private ImageView modelVideo;
    private ImageView playVideo;
    private ImageView  youTubeBack;
    private RecyclerView gallery_Rv;
    private static final String TAG = "Model_Portfolio";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                if (ModelData != null) {
                    Intent intent = new Intent(getContext(),
                            ModelEditProfile_Activity.class);
                    intent.putExtra("modelData", ModelData);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                } else {
                    Toast.makeText(getContext(), "Nothing to show!! ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.optionsmenu:
                BottomSheettl bottomSheettl = new BottomSheettl();
                assert getFragmentManager() != null;
                bottomSheettl.show(getFragmentManager(), "MOdel");
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: Enter in Model_Portfolio");
        intiview(view);
        protfolioviewopen();
        galleryview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryopen();
            }
        });
        portfolioview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                protfolioviewopen();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Gson gson = new Gson();
        String modeldata_detail = SharedPreferanceManager.getInstance(getContext()).get_Model_userInformation();
        ModelData  =  gson.fromJson(modeldata_detail, GetModelData.class);

//        Log.e("ModelsEditProfile_Activity --> modelport ", "onStart:  image url "+ModelData.getProfile_img() );
        Log.d(TAG, "onStart: getmodeldata fetched-> "+ModelData.getSkill());
        setViews(ModelData);
//        initCallApi();
    }
    @SuppressLint("CutPasteId")
    private void intiview(View view) {

//        gallerytv = view.findViewById(R.id.gallerytv);
        menuoptions = view.findViewById(R.id.optionsmenu);
        fullname = view.findViewById(R.id.name);
        profileimage = view.findViewById(R.id.profileimage);
        professionltitle = view.findViewById(R.id.professiontitle);
        location = view.findViewById(R.id.location);
        rv_recyelrview = view.findViewById(R.id.rv_horizontal);
        pro_journey = view.findViewById(R.id.professionaljourney);
        bio = view.findViewById(R.id.bio);
        idelageforrole = view.findViewById(R.id.idealageforrolemodel);
        skillText = view.findViewById(R.id.skills);
        languagetxt = view.findViewById(R.id.languages);

        bodyApperancetxt = view.findViewById(R.id.bodyappearance);
        heighttxt = view.findViewById(R.id.height);

        chesttxt = view.findViewById(R.id.chest);
        waisttxt = view.findViewById(R.id.waist);

        hiptxt = view.findViewById(R.id.hip);
        ethnicitytxt = view.findViewById(R.id.ethnicity);
        galleryview = view.findViewById(R.id.galleryview);
        gallerytxt = view.findViewById(R.id.gallerytv);
        underlinegalleryview = view.findViewById(R.id.underlinegalleryview);
        portfolioview = view.findViewById(R.id.portfolioview);
        portfoliotxt = view.findViewById(R.id.portfoliotv);
        underlineportfolioview = view.findViewById(R.id.underlineportfolioview);
        portfolioll = view.findViewById(R.id.portfolioll);
        galleryll = view.findViewById(R.id.galleryll);
        gallery_Rv = view.findViewById(R.id.rv_images);
        view.findViewById(R.id.galleryview);
        youtubevideoShow = view.findViewById(R.id.youtubevideoShow);
        youTubeTitle = view.findViewById(R.id.youtube_video_title);
        youTubeVideo = view.findViewById(R.id.youtube_video);
        modelVideo = view.findViewById(R.id.model_video);
        playVideo = view.findViewById(R.id.play_button);
        youTubeBack = view.findViewById(R.id.youtube_back);
        view.findViewById(R.id.edit).setOnClickListener(this::onClick);
        menuoptions.setOnClickListener(this::onClick);
    }

    String youTubeUrl;

    @SuppressLint("SetTextI18n")

    private void setViews(GetModelData modelData) {
        Log.d(TAG, "setViews: get in setviews ");
        Log.e("editdeatail", "setViews: personal bio---> "+modelData.getPersonal_bio() );

        GridLayoutManager manager = new GridLayoutManager(getContext(),
                2
                ,GridLayoutManager.VERTICAL,false);


//        AdapterDataforimage adapterDataforimage =
//                new AdapterDataforimage(getContext(),ModelData.getHd_images());
        GalleryImageView adapterDataforimage = new GalleryImageView(modelData.getHd_images(),getContext());
        adapterDataforimage.SetonimageclickListener(this);
//        adapterDataforimage.setHideImage();

        gallery_Rv.setAdapter(adapterDataforimage);
        gallery_Rv.setLayoutManager(manager);
        if (modelData.getChest() != null && !modelData.getChest().equals("") && !modelData.getChest().toLowerCase().equals("null") ){
            chesttxt.setText(modelData.getChest()+" inches");
        }
        else{
            chesttxt.setText("NA");
        }

        if (modelData.getWaist() != null && !modelData.getWaist().equals("") && !modelData.getWaist().toLowerCase().equals("null") ) {
            waisttxt.setText(modelData.getWaist()+ " inches");
        }
        else{
            waisttxt.setText("NA");
        }

        if (modelData.getHip() != null && !modelData.getHip().equals("") && !modelData.getHip().toLowerCase().equals("null") ) {
            hiptxt.setText(modelData.getHip()+" inches");
        }else{
            hiptxt.setText("NA");
        }
        String name = modelData.getName();

        fullname.setText(name.trim().toString().substring(0,1).toUpperCase()+name.trim().substring(1).toLowerCase());
        Glide.with(Objects.requireNonNull(getContext()))
                .load(modelData.getProfile_img())
                .into(profileimage);
        professionltitle.setText(modelData.getProffesion() + " | " + modelData.getGender());
        if(modelData.getState_pojo() != null && !modelData.getState_pojo().getLocation_name().toLowerCase().equals("null")){
            location.setText(modelData.getState_pojo().getLocation_name() + " | " + modelData.getCountry_pojo().getLocation_name());
        }
        else{
            location.setText(modelData.getCountry_pojo().getLocation_name());
        }

        pro_journey.setText(modelData.getPersonal_journey());
        bio.setText(modelData.getPersonal_bio());
        idelageforrole.setText(modelData.getRole_age_min() + "yrs - " + modelData.getRole_age_max() + "yrs");
        heighttxt.setText(modelData.getHeight_feet() + "ft " + modelData.getHeight_inches() + " inches");

        languagetxt.setText(jsonarrayconvert(modelData.getLanguage()));
        bodyApperancetxt.setText(jsonarrayconvert(modelData.getBody_type()));
        skillText.setText(jsonarrayconvert(modelData.getSkill()));
        ethnicitytxt.setText(jsonarrayconvert(modelData.getEthnicity()));

        imageadapter = new GalleryViewAdapter(modelData.getHd_images(),getContext());
//        imageadapter.setHideImage();
        LinearLayoutManager HorizontalLayout;
        HorizontalLayout
                = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        rv_recyelrview.setLayoutManager(HorizontalLayout);
        rv_recyelrview.setAdapter(imageadapter);
        imageadapter.SetonimageclickListener(this::onclickimage);

//        youTubeUrl ="";
        youTubeUrl = modelData.getVideo_url();
//        youTubeUrl = "https://www.youtube.com/watch?v=eYlDQBB3Sk0";
        if (modelData!= null && !TextUtils.isEmpty(getYoutubeVideoId(youTubeUrl))){
                 youTubeTitle.setVisibility(View.VISIBLE);
                youtubevideoShow.setVisibility(View.VISIBLE);
                youtubevideoShow.setText(Html.fromHtml("<u>"+youTubeUrl+"</u>"));
            }

        youTubeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               youTubeVideo.setVisibility(View.GONE);
               youtubevideoShow.setVisibility(View.VISIBLE);
               youTubeBack.setVisibility(View.GONE);
            }
        });

        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callYoutubeVideoPlayer();
            }
        });

        youtubevideoShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                modelVideoThumbnail(youTubeUrl);
                 callYoutubeVideoPlayer();
            }
        });

    }

    private void callYoutubeVideoPlayer() {

//        modelVideoThumbnail(youTubeUrl);

        String url = youTubeUrl;
        youtubevideoShow.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty( getYoutubeVideoId(url))){
            Intent intent = new Intent(getActivity(), YouTubeActivity.class);
            intent.putExtra("apiKey", "youtube");
            intent.putExtra("videoId",getYoutubeVideoId(url));
            startActivity(intent);

            // calling fragment for the youtube player


//            YouTubePlayerView playerView = new YouTubePlayerView(getActivity());
//            playerView.initPlayer("youtube", getYoutubeVideoId(url), "https://cdn.rawgit.com/flipkart-incubator/inline-youtube-view/60bae1a1/youtube-android/youtube_iframe_player.html",
//                    0, new YouTubeEventListener() {
//                        @Override
//                        public void onReady() {
//
//                        }
//
//                        @Override
//                        public void onPlay(int i) {
//
//                        }
//
//                        @Override
//                        public void onPause(int i) {
//
//                        }
//
//                        @Override
//                        public void onStop(int i, int i1) {
//
//                        }
//
//                        @Override
//                        public void onBuffering(int i, boolean b) {
//
//                        }
//
//                        @Override
//                        public void onSeekTo(int i, int i1) {
//
//                        }
//
//                        @Override
//                        public void onInitializationFailure(String s) {
//
//                        }
//
//                        @Override
//                        public void onNativeNotSupported() {
//
//                        }
//
//                        @Override
//                        public void onCued() {
//
//                        }
//                    }, new Model_Portfolio(), new ImageLoader() {
//                        @Override
//                        public void loadImage(@NonNull ImageView imageView, @NonNull String s, int i, int i1) {
//                            Log.e(TAG, "loadImage: "+imageView );
//                        }
//                    });
}
//        Intent intent = new Intent(getActivity(), WebView_Youtube.class);
//        intent.putExtra("youtubeurl",modelData.getVideo_url());
//        startActivity(intent);
    }

    public static String getYoutubeVideoId(String youtubeUrl) {
        String videoId = "";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http")) {
            String expression = "^.*((youtu.be"+ "/)" + "|(v/)|(/u/w/)|(embed/)|(watch\\?))\\??v?=?([^#&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(youtubeUrl);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if(groupIndex1!=null && groupIndex1.length()==11)
                    videoId = groupIndex1;
            }

        }
        return videoId;
    }

    @Override
    public void onResume() {
        super.onResume();
        youTubeVideo.setVisibility(View.GONE);
        youTubeBack.setVisibility(View.GONE);

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void modelVideoThumbnail(String video_url) {
        Log.e(TAG, "modelVideoThumbnail: " );
        youtubevideoShow.setVisibility(View.GONE);
        youTubeBack.setVisibility(View.VISIBLE);
        youTubeVideo.setVisibility(View.VISIBLE);
        String url = "https://img.youtube.com/vi/"+video_url.split("\\=")[1]+"/0.jpg";
        Glide.with(this).load(url).into(modelVideo);
    }

    private void showvaldationError(String msg) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg);
        bottomSheet_for_error.setCancelable(false);
        assert getFragmentManager() != null;
        bottomSheet_for_error.show(getFragmentManager(), "error bottom");


    }

    public void protfolioviewopen() {
        Log.d(TAG, "protfolioviewopen: ");
         portfolioll.setVisibility(View.VISIBLE);
        underlinegalleryview.setVisibility(View.GONE);
        underlineportfolioview.setVisibility(View.VISIBLE);
        galleryll.setVisibility(View.GONE);
        gallerytxt.setTextColor(getResources().getColor(R.color.gray1));
        portfoliotxt.setTextColor(getResources().getColor(R.color.blue));
    }

    public void galleryopen() {
        Log.d(TAG, "galleryopen: ");
        portfolioll.setVisibility(View.GONE);
                galleryview.setVisibility(View.VISIBLE);
        galleryll.setVisibility(View.VISIBLE);
        underlinegalleryview.setVisibility(View.VISIBLE);
        underlineportfolioview.setVisibility(View.GONE);
        portfoliotxt.setTextColor(getResources().getColor(R.color.gray1));
        gallerytxt.setTextColor(getResources().getColor(R.color.blue));
    }



    @Override
    public void onclickimage(int postion, String selecteimageString) {
        Intent intent = new Intent(getContext(), GalleryViewImageModel.class);
        ImageViewSinglefullsize datafullimage = new ImageViewSinglefullsize(
                ModelData.getProfile_img(),
                selecteimageString,
                ModelData.getName(),
                ModelData.getProffesion()
        );
            datafullimage.setImages_hd(ModelData.getHd_images());
            datafullimage.setPostion(postion);
        intent.putExtra("profileGallery",1);
            intent.putExtra("fullimagdetail",datafullimage);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private StringBuilder jsonarrayconvert(ArrayList<String> jsondata) {
        StringBuilder locationstring = new StringBuilder();

        for (int i = 0; i < jsondata.size(); i++) {

            locationstring.append(jsondata.get(i));
            if (i < jsondata.size() - 1) {
                locationstring.append(", ");
            }
        }

        if (locationstring.length() <= 0) {
            locationstring.append("NA");
            return locationstring;
        }
        return locationstring;
    }


    private locationpojo convertlocationpojo(JSONObject location){
        locationpojo locationpojo = new locationpojo();
        try {
            locationpojo.setLocationid(location.getString("id"));
            locationpojo.setLocation_name(location.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return locationpojo;
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//       login_client_request.cancel();
//    }
}












