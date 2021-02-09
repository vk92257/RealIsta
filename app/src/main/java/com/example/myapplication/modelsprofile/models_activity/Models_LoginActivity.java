package com.example.myapplication.modelsprofile.models_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.myapplication.clientsprofile.clients_activity.Clients_CreateAccount;
import com.example.myapplication.clientsprofile.clients_activity.Clients_Home;
import com.example.myapplication.clientsprofile.clients_activity.Clients_LoginActivity;
import com.example.myapplication.clientsprofile.clients_activity.Clients_SignUpActivity;
import com.example.myapplication.clientsprofile.clients_activity.FetchAll_ClientData;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.InternetAccess;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.quickblox.users.QBUsers.updateUser;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Models_LoginActivity extends AppCompatActivity {
    private String loginAs;
    private Button loginbtn;
    private EditText loginemail;
    private EditText loginpwd;
    private View proressbar;
    private ScrollView createscroll;
    private static final String TAG = " Models_LoginActivity";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        initvalue();
        loginAs = getIntent().getAction();
    }


    private void initvalue() {
        loginbtn = findViewById(R.id.btn_signup);
        loginemail = findViewById(R.id.input_email);
        loginpwd = findViewById(R.id.input_password);

    }


    /**
     * on click view
     */
    public void try_login(View view) {
        String emailcheck = loginemail.getText().toString();
        String pwdcheck = loginpwd.getText().toString();
        loginbtn.setText(R.string.logging);
        Log.d(TAG, "try_login: logging..... email " + emailcheck + " pwd-> " + pwdcheck);
        disableView();
        if (isvalidate(emailcheck, pwdcheck)) {
            if (InternetAccess.isOnline(this)) {
                setlogin(emailcheck, pwdcheck);
            } else {
                enableView();
                showvaldationError((String) getResources().getText(R.string.errorinternet),
                        R.raw.no_internet);
                loginbtn.setText(R.string.login);
                Log.d(TAG, "try_login: ");
            }
        } else {
            enableView();
            Log.d(TAG, "try_login: is invalid login");
            loginbtn.setText(R.string.login);
        }
    }

    public void resetpassword(View view) {

    }

    public void signuppage(View view) {
        Log.d(TAG, "signuppage: -------------------------");

        Intent intent = new Intent(this, Model_SignUpActivity.class);
        intent.setAction(ConstantString.MODEL_LOGIN);
        finish();
        startActivity(intent);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }

    /**
     * on click view end
     */

    private boolean isvalidate(String email, String pwd) {

        if (email.isEmpty()) {
            showvaldationError(getResources().getString(R.string.errorloginemail), R.raw.questionmark);
            return false;
        }
        if (!isEmailValid(email)) {
            Log.d(TAG, "isvalidate: email = false");
//            showvaldationError();
            showvaldationError(getResources().getString(R.string.erroremail), R.raw.questionmark);
            return false;
        }

        if (pwd.isEmpty()) {
            showvaldationError(getResources().getString(R.string.errorloginpwd), R.raw.questionmark);
            Log.d(TAG, "isvalidate: pwd = false");
            return false;
        }
        if (pwd.length() < 8) {
            showvaldationError(getResources().getString(R.string.errorpws), R.raw.questionmark);
            Log.d(TAG, "isvalidate: pwd = false");
            return false;
        }


        return true;
    }

    private boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public void setlogin(String l_email, String l_pwd) {

        StringRequest loginstringRequest = new StringRequest(Request.Method.POST,
                ApiConstant.LOGIN,
                (Response.Listener<String>) response -> {
                    Log.d(TAG, "setlogin: response=> " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                            JSONObject succesobject = jsonObject.getJSONObject(ConstantString.RESPONSE_SUCCESS);
                            Log.d(TAG, "setlogin: token-> " + succesobject.getString(ConstantString.TOKEN));
                            Log.d(TAG, "setlogin: id-> " + succesobject.getInt(ConstantString.ID));
                            Log.d(TAG, "setlogin: token-> " + succesobject.getString(ConstantString.NAME));
                            Log.d(TAG, "setlogin: token-> " + succesobject.getString(ConstantString.EMAIL));
                            Log.d(TAG, "setlogin: role-> " + succesobject.getString(ConstantString.ROLE));
                            Log.d(TAG, "setlogin: token-> " + succesobject.getBoolean(ConstantString.CHECK_PROFILE));
//                            String roleapi = succesobject.getString(ConstantString.ROLE);
                            Log.d(TAG, "setlogin: token is -> " + succesobject.getString(ConstantString.TOKEN));


//                            SharedPreferanceManager.getInstance(this).removeQbUser();


                            try {
                                String roleapi = succesobject.getString(ConstantString.ROLE);
                                LoginTimesaveData loginTimesaveData = new LoginTimesaveData(
                                        succesobject.getString(ConstantString.TOKEN),
                                        succesobject.getString(ConstantString.ID),
                                        ConstantString.MODEL_LOGIN);

                                if (roleapi.equals(ConstantString.MODEL_LOGIN)) {

                                    if (!succesobject.getBoolean(ConstantString.CHECK_PROFILE)) {
                                        Log.d(TAG, "setlogin: successfull check");
                                        int id = succesobject.getInt(ConstantString.ID);

                                        Log.e(TAG, "onSuccess: id-> " + id);
                                        Intent intent = new Intent(Models_LoginActivity.this,
                                                Models_createportfolioActivity.class);

                                        intent.putExtra("Modellogindata", loginTimesaveData);
//                                        intent.putExtra("QBuserdata", user);

                                        intent.putExtra(ConstantString.USERID, String.valueOf(id));
                                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                        startActivity(intent);
                                        finish();

                                    } else {
//                                    enableView();
                                        Log.d(TAG, "setlogin: " + loginTimesaveData + " role-> " + roleapi);
                                        SharedPreferanceManager.getInstance(Models_LoginActivity.this).userLogin(loginTimesaveData);

                                        enableView();
                                        Intent intent = new Intent(Models_LoginActivity.this,
                                                FetchAll_ModelData.class);
                                        Log.d(TAG, "setlogin:inside id-> " + succesobject.getInt(ConstantString.ID));

                                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                        startActivity(intent);
                                        finish();

                                        loginbtn.setText(R.string.login);

                                    }
                                } else {
                                    enableView();
                                    showvaldationError(getResources().getString(R.string.errornotclient), R.raw.onboard);
                                    loginbtn.setText(R.string.login);

                                }

                            } catch (JSONException e) {
                                enableView();
                                e.printStackTrace();
                            }

//                            /**signin to quickblox*/
//                            signIn(succesobject);

//                            if(QBChatService.getInstance().isLoggedIn()){
//                                signIn(succesobject);
//                            }
//                            else{
//                                Log.e(TAG, "setlogin: already login" );
//                            }


                        } else {
                            showvaldationError(jsonObject.getString(ConstantString.RESPONSE_MESSAGE), R.raw.onboard);
                            loginbtn.setText(R.string.login);
                            enableView();
                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                },
                error -> {
                    enableView();
                    Log.d(TAG, "setlogin: error-> " + error.getMessage());
                    showvaldationError(error.getMessage(), R.raw.onboard);
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

                    loginbtn.setText(R.string.login);
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> loginparam = new HashMap<>();
                loginparam.put(ConstantString.EMAIL, l_email);
                loginparam.put(ConstantString.PASSWORD, l_pwd);
                return loginparam;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(loginstringRequest);
    }

    private void showvaldationError(String msg, int errorimage) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg, errorimage);
        bottomSheet_for_error.setCancelable(false);
//        bottomSheet_for_error.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        bottomSheet_for_error.setlottiimage(errorimage);
        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    /**
     * login to quickblock
     */

    private void signIn(JSONObject succesobject) {
//        QBUser user = new QBUser();
//        user.setLogin("37");
//        user.setPassword(ConstantString.USER_DEFAULT_PASSWORD);


        QBUser user = new QBUser();
        try {
            user.setLogin(succesobject.getString(ConstantString.ID));
            user.setEmail(succesobject.getString(ConstantString.EMAIL));
            user.setFullName(succesobject.getString(ConstantString.NAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user.setPassword(ConstantString.USER_DEFAULT_PASSWORD);

//        showProgressDialog(R.string.dlg_login);
        ChatHelper.getInstance().login(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser userFromRest, Bundle bundle) {
                Log.e(TAG, "onSuccess: signin " + user);
                Log.e(TAG, "onSuccess: email-> " + user.getLogin() + " emailcallback-> " + userFromRest.getLogin());
                if (userFromRest.getLogin().equalsIgnoreCase(user.getLogin())) {
//                    loginToChat(user);
                    Log.e(TAG, "onSuccess: signin " + user);
                    loginToChat(user, succesobject);


                } else {
                    //Need to set password NULL, because server will update user only with NULL password
                    user.setPassword(null);
                    updateUser(user);
                }
            }

            @Override
            public void onError(QBResponseException e) {
                enableView();
                Log.e(TAG, "onError: " + e.toString());
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


    private void loginToChat(final QBUser user, JSONObject succesobject) {
        Log.e(TAG, "loginToChat: inside");
        //Need to set password, because the server will not register to chat without password
        user.setPassword(ConstantString.USER_DEFAULT_PASSWORD);
        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                Log.e(TAG, "onSuccess: login to chat");

                try {
                    String roleapi = succesobject.getString(ConstantString.ROLE);
                    LoginTimesaveData loginTimesaveData = new LoginTimesaveData(
                            succesobject.getString(ConstantString.TOKEN),
                            succesobject.getString(ConstantString.ID),
                            ConstantString.MODEL_LOGIN);

                    if (roleapi.equals(ConstantString.MODEL_LOGIN)) {

                        if (!succesobject.getBoolean(ConstantString.CHECK_PROFILE)) {
                            Log.d(TAG, "setlogin: successfull check");
                            int id = succesobject.getInt(ConstantString.ID);

                            Log.e(TAG, "onSuccess: id-> " + id);
                            Intent intent = new Intent(Models_LoginActivity.this,
                                    Models_createportfolioActivity.class);
                            Log.e("checkuser", "onSuccess: " + user);
                            Log.d(TAG, "setlogin:inside id-> " + succesobject.getInt(ConstantString.ID));
                            intent.putExtra("Modellogindata", loginTimesaveData);
                            intent.putExtra("QBuserdata", user);

                            intent.putExtra(ConstantString.USERID, String.valueOf(id));
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            startActivity(intent);
                            finish();

                        } else {
//                                    enableView();
                            Log.d(TAG, "setlogin: " + loginTimesaveData + " role-> " + roleapi);

                            SharedPreferanceManager.getInstance(Models_LoginActivity.this).userLogin(loginTimesaveData);


                            SharedPreferanceManager.getInstance(Models_LoginActivity.this)
                                    .saveQbUser(user);


                            enableView();
                            Intent intent = new Intent(Models_LoginActivity.this,
                                    FetchAll_ModelData.class);
                            Log.d(TAG, "setlogin:inside id-> " + succesobject.getInt(ConstantString.ID));

                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            startActivity(intent);
                            finish();

                            loginbtn.setText(R.string.login);

                        }
                    } else {
                        enableView();
                        showvaldationError(getResources().getString(R.string.errornotclient), R.raw.onboard);
                        loginbtn.setText(R.string.login);

                    }

                } catch (JSONException e) {
                    enableView();
                    e.printStackTrace();
                }


////                SharedPreferanceManager.getInstance(FetchAll_ClientData.this).saveQbUser(user);
//                Intent intent = new Intent(Clients_LoginActivity.this,
//                        Clients_Home.class);
////                            Log.d(TAG, "setlogin:inside id-> "+succesobject.getInt(ConstantString.ID));
////                intent.putExtra(ConstantString.USER_TAG,user);
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.anim.fadein,R.anim.fadeout);

            }

            @Override
            public void onError(QBResponseException e) {
                enableView();
                Log.e(TAG, "onError: " + e.toString());
//                hideProgressDialog();
//                showErrorSnackbar(R.string.login_chat_login_error, e, null);
            }
        });
    }


    private void disableView() {
        loginemail.setClickable(false);
        loginpwd.setClickable(false);
        loginemail.setEnabled(false);
        loginpwd.setEnabled(false);

        loginemail.setAlpha((float) 0.5);
        loginpwd.setAlpha((float) 0.5);

    }


    private void enableView() {
        loginemail.setClickable(true);
        loginpwd.setClickable(true);
        loginemail.setEnabled(true);
        loginpwd.setEnabled(true);

        loginemail.setAlpha((float) 1);
        loginpwd.setAlpha((float) 1);
    }


//
//    private void initvalue() {
//        loginbtn = findViewById(R.id.btn_signup);
//        loginemail = findViewById(R.id.input_email);
//        loginpwd = findViewById(R.id.input_password);
//
//    }
//
//    public void try_login(View view) {
//        String emailcheck = loginemail.getText().toString();
//        String pwdcheck = loginpwd.getText().toString();
//        loginbtn.setText(R.string.logging);
//        disableView();
//        Log.d(TAG, "try_login: logging..... email " + emailcheck + " pwd-> " + pwdcheck);
//        if (isvalidate(emailcheck, pwdcheck)) {
//            if (InternetAccess.isOnline(this)) {
//                setlogin(emailcheck, pwdcheck);
//            } else {
//                enableView();
//                showvaldationError((String) getResources().getText(R.string.errorinternet),
//                        R.raw.no_internet);
//                loginbtn.setText(R.string.login);
//                Log.d(TAG, "try_login: ");
//            }
//        } else {
//            enableView();
//            Log.d(TAG, "try_login: is invalid login");
//            loginbtn.setText(R.string.login);
//        }
//    }
//
//    public void resetpassword(View view) {
//
//    }
//
//    public void signuppage(View view) {
//        this.finish();
//        Intent intent = new Intent(this, Model_SignUpActivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//
//    }
//
//    /**
//     * on click view end
//     */
//
//    private boolean isvalidate(String email, String pwd) {
//
//        if (!isEmailValid(email)) {
//            Log.d(TAG, "isvalidate: email = false");
////            showvaldationError();
//            showvaldationError(getResources().getString(R.string.erroremail), R.raw.questionmark);
//            return false;
//        }
//
//        if (pwd.isEmpty()) {
//            showvaldationError(getResources().getString(R.string.errorloginpwd), R.raw.questionmark);
//            Log.d(TAG, "isvalidate: pwd = false");
//            return false;
//        }
//        if (pwd.length() < 8) {
//            showvaldationError(getResources().getString(R.string.errorpws), R.raw.questionmark);
//            Log.d(TAG, "isvalidate: pwd = false");
//            return false;
//        }
//
//        return true;
//    }
//
//    private boolean isEmailValid(String email) {
//        Pattern pattern;
//        Matcher matcher;
//        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//        pattern = Pattern.compile(EMAIL_PATTERN);
//        matcher = pattern.matcher(email);
//        return matcher.matches();
//    }
//
//    private void showvaldationError(String msg) {
//        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg);
//        bottomSheet_for_error.setCancelable(false);
//        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");
//
//        loginbtn.setText(R.string.login);
//
//    }
//
//
//    public void setlogin(String l_email, String l_pwd) {
//
//        StringRequest loginstringRequest = new StringRequest(Request.Method.POST,
//                ApiConstant.LOGIN,
//                (Response.Listener<String>) response -> {
//                    Log.d(TAG, "setlogin: response=> " + response);
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
////                        Log.d(TAG, "setlogin: response-> "+jsonObject.getJSONObject(ConstantString.RESPONSE_SUCCESS));
//
//
//                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
//                            JSONObject succesobject = jsonObject.getJSONObject(ConstantString.RESPONSE_SUCCESS);
//                            Log.d(TAG, "setlogin: token-> " + succesobject.getString(ConstantString.TOKEN));
//                            Log.d(TAG, "setlogin: id-> " + succesobject.getInt(ConstantString.ID));
//                            Log.d(TAG, "setlogin: token-> " + succesobject.getString(ConstantString.NAME));
//                            Log.d(TAG, "setlogin: token-> " + succesobject.getString(ConstantString.EMAIL));
//                            Log.d(TAG, "setlogin: role-> " + succesobject.getString(ConstantString.ROLE));
//                            Log.d(TAG, "setlogin: token-> " + succesobject.getBoolean(ConstantString.CHECK_PROFILE));
//
//                            Log.d(TAG, "setlogin: token is -> " + succesobject.getString(ConstantString.TOKEN));
//
//
//                            /**signin to quickblox*/
//
//                            signIn(succesobject);
//
//
//
//                        } else {
//
//                            showvaldationError(jsonObject.getString(ConstantString.RESPONSE_MESSAGE));
//                            loginbtn.setText(R.string.login);
//                            enableView();
//                        }
//
//                    } catch (JSONException e) {
//
//                        e.printStackTrace();
//                    }
//
//                },
//                error -> {
//
//                    enableView();
//                    Log.d(TAG, "setlogin: error-> " + error.getMessage());
//                    showvaldationError(error.getMessage());
//                    if (error instanceof NetworkError) {
//                    } else if (error instanceof ServerError) {
//                    } else if (error instanceof AuthFailureError) {
//                    } else if (error instanceof ParseError) {
//                    } else if (error instanceof NoConnectionError) {
//                    } else if (error instanceof TimeoutError) {
//                        showvaldationError("Oops. Timeout error!");
//                    }
//                    loginbtn.setText(R.string.login);
//                }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> loginparam = new HashMap<>();
//                loginparam.put(ConstantString.EMAIL, l_email);
//                loginparam.put(ConstantString.PASSWORD, l_pwd);
//                return loginparam;
//            }
//        };
//
//        VolleySingleton.getInstance(this).addToRequestQueue(loginstringRequest);
//    }
//
//    private void showvaldationError(String msg, int errorimage) {
//        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg, errorimage);
//        bottomSheet_for_error.setCancelable(false);
////        bottomSheet_for_error.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        bottomSheet_for_error.setlottiimage(errorimage);
//        bottomSheet_for_error.show(getSupportFragmentManager(), "error bottom");
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//    }
//
//
//    /**login to quickblock*/
//
//    private void signIn(JSONObject succesobject) {
//
//        Log.e(TAG, "signIn: " );
//
//        QBUser user = new QBUser();
//        try {
//            user.setLogin(succesobject.getString(ConstantString.ID));
//            user.setEmail(succesobject.getString(ConstantString.EMAIL));
//            user.setFullName(succesobject.getString(ConstantString.NAME));
////            user.setEmail(succesobject.getString(ConstantString.E));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        user.setPassword(ConstantString.USER_DEFAULT_PASSWORD);
//        Log.e(TAG, "signIn: user-> "+user );
//
////        showProgressDialog(R.string.dlg_login);
//        ChatHelper.getInstance().login(user, new QBEntityCallback<QBUser>() {
//            @Override
//            public void onSuccess(QBUser userFromRest, Bundle bundle) {
//                Log.e(TAG, "onSuccess: signin "+user );
//                Log.e(TAG, "onSuccess: email-> "+user.getLogin()+" emailcallback-> "+userFromRest.getLogin());
//                if (userFromRest.getLogin().equalsIgnoreCase(user.getLogin())) {
////                    loginToChat(user);
//                    Log.e(TAG, "onSuccess: signin "+user );
//                    loginToChat(user, succesobject);
//
//
//
//
//                } else {
//                    //Need to set password NULL, because server will update user only with NULL password
//                    user.setPassword(null);
//                    updateUser(user);
//                }
//            }
//
//            @Override
//            public void onError(QBResponseException e) {
//                Log.e(TAG, "onError: "+e.toString() );
////                if (e.getHttpStatusCode() == UNAUTHORIZED) {
//////                    signUp(user);
////                } else {
////                    hideProgressDialog();
////                    showErrorSnackbar(R.string.login_chat_login_error, e, new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            signIn(user);
////                        }
////                    });
////                }
//            }
//        });
//    }
//
//    private void loginToChat(final QBUser user,JSONObject succesobject) {
//        Log.e(TAG, "loginToChat: inside" +user);
////        //Need to set password, because the server will not register to chat without password
////        user.setPassword(ConstantString.USER_DEFAULT_PASSWORD);
//        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
//            @Override
//            public void onSuccess(Void aVoid, Bundle bundle) {
//                Log.e(TAG, "onSuccess: login to chat");
//
//                try {
//                    String roleapi = succesobject.getString(ConstantString.ROLE);
//                    LoginTimesaveData loginTimesaveData = new LoginTimesaveData(
//                            succesobject.getString(ConstantString.TOKEN),
//                            succesobject.getString(ConstantString.ID),
//                            succesobject.getString(ConstantString.ROLE));
//
//                    Log.d(TAG, "setlogin: " + loginTimesaveData + " role-> " + roleapi);
//
//                    if (roleapi.equals(ConstantString.MODEL_LOGIN)) {
//
//                        if (!succesobject.getBoolean(ConstantString.CHECK_PROFILE)) {
//
//                            Log.d(TAG, "setlogin: successfull check");
//                            int id = succesobject.getInt(ConstantString.ID);
//                            Intent intent = new Intent(Models_LoginActivity.this,
//                                    Models_createportfolioActivity.class);
//
//
//                            Log.d(TAG, "setlogin:inside id-> " + succesobject.getInt(ConstantString.ID));
//
//                            intent.putExtra("Modellogindata", loginTimesaveData);
//                            intent.putExtra(ConstantString.USERID, id);
//                            intent.putExtra("QBuserdata",user);
//
//
//                            startActivity(intent);
//                            finish();
//                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                        } else {
//
//                            SharedPreferanceManager.getInstance(Models_LoginActivity.this).userLogin(loginTimesaveData);
////                                        Intent intent = new Intent(Models_LoginActivity.this,
////                                                Model_HomeActivity.class);
//
//                            SharedPreferanceManager.getInstance(Models_LoginActivity.this)
//                                    .saveQbUser(user);
//
//                            Intent intent = new Intent(Models_LoginActivity.this,
//                                    FetchAll_ModelData.class);
//                            Log.d(TAG, "setlogin:inside id-> " + succesobject.getInt(ConstantString.ID));
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                            finish();
//
//
//                            loginbtn.setText(R.string.login);
////                                    enableView();
//                        }
//                    } else {
//                        enableView();
//                        showvaldationError(getResources().getString(R.string.errornottalent), R.raw.onboard);
//                        loginbtn.setText(R.string.login);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//////                SharedPreferanceManager.getInstance(FetchAll_ClientData.this).saveQbUser(user);
////                Intent intent = new Intent(Clients_LoginActivity.this,
////                        Clients_Home.class);
//////                            Log.d(TAG, "setlogin:inside id-> "+succesobject.getInt(ConstantString.ID));
//////                intent.putExtra(ConstantString.USER_TAG,user);
////                startActivity(intent);
////                finish();
////                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
//
//            }
//
//            @Override
//            public void onError(QBResponseException e) {
//                Log.e(TAG, "onError: "+e.toString() );
////                hideProgressDialog();
////                showErrorSnackbar(R.string.login_chat_login_error, e, null);
//            }
//        });
//    }
//
//
//
//
//
//    private void disableView() {
//        loginemail.setClickable(false);
//        loginpwd.setClickable(false);
//        loginemail.setEnabled(false);
//        loginpwd.setEnabled(false);
//
//        loginemail.setAlpha((float) 0.5);
//        loginpwd.setAlpha((float) 0.5);
//
//    }
//
//    private void enableView() {
//        loginemail.setClickable(true);
//        loginpwd.setClickable(true);
//        loginpwd.setEnabled(true);
//        loginemail.setEnabled(true);
//
//        loginemail.setAlpha((float) 1);
//        loginpwd.setAlpha((float) 1);
//    }
}
