package com.example.myapplication.clientsprofile.clients_activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
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
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.InternetAccess;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class Clients_EndContact extends AppCompatActivity {

    private String contract_id;
    private TextView confirmaction;
    private RadioButton accepttermsradio;
    private MaterialCardView reject;
    private MaterialCardView accept;
    private StringRequest end_contract_request;
    public static final String TAG = "Clients_EndContact";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
//        setContentView(R.layout.clients_viewfulljobdetails);
        setContentView(R.layout.clients_endcontract);
        contract_id = getIntent().getStringExtra("contract_id");
        Log.d(TAG, "onCreate: contract_id-> "+contract_id);
        intview();
    }

    private void intview() {
        confirmaction = findViewById(R.id.confirmaction);
        accepttermsradio = findViewById(R.id.accepttermsradio);
        reject = findViewById(R.id.reject);
        accept = findViewById(R.id.accept);
    }

    public void back(View view) {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void cancel(View view) {
        finish();
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }

    public void endcontract(View view) {
        if (isvalid()) {
            if (InternetAccess.isOnline(this)) {
                volley_endcall();
            } else {
                showvaldationError((String) getResources().getText(R.string.errorinternet),
                        R.raw.no_internet);
            }
        }

    }


    private boolean isvalid() {
        if (!accepttermsradio.isChecked()) {
            Toast.makeText(this,
                    getResources().getText(R.string.errorconfirmationendcontract),
                    Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private void volley_endcall() {
        end_contract_request = new StringRequest(StringRequest.Method.POST,
                ApiConstant.CLIENT_END_FINAL_CONTRACT,
                (Response.Listener<String>) response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d(TAG, "volley_endcall: error response-> "+response);

                        if(!jsonObject.getBoolean(ConstantString.IS_ERROR)){
                            finish();
                            Toast.makeText(this, R.string.endcontactmsg, Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> {
                    showvaldationError(error.toString(),R.raw.onboard );
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> requestEnd = new HashMap<>();
                requestEnd.put("contract_id", contract_id);

                return requestEnd;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(Clients_EndContact.this).getUserData();
                Log.d(TAG, "getHeaders: token value -> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(end_contract_request);
    }

    private void showvaldationError(String msg, int errorimage) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg, errorimage);
        bottomSheet_for_error.setCancelable(false);
//        bottomSheet_for_error.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        bottomSheet_for_error.setlottiimage(errorimage);
        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");
    }
}
