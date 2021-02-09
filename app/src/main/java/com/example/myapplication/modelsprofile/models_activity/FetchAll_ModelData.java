package com.example.myapplication.modelsprofile.models_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.R;
import com.example.myapplication.chatModule.ChatHelper;
import com.example.myapplication.clientsprofile.clients_activity.Clients_Home;
import com.example.myapplication.clientsprofile.clients_activity.FetchAll_ClientData;
import com.example.myapplication.pojo.GetModelData;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.pojo.locationpojo;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.quickblox.users.QBUsers.updateUser;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FetchAll_ModelData extends AppCompatActivity {
    private RippleBackground content;
    private TextView msg;
    public static final String TAG = "FetchAll_ModelData";
    private StringRequest login_client_request;
    private GetModelData ModelData;

    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_fetch_all__model_data);
        iniview();
    }

    @SuppressLint("SetTextI18n")
    private void iniview(){
        content = findViewById(R.id.content);
        msg = findViewById(R.id.msg);
//        msg.setText("this is dat");
        content.startRippleAnimation();
        msg.setText(getResources().getText(R.string.fetchthebestjobforyou));
    }


    @Override
    protected void onStart() {
        super.onStart();
        initCallApi();
    }


    @Override
    protected void onStop() {
        super.onStop();
        content.stopRippleAnimation();
        if(login_client_request != null){
            login_client_request.cancel();
        }

    }



    private void initCallApi() {

        login_client_request = new StringRequest(Request.Method.GET,
                ApiConstant.MODEL_GET_PROFILE,
                (Response.Listener<String>) response -> {
                    Log.e(TAG, "initCallApi: \n\n\n response-> " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                            JSONObject modeldetail = jsonObject.getJSONObject(ConstantString.DETAIL_TAG);

                            ArrayList<String> languagelist = new ArrayList<>();
                            JSONArray langaray = modeldetail.getJSONArray(ConstantString.GET_TALENT_LANGUAGE);
                            for (int i = 0; i < langaray.length(); i++) {
                                languagelist.add(langaray.get(i).toString());
                            }

                            ArrayList<String> skilllist = new ArrayList<>();
                            JSONArray skillarray = modeldetail.getJSONArray(ConstantString.GET_TALENT_SKILL);
                            for (int i = 0; i < skillarray.length(); i++) {
                                skilllist.add(skillarray.get(i).toString());
                            }

                            ArrayList<String> bodytype = new ArrayList<>();
                            JSONArray bodyaray = modeldetail.getJSONArray(ConstantString.GET_TALENT_BODY_TYPE);
                            for (int i = 0; i < bodyaray.length(); i++) {
                                bodytype.add(bodyaray.get(i).toString());
                            }

                            ArrayList<String> ethicitytype = new ArrayList<>();
                            JSONArray etharay = modeldetail.getJSONArray(ConstantString.GET_TALENT_ETHNICITY);

                            for (int i = 0; i < etharay.length(); i++) {
                                ethicitytype.add(etharay.get(i).toString());
                            }

                            ArrayList<String> imagetype = new ArrayList<>();
                            JSONArray imagehd = modeldetail.getJSONArray(ConstantString.GET_TALENT_HD_IMAGE);
                            for (int i = 0; i < imagehd.length(); i++) {
                                imagetype.add(imagehd.get(i).toString());
                            }


//                            JSONObject countryjson =  modeldetail.getJSONObject(ConstantString.GET_TALENT_COUNTRY);


//                            modeldetail.getString(ConstantString.GET_TALENT_COUNTRY),
//                                    modeldetail.getString(ConstantString.GET_TALENT_STATE),
//                                    modeldetail.getString(ConstantString.GET_TALENT_CITY),

                            ModelData = new GetModelData(
                                    modeldetail.getString(ConstantString.USERID),
                                    modeldetail.getString(ConstantString.NAME),
                                    modeldetail.getString(ConstantString.EMAIL),
                                    modeldetail.getString(ConstantString.GET_TALENT_MOBILE),
                                    modeldetail.getString(ConstantString.GET_TALENT_AGE),
                                    modeldetail.getString(ConstantString.GET_TALENT_PROFFESION),
                                    modeldetail.getString(ConstantString.GENDER),
                                    modeldetail.getString(ConstantString.GET_TALENT_PASSPORT),
                                    modeldetail.getString(ConstantString.GET_TALENT_PERSONALBIO),
                                    modeldetail.getString(ConstantString.GET_TALENT_PERSONAL_JOURNEY),
                                    modeldetail.getString(ConstantString.GET_TALENT_HOULY_RATE),
                                    modeldetail.getString(ConstantString.GET_TALENT_ROLE_AGEMIN),
                                    modeldetail.getString(ConstantString.GET_TALENT_ROLE_AGEMAX),
                                    modeldetail.getString(ConstantString.GET_TALENT_HEIGHT_FEET),
                                    modeldetail.getString(ConstantString.GET_TALENT_HEIGHT_INC),
                                    skilllist,
                                    imagetype,
                                    modeldetail.getString(ConstantString.PROFILE_IMG),
                                    modeldetail.getString(ConstantString.GET_TALENT_WEIGHT),
                                    modeldetail.getString(ConstantString.GET_TALENT_VIDEO_URL),
                                    languagelist,
                                    bodytype,
                                    ethicitytype,
                                    modeldetail.getString(ConstantString.GET_TALENT_CREATEAT),
                                    modeldetail.getString(ConstantString.GET_TALENT_UPDATEDAT)
                            );

                            ModelData.setWaist(modeldetail.getString(ConstantString.GET_TAlENT_WAIST));
                            ModelData.setHip(modeldetail.getString(ConstantString.GET_TALENT_HIP));
                            ModelData.setChest(modeldetail.getString(ConstantString.GET_TALENT_CHEST));

                            ModelData.setCountry_pojo(convertlocationpojo(modeldetail.getJSONObject(ConstantString.GET_TALENT_COUNTRY)));
                            ModelData.setState_pojo(convertlocationpojo(modeldetail.getJSONObject(ConstantString.GET_TALENT_STATE)));
                            ModelData.setCity_pojo(convertlocationpojo(modeldetail.getJSONObject(ConstantString.GET_TALENT_CITY)));


                            Log.d(TAG, "initCallApi: chest-> "+modeldetail.getString(ConstantString.GET_TALENT_CHEST));
//                            ModelData


//                            Log.e(TAG, "initCallApi: is signin to chat service-> "+ChatHelper.getInstance().isLogged());



                            /** chat signin*/
                            if(!ChatHelper.getInstance().isLogged()){
//                                mainThreadHandler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
                                        Log.e(TAG, "initCallApi: is not login sharedpref user detail-> "+
                                                SharedPreferanceManager.getInstance(FetchAll_ModelData.this).getQbUser() );
                                        QBUser user = new QBUser();
                                        try {
                                            user.setLogin( modeldetail.getString(ConstantString.USERID));
                                            user.setPassword(ConstantString.USER_DEFAULT_PASSWORD);
                                            user.setEmail( modeldetail.getString(ConstantString.EMAIL));
                                            user.setFullName(modeldetail.getString(ConstantString.NAME));
//                                QBUser user = SharedPreferanceManager.getInstance(this).getQbUser();
                                            prepareUser(user);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

//                                    }
//                                });

                            }
                            else{

//                                SharedPreferanceManager.getInstance(FetchAll_ModelData.this)
//                                        .setSharedPreferences();

                            }

                            Gson modelgson = new Gson();
                            SharedPreferanceManager.getInstance(this)
                                    .save_model_userInformation(modelgson.toJson(ModelData));

//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Intent intent = new Intent(FetchAll_ModelData.this,Model_HomeActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);

//                                }
//                            },2000);
//

                        }
                        else{
                            Log.d(TAG, "initCallApi: went something wrong");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d(TAG, "initCallApi: error-> " + error.toString());
//                    showvaldationError(eror.toString());
                    if (error instanceof NetworkError) {
                        showvaldationError("Network Error please check internet connection", R.raw.onboard);
                    } else if (error instanceof ServerError) {
                        //handle if server error occurs with 5** status code
                        showvaldationError("Server side error", R.raw.onboard);
                    } else if (error instanceof AuthFailureError) {
                        //handle if authFailure occurs.This is generally because of invalid credentials
                        showvaldationError("please check your credentials ", R.raw.onboard);
                    } else if (error instanceof ParseError) {
                        //handle if the volley is unable to parse the response data.
                        showvaldationError(" Unable to parse the response data ", R.raw.onboard);
                    } else if (error instanceof NoConnectionError) {
                        //handle if no connection is occurred
                        showvaldationError(" No Connection to server ", R.raw.onboard);
                    } else if (error instanceof TimeoutError) {
                        showvaldationError("Time out error Please restart the app  ", R.raw.onboard);
                        //handle if socket time out is occurred.
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(FetchAll_ModelData.this).getUserData();
                Log.d(TAG, "getHeaders: token value-> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(login_client_request);

//        galleryll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                galleryopen();
//            }
//        });
//        portfolioll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                protfolioviewopen();
//            }
//        });

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


    private void prepareUser( QBUser user ) {
//        QBUser qbUser = new QBUser();
//        qbUser.setLogin("vivek@gmail.com");
//        qbUser.setFullName("sushil");
//        qbUser.setPassword(ConstantString.USER_DEFAULT_PASSWORD);
        signIn(user);
    }
    private void signIn(QBUser usersingin) {
        Log.e("signin", "signIn: signin----->" );
//        showProgressDialog(R.string.dlg_login);
        ChatHelper.getInstance().login(usersingin, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser userFromRest, Bundle bundle) {
                Log.e(TAG, "onSuccess: signin "+usersingin );
                Log.e(TAG, "onSuccess: email-> "+usersingin.getLogin()+" emailcallback-> "+userFromRest.getLogin());

                SharedPreferanceManager.getInstance(FetchAll_ModelData.this)
                        .saveQbUser(usersingin);

                Intent intent = new Intent(FetchAll_ModelData.this,
                        Model_HomeActivity.class);

//                            Log.d(TAG, "setlogin:inside id-> "+succesobject.getInt(ConstantString.ID));
//                intent.putExtra(ConstantString.USER_TAG,user);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);

                if (userFromRest.getLogin().equalsIgnoreCase(usersingin.getLogin())) {
//                    loginToChat(usersingin);
                    Log.e(TAG, "onSuccess: signin "+usersingin );
                    loginToChat(usersingin);

                } else {
                    //Need to set password NULL, because server will update usersingin only with NULL password
                    usersingin.setPassword(null);
                    updateUser(usersingin);
                }
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("signin", "onError: fetchall_modelData sigin again" );
                signIn(usersingin);
//                if (e.getHttpStatusCode() == UNAUTHORIZED) {
////                    signUp(usersingin);
//                } else {
//                    hideProgressDialog();
//                    showErrorSnackbar(R.string.login_chat_login_error, e, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            signIn(usersingin);
//                        }
//                    });
//                }
            }
        });
    }
    private void loginToChat(final QBUser user) {
        Log.e(TAG, "loginToChat: inside" );
        //Need to set password, because the server will not register to chat without password
        user.setPassword(ConstantString.USER_DEFAULT_PASSWORD);
        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                Log.e(TAG, "onSuccess: login to chat");
//                SharedPreferanceManager.getInstance(FetchAll_ModelData.this).saveQbUser(user);
//
//                Intent intent = new Intent(FetchAll_ModelData.this,
//                        Model_HomeActivity.class);
////                            Log.d(TAG, "setlogin:inside id-> "+succesobject.getInt(ConstantString.ID));
////                intent.putExtra(ConstantString.USER_TAG,user);
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
//                SharedPrefsHelper.getInstance().saveQbUser(user);
//                if (!chbSave.isChecked()) {
//                    clearDrafts();
//                }
//                DialogsActivity.start(LoginActivity.this);
//                finish();
//                hideProgressDialog();
            }

            @Override
            public void onError(QBResponseException e) {
//                hideProgressDialog();
//                showErrorSnackbar(R.string.login_chat_login_error, e, null);
            }
        });
    }


    private void showvaldationError(String msg, int errorimage) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg, errorimage);
        bottomSheet_for_error.setCancelable(false);
//        bottomSheet_for_error.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        bottomSheet_for_error.setlottiimage(errorimage);
        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");
    }



}