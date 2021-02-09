package com.example.myapplication.clientsprofile.clients_activity;

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
import com.example.myapplication.modelsprofile.models_activity.Models_createportfolioActivity;
import com.example.myapplication.pojo.GetClientData;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.pojo.locationpojo;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.google.gson.Gson;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.quickblox.users.QBUsers.updateUser;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FetchAll_ClientData extends AppCompatActivity {
    private RippleBackground content;
    private TextView msg;
    private StringRequest login_client_request;
    private static final String TAG = "FetchAll_ClientData";
    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_fetch_all__client_data);
        iniview();
    }

    @SuppressLint("SetTextI18n")
    private void iniview() {
        content = findViewById(R.id.content);
        msg = findViewById(R.id.msg);
        content.startRippleAnimation();
        msg.setText(getResources().getText(R.string.filterthebesttalent));
    }

    @Override
    protected void onStart() {
        super.onStart();
        initCallApi();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (login_client_request != null) {
            login_client_request.cancel();
        }

    }

    private void initCallApi() {

        LoginTimesaveData logindata = SharedPreferanceManager.getInstance(FetchAll_ClientData.this).getUserData();
        Log.e(TAG, "\n\n\n\n\ninitCallApi: token --> \n\n\n\n"+logindata.getToken() +"\n\n\n\n\n\n\n\n");
//        Log.d(TAG, "initCallApi: called");
        login_client_request = new StringRequest(Request.Method.GET,
                ApiConstant.CLIENT_GET_PROFILE,
                (Response.Listener<String>) response -> {
                    Log.e(TAG, "initCallApi: response-> " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                            JSONObject clientdetail = jsonObject.getJSONObject(ConstantString.DETAIL_TAG);
//                            Log.d(TAG, "initCallApi: data ->"+clientdetail.getString(ConstantString.ID)
//                                    +" name "+clientdetail.getString(ConstantString.EMAIL)+
//                                    "gender "+clientdetail.getString(ConstantString.GENDER));
                            GetClientData clientData = new GetClientData(
                                    clientdetail.getString(ConstantString.USERID),
                                    clientdetail.getString(ConstantString.NAME),
                                    clientdetail.getString(ConstantString.EMAIL),
                                    clientdetail.getString(ConstantString.GENDER),
                                    clientdetail.getString(ConstantString.GET_CLIENT_MOBILE),
                                    convertlocationpojo(clientdetail.getJSONObject(ConstantString.GET_CLIENT_COUNTRY)),
                                    convertlocationpojo(clientdetail.getJSONObject(ConstantString.GET_CLIENT_STATE)),
                                    convertlocationpojo(clientdetail.getJSONObject(ConstantString.GET_CLIENT_CITY)),
                                    clientdetail.getString(ConstantString.PROFILE_IMG),
                                    clientdetail.getString(ConstantString.GET_CLIENT_CREATED_AT),
                                    clientdetail.getString(ConstantString.GET_CLIENT_UPDATED_AT)
                            );


                            Log.e(TAG, "initCallApi: " + clientdetail.getJSONObject(ConstantString.GET_CLIENT_COUNTRY));
                            Log.e(TAG, "initCallApi: image-> " + clientdetail.getString(ConstantString.PROFILE_IMG));


//                            Intent intent = new Intent(FetchAll_ClientData.this,
//                                    Clients_Home.class);
////
//                            startActivity(intent);
//                            finish();
//                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                            /** chat signin*/

                            if(!QBChatService.getInstance().isLoggedIn()){
                                Log.e(TAG, "initCallApi: is loggedIN" );

//                               mainThreadHandler.post(new Runnable() {
//                                   @Override
//                                   public void run() {
                                       Log.e(TAG, "initCallApi: is not login" );
                                       QBUser user = new QBUser();
                                       try {
                                           user.setLogin( clientdetail.getString(ConstantString.USERID));
                                           user.setPassword(ConstantString.USER_DEFAULT_PASSWORD);
                                           user.setEmail( clientdetail.getString(ConstantString.EMAIL));
                                           user.setFullName(clientdetail.getString(ConstantString.NAME));
                                           prepareUser(user);
                                       } catch (JSONException e) {
                                           e.printStackTrace();
                                       }

//                                   }
//                               });
                            }



//                            setViews(clientData);
                            Gson modelgson = new Gson();
                            SharedPreferanceManager.getInstance(this)
                                    .save_client_userInformation(modelgson.toJson(clientData));


                        } else {
                            Log.d(TAG, "initCallApi: error found in client fetch ");
                        }


                    } catch (JSONException e) {
                        Log.d(TAG, "initCallApi: " + e);
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d(TAG, "initCallApi: error-> " + error.toString());
                    error.printStackTrace();

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
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(FetchAll_ClientData.this).getUserData();
                Log.d(TAG, "getHeaders: token value -> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(login_client_request);
    }

    private locationpojo convertlocationpojo(JSONObject location) {
        locationpojo locationpojo = new locationpojo();
        try {
            locationpojo.setLocationid(location.getString("id"));
            locationpojo.setLocation_name(location.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return locationpojo;
    }

    private void showvaldationError(String msg, int errorimage) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg, errorimage);
        bottomSheet_for_error.setCancelable(false);
//        bottomSheet_for_error.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        bottomSheet_for_error.setlottiimage(errorimage);
        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");
    }

    private void prepareUser( QBUser user) {

        signIn(user);
    }


    private void signIn(final QBUser user) {
//        showProgressDialog(R.string.dlg_login);
        ChatHelper.getInstance().login(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser userFromRest, Bundle bundle) {
                Log.e(TAG, "onSuccess: signin " + user);
                Log.e(TAG, "onSuccess: email-> " + user.getLogin() + " emailcallback-> " + userFromRest.getLogin());
//                SharedPreferanceManager.getInstance(FetchAll_ClientData.this)
//                        .setSharedPreferences();

                SharedPreferanceManager.getInstance(FetchAll_ClientData.this)
                        .saveQbUser(user);
                Intent intent = new Intent(FetchAll_ClientData.this,
                        Clients_Home.class);
//
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                if (userFromRest.getLogin().equalsIgnoreCase(user.getLogin())) {
//                    loginToChat(user);
                    Log.e(TAG, "onSuccess: signin " + user);
                    loginToChat(user);


                } else {
                    //Need to set password NULL, because server will update user only with NULL password
                    user.setPassword(null);
                    updateUser(user);
                }
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: signin again---> " );
                signIn(user);
//                if (e.getHttpStatusCode() == UNAUTHORIZED) {
////                    signUp(user);
//                } else {
//                    hideProgressDialog();
//                    showErrorSnackbar(R.string.login_chat_login_error, e, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            signIn(user);
//                        }
//                    });
//                }
            }
        });
    }


    private void loginToChat(final QBUser user) {
        Log.e(TAG, "loginToChat: inside");
        //Need to set password, because the server will not register to chat without password
        user.setPassword(ConstantString.USER_DEFAULT_PASSWORD);
        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                Log.e(TAG, "onSuccess: login to chat");
//                SharedPreferanceManager.getInstance(FetchAll_ClientData.this).saveQbUser(user);
//                Intent intent = new Intent(FetchAll_ClientData.this,
//                        Clients_Home.class);
////                            Log.d(TAG, "setlogin:inside id-> "+succesobject.getInt(ConstantString.ID));
////                intent.putExtra(ConstantString.USER_TAG,user);
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: loginchat-> "+e.toString() );
//                hideProgressDialog();
//                showErrorSnackbar(R.string.login_chat_login_error, e, null);
            }
        });
    }


}