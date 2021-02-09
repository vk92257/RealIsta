package com.example.myapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.example.myapplication.landingpage;
import com.example.myapplication.pojo.ChatDialogHistory_users;
import com.example.myapplication.pojo.Chat_parameter;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.pojo.QbDialogs_userholderArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.users.model.QBUser;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferanceManager {
    //the constants
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String USER_INFO_PREF_MODEL = "com.example.myapplication_MODEL";
    private static final String USET_INFO_PREF_CLIENT = "com.example.myapplication_CLIENT";
    private static final String CHAT_USER_DETAIL_PREF  = "ChatModule_pref";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_GENDER = "keygender";
    private static final String KEY_ID = "keyid";
    private static final String MODEL_DATA = "modeldata";
    private static final String CLIENT_DATA = "clientdata";

    public static final String USER_DIALOG = "dilogsarray";
    
    public static final String USER_DIALOGARRAY = "dialogHArray";
    public static final String USER_CHATPARAM = "chatparamArray";
    private static SharedPreferences sharedPreferences;




    //    Chat User Details
    private static final String QB_USER_ID = "qb_user_id";
    private static final String QB_USER_LOGIN = "qb_user_login";
    private static final String QB_USER_PASSWORD = "qb_user_password";
    private static final String QB_USER_FULL_NAME = "qb_user_full_name";
    private static final String QB_USER_TAGS = "qb_user_tags";
    public static final String QB_USER_DIALOG = "userdialogs";

    private static SharedPreferanceManager mInstance;
    private static Context mCtx;

    private SharedPreferanceManager(Context context) {
        mCtx = context.getApplicationContext();

    }

    public static synchronized SharedPreferanceManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPreferanceManager(context.getApplicationContext());
            sharedPreferences = mCtx.getSharedPreferences(CHAT_USER_DETAIL_PREF, Context.MODE_PRIVATE);
        }
        return mInstance;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences2 = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences2.getString(ConstantString.TOKEN, null) != null;
    }

    public void userLogin(LoginTimesaveData loginTimesaveData) {
        Log.e("sharepref ", "userLogin: " );
        SharedPreferences sharedPreferences1 = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences= mCtx.getSharedPreferences(CHAT_USER_DETAIL_PREF, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putString(ConstantString.TOKEN, loginTimesaveData.getToken());
        editor.putString(ConstantString.ID, loginTimesaveData.getUserid());
        editor.putString(ConstantString.ROLE, loginTimesaveData.getRole());
        editor.apply();

    }

    public void save_model_userInformation(String modeldata) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(USER_INFO_PREF_MODEL, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MODEL_DATA, modeldata);
        editor.apply();
    }

    public String get_Model_userInformation() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(USER_INFO_PREF_MODEL, Context.MODE_PRIVATE);
        return sharedPreferences.getString(MODEL_DATA, null);
    }

    public void save_client_userInformation(String clientdata) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(USET_INFO_PREF_CLIENT, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CLIENT_DATA, clientdata);
        editor.apply();
    }


    public String get_CLIENT_userInformation() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(USET_INFO_PREF_CLIENT, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CLIENT_DATA, null);
    }

    public void clear_model_previoudDetail() {
        SharedPreferences sharedPreferences_modeldata = mCtx.getSharedPreferences(USER_INFO_PREF_MODEL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_modeldata = sharedPreferences_modeldata.edit();
        editor_modeldata.clear();
        editor_modeldata.apply();
    }

    public void clear_Client_previoudDetail() {
        SharedPreferences sharedPreferences_modeldata = mCtx.getSharedPreferences(USET_INFO_PREF_CLIENT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_modeldata = sharedPreferences_modeldata.edit();
        editor_modeldata.clear();
        editor_modeldata.apply();
    }


    public void SaveProfilImage(String profileimage) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstantString.PROFILE_IMAGESAVE, profileimage);
        editor.apply();
    }

    public String getprofile() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ConstantString.PROFILE_IMG, "null");
    }


    public LoginTimesaveData getUserData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return new LoginTimesaveData(
                sharedPreferences.getString(ConstantString.TOKEN, null),
                sharedPreferences.getString(ConstantString.ID, null),
                sharedPreferences.getString(ConstantString.ROLE, null));
    }

    public String Rolewith() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ConstantString.ROLE, null);
    }


    /** chat user Save*/

//    public boolean has(String key) {
//        return .contains(key);
//    }
    public void saveQbUser(QBUser qbUser) {
//        setSharedPreferences();
        Log.e("sharepref", "!!!!!saveQbUser: "+qbUser );
        save(QB_USER_ID, qbUser.getId());
        save(QB_USER_LOGIN, qbUser.getLogin());
        save(QB_USER_PASSWORD, qbUser.getPassword());
        save(QB_USER_FULL_NAME, qbUser.getFullName());
//        save(QB_USER_TAGS, qbUser.getTags().getItemsAsString());
    }


    public void setSharedPreferences(){
       sharedPreferences = mCtx.getSharedPreferences(CHAT_USER_DETAIL_PREF, Context.MODE_PRIVATE);
    }

    public void save(String key, Object value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof Boolean) {
            Log.e("sharepref", "save: "+ value);
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            Log.e("sharepref", "save: "+ value);
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            Log.e("sharepref", "save: "+ value);
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            Log.e("sharepref", "save: "+ value);
            editor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            Log.e("sharepref", "save: "+ value);
            editor.putString(key, (String) value);
        } else if (value instanceof Enum) {
            Log.e("sharepref", "save: "+ value);
            editor.putString(key, value.toString());
        } else if (value != null) {
            Log.e("sharepref", "save: "+ value);
            throw new RuntimeException("Attempting to save non-supported preference");
        }

        editor.apply();
    }

    public void removeQbUser() {
        delete(QB_USER_ID);
        delete(QB_USER_LOGIN);
        delete(QB_USER_PASSWORD);
        delete(QB_USER_FULL_NAME);
//        delete(QB_USER_TAGS);
    }
    public void delete(String key) {
        Log.e("delete", "delete: "+key+" object - >"+sharedPreferences );
        if (sharedPreferences.contains(key)) {
            getEditor().remove(key).commit();
        }
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public QBUser getQbUser() {
        if (hasQbUser()) {
            Integer id = get(QB_USER_ID);
            String login = get(QB_USER_LOGIN);
            String password = get(QB_USER_PASSWORD);
            String fullName = get(QB_USER_FULL_NAME);
//            String tagsInString = get(QB_USER_TAGS);


            QBUser user = new QBUser(login, password);
            user.setId(id);
            user.setFullName(fullName);
            return user;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) sharedPreferences.getAll().get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defValue) {
        T returnValue = (T) sharedPreferences.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }

    public boolean hasQbUser() {
        return has(QB_USER_LOGIN) && has(QB_USER_PASSWORD);
    }

    public boolean has(String key) {
//        setSharedPreferences();
        return sharedPreferences.contains(key);
    }


    public void logout() {
        SharedPreferences sharedPreferencesmain = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesmain.edit();

        editor.clear();
        editor.apply();

        clear_model_previoudDetail();
        clear_Client_previoudDetail();

        clear_chat_dataSave();

        Intent intent = new Intent(mCtx, landingpage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mCtx.startActivity(intent);
    }

    private void clear_chat_dataSave() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }

    public void clear_chatDialogArrayOnly(){
        sharedPreferences.edit().remove(USER_DIALOGARRAY).commit();
        sharedPreferences.edit().remove(USER_CHATPARAM).commit();
    }

//only for check
    public void getdata(){
        Log.e("contain", "shared preferance getdata: "+sharedPreferences.getString(USER_DIALOGARRAY, null) );
    }

    public void save_chatDialogs(ArrayList<QBChatDialog> qbChatDialogs){
        Gson gson = new Gson();
        String userdilaogs = gson.toJson(qbChatDialogs);
        chatdilogids(userdilaogs);
    }

    /**chat dilogs changes ids*/
    public void chatdilogids(String dialogs){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_DIALOG, dialogs);
        editor.apply();

    }
    
    public void saveArray_DialogHistory(QbDialogs_userholderArray qbDialogsUserholderArray){
        Gson gson = new Gson();
        String userdilaogs = gson.toJson(qbDialogsUserholderArray.getQbChatDialogs());
        chatdialogArray(userdilaogs);
        chatUserparamArray(gson.toJson(qbDialogsUserholderArray.getChat_parameters()));
    }

    private void chatUserparamArray(String chat_parameters) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_CHATPARAM, chat_parameters);
        editor.apply();
    }

    private void chatdialogArray(String userdilaogs) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_DIALOGARRAY, userdilaogs);
        editor.apply();
    }

    public QbDialogs_userholderArray getChatDialogHistory(){
        Gson gson = new Gson();
        String userdilaogs = sharedPreferences.getString(USER_DIALOGARRAY, null);
        String chat_parameters =  sharedPreferences.getString(USER_CHATPARAM, null);

        Type type = new TypeToken<ArrayList<QBChatDialog>>() {}.getType();

        Type typechatparam = new TypeToken<ArrayList<Chat_parameter>>() {}.getType();

        QbDialogs_userholderArray qbDialogs_userholderArray = new QbDialogs_userholderArray();

        qbDialogs_userholderArray.setChat_parameters(gson.fromJson(chat_parameters, typechatparam));

        qbDialogs_userholderArray.setQbChatDialogs(gson.fromJson(userdilaogs, type));

        return qbDialogs_userholderArray;
    }


    public ArrayList<QBChatDialog> getchatDialogs(){
        Gson gson = new Gson();
        String userdialogjson = sharedPreferences.getString(USER_DIALOG, null);
//        ArrayList<QBChatDialog> userdialogs = new ArrayList<QBChatDialog>(gson.fromJson(jsonText, String[].class));

        Type type = new TypeToken<ArrayList<QBChatDialog>>() {}.getType();
        return gson.fromJson(userdialogjson, type);
    }


    public boolean hascontaindialog(){
        return has(USER_DIALOG);
    }

    public boolean hascontainDialogAndChatparam(){
        return has(USER_DIALOGARRAY) && has(USER_CHATPARAM);
    }
}
