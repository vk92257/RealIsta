package com.example.myapplication.Chat_module;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myapplication.Chat_module.AdapterChat.ChatDialogAdapter;
import com.example.myapplication.Chat_module.Holder.QBUsersHolder;
import com.example.myapplication.R;
import com.example.myapplication.chatModule.ChatHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import static com.quickblox.users.QBUsers.updateUser;

public class ChatDialogActivity extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton chatdialog_adduser;
    private static final String TAG = "ChateDialogActivity";
    private boolean isProcessingResultInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_chat_dialog);
        initview();
        createSecctionforChat();
        loadChatDialogs();

        chatdialog_adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDialogActivity.this,ListUserActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QBChatDialog qbChatDialog = (QBChatDialog)listView.getAdapter().getItem(position);
                Intent intent = new Intent(ChatDialogActivity.this,ChatMessageActivity.class);
                intent.putExtra(Common.DIALOG_EXTRA,qbChatDialog);
                startActivity(intent);
            }
        });
    }

    private void loadChatDialogs() {

        Log.e(TAG, "loadChatDialogs: ");
        QBRequestGetBuilder getBuilder = new QBRequestGetBuilder();
        getBuilder.setLimit(3);

        QBRestChatService.getChatDialogs(null,getBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                ChatDialogAdapter dialogAdapter =
                        new ChatDialogAdapter(ChatDialogActivity.this,qbChatDialogs);
                listView.setAdapter(dialogAdapter);
                dialogAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    private void createSecctionforChat() {
        ProgressDialog dialog = new ProgressDialog(ChatDialogActivity.this);
        dialog.setMessage("Please Wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        String name = getIntent().getStringExtra("username");
        String pwd = getIntent().getStringExtra("password");
        Log.d(TAG, "createSecctionforChat: username-> "+name+" password-> "+pwd);

        //load All user and save to cache

        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                QBUsersHolder.getInstance().putUsers(qbUsers);
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });

        QBUser qbUser = new QBUser(name,pwd);

        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {

                qbUser.setId(qbSession.getUserId());
                try {
                    Log.e(TAG, "onSuccess: qbSession.getUserId()-> "+qbSession.getUserId()+" token-> "+BaseService.getBaseService().getToken());
                    qbUser.setPassword(BaseService.getBaseService().getToken());
                } catch (BaseServiceException e) {
                    e.printStackTrace();
                }

                QBChatService.getInstance().login(qbUser, new QBEntityCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid, Bundle bundle) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e(TAG, "onError: "+e.getMessage() );
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });

    }

    private void initview() {

        listView = findViewById(R.id.lstChatDialogs);
        chatdialog_adduser = findViewById(R.id.chatdialog_adduser);
    }

    /**dialog ini*/
//    private void loadDialogsFromQb(final boolean silentUpdate, final boolean clearDialogHolder) {
//        isProcessingResultInProgress = true;
//        if (silentUpdate) {
//            progress.setVisibility(View.VISIBLE);
//        } else {
//            showProgressDialog(R.string.dlg_loading);
//        }
//        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
//        requestBuilder.setLimit(DIALOGS_PER_PAGE);
//        requestBuilder.setSkip(clearDialogHolder ? 0 : QbDialogHolder.getInstance().getDialogs().size());
//
//        ChatHelper.getInstance().getDialogs(requestBuilder, new QBEntityCallback<ArrayList<QBChatDialog>>() {
//            @Override
//            public void onSuccess(ArrayList<QBChatDialog> dialogs, Bundle bundle) {
//                if (dialogs.size() < DIALOGS_PER_PAGE) {
//                    hasMoreDialogs = false;
//                }
//                if (clearDialogHolder) {
//                    QbDialogHolder.getInstance().clear();
//                    hasMoreDialogs = true;
//                }
//                QbDialogHolder.getInstance().addDialogs(dialogs);
//                updateDialogsAdapter();
//
//                DialogJoinerAsyncTask joinerTask = new DialogJoinerAsyncTask(DialogsActivity.this, dialogs);
//                joinerTasksSet.add(joinerTask);
//                joinerTask.execute();
//
//                disableProgress();
//                if (hasMoreDialogs) {
//                    loadDialogsFromQb(true, false);
//                }
//            }
//
//            @Override
//            public void onError(QBResponseException e) {
//                disableProgress();
//                ToastUtils.shortToast(e.getMessage());
//            }
//        });
//    }




    @Override
    protected void onResume() {
        super.onResume();
        loadChatDialogs();
    }
}