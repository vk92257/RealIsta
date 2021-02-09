package com.example.myapplication.Chat_module;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class SignupDemo_quickblox extends AppCompatActivity {

    private EditText input_email;
    private EditText input_password;
    private static  String TAG = "SignupDemo_quickblox";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.signupdemo_quickblox);

        initviews();
    }

    private void initviews() {

        input_password = findViewById(R.id.input_password);
        input_email = findViewById(R.id.input_email);
    }

    public void try_signup(View view) {
        String username = input_email.getText().toString();
        String pswd = input_password.getText().toString();

        QBUser qbUser = new QBUser(username, pswd);
        qbUser.setFullName(username);

        QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Toast.makeText(SignupDemo_quickblox.this, "signup successfully ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(SignupDemo_quickblox.this, "signup -> "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void try_login(View view) {

        String username = input_email.getText().toString();
        String pswd = input_password.getText().toString();


        QBUser qbUser = new QBUser(username, pswd);
        qbUser.setFullName(username);

        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Toast.makeText(SignupDemo_quickblox.this, "Login successfully ", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onSuccess: username-> "+username + " password -> "+pswd);
                Intent intent = new Intent(SignupDemo_quickblox.this,ChatDialogActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("password",pswd);
                startActivity(intent);

//                ChatHelper.getInstance().loginToChat(qbUser, new QBEntityCallback<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid, Bundle bundle) {
//
//                    }
//
//                    @Override
//                    public void onError(QBResponseException e) {
//
//                    }
//                });
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(SignupDemo_quickblox.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}