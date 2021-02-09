package com.example.myapplication.modelsprofile.models_activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Adapter.AttachmentAdapter;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.Complete_checkAcitivity;
import com.example.myapplication.R;
import com.example.myapplication.pojo.FilenameAndSize;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.InternetAccess;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Models_submitProposal extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;
    private TextView rate;
    private TextView duration;
    private EditText proposedrate;
    private EditText proposal;
    private TextView totalcharleft;
    private String proposalid_jobid;
    private LinearLayout emptyll;
    private RecyclerView rv_attachments;
    private static int countpropose = 2000;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    public static final String TAG = "Models_submitProposal";
    private ArrayList<FilenameAndSize> filenameAndSizesarray;
    private AttachmentAdapter attachmentAdapter;
    private ArrayList<String> attchementforproposal;
    private static final DecimalFormat format = new DecimalFormat("#.##");
    private static final long MiB = 1024 * 1024;
    private static final long KiB = 1024;
    private TextView msg;
    private View progModelCreate;
private LottieAnimationView lottieanimation;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void initfirebase() {

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initfirebase();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.models_submitproposal);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        initview();
        String rate_budget = getIntent().getStringExtra("client_budget");
        Log.d(TAG, "onCreate: " + rate_budget);

        String job_duration = getIntent().getStringExtra("Client_job_duration");

        proposalid_jobid = getIntent().getStringExtra("propsalid");
        Log.d(TAG, "onCreate: propsalid-> "+ proposalid_jobid);

        Log.d(TAG, "onCreate: id -> " + proposalid_jobid);
        setdata(rate_budget, job_duration);

        sendproposal();
    }

    public void submit(View view) {

        if (isvalidate()) {
            if (InternetAccess.isOnline(this)) {
                int count = attachmentAdapter.getItemCount();
                if(count>0){
                    showprogress();
                    firebaseurlconverter(count);
                }
                else{
                    showprogress();
                    volleyapicall();
                }
//
                
            } else {
                showvaldationError(getResources().getString(R.string.errorinternet),R.raw.no_internet);
            }

        }
    }

    public void back(View view) {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void add_attachments(View view){
        @SuppressLint("IntentReset") Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/* video/* pdf/*");
//        pickIntent.setType("*/*");
        startActivityForResult(pickIntent, ConstantString.ALLFILEGETCODE);
    }


    private void initview() {
        msg = findViewById(R.id.msg);
        progModelCreate = findViewById(R.id.progModelCreate);
        attchementforproposal = new ArrayList<>();
        rate = findViewById(R.id.rate);
        duration = findViewById(R.id.duration);
        proposedrate = findViewById(R.id.proposedrate);
        proposal = findViewById(R.id.proposal);
        totalcharleft = findViewById(R.id.totalcharleft);
        emptyll = findViewById(R.id.emptyll);
        rv_attachments = findViewById(R.id.rv_attachments);
        filenameAndSizesarray = new ArrayList<>();
        attachmentAdapter = new AttachmentAdapter(filenameAndSizesarray, this);
        attachmentAdapter.SetonDelete_Attachment(postion -> {
            Log.d(TAG, "initview: filenameandsizearay-> "+
                    filenameAndSizesarray.get(postion).getFilename()+
                    " position-> "+postion);
            filenameAndSizesarray.remove(postion);
            attachmentAdapter.notifyDataSetChanged();
        });
        rv_attachments.setLayoutManager(new LinearLayoutManager(this));
        rv_attachments.setAdapter(attachmentAdapter);
        msg.setText("Submitting your Proposal ");
        lottieanimation = findViewById(R.id.lottieanimation);

        lottieanimation.setAnimation(R.raw.filling_list);

    }

    @SuppressLint("SetTextI18n")
    private void setdata(String rate_budget, String job_duration) {

        rate.setText("$" + rate_budget + " per day");
        duration.setText("Job duration: " + job_duration);

    }


    private boolean isvalidate() {

        if (proposedrate.getText().toString().length() <= 0) {
            showvaldationError("proposal rate can't be left blank",R.raw.questionmark);
            return false;
        }
        if(Integer.parseInt(proposedrate.getText().toString().trim() ) <= 0){
            showvaldationError("Please provide a valid proposal rate",R.raw.questionmark);
            return false;
        }
        if (proposal.getText().toString().length() <= 0) {
            showvaldationError("Cover letter can't be left blank",R.raw.questionmark);
            return false;
        }
        return true;
    }

    private void sendproposal() {
        proposal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: start-> "+start+" before-> "+before+" count-> "+count);
                if(count==0 && before == 0){
                    return;
                }
                if(count > before){
                    countpropose -- ;
                }
                else if(before > count){
                    countpropose ++;
                }

                totalcharleft.setText(countpropose+"");

                Log.d(TAG, "onTextChanged: count propose-> "+countpropose);
            }

            @Override
            public void afterTextChanged(Editable editable) {



            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if  (requestCode == ConstantString.ALLFILEGETCODE) {
            ContentResolver cr = getContentResolver();
            InputStream is = null;
            Uri _uri = data.getData();
            assert _uri != null;
            File file = new File(_uri.getPath());
            Log.d(TAG, "onActivityResult: _uri=> " + _uri +
                    " file data-> " + file.getName() + " file size-> " + file.length());
            try {
                is = cr.openInputStream(_uri);
                cr.getType(_uri);
                int size = is.available();


                double imgsize = Double.parseDouble(String.valueOf(size));
                Log.d(TAG, "onActivityResult: imagesize.mib -> "+imgsize/MiB);
                if (imgsize/MiB > 5.00  ) {
                    showvaldationError("Image size should not be greater then 5 mb ",R.raw.onboard);
                } else {
                    FilenameAndSize filedatasize = new FilenameAndSize(file.getName(),
                            getFileSize(size), _uri);
                    filenameAndSizesarray.add(filedatasize);
                    attachmentAdapter.notifyDataSetChanged();
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    public String getFileSize(long file) {


        final double length = Double.parseDouble(String.valueOf(file));
        Log.d(TAG, "getFileSize: length-> "+length);

        if (length > MiB) {
            return format.format(length / MiB) + " MB";
        }
        if (length > KiB) {
            return format.format(length / KiB) + " KB";
        }
        return format.format(length) + " B";
    }


    private void showvaldationError(String msg,int res) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg,res);
        bottomSheet_for_error.setCancelable(false);
        bottomSheet_for_error.setlottiimage(R.raw.questionmark);
        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");

    }

    private void firebaseurlconverter(int count){

        attchementforproposal = new ArrayList<>();
        for(int i=0 ; i <count;i++ ) {

            Log.d(TAG, "firebaseurlconverter: filenameandsize-> "+
                    filenameAndSizesarray.get(i).getFileuri());
            
            
            Calendar calendar = Calendar.getInstance();
            long timeMilli2 = calendar.getTimeInMillis();
            StorageReference reference = storageReference.child("profile")
                    .child("image_" + String.valueOf(timeMilli2));

            int finalI = i;
            reference.putFile( filenameAndSizesarray.get(i).getFileuri())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "onSuccess: second");
                    Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override

                        public void onSuccess(Uri uri) {
                            Log.d(TAG, "onSuccess:i"+ finalI +" uri -> "+uri);
                            attchementforproposal.add(uri.toString());
                            if(attchementforproposal.size() == count){
                                volleyapicall();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.getMessage());
                        }
                    })
                    ;
                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
            });
        }

    }
    private void volleyapicall() {
        Log.d(TAG, "volleyapicall: ");
        StringRequest proposalrequest = new StringRequest(Request.Method.POST,
                ApiConstant.JOB_APPLIED,
                (Response.Listener<String>) response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d(TAG, "volleyapicall: response-> "+response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)){

                            lottieanimation.setAnimation(R.raw.successnew);
                            msg.setText("Proposal submitted");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
                                    finish();
                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                }
                            },SPLASH_TIME_OUT);

                        }
                        else{
                            hideprogress();
                        }
                    } catch (JSONException e) {

                    }
                },
                error -> {
                    hideprogress();
                    showvaldationError(error.toString(),R.raw.onboard);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> submitproposal = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(Models_submitProposal.this).getUserData();
                Log.d(TAG, "getHeaders: token value-> " + logindata.getToken());
                submitproposal.put("Authorization", "Bearer " + logindata.getToken());
                return submitproposal;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Gson gson = new Gson();
                Log.d(TAG, "getParams: cover_letter-> "+proposal.getText().toString().trim()+
                        "attachment-> "+gson.toJson(attchementforproposal)+
                        " jobId->  "+proposalid_jobid+" rate-> "+rate.getText().toString().trim());
                HashMap<String, String> submitpropsalparam = new HashMap<>();
                submitpropsalparam.put("cover_letter",proposal.getText().toString().trim());
                if(attchementforproposal != null && !attchementforproposal.isEmpty()){
                    submitpropsalparam.put("attachment", gson.toJson(attchementforproposal));
                }

                submitpropsalparam.put("jobId",proposalid_jobid);
                submitpropsalparam.put("rate",proposedrate.getText().toString().trim());
                Log.d(TAG, "getParams: parameters-> "+submitpropsalparam);

                return submitpropsalparam;
            }
        };


        VolleySingleton.getInstance(this).addToRequestQueue(proposalrequest);

    }


    private void showprogress(){

        progModelCreate.setVisibility(View.VISIBLE);
    }

    private void hideprogress(){
        progModelCreate.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
