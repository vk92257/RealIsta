package com.example.myapplication.modelsprofile.models_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_activity.Clients_CreateAccount;
import com.example.myapplication.clientsprofile.clients_activity.Clients_SignUpActivity;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.InternetAccess;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Model_SignUpActivity extends AppCompatActivity {

    private String signinusing;
    private EditText fullname, email, pwd,conf_password;
    private Button signinbtn;
    private RequestQueue requestforSignup;
    private EditText m_mobileno;
    private static InternetAccess isinternet = new InternetAccess();
    public static final String TAG = "Models_SignUpActivity";
    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.signup2);
        initvalue();
    }

    /*** initialize value */
    private void initvalue() {
        fullname = findViewById(R.id.input_name);
        email = findViewById(R.id.input_email);
        pwd = findViewById(R.id.input_password);
        signinbtn = findViewById(R.id.btn_signup);
        conf_password  = findViewById(R.id.conf_password);
        m_mobileno = findViewById(R.id.mobile);
    }
    public void login(View view){
        finish();
        Intent intent = new Intent(this, Models_LoginActivity.class);
        intent.setAction(ConstantString.MODEL_LOGIN);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    /**
     * Button click
     */
    public void createaccount(View view) {
        signinbtn.setText(getResources().getText(R.string.signup));
        String name = fullname.getText().toString();
        String signupemail = email.getText().toString();
        String signuppwd = pwd.getText().toString();
        String confpwd = conf_password.getText().toString().trim();
        String mobileno = m_mobileno.getText().toString().trim();
        Log.d(TAG, "createaccount: date name= " + name + " signupemail = "
                + signupemail + " signuppwd= " + signuppwd);
        if (isvalidate(name, signupemail, signuppwd,confpwd,mobileno)) {
            if (InternetAccess.isOnline(this)) {

                String role = ConstantString.MODEL_LOGIN;
                Log.d(TAG, "createaccount: role-> " + role);
                vollyPostRequest(name, signupemail, signuppwd, role,mobileno);

            } else {
                signinbtn.setText(R.string.letgo);
                showvaldationError(getResources().getString(R.string.errorinternet),R.raw.no_internet);
            }
        }
    }

    /**
     * volley request for signup
     */
    private void vollyPostRequest(String fullname, String email, String password, String role,String mobileno) {
        disableView();
        Log.d(TAG, "vollyPostRequest: fullname-> " + fullname + " email-> " + email + " password-> " + password + " role-> " + role);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ApiConstant.REGISTER,
                response -> {
                    Log.d(TAG, "vollyPostRequest: response -> " + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {

                            JSONObject successresponse = jsonObject.getJSONObject(ConstantString.RESPONSE_SUCCESS);

                            Log.d(TAG, "vollyPostRequest: " + jsonObject.getString(ConstantString.RESPONSE_SUCCESS));
                            LoginTimesaveData loginTimesaveData = new LoginTimesaveData(
                              successresponse.getString(ConstantString.TOKEN),
                                    successresponse.getString(ConstantString.ID),
                                    ConstantString.MODEL_LOGIN
                            );

                            signup_quickblox(fullname, email, String.valueOf(successresponse.getString(ConstantString.ID)),
                                    loginTimesaveData);
//                            SharedPreferanceManager.getInstance(this).userLogin(loginTimesaveData);


                        } else {
                            enableView();
                            Log.d(TAG, "vollyPostRequest: message-> " + jsonObject.getString(ConstantString.RESPONSE_MESSAGE));
                            String apierror = jsonObject.getString(ConstantString.RESPONSE_MESSAGE);
                            showvaldationError(apierror,R.raw.onboard);
                            signinbtn.setText(R.string.letgo);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
            enableView();
                    Log.d(TAG, "vollyPostRequest: error-> " + error);
                    showvaldationError(error.getMessage(),R.raw.onboard);

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                postMap.put(ConstantString.NAME, fullname);
                postMap.put(ConstantString.EMAIL, email);
                postMap.put(ConstantString.PASSWORD, password);
                postMap.put(ConstantString.ROLE, role);
                postMap.put(ConstantString.MOBILE_NO,mobileno);
                //..... Add as many key value pairs in the map as necessary for your request
                return postMap;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
//       requestforSignup.add(stringRequest);
    }

    /**
     * textfields validation check
     */
    private boolean isvalidate(String name, String email, String signuppwd,String confpwd,String mobile_no) {

        if(name.isEmpty()){
            showvaldationError(getResources().getString(R.string.errornameempty),R.raw.questionmark);
            return false;
        }
        if ( name.length() <3) {
            showvaldationError(getResources().getString(R.string.errorenterfullname),R.raw.questionmark);
            return false;
        }
        if(email.isEmpty() ){
            showvaldationError(getResources().getString(R.string.errorloginemail),R.raw.questionmark);
            return false;
        }

        Log.d(TAG, "isvalidate: email "+isEmailValid(email));
        if ( !isEmailValid(email)) {
            showvaldationError(getResources().getString(R.string.erroremail),R.raw.questionmark);
            return false;
        }

        if (mobile_no.isEmpty()) {
            m_mobileno.setError(getResources().getString(R.string.errorphoneno));
            m_mobileno.requestFocus();
//            showvaldationError(getResources().getString(R.string.errorphoneno), R.raw.questionmark);
            return false;
        }
        if (!android.util.Patterns.PHONE.matcher(mobile_no).matches()) {
            m_mobileno.setError(getResources().getString(R.string.errorphonenovalidation));
            m_mobileno.requestFocus();
            return false;
        }

        if(signuppwd.isEmpty()){
            showvaldationError(getResources().getString(R.string.errorloginpwd),R.raw.questionmark);
            return false;
        }

        if ( signuppwd.length() < 8 ) {
            showvaldationError(getResources().getString(R.string.errorpws),R.raw.questionmark);
            return false;
        }
        if(confpwd.isEmpty()){
            showvaldationError(getResources().getString(R.string.errorconempty),R.raw.questionmark);
            return false;
        }
        if(!signuppwd.equals(confpwd)){
            showvaldationError(getResources().getString(R.string.errorconfi),R.raw.questionmark);
            return false;
        }

        return true;
    }


    /**
     * quick blox signup function
     */

    private void signup_quickblox(String fullname, String email,
                                  String id, LoginTimesaveData loginTimesaveData) {
        QBUser qbUserm = new QBUser();
        qbUserm.setLogin(id);
        qbUserm.setPassword(ConstantString.USER_DEFAULT_PASSWORD);
        qbUserm.setFullName(fullname);
        qbUserm.setEmail(email);

        QBUsers.signUp(qbUserm).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {

//                SharedPreferanceManager.getInstance(Model_SignUpActivity.this)
//                        .saveQbUser(qbUser);
                signinbtn.setText(R.string.letgo);
                goToHome(loginTimesaveData, id,qbUserm);
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(Model_SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void goToHome(LoginTimesaveData loginTimesaveData, String id,QBUser qbUser) {
        Intent intent = new Intent(Model_SignUpActivity.this, Models_createportfolioActivity.class);
        intent.putExtra("Modellogindata",loginTimesaveData);
        intent.putExtra(ConstantString.USERID, id);
        intent.putExtra("QBuserdata",qbUser);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }



    private void showvaldationError(String msg,int res) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg,res);
        bottomSheet_for_error.setCancelable(false);
        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");
        signinbtn.setText(R.string.letgo);

    }


    private boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onDestroy() {
        isinternet = null;
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private void disableView() {
        fullname.setClickable(false);
        email.setClickable(false);
        pwd.setClickable(false);
        signinbtn.setClickable(false);
        conf_password.setClickable(false);
        m_mobileno.setClickable(false);

        fullname.setEnabled(false);
        email.setClickable(false);
        signinbtn.setEnabled(false);
        pwd.setEnabled(false);
        conf_password.setEnabled(false);
        m_mobileno.setEnabled(false);


        fullname.setAlpha((float)0.5);
        email.setAlpha((float)0.5);
        signinbtn.setAlpha((float)0.5);
        pwd.setAlpha((float)0.5);
        conf_password.setAlpha((float)0.5);
        m_mobileno.setAlpha((float)0.5);



    }
    private void enableView() {
        fullname.setClickable(true);
        email.setClickable(true);
        pwd.setClickable(true);
        signinbtn.setClickable(true);
        conf_password.setClickable(true);
        m_mobileno.setClickable(true);

        fullname.setEnabled(true);
        email.setClickable(true);
        signinbtn.setEnabled(true);
        pwd.setEnabled(true);
        conf_password.setEnabled(true);
        m_mobileno.setEnabled(true);


        fullname.setAlpha((float)1);
        email.setAlpha((float)1);
        signinbtn.setAlpha((float)1);
        pwd.setAlpha((float)1);
        conf_password.setAlpha((float)1);
        m_mobileno.setAlpha((float) 1);


//        loginemail.setAlpha((float) 0.5);
//        loginpwd.setAlpha((float) 0.5);

    }
}
