package com.example.myapplication.Chat_module;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.Chat_module.AdapterChat.ListUserAdapter;
import com.example.myapplication.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

public class

ListUserActivity extends AppCompatActivity {
    private ListView lstUsers;
    private Button btnCreateChat;
    private static  String TAG = "ListUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_list_user);

        initViews();
        reteriveAllUser();

        btnCreateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int countChoice = lstUsers.getCount();
                Log.e(TAG, "onClick: countechoice-> "+countChoice);
                if(lstUsers.getCheckedItemPositions().size() == 1){
                    createPrivateChat(lstUsers.getCheckedItemPositions());
                    Log.e(TAG, "onClick: createPrivateChat-> "+lstUsers.getCheckedItemPosition());
                }
                else{
                    Toast.makeText(ListUserActivity.this,"no chat buddy choose",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createPrivateChat(SparseBooleanArray checkedItemPositions) {

        Log.e(TAG, "createPrivateChat:checkedItemPositions->  "+checkedItemPositions );
        ProgressDialog progressDialog = new ProgressDialog(ListUserActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
//
        int countChoice  = lstUsers.getCount();
//        ArrayList<Integer> occuastidsList = new ArrayList<>();
        Log.e(TAG, "createPrivateChat: countchoice-> "+countChoice);
        for(int i=0;i<countChoice;i++){
            Log.e(TAG, "createPrivateChat: "+checkedItemPositions.get(i) );
            if(checkedItemPositions.get(i)){

                QBUser qbUser = (QBUser)lstUsers.getItemAtPosition(i);
                Log.e(TAG, "createPrivateChat: " );
                QBChatDialog dialog = DialogUtils.buildPrivateDialog(qbUser.getId());
                QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        progressDialog.dismiss();
                        Toast.makeText(ListUserActivity.this,"Create chat successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });
            }
        }



    }

    private void initViews() {

        lstUsers = findViewById(R.id.lstUsers);
        btnCreateChat = findViewById(R.id.btn_create_chat);
        lstUsers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }
    private void reteriveAllUser() {

        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                ArrayList<QBUser> qbUserWithoutCurrent = new ArrayList<>();
                for(QBUser user : qbUsers){
                    Log.e(TAG, "onSuccess:QBChatService.getInstance().getUser().getLogin() ->  "+
                            QBChatService.getInstance().getUser().getLogin()+"user.getLogin() "+user.getLogin());
                    if(!user.getLogin().equals(QBChatService.getInstance().getUser().getLogin())){
                        qbUserWithoutCurrent.add(user);
                    }

                }
                Log.e(TAG, "onSuccess: qbuserWithoutCurrent-> "+qbUserWithoutCurrent.size());
                ListUserAdapter listUserAdapter = new ListUserAdapter(getBaseContext(),qbUserWithoutCurrent);
                lstUsers.setAdapter(listUserAdapter);
                listUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: "+e.getMessage() );
            }
        });
    }


}