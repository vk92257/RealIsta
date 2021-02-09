package com.example.myapplication.clientsprofile.clients_activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.bumptech.glide.Glide;
import com.example.myapplication.Adapter.AttachmentAdapter;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.R;
import com.example.myapplication.modelsprofile.models_activity.Gender_Selection_radio;
import com.example.myapplication.modelsprofile.models_activity.Models_createportfolioActivity;
import com.example.myapplication.modelsprofile.models_selectoptions.Languages_selectActivity;
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
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class Clients_CreateJobPost extends AppCompatActivity {

    private static String TAG = "Clients_CreateJobPost";
    private EditText projectinfo;
    private EditText typeflooktxt;
    private EditText minage_txt;
    private EditText maxage_txt;
    private EditText feet_txt;
    private EditText inchestxt;
    private EditText pounds_txt;
    private TextView ethnicitytv_tv;
    private TextView haircolortv_tv;
    private TextView eyecolortv_tv;
    private TextView specialskillstv_tv;
    private TextView hairlengthtv_tv;
    private EditText jobtitle_txt;
    private TextView jobtypetextview_tv;
    private EditText vacancies_tv;

    private TextView gender_tv;
    private EditText productname_txt;
    private EditText jobrate_txt;
    private EditText jobduration_txt;
    private EditText jobdescription_txt;
    private EditText shootlocation_txt;
    private TextView shootdatesfrom_Tv;
    private TextView shootdatesto_tv;
    private EditText castingcompany_txt;
    private LinearLayout emptyll_view;
    private RecyclerView rv_attachments_images;
    private Button createpost_btn;
    private LinearLayout ethnicitylayout, haircolorlayout, eyecolorlayout,
            specialskillslayout, hairlengthlayout, jobtypelayout, genderlayaout;

    private String gendervalue;
    private String jobtypevalue;

    private ArrayList<String> genderarray;
    private ArrayList<String> jobtypearray;
    private ArrayList<String> hairlengtharray;
    private ArrayList<String> skillarray;
    private ArrayList<String> eyecolor;
    private ArrayList<String> haircolorArray;
    private ArrayList<String> ethnicityarray;
    private ArrayList<FilenameAndSize> filenameAndSizesarray;
    private AttachmentAdapter attachmentAdapter;
    private ArrayList<String> demofilenamesizearray;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private LinearLayout jobpostlayout;
    private View progressbarcreate;
    private LottieAnimationView progresscreate;

    private TextView progress_msg;
    private static final DecimalFormat format = new DecimalFormat("#.##");
    private static final long MiB = 1024 * 1024;
    private static final long KiB = 1024;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.clients_createjobpost);
        initview();
        initfirebase();
    }

    private void initview() {
        filenameAndSizesarray = new ArrayList<>();
        genderarray = new ArrayList<>();
        jobtypearray = new ArrayList<>();
        hairlengtharray = new ArrayList<>();
        skillarray = new ArrayList<>();
        eyecolor = new ArrayList<>();
        haircolorArray = new ArrayList<>();
        ethnicityarray = new ArrayList<>();
        demofilenamesizearray = new ArrayList<>();

        jobpostlayout = findViewById(R.id.jobpostcreatelayout);
        progressbarcreate = findViewById(R.id.progressbarcreate);
        progress_msg = findViewById(R.id.msg);
        projectinfo = findViewById(R.id.projectinformation);
        typeflooktxt = findViewById(R.id.typeoflook);
        minage_txt = findViewById(R.id.minage);
        maxage_txt = findViewById(R.id.maxage);
        feet_txt = findViewById(R.id.feet);
        inchestxt = findViewById(R.id.inches);
        pounds_txt = findViewById(R.id.pounds);
        ethnicitytv_tv = findViewById(R.id.ethnicitytv);
        haircolortv_tv = findViewById(R.id.haircolortv);
        eyecolortv_tv = findViewById(R.id.eyecolortv);
        specialskillstv_tv = findViewById(R.id.specialskillstv);
        hairlengthtv_tv = findViewById(R.id.hairlengthtv);
        jobtitle_txt = findViewById(R.id.jobtitle);
        jobtypetextview_tv = findViewById(R.id.jobtypetextview);
        vacancies_tv = findViewById(R.id.vacancies);
        gender_tv = findViewById(R.id.gender);
        productname_txt = findViewById(R.id.productname);
        jobrate_txt = findViewById(R.id.jobrate);
        jobduration_txt = findViewById(R.id.jobduration);
        jobdescription_txt = findViewById(R.id.jobdescription);
        shootlocation_txt = findViewById(R.id.shootlocation);
        shootdatesfrom_Tv = findViewById(R.id.shootdatesfrom);
        shootdatesto_tv = findViewById(R.id.shootdatesto);
        castingcompany_txt = findViewById(R.id.castingcompany);
        emptyll_view = findViewById(R.id.emptyll);
        rv_attachments_images = findViewById(R.id.rv_attachments);
        createpost_btn = findViewById(R.id.createpost);
        ethnicitylayout = findViewById(R.id.ethnicitylayout);
        haircolorlayout = findViewById(R.id.haircolorlayout);
        eyecolorlayout = findViewById(R.id.eyecolorlayout);
        specialskillslayout = findViewById(R.id.specialskillslayout);
        hairlengthlayout = findViewById(R.id.hairlengthlayout);
        jobtypelayout = findViewById(R.id.jobtypelayout);
        genderlayaout = findViewById(R.id.genderlayaout);

        attachmentAdapter = new AttachmentAdapter(filenameAndSizesarray, this);
        attachmentAdapter.SetonDelete_Attachment(postion -> {
            Log.d(TAG, "initview: filenameandsizearay-> " +
                    filenameAndSizesarray.get(postion).getFilename() +
                    " position-> " + postion);
            filenameAndSizesarray.remove(postion);
            attachmentAdapter.notifyDataSetChanged();
        });
        rv_attachments_images.setLayoutManager(new LinearLayoutManager(this));
        rv_attachments_images.setAdapter(attachmentAdapter);


        progress_msg.setText(getResources().getText(R.string.Createingjobpost));

    }


    private void initfirebase() {
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    /**
     * initview
     */

    public void postjob(View view) {
        if (isValidate()) {
            if (InternetAccess.isOnline(this)) {
                addnewjobfirebase();
            } else {
                showvaldationError(getResources().getString(R.string.errorinternet), R.raw.onboard);
            }
        }
    }

    public void add_attachments(View view) {
        @SuppressLint("IntentReset") Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/* video/*");
        startActivityForResult(pickIntent, ConstantString.ALLFILEGETCODE);
    }

    public void dateto(View view) {
        Log.d(TAG, "dateto: click ");
        fetchdate(0,shootdatesto_tv);
//        shootdatesto_tv.setClickable(false);


    }

    public void datesfromclicked(View view) {
        Log.d(TAG, "datesfromclicked: click");
        fetchdate(1,shootdatesfrom_Tv);
//        shootdatesfrom_Tv.setClickable(false);

    }

    public void back(View view) {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    //    public void genderselect(View view) {
//        Intent intent = new Intent(this, Languages_selectActivity.class);
//        String[] data = getResources().getStringArray(R.array.gender);
//        Log.d(TAG, "languagesselect: data string -> " + Arrays.toString(data));
//        if (genderarray != null && !genderarray.isEmpty()) {
//            intent.putExtra("checkdata", genderarray);
//        }
//        intent.putExtra("data", data);
//        intent.putExtra(ConstantString.CODETEXT, ConstantString.GENDERSELECTIONCODE);
//        intent.putExtra(ConstantString.SELECT_TOP_TXT, "Select Gender");
//        startActivityForResult(intent, ConstantString.GENDERSELECTIONCODE);
//    }


    public void genderselect(View view) {
        Intent intent = new Intent(this, Gender_Selection_radio.class);

        if (gendervalue != null && !gendervalue.isEmpty()) {
            intent.putExtra("data", gendervalue);
        }
        startActivityForResult(intent, ConstantString.GENDERSELECTIONCODE);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    public void selectjobtype(View view) {
        Intent intent = new Intent(this, JobTypeSelect_option.class);

        if (jobtypevalue != null && !jobtypevalue.isEmpty()) {
            intent.putExtra("data", jobtypevalue);
        }

        startActivityForResult(intent, ConstantString.JOBETYPECODE);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void selecthairlength(View view) {
        Intent intent = new Intent(this, Languages_selectActivity.class);
        String[] data = getResources().getStringArray(R.array.hairlength);
        Log.d(TAG, "languagesselect: data string -> " + Arrays.toString(data));
        if (hairlengtharray != null && !hairlengtharray.isEmpty()) {
            intent.putExtra("checkdata", hairlengtharray);
        }
        intent.putExtra("data", data);
        intent.putExtra(ConstantString.CODETEXT, ConstantString.HAIRLENGTHCODE);
        intent.putExtra(ConstantString.SELECT_TOP_TXT, "Select hair length");
        startActivityForResult(intent, ConstantString.HAIRLENGTHCODE);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void selectspecialskills(View view) {
        Intent intent = new Intent(this, Languages_selectActivity.class);
        String[] data = getResources().getStringArray(R.array.skill);
        Log.d(TAG, "languagesselect: data string -> " + Arrays.toString(data));
        if (skillarray != null && !skillarray.isEmpty()) {
            intent.putExtra("checkdata", skillarray);
        }
        intent.putExtra("data", data);
        intent.putExtra(ConstantString.CODETEXT, ConstantString.SKILLCODE);
        intent.putExtra(ConstantString.SELECT_TOP_TXT, "Select Skill");
        startActivityForResult(intent, ConstantString.SKILLCODE);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    public void selecteyecolor(View view) {
        Intent intent = new Intent(this, Languages_selectActivity.class);
        String[] data = getResources().getStringArray(R.array.eyecolor);
//        Log.d(TAG, "languagesselect: data string -> " + Arrays.toString(data));
//        if (eyecolor != null && !eyecolor.isEmpty()) {
//            intent.putExtra("checkdata", eyecolor);
//        }
        intent.putExtra("data", data);
        intent.putExtra(ConstantString.CODETEXT, ConstantString.EYECOLORCODE);
        intent.putExtra(ConstantString.SELECT_TOP_TXT, "Select Eye Color");
        startActivityForResult(intent, ConstantString.EYECOLORCODE);
    }

    public void selecthaircolor(View view) {
        Intent intent = new Intent(this, Languages_selectActivity.class);
        String[] data = getResources().getStringArray(R.array.haircolor);
        Log.d(TAG, "languagesselect: data string -> " + Arrays.toString(data));
        if (haircolorArray != null && !haircolorArray.isEmpty()) {
            intent.putExtra("checkdata", haircolorArray);
        }
        intent.putExtra("data", data);
        intent.putExtra(ConstantString.CODETEXT, ConstantString.HAIRCOLORCODE);
        intent.putExtra(ConstantString.SELECT_TOP_TXT, "Select Hair Color");
        startActivityForResult(intent, ConstantString.HAIRCOLORCODE);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void selectethnicity(View view) {
        Intent intent = new Intent(this, Languages_selectActivity.class);
        String[] data = getResources().getStringArray(R.array.Ethnicity);
        Log.d(TAG, "languagesselect: data string -> " + Arrays.toString(data));
        if (ethnicityarray != null && !ethnicityarray.isEmpty()) {
            intent.putExtra("checkdata", ethnicityarray);
        }
        intent.putExtra("data", data);
        intent.putExtra(ConstantString.CODETEXT, ConstantString.ETHNICITYCODE);
        intent.putExtra(ConstantString.SELECT_TOP_TXT, "Ethnicity");
        startActivityForResult(intent, ConstantString.ETHNICITYCODE);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    private boolean isValidate() {

        if (projectinfo.getText().toString().isEmpty()) {
            projectinfo.setError(getResources().getString(R.string.errorprojectinfo));
            projectinfo.requestFocus();
//            showvaldationError(getResources().getString(R.string.errorprojectinfo), R.raw.onboard);
            return false;
        }
        if (typeflooktxt.getText().toString().isEmpty()) {
            typeflooktxt.requestFocus();
            typeflooktxt.setError(getResources().getString(R.string.errortypeoflook));
//            showvaldationError(getResources().getString(R.string.errortypeoflook), R.raw.onboard);
            return false;
        }
        if (minage_txt.getText().toString().isEmpty()) {
            minage_txt.requestFocus();
            minage_txt.setError(getResources().getString(R.string.erroridealageisreq));
//            showvaldationError(, R.raw.onboard);
            return false;
        }

        if (minage_txt.getText().toString().trim().equals("6")) {

            minage_txt.requestFocus();
            minage_txt.setError(getResources().getString(R.string.erroragelimit));
            return false;
        }
        if (maxage_txt.getText().toString().isEmpty()) {
            maxage_txt.requestFocus();
            maxage_txt.setError(getResources().getString(R.string.erroridealageisreq));
//            showvaldationError(, R.raw.onboard);
            return false;
        }
        if (maxage_txt.getText().toString().trim().equals("6")) {
            maxage_txt.requestFocus();
            maxage_txt.setError(getResources().getString(R.string.erroragelimit));
//            showvaldationError(, R.raw.onboard);
            return false;
        }

        if (feet_txt.getText().toString().isEmpty() && inchestxt.getText().toString().isEmpty()) {
            feet_txt.requestFocus();
            feet_txt.setError(getResources().getString(R.string.errorheightmeasurement));
//            showvaldationError(getResources().getString(R.string.errorheightmeasurement), R.raw.onboard);
            return false;
        }

        if (feet_txt.getText().toString().trim().equals("0")) {
            feet_txt.requestFocus();
            feet_txt.setError(getResources().getString(R.string.errorfeetzero));
//            showvaldationError(getResources().getString(R.string.errorheightmeasurement), R.raw.onboard);
            return false;
        }

        if(Integer.parseInt(inchestxt.getText().toString().trim()) <=13){
            feet_txt.requestFocus();
            feet_txt.setError(getResources().getString(R.string.errorfeetinch));
        }

        if (pounds_txt.getText().toString().isEmpty()) {
            pounds_txt.requestFocus();
            pounds_txt.setError(getResources().getString(R.string.errorweight));
//            showvaldationError(getResources().getString(R.string.errorweight), R.raw.onboard);
            return false;
        }
        if (pounds_txt.getText().toString().trim().equals("0")) {
            pounds_txt.requestFocus();
            pounds_txt.setError(getResources().getString(R.string.errorweight));
//            showvaldationError(getResources().getString(R.string.errorweight), R.raw.onboard);
            return false;
        }

        if (ethnicityarray.size() <= 0) {
            ethnicitylayout.setBackgroundResource(R.drawable.redstrok_invalid);
            ethnicitylayout.requestFocus();
            showvaldationError(getResources().getString(R.string.errorethinicity), R.raw.onboard);
//            if (ethnicitylayout.isFocused()) {
//
//            }
//            showvaldationError(getResources().getString(R.string.errorethinicity), R.raw.onboard);
            return false;
        }

        if (haircolorArray.size() <= 0) {
            haircolorlayout.setBackgroundResource(R.drawable.redstrok_invalid);
            haircolorlayout.requestFocus();
            showvaldationError(getResources().getString(R.string.errorhaircolor), R.raw.onboard);
//            if (haircolorlayout.isFocused()) {
//            }
//            showvaldationError(getResources().getString(R.string.errorhaircolor), R.raw.onboard);
            return false;
        }
        if (eyecolor.size() <= 0) {
            eyecolorlayout.setBackgroundResource(R.drawable.redstrok_invalid);
            eyecolorlayout.requestFocus();
            showvaldationError(getResources().getString(R.string.erroreeyecolor), R.raw.onboard);
//            if (eyecolorlayout.isFocused()) {
//
//            }
//            showvaldationError(getResources().getString(R.string.erroreeyecolor), R.raw.onboard);
            return false;
        }
        if (skillarray.size() <= 0) {
            specialskillslayout.setBackgroundResource(R.drawable.redstrok_invalid);
            specialskillslayout.requestFocus();
            showvaldationError(getResources().getString(R.string.errorskills), R.raw.onboard);
//            if (specialskillslayout.isFocused()) {
//
//            }
//            showvaldationError(getResources().getString(R.string.errorskills), R.raw.onboard);
            return false;
        }
        if (hairlengtharray.size() <= 0) {
            hairlengthlayout.setBackgroundResource(R.drawable.redstrok_invalid);
            hairlengthlayout.requestFocus();
            showvaldationError(getResources().getString(R.string.errorhairlength), R.raw.onboard);
//            if (hairlengthlayout.isFocused()) {
//
//            }
//            showvaldationError(getResources().getString(R.string.errorhairlength), R.raw.onboard);
            return false;
        }


        if (jobtitle_txt.getText().toString().isEmpty()) {
            jobtitle_txt.requestFocus();
            jobtitle_txt.setError(getResources().getString(R.string.errorjobtitlempty));
//            showvaldationError(getResources().getString(R.string.errorjobtitle), R.raw.onboard);
            return false;
        }
        if (jobtitle_txt.getText().toString().length() < 10) {
            jobtitle_txt.requestFocus();
            jobtitle_txt.setError(getResources().getString(R.string.errorjobtitle));
//            showvaldationError(getResources().getString(R.string.errorjobtitle), R.raw.onboard);
            return false;
        }

        if (jobtypearray.size() <= 0) {
            jobtypelayout.setBackgroundResource(R.drawable.redstrok_invalid);
            jobtypelayout.requestFocus();
            showvaldationError(getResources().getString(R.string.errorjobtype), R.raw.onboard);
//            if (jobtypelayout.isFocused()) {
//
//            }
//            showvaldationError(getResources().getString(R.string.errorjobtype), R.raw.onboard);
            return false;
        }

        if (vacancies_tv.getText().toString().isEmpty()) {
            vacancies_tv.setError(getResources().getString(R.string.errortotalrole));
//            showvaldationError(getResources().getString(R.string.errortotalrole), R.raw.onboard);
            return false;
        }
        if (vacancies_tv.getText().toString().trim().equals("0")) {
            vacancies_tv.setError(getResources().getString(R.string.errortotalrolezero));
//            showvaldationError(getResources().getString(R.string.errortotalrole), R.raw.onboard);
            return false;
        }

        if (genderarray.size() <= 0) {
            genderlayaout.setBackgroundResource(R.drawable.redstrok_invalid);
            genderlayaout.requestFocus();
            showvaldationError(getResources().getString(R.string.errorgendertype), R.raw.onboard);
            if (genderlayaout.isFocused()) {

            }
//            showvaldationError(getResources().getString(R.string.errorgendertype), R.raw.onboard);
            return false;
        }


        if (productname_txt.getText().toString().isEmpty()) {
            productname_txt.requestFocus();
            productname_txt.setError(getResources().getString(R.string.errorproductname));
//            showvaldationError(getResources().getString(R.string.errorproductname), R.raw.onboard);
            return false;
        }
        if (jobrate_txt.getText().toString().isEmpty()) {
            jobrate_txt.requestFocus();
            jobrate_txt.setError(getResources().getString(R.string.errorjobrate));
//            showvaldationError(getResources().getString(R.string.errorjobrate), R.raw.onboard);
            return false;
        }
        if (jobrate_txt.getText().toString().trim().equals("0")) {
            jobrate_txt.requestFocus();
            jobrate_txt.setError(getResources().getString(R.string.errorjobratezero));
//            showvaldationError(getResources().getString(R.string.errorjobrate), R.raw.onboard);
            return false;
        }
        if (jobduration_txt.getText().toString().isEmpty()) {
            jobduration_txt.requestFocus();
            jobduration_txt.setError(getResources().getString(R.string.errorjobduration));
//            showvaldationError(getResources().getString(R.string.errorjobduration), R.raw.onboard);
            return false;
        }
        if (jobdescription_txt.getText().toString().isEmpty()) {
            jobdescription_txt.requestFocus();
            jobdescription_txt.setError(getResources().getString(R.string.errorjobjobdesciotionisemptry));
//            showvaldationError(getResources().getString(R.string.errorjobjobdesciotionisemptry), R.raw.onboard);
            return false;
        }
        if (jobdescription_txt.getText().toString().length() < 50) {
            jobdescription_txt.requestFocus();
            jobdescription_txt.setError(getResources().getString(R.string.errorjobdescription50char));
//            showvaldationError(getResources().getString(R.string.errorjobdescription50char), R.raw.onboard);
            return false;
        }
        if (shootlocation_txt.getText().toString().isEmpty()) {
            shootlocation_txt.requestFocus();
            shootlocation_txt.setError(getResources().getString(R.string.errorlocation));
//            showvaldationError(getResources().getString(R.string.errorlocation), R.raw.onboard);
            return false;
        }

        if (shootdatesfrom_Tv.getText().length() <= 0 &&
                shootdatesto_tv.getText().length() <= 0) {

            shootdatesfrom_Tv.setBackgroundResource(R.drawable.redstrok_invalid);
            shootdatesfrom_Tv.requestFocus();
            shootdatesto_tv.setBackgroundResource(R.drawable.redstrok_invalid);
            shootdatesto_tv.requestFocus();
            showvaldationError(getResources().getString(R.string.errorshootperformationdate), R.raw.onboard);
            return false;
        }
        if (!isdatevalidate(shootdatesfrom_Tv.getText().toString(), shootdatesto_tv.getText().toString())) {
            shootdatesfrom_Tv.setBackgroundResource(R.drawable.redstrok_invalid);
            shootdatesfrom_Tv.requestFocus();
            shootdatesto_tv.setBackgroundResource(R.drawable.redstrok_invalid);
            shootdatesto_tv.requestFocus();

            showvaldationError(getResources().getString(R.string.errorshootperformationdate), R.raw.onboard);
            return false;
        }

        if (castingcompany_txt.getText().toString().isEmpty()) {
            castingcompany_txt.requestFocus();
            castingcompany_txt.setError(getResources().getString(R.string.errorcastingcompany));
//            showvaldationError(getResources().getString(R.string.errorcastingcompany), R.raw.onboard);
            return false;
        }


        return true;
    }

    private void showvaldationError(String msg, int errorimage) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg);
        bottomSheet_for_error.setCancelable(false);
        bottomSheet_for_error.setlottiimage(errorimage);
        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");
    }

    private void fetchdate(int i,View view) {
        view.setClickable(false);
        Log.d(TAG, "fetchdate: datafetch");
//
//        final Dialog dialog = new Dialog(this,android.R.style.Theme_Light);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.clanderdilogpicker);
//
//        final CalendarView calendarview_picker = (CalendarView) dialog.findViewById(R.id.calendarview_picker);
//        calendarview_picker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                Log.d(TAG, "onSelectedDayChange: year-> "+year+" month-> "+month+" day-> "+dayOfMonth);
//            }
//        });
//
//        dialog.show();
        DatePicker datePicker = new DatePickerBuilder(this, new OnSelectDateListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelect(List<Calendar> calendar) {

                shootdatesfrom_Tv.setClickable(true);
                shootdatesto_tv.setClickable(true);
                Log.d(TAG, "onSelect: " + calendar);
                Date d = calendar.get(0).getTime();
//                yyyy-MM-dd
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Log.d(TAG, "onSelect: index --> " + formatter.format(d));

                String dateset = formatter.format(d);
                if (i == 1) {
                    shootdatesfrom_Tv.setText(dateset);


                } else if (i == 0) {
                    shootdatesto_tv.setText(dateset);

                }
            }

        }).setHeaderColor(R.color.white)
                .setHeaderLabelColor(R.color.black)
                .setSelectionLabelColor(R.color.white).
                        setPreviousButtonSrc(R.drawable.ic_baseline_keyboard_arrow_left_24)
                .setForwardButtonSrc(R.drawable.ic_baseline_keyboard_arrow_right_24)
                .setSelectionColor(R.color.black)
                .setDate(Calendar.getInstance())
                .setPickerType(1)
                .setMinimumDate(Calendar.getInstance())
                .build().show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                view.setClickable(true);

            }},2000);


    }


    private void addnewjobfirebase() {

        startprocess();
        Log.d(TAG, "addnewjobfirebase: \n\n demofilenamesizearray-> " +
                demofilenamesizearray.size() + " \n\n filenameandsizearray-> " + filenameAndSizesarray.size());
        if (filenameAndSizesarray.size() > 0) {
            for (int i = 0; i < filenameAndSizesarray.size(); i++) {
                Log.d(TAG, "addnewjobfirebase: " + filenameAndSizesarray.get(i));
                Bitmap originalImage = null;
                try {
                    originalImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                            filenameAndSizesarray.get(i).getFileuri());
                } catch (IOException e) {
                    Log.d(TAG, "addnewjobfirebase: " + e.toString());
                    e.printStackTrace();
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                assert originalImage != null;
                originalImage.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                byte[] data = outputStream.toByteArray();
                Log.d(TAG, "addnewjobfirebase: data-> " + data);
                Calendar calendar = Calendar.getInstance();
                //Returns current time in millis
                long timeMilli2 = calendar.getTimeInMillis();
                StorageReference riversRef = storageReference.child("attachment")
                        .child("image_" + String.valueOf(timeMilli2));
                Log.d(TAG, "inside addnewjobfirebase: \n\n demofilenamesizearray-> " +
                        demofilenamesizearray + " \n\n filenameandsizearray-> " + filenameAndSizesarray +
                        "riversRef = " + riversRef);

                riversRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: cooo-> ");
                        final Uri[] downloadUrl = {null};


                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d(TAG, "onSuccess: uri-> " + uri.toString());
                                demofilenamesizearray.add(uri.toString());
//                            downloadUrl[0] = uri;
                                if (demofilenamesizearray.size() == filenameAndSizesarray.size()) {

                                    volleyapiadd();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                stopprocess();
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                        });
                        Log.d(TAG, "onSuccess: filenameAndSizesarray-> " + filenameAndSizesarray.size());
//                    storedata(downloadUrl.toString());


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        stopprocess();
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });

            }
        } else {
            Log.d(TAG, "addnewjobfirebase: no data on file name And size");
            volleyapiadd();
        }
//
//        if (filenameAndSizesarray.size() <= 0) {
//
//        }

    }

    private void volleyapiadd() {
        StringRequest loginstringRequest = new StringRequest(Request.Method.POST,
                ApiConstant.ADD_JOB_CLIENT,
                (Response.Listener<String>) response -> {
                    Log.d(TAG, "setlogin: response=> " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                            finish();
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            Toast.makeText(Clients_CreateJobPost.this, jsonObject.getString(ConstantString.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
//                            showvaldationError(jsonObject.getString(ConstantString.RESPONSE_MESSAGE),R.raw.beep);


                        } else {
                            showvaldationError(jsonObject.getString(ConstantString.RESPONSE_MESSAGE), R.raw.beep);
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                },
                error -> {

                    Log.d(TAG, "setlogin: error-> " + error.getMessage());
                    stopprocess();
                    showvaldationError(error.toString(), R.raw.beep);
                    if (error instanceof NetworkError) {
                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                    } else if (error instanceof ParseError) {
                    } else if (error instanceof NoConnectionError) {
                    } else if (error instanceof TimeoutError) {
                        showvaldationError("Oops. Timeout error!", R.raw.beep);
                    } else {
                        showvaldationError(error.toString(), R.raw.beep);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(Clients_CreateJobPost.this).getUserData();
                Log.d(TAG, "getHeaders: token value -> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Gson gson = new Gson();
                Gson imagegson = new GsonBuilder().disableHtmlEscaping().create();

//                Log.d(TAG, "getParams:\n id-> " + id + "\n phoneno-> " + f_mobile +
//                        "\n city-> " + f_city + " \n state=> " + f_state + "\n f_country-> " + f_county +
//                        "\n age-> " + f_age + " \n profession-> " + f_profession + "\n bio => " + f_bio +
//                        "\n projourney=> " + f_pro_journey + "\n minage=>  " + f_minage +
//                        "\n maxage = > " + f_maxage + "\n feet=> " + f_feet + "\n  inch-> " + f_feetinches +
//                        "\n youtube url-> " + yturl + " profile image -> " + imageuri +
//                        "\n arrayimage-> " + gson.toJson(arrayofimage) + "\n langarray -> " + gson.toJson(langarray) +
//                        "\n skillarray-> " + gson.toJson(skillarray) + "\n ethnicityaray-> " + gson.toJson(ethnicityarray) +
//                        " \n bodyarray-> " + gson.toJson(bodytypearray) + "\n gender-> " + gender + "\n passport-> " + havepassport);

                Log.d(TAG, "getParams: " + projectinfo.getText().toString() +
                        " " + gson.toJson(eyecolor) + " demofilename " + gson.toJson(demofilenamesizearray));
                HashMap<String, String> loginparam = new HashMap<>();
                loginparam.put(ConstantString.ADD_HAIR_COLOR, gson.toJson(haircolorArray));
                loginparam.put(ConstantString.ADD_EYE_COLOR, gson.toJson(eyecolor));
                loginparam.put(ConstantString.ADD_SPECIAL_SKILLS, gson.toJson(skillarray));
                loginparam.put(ConstantString.ADD_ETHINICITY, gson.toJson(ethnicityarray));
                loginparam.put(ConstantString.ADD_HAIR_LENGTH, gson.toJson(hairlengtharray));
//                if (jobtypearray.size() == 1 && jobtypearray.get(0).toLowerCase().equals("both")) {
//                    jobtypearray.clear();
//                    jobtypearray.add("Still photoshoot");
//                    jobtypearray.add("video shoot");
//                }
                loginparam.put(ConstantString.ADD_JOB_TYPE, gson.toJson(jobtypearray));

                if (!demofilenamesizearray.isEmpty()) {
                    loginparam.put(ConstantString.ADD_ATTACHMENT, gson.toJson(demofilenamesizearray));
                }


                loginparam.put(ConstantString.ADD_MIN_AGE, minage_txt.getText().toString());
                loginparam.put(ConstantString.ADD_MAX_AGE, maxage_txt.getText().toString());
                loginparam.put(ConstantString.ADD_HEIGHT_FEET, feet_txt.getText().toString());
                loginparam.put(ConstantString.ADD_HEIGHT_INCH, inchestxt.getText().toString());
                loginparam.put(ConstantString.ADD_IDEAL_WEIGTH, pounds_txt.getText().toString());

                loginparam.put(ConstantString.ADD_ABOUT_PROJECT, projectinfo.getText().toString());
                loginparam.put(ConstantString.ADD_ABOUT_PERSONALITY, typeflooktxt.getText().toString());
                loginparam.put(ConstantString.ADD_HOURLY_RATE, jobrate_txt.getText().toString().trim());//hourly_rate
                loginparam.put(ConstantString.ADD_JOBINFO, jobtitle_txt.getText().toString());//May be wrong
                loginparam.put(ConstantString.ADD_TOTAL_ROLES, vacancies_tv.getText().toString());
//                loginparam.put(ConstantString.ADD_GENDER_TYPE, gson.toJson(genderarray));
                loginparam.put(ConstantString.ADD_GENDER_TYPE, gendervalue);
                loginparam.put(ConstantString.ADD_PRODUCT_NAME, productname_txt.getText().toString());
                loginparam.put(ConstantString.ADD_JOB_DURATION, jobduration_txt.getText().toString());
                loginparam.put(ConstantString.ADD_ROLE_DESCRIPTION, jobdescription_txt.getText().toString());


                loginparam.put(ConstantString.ADD_SHOOT_PERFORMANCE, shootlocation_txt.getText().toString());
                loginparam.put(ConstantString.ADD_SHOOT_DATE_START, shootdatesfrom_Tv.getText().toString());
                loginparam.put(ConstantString.ADD_SHOOT_DATE_END, shootdatesto_tv.getText().toString());
                loginparam.put(ConstantString.ADD_CLIENT_INFO, castingcompany_txt.getText().toString());

                Log.d(TAG, "getParams: parameter-> " + loginparam);


                return loginparam;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(loginstringRequest);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: return code-> " + requestCode + " resultcode " + resultCode);
        if (resultCode == RESULT_OK) {
//            if (requestCode == ConstantString.GENDERSELECTIONCODE) {
//                Log.d(TAG, "onActivityResult: language");
//                assert data != null;
//                genderarray = (ArrayList<String>) data.getSerializableExtra("senddata");
//                if (!genderarray.isEmpty()) {
//                    if (genderarray.size() > 1) {
//                        gender_tv.setText(genderarray.get(0) + " and others");
//                    } else {
//                        gender_tv.setText(genderarray.get(0));
//                    }
////                    languagestxtview.setText(genderarray.get(0) + " and other");
//                } else {
//                    gender_tv.setText("Select Gender");
//                }
//            }
//
//
            if (requestCode == ConstantString.GENDERSELECTIONCODE) {
                Log.d(TAG, "onActivityResult: language");
                assert data != null;
                genderarray = new ArrayList<>();
                genderarray.add(data.getStringExtra("senddata"));
                gendervalue = data.getStringExtra("senddata");
                if (!genderarray.isEmpty()) {
                    genderlayaout.setBackgroundResource(R.color.verylightgray2);
                    if (genderarray.size() > 1) {
                        gender_tv.setText(genderarray.get(0) + " and others");
                    } else {
                        gender_tv.setText(genderarray.get(0));
                    }

                } else {
                    gender_tv.setText("Select Gender");
                }
            } else if (requestCode == ConstantString.JOBETYPECODE) {
                Log.d(TAG, "onActivityResult: skill");
                jobtypearray = new ArrayList<>();
//                jobtypearray.add(data.getStringExtra("senddata"));
                jobtypevalue = data.getStringExtra("senddata");

                if (!jobtypevalue.isEmpty() &&
                        jobtypevalue.trim().toLowerCase().equals("both")) {
                    jobtypelayout.setBackgroundResource(R.color.verylightgray2);
                    jobtypearray.add("Still photoshoot");
                    jobtypearray.add("Video shoot");
                    jobtypetextview_tv.setText(jobtypearray.get(0) +
                            " and others");
                } else if (!jobtypevalue.isEmpty()) {
                    jobtypearray.add(data.getStringExtra("senddata"));
                    jobtypetextview_tv.setText(jobtypearray.get(0));
                } else {
                    jobtypetextview_tv.setText("Select job type");
                }
            }

//                assert jobtypearray != null;
//                if (!jobtypearray.isEmpty()) {
//                    if (jobtypearray.size() > 1) {
//                        jobtypetextview_tv.setText(jobtypearray.get(0) + " and others");
//                    } else {
//                        jobtypetextview_tv.setText(jobtypearray.get(0));
//                    }
////                    skillstxt.setText(jobtypearray.get(0) + " and other");
//                } else {
//
//                }
//            }
//            else if (requestCode == ConstantString.JOBETYPECODE) {
//                Log.d(TAG, "onActivityResult: skill");
//                jobtypearray = (ArrayList<String>) data.getSerializableExtra("senddata");
//                assert jobtypearray != null;
//                if (!jobtypearray.isEmpty()) {
//                    if (jobtypearray.size() > 1) {
//                        jobtypetextview_tv.setText(jobtypearray.get(0) + " and others");
//                    } else {
//                        jobtypetextview_tv.setText(jobtypearray.get(0));
//                    }
////                    skillstxt.setText(jobtypearray.get(0) + " and other");
//                } else {
//                    jobtypetextview_tv.setText("Select job type");
//                }
//            }
            else if (requestCode == ConstantString.HAIRLENGTHCODE) {
                Log.d(TAG, "onActivityResult: bodytype");
                hairlengtharray = (ArrayList<String>) data.getSerializableExtra("senddata");
                assert hairlengtharray != null;
                if (!hairlengtharray.isEmpty()) {
                    hairlengthlayout.setBackgroundResource(R.color.verylightgray2);
                    if (hairlengtharray.size() > 1) {
                        hairlengthtv_tv.setText(hairlengtharray.get(0) + " and others");
                    } else {
                        hairlengthtv_tv.setText(hairlengtharray.get(0));
                    }

//                    m_bodytype.setText(hairlengtharray.get(0) + " and other");
                } else {
                    hairlengthtv_tv.setText("Select hair length");
                }
            } else if (requestCode == ConstantString.ETHNICITYCODE) {
                Log.d(TAG, "onActivityResult: ethnicitycode");
                ethnicityarray = (ArrayList<String>) data.getSerializableExtra("senddata");
                assert ethnicityarray != null;
                Log.d(TAG, "onActivityResult:ethnicityarray->  " + ethnicityarray);
                if (!ethnicityarray.isEmpty()) {
                    ethnicitylayout.setBackgroundResource(R.color.verylightgray2);
                    if (ethnicityarray.size() > 1) {
                        ethnicitytv_tv.setText(ethnicityarray.get(0) + " and others");
                    } else {
                        ethnicitytv_tv.setText(ethnicityarray.get(0));
                    }
//                    m_ethnicitytv.setText(ethnicityarray.get(0) + " and other");
                } else {
                    ethnicitytv_tv.setText("Select ethnicity");
                }
            } else if (requestCode == ConstantString.SKILLCODE) {
                Log.d(TAG, "onActivityResult: ethnicitycode");
                skillarray = (ArrayList<String>) data.getSerializableExtra("senddata");
                assert skillarray != null;
                Log.d(TAG, "onActivityResult:ethnicityarray->  " + skillarray);
                if (!skillarray.isEmpty()) {
                    specialskillslayout.setBackgroundResource(R.color.verylightgray2);
                    if (skillarray.size() > 1) {
                        specialskillstv_tv.setText(skillarray.get(0) + " and others");
                    } else {
                        specialskillstv_tv.setText(skillarray.get(0));
                    }
//                    m_ethnicitytv.setText(skillarray.get(0) + " and other");
                } else {
                    specialskillstv_tv.setText("Select special skills");
                }
            } else if (requestCode == ConstantString.EYECOLORCODE) {
                Log.d(TAG, "onActivityResult: ethnicitycode");
                eyecolor = (ArrayList<String>) data.getSerializableExtra("senddata");
                assert eyecolor != null;
                Log.d(TAG, "onActivityResult:ethnicityarray->  " + eyecolor);
                if (!eyecolor.isEmpty()) {
                    eyecolorlayout.setBackgroundResource(R.color.verylightgray2);
                    if (eyecolor.size() > 1) {
                        eyecolortv_tv.setText(eyecolor.get(0) + " and others");
                    } else {
                        eyecolortv_tv.setText(eyecolor.get(0));
                    }
//                    m_ethnicitytv.setText(eyecolor.get(0) + " and other");
                } else {
                    eyecolortv_tv.setText("Select eye color");
                }
            } else if (requestCode == ConstantString.HAIRCOLORCODE) {
                Log.d(TAG, "onActivityResult: ethnicitycode");
                haircolorArray = (ArrayList<String>) data.getSerializableExtra("senddata");
                assert haircolorArray != null;
                Log.d(TAG, "onActivityResult:ethnicityarray->  " + haircolorArray);
                if (!haircolorArray.isEmpty()) {
                    haircolorlayout.setBackgroundResource(R.color.verylightgray2);
                    if (haircolorArray.size() > 1) {
                        haircolortv_tv.setText(haircolorArray.get(0) + " and others");
                    } else {
                        haircolortv_tv.setText(haircolorArray.get(0));
                    }
//                    m_ethnicitytv.setText(haircolorArray.get(0) + " and other");
                } else {
                    haircolortv_tv.setText("Select hair color");
                }
            }
            else if (requestCode == ConstantString.ALLFILEGETCODE) {
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
                    Log.d(TAG, "onActivityResult: imagesize.mib -> " + imgsize / MiB);
                    if (imgsize / MiB > 5.00) {
                        showvaldationError("Image size should not be greater then 5 mb ", R.raw.questionmark);
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
    }


    private void startprocess() {
        jobpostlayout.setVisibility(View.GONE);
        progressbarcreate.setVisibility(View.VISIBLE);
    }

    private void stopprocess() {
        jobpostlayout.setVisibility(View.VISIBLE);
        progressbarcreate.setVisibility(View.GONE);
    }

    public String getFileSize(long file) {


        final double length = Double.parseDouble(String.valueOf(file));
        Log.d(TAG, "getFileSize: length-> " + length);

        if (length > MiB) {
            return format.format(length / MiB) + " MB";
        }
        if (length > KiB) {
            return format.format(length / KiB) + " KB";
        }
        return format.format(length) + " B";
    }

    private boolean isdatevalidate(String sdate, String edate) {
        Log.d(TAG, "isdatevalidate: errormessase ");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate = new Date();
        Date convertedDate2 = new Date();
        try {

            convertedDate = sdf.parse(sdate);
            convertedDate2 = sdf.parse(edate);


            assert convertedDate2 != null;
            Log.d(TAG, "isdatevalidate: compare = " + convertedDate2.after(convertedDate));

            if (!convertedDate2.after(convertedDate)) {
                return false;
            }
        } catch (Exception ignored) {
            Log.d(TAG, "isdatevalidate: " + ignored.getMessage());
        }
//
        Log.d(TAG, "isdatevalidate: errormessase ");
        return true;
    }


//    @Override
//    public void onBackPressed() {
//
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        } else if (pressedTime + 2000 > System.currentTimeMillis()) {
//            super.onBackPressed();
//        } else {
//            Toast.makeText(getBaseContext(), R.string.pressback, Toast.LENGTH_SHORT).show();
//        }
//        pressedTime = System.currentTimeMillis();
//    }

}
