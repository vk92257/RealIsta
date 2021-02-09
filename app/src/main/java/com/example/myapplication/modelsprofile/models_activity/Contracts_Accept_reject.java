package com.example.myapplication.modelsprofile.models_activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.R;
import com.example.myapplication.chatModule.ChatHelper;
import com.example.myapplication.clientsprofile.clients_activity.Chat_Activity;
import com.example.myapplication.clientsprofile.clients_activity.FetchAll_ClientData;
import com.example.myapplication.pojo.Chat_parameter;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.Loading_dialog;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.google.android.material.card.MaterialCardView;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class
Contracts_Accept_reject extends AppCompatActivity {
    private TextView confirmaction;
    private RadioButton accepttermsradio;
    private MaterialCardView reject;
    private MaterialCardView accept;
    private Boolean value;
    private String contractID;
    private StringRequest model_request;
    private RadioGroup radioGroup;
    private Loading_dialog lodDialog;

    private String userid;
    private static final String TAG = "Contracts_Accept_reject";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.models_jobinvitation_sendresponse);
        value = getIntent().getBooleanExtra("value", true);
        contractID = getIntent().getStringExtra("confirm");
        if (getIntent().getStringExtra("userid") != null) {
            userid = getIntent().getStringExtra("userid");
        }
        initview(value);
    }

    @SuppressLint("SetTextI18n")
    private void initview(Boolean value) {
        confirmaction = findViewById(R.id.confirmaction);
        accepttermsradio = findViewById(R.id.accepttermsradio);
        radioGroup = findViewById(R.id.radiogroup);
        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.reject);

        lodDialog = new Loading_dialog(this);
        if (value) {
            confirmaction.setText("Are you sure you want to reject the contract? " +
                    "Confirm again to Continue");
            accept.setVisibility(View.INVISIBLE);
            accepttermsradio.setVisibility(View.GONE);
        } else {
            confirmaction.setText("You are about to accept the contract invitation. " +
                    "Confirm your action to start the contract.");
            reject.setVisibility(View.INVISIBLE);
        }

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(i);
//                boolean isChecked = checkedRadioButton.isChecked();
//                if (isChecked) {
//                    checkedRadioButton.setChecked(false);
//                } else {
//                    checkedRadioButton.setChecked(true);
//                }
//            }
//        });
//        accepttermsradio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Toast.makeText(Contracts_Accept_reject.this,"b-> "+b,Toast.LENGTH_SHORT).show();
//                if(!b){
//                    accepttermsradio.setChecked(false);
//                }else{
//                    accepttermsradio.setChecked(true);
//                }
//            }
//        });


    }

    public void radiosingle(View view) {

        Toast.makeText(Contracts_Accept_reject.this, "radiobutton -> " + accepttermsradio.isChecked(), Toast.LENGTH_SHORT).show();
        if (!accepttermsradio.isChecked()) {
            accepttermsradio.setChecked(false);
            accepttermsradio.setActivated(false);
        } else {
            accepttermsradio.setChecked(true);
            accepttermsradio.setActivated(true);
        }
    }

    public void back(View view) {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    public void reject_contract_invitation(View view) {
        initCallApi(ApiConstant.MODEL_REJECT_CONTRACT);
    }

    public void accept_contract_invitation(View view) {
        if (accepttermsradio.isChecked()) {
            initCallApi(ApiConstant.MODEL_ACCEPT_CONTRACT);
        } else {
            showvaldationError("Please accept the contract invitaion");
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private void initCallApi(String apiconform) {
        lodDialog.showDialog(R.raw.load);
//        Log.d(TAG, "initCallApi: called");
        model_request = new StringRequest(Request.Method.POST,
                apiconform,
                (Response.Listener<String>) response -> {
                    Log.d(TAG, "initCallApi: response-> " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {

//                            setResult(Activity.RESULT_OK);
//                            finish();
//                            Toast.makeText(this, jsonObject.getString(ConstantString.RESPONSE_MESSAGE)
//                                    , Toast.LENGTH_SHORT).show();
//                            createchatsession();


                            createchatsession(userid);


                        } else {
                            Log.d(TAG, "initCallApi: error found in client fetch ");
                            lodDialog.hideDialog();
                        }


                    } catch (JSONException e) {
                        Log.d(TAG, "initCallApi: " + e);
                        e.printStackTrace();
                        lodDialog.hideDialog();
                    }
                },
                eror -> {
                    lodDialog.hideDialog();
                    Log.d(TAG, "initCallApi: error-> " + eror.toString());
                    eror.printStackTrace();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> contract_value = new HashMap<>();
                contract_value.put("contract_id", contractID);
                return contract_value;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(Contracts_Accept_reject.this).
                        getUserData();
                Log.d(TAG, "getHeaders: token value -> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(model_request);
    }

    private void showvaldationError(String msg) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg);
        bottomSheet_for_error.setCancelable(false);
        bottomSheet_for_error.setlottiimage(R.raw.questionmark);
        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");

    }


    /**
     * connect chat session and create private dialog quickblox
     */

    private void createchatsession(String userid) {

        ChatHelper.getInstance().loadUserByLogin(userid, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {

                ChatHelper.getInstance().createPrivateChat(user, "new Chat", new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {

                        Log.e(TAG, "<---------------- onSuccess: dialog session created ------> ");
                        lodDialog.hideDialog();
                        setResult(Activity.RESULT_OK);
                        finish();

                    }

                    @Override
                    public void onError(QBResponseException e) {

                        Log.e(TAG, "onError:----------------- " + e.toString());
                        lodDialog.hideDialog();
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError:loaduser --------------------> " + e.toString());
                lodDialog.hideDialog();
            }
        });


    }

}
