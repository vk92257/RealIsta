package com.example.myapplication.clientsprofile.clients_activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.myapplication.AttachmentPreviewAdapterView;
import com.example.myapplication.Chat_module.Holder.QBChatMessageHolder;
import com.example.myapplication.R;
import com.example.myapplication.chatModule.CallActivity;
import com.example.myapplication.chatModule.ChatHelper;
import com.example.myapplication.chatModule.Chat_Adapter.Attachment_Adapter;
import com.example.myapplication.chatModule.Chat_Adapter.MainChatAdapter;
import com.example.myapplication.chatModule.calling_services.VideoCallService;
import com.example.myapplication.chatModule.calling_services.VoiceCallActivity;
import com.example.myapplication.chatModule.chatInterface.SendAttachmentCallback;
import com.example.myapplication.chatModule.holder.QbDialogHolder;
import com.example.myapplication.pojo.Chat_parameter;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.Loading_dialog;
import com.example.myapplication.utils.NetworkChangeReceiver;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.UriUtils;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBMessageStatusesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSignaling;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBMessageStatusListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.listeners.QBVideoChatSignalingManagerListener;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks;
import com.quickblox.videochat.webrtc.view.QBRTCSurfaceView;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.DiscussionHistory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import static com.quickblox.videochat.webrtc.QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
import static com.quickblox.videochat.webrtc.QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;

import static com.quickblox.users.QBUsers.updateUser;

public class Chat_Activity extends AppCompatActivity implements SendAttachmentCallback, QBMessageStatusListener {

    private static final int MY_PERMISSION_REQUEST_CAMERA = 0102;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 0101;
    private CircularImageView profileimage;
    private TextView title;
    private RecyclerView rv;
    private EditText messageArea;
    private QBChatDialog qbChatDialog;
    private Chat_parameter chat_recever;
    //    private ChatMessageAdapter_2 adapter;
    private MainChatAdapter adapter;
    private ArrayList<QBChatMessage> qbChatrecent;
    private ProgressDialog dialog;
    private Attachment_Adapter attachment_adapter;
    private QBUser qbUser;
    private QBChatService qbChatService;
    private LinearLayout user_chat_profile_id;

    private RelativeLayout loding_message_layout_id;

    private SwipeRefreshLayout messages_refresh;

    private QBMessageStatusesManager qbMessageStatusesManager;

    //    calling activity
    private QBChatService chatSevice;
    private QBRTCClient rtcClient;
    private MediaPlayer mp;
    private QBRTCSurfaceView localView, surfaceView;
    public static QBRTCSession qbrtcSession1;
    private HashMap<String, String> data;
    private static String TAG = "Model_chat_Activity";
    private Intent intent;
    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();

    private int skipPagination = 0;
    private Boolean checkAdapterInit = false;

    private ArrayList<File> show_attachment = new ArrayList<>();
    public static final int PERMISSIONS_FOR_SAVE_FILE_IMAGE_REQUEST = 1010;
    public static final int GALLERY_REQUEST_CODE = 183;
    private static final int MESSAGE_LIMIT = 1000;

    //    private static final String VIDEO_OR_IMAGE_MIME = "image/* video/*";
    private static final String VIDEO_OR_IMAGE_MIME = "*/*";
    private static final String IMAGE_MIME = "image/*";

    private static final String SCHEME_CONTENT = "content";
    private static final String SCHEME_CONTENT_GOOGLE = "content://com.google.android";
    private static final String SCHEME_FILE = "file";


    private QBIncomingMessagesManager incomingMessagesManager;
    private QBChatDialogMessageListener allDialogsMessagesListener = new AllDialogsMessageListener();
    private SystemMessagesListener systemMessagesListener = new SystemMessagesListener();
    private RecyclerView fileattachment;
    private LinearLayout ll_attachment_preview_container;
    private int countattach = 0;

    private AttachmentPreviewAdapterView attachmentPreviewAdapterView;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("multipleimage", "onReceive: broadcast call->>>> countattach-> ");


//            if (intent != null) {
//                int data = intent.getIntExtra("size", 0);
//                Log.e("multipleimage", "onReceive: broadcast call->>>> size-> " + data);
//                if (data <= 0) {
//                    show_attachment.clear();
//                }
//            }


        }
    };

    private QBChatDialogMessageListener messagelistenerObject = new QBChatDialogMessageListener() {
        @Override
        public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
            qbChatrecent.add(qbChatMessage);
            adapter.notifyDataSetChanged();
            qbChatMessage.setMarkable(true);
            if (qbChatrecent.size() > 1) {
                rv.smoothScrollToPosition(qbChatrecent.size() - 1);
            }

        }

        @Override
        public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

        }
    };


    private ConnectionListener connectionListener = new ConnectionListener() {

        @Override
        public void connected(XMPPConnection xmppConnection) {
            Log.e(TAG, "connected: ");
        }

        @Override
        public void authenticated(XMPPConnection xmppConnection, boolean b) {
            Log.e(TAG, "authenticated: ");
        }

        @Override
        public void connectionClosed() {
            Log.e(TAG, "connectionClosed: ");
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            Log.e(TAG, "connectionClosedOnError: ");

//            signIn(SharedPreferanceManager.getInstance(Chat_Activity.this).getQbUser());
        }

        @Override
        public void reconnectionSuccessful() {
            Log.e(TAG, "reconnectionSuccessful: ");
        }

        @Override
        public void reconnectingIn(int i) {
            Log.e(TAG, "reconnectingIn: ");
        }

        @Override
        public void reconnectionFailed(Exception e) {
            Log.e(TAG, "reconnectionFailed: ");
        }
    };

    private QBRTCClientVideoTracksCallbacks BaseSession;
    private QBRTCSession currentSession;

    public Chat_Activity() {
    }
//private QBMessageListener messageListener = new MessageListener();

    //    private DialogsActivity.SystemMessagesListener systemMessagesListener = new SystemMessagesListener();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatactivitylayout);
        chatSevice = QBChatService.getInstance();
        rtcClient = QBRTCClient.getInstance(getApplicationContext());
        qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(ConstantString.DIALOG_EXTRA);
        if (getIntent().getParcelableExtra(ConstantString.CHAT_RCV_USER) != null) {
            chat_recever = getIntent().getParcelableExtra(ConstantString.CHAT_RCV_USER);
        }
        qbChatService = ChatHelper.getInstance().getclient();

        if (qbChatService.isLoggedIn()) {
            Log.e(TAG, "onCreate: client_chat_activity qbchatService login ");
            ifloginthen();
        } else {
            Log.e(TAG, "onCreate: chat service not login");
        }

        cameraPermission();

    }


    /**
     * init listeners
     */
    private void initListeners() {
        if( QBChatService.getInstance().getMessageStatusesManager() != null){
            qbMessageStatusesManager = QBChatService.getInstance().getMessageStatusesManager();
            qbMessageStatusesManager.addMessageStatusListener(this);
        }


    }

    private void ifloginthen() {
        qbUser = qbChatService.getUser();
        qbChatDialog.initForChat(qbChatService);
        qbChatService.addConnectionListener(connectionListener);
        Log.e(TAG, "initviews: qbchatDialog-> " + qbChatDialog);
        initviews();
        initChatDialog();
//        retrievemessage();
        reterivemssageDemo();
        user_chat_profile_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String role = SharedPreferanceManager.getInstance(Chat_Activity.this).getUserData().getRole();
                if (role.equalsIgnoreCase(ConstantString.CLIENT_LOGIN)) {
                    Intent intent = new Intent(Chat_Activity.this, View_proposal_userProfile.class);
                    intent.putExtra("user_id", chat_recever.getId());
                    startActivity(intent);
                } else if (role.equalsIgnoreCase(ConstantString.MODEL_LOGIN)) {
                    Intent intent = new Intent(Chat_Activity.this, ClientDetailShow_activity.class);
                    intent.putExtra("user_id", chat_recever.getId());
                    startActivity(intent);
                } else {
                    Log.e(TAG, "onClick: nothing not client and not model");
                }

            }
        });
        inComingVideoCalls();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        registerReceiver(
//                networkChangeReceiver,
//                new IntentFilter(
//                        ConnectivityManager.CONNECTIVITY_ACTION));

        registerReceiver(broadcastReceiver, new IntentFilter("countarray"));

        Log.e(TAG, "%%%%%%%%%%%%%onStart: ");
    }

    public void backbutton(View view) {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: " + ChatHelper.getInstance().getclient().isLoggedIn());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        unregisterReceiver(networkChangeReceiver);
        if(qbMessageStatusesManager != null){
            qbMessageStatusesManager.removeMessageStatusListener(this);
        }

        Log.e("loginchat", "onDestroy: current session-> "+currentSession );
        if (currentSession!=null){
            currentSession.removeVideoTrackCallbacksListener(BaseSession);
            currentSession.removeEventsCallback(clientSessionCallbacks);
        }
        QBRTCClient.getInstance(this).destroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: " + ChatHelper.getInstance().getclient().isLoggedIn());
    }

    private void initviews() {

        Log.e("Check", "initviews onSuccess: ");
        profileimage = findViewById(R.id.profileimage);
        title = findViewById(R.id.title);
        rv = findViewById(R.id.rv);
        messageArea = findViewById(R.id.messageArea);
        fileattachment = findViewById(R.id.fileattachment);
        ll_attachment_preview_container = findViewById(R.id.ll_attachment_preview_container);
        user_chat_profile_id = findViewById(R.id.user_chat_profile_id);
        QBChatMessageHolder.getInstance().putMessages(qbChatDialog.getDialogId(), qbChatrecent);
        title.setText(qbChatDialog.getName().toString());
        loding_message_layout_id = findViewById(R.id.loding_message_layout_id);

        messages_refresh = findViewById(R.id.messages_refresh);

        qbChatrecent = new ArrayList<>();
        adapter = new MainChatAdapter(getBaseContext(), qbChatrecent, qbChatDialog);

        adapter.setHasStableIds(true);

//        /**attachment preview Adapter view*/
//        attachmentPreviewAdapterView = findViewById(R.id.adapter_attachment_preview);
//        attachmentPreviewAdapterView.setAdapter((Adapter) adapter);

        adapter.getrecivername(qbChatDialog.getName());


        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new LinearLayoutManager(Chat_Activity.this));
        rv.setAdapter(adapter);
        rv.addItemDecoration(new StickyRecyclerHeadersDecoration(adapter));
//        rv.setHasFixedSize(true);

        attachment_adapter = new Attachment_Adapter(show_attachment, Chat_Activity.this);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(Chat_Activity.this, LinearLayoutManager.HORIZONTAL, false);
        fileattachment.setAdapter(attachment_adapter);
        fileattachment.setLayoutManager(horizontalLayoutManager);
        attachment_adapter.onsendAttachment(this);

        Glide.with(Chat_Activity.this)
                .load(chat_recever.getProfileImg())
                .placeholder(R.drawable.ic_profile)
                .into(profileimage);
//        inComingVideoCalls();


        initswaptoreferesh();
    }

    private void initswaptoreferesh() {

        messages_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG, "onRefresh:skipPagination->  " + skipPagination);
                reterivemssageDemo();
            }
        });
    }

    private void initChatDialog() {

        Log.e("Check", "initchatDialog onSuccess: ");
        Log.e(TAG, "initChatDialog: " + qbChatDialog);
        assert qbChatDialog != null;

        Log.e(TAG, "initChatDialog:qbchatDialog " + qbChatDialog + "  qbchatDialog detail=> " + qbChatDialog.getLastMessage());

        //        Register listener incoming message
        incomingMessagesManager = ChatHelper.getInstance().getclient().getIncomingMessagesManager();
        incomingMessagesManager.addDialogMessageListener(allDialogsMessagesListener);
//        incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        returnToChat();

        /**message Listener*/
        messageListener();


    }


    private void retrievemessage() {

        oldchatloading();
        QBMessageGetBuilder messageGetBuilder = new QBMessageGetBuilder();
        messageGetBuilder.setLimit(MESSAGE_LIMIT);
//        messageGetBuilder.setL
        if (qbChatDialog != null) {
            QBRestChatService.getDialogMessages(qbChatDialog, messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {

                    Log.e("sushilchat-> ", "onSuccess: qbRestChatservice_retrirvemessage-> \n\n\n\n\n\n\n\n\n" + qbChatMessages + "n\n\n\n\n\n\n\n\n");


//                    Log.e(TAG, "onSuccess: chatidlogin-> "+QBChatService.getInstance().getUser() );
                    Log.e("Check", "retrievemessage onSuccess: ");
                    Log.e(TAG, "onSuccess: \n\n\n\n " + qbChatMessages);

                    if (qbChatMessages != null) {
//                        qbChatrecent.addAll(qbChatMessages);

                        qbChatrecent.addAll(qbChatMessages);
                        Log.e(TAG, "onSuccess: %%%%%%%*****\n\n\n\n\n\n\n\n\n\n\n\n\n " + qbChatMessages + " \n\n\n\n\n\n\n\n\n\n\n\n");


                        if (qbChatMessages.size() > 1) {
                            rv.smoothScrollToPosition(qbChatMessages.size() - 1);
                        }

                        adapter.notifyDataSetChanged();

                        oldchatloading_complete();
                    }

                }

                @Override
                public void onError(QBResponseException e) {
                    Log.e(TAG, "onError: " + e.getMessage());
                }
            });
        }
    }


    /**
     * demo chat message fetch (history)
     */

    private void reterivemssageDemo() {

        if (!checkAdapterInit) {
            oldchatloading();
        }


        ChatHelper.getInstance().loadChatHistory(qbChatDialog, skipPagination, new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                Log.e("sushilchat-> ", "onSuccess: qbRestChatservice_retrirvemessage-> \n\n\n\n\n\n\n\n\n" + qbChatMessages + "n\n\n\n\n\n\n\n\n");


//                    Log.e(TAG, "onSuccess: chatidlogin-> "+QBChatService.getInstance().getUser() );
                Log.e("Check", "retrievemessage onSuccess: ");
                Log.e(TAG, "onSuccess: \n\n\n\n " + qbChatMessages);
                Collections.reverse(qbChatMessages);
                if (qbChatMessages != null) {

                    if (checkAdapterInit) {
                        adapter.addMessages(qbChatMessages);
                    } else {
                        checkAdapterInit = true;
                        adapter.setMessages(qbChatMessages);
                    }

                    Log.e(TAG, "onSuccess: %%%%%%%*****\n\n\n\n\n\n\n\n\n\n\n\n\n " + qbChatMessages + " \n\n\n\n\n\n\n\n\n\n\n\n");

                    if (skipPagination == 0) {
                        scrollMessageListDown();
                    }


                    oldchatloading_complete();
                }

                skipPagination += ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }
        });

        if (messages_refresh.isRefreshing()) {
            messages_refresh.setRefreshing(false);
        }
    }

    private void scrollMessageListDown() {
        rv.scrollToPosition(qbChatrecent.size() - 1);
    }

    /**
     * download attachment
     **/
    private void downloadAttachment(ArrayList<QBAttachment> attachment) {

        for (QBAttachment attachment1 : attachment) {
            String fileID = attachment1.getId();
            QBContent.downloadFile(fileID).performAsync(new QBEntityCallback<InputStream>() {
                @Override
                public void onSuccess(InputStream inputStream, Bundle bundle) {
                    // process file
                    QBAttachment attachment2 = new QBAttachment(inputStream.toString());
                    QBChatMessage chatMessage = new QBChatMessage();
                    chatMessage.addAttachment(attachment2);
                    qbChatrecent.add(chatMessage);

                }

                @Override
                public void onError(QBResponseException e) {

                }
            });
        }

    }

    public void sendMsssageButtonClicked(View view) {
        qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(ConstantString.DIALOG_EXTRA);
        somecheck();


//        if (!messageArea.getText().toString().isEmpty() && attachment_adapter.checkattchmentReady()) {
//            Log.e("sushilchat-> ", "sendMsssageButtonClicked: 1.0. " );
//            if (attachment_adapter.size() > 0) {
//                Map<File, QBAttachment> allattach = attachment_adapter.getuploadedattachment();
//                Log.e("sushilchat-> ", "sendMsssageButtonClicked: 1. " );
//                check_forattachment(allattach);
//                sendattachment_chat(allattach);
//            }
//            simplechatflow();
//            return;
//        }

        if (!messageArea.getText().toString().isEmpty()) {
            Log.e("sushilchat-> ", "sendMsssageButtonClicked: 2.0 ");
            simplechatflow();
        }


//        if(attachment_adapter.checkattchmentReady() ){
//            if(attachment_adapter.size() > 0){
//                Log.e("sushilchat-> ", "sendMsssageButtonClicked: 2. " );
//                Map<File,QBAttachment> allattach = attachment_adapter.getuploadedattachment();
//                check_forattachment(allattach);
//                sendattachment_chat(allattach);
//            }
//
//        }


    }


    void check_forattachment(Map<File, QBAttachment> allattach) {

        for (Map.Entry<File, QBAttachment> entry : allattach.entrySet()) {
            Log.e("entry-> ", "check_forattachment: " + entry.getValue());
        }
    }

    public void sendAttachment(View view) {
        Log.e(TAG, "sendAttachment: clicked ");

        if (isReadStoragePermissionGranted() && isWriteStoragePermissionGranted()) {
            imagepicker();
        } else {
            Toast.makeText(this, "Permission cancle", Toast.LENGTH_SHORT).show();
        }
//        SystemPermissionHelper permissionHelper = new SystemPermissionHelper(this);
//        if (permissionHelper.isSaveImagePermissionGranted()) {
//            imagepicker();
//        } else {
//            permissionHelper.requestPermissionsForSaveFileImage();
//        }
//
//        boolean permission = ContextCompat.checkSelfPermission(Chat_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
////        if(permission){

//        }
//        else{
//            ActivityCompat.requestPermissions(Chat_Activity.this,
//                    collectDeniedPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                    PERMISSIONS_FOR_SAVE_FILE_IMAGE_REQUEST);
//        }
    }


    @SuppressLint("IntentReset")
    private void imagepicker() {
        @SuppressLint("IntentReset") Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType(VIDEO_OR_IMAGE_MIME);


        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private String[] collectDeniedPermissions(String... permissions) {
        ArrayList<String> deniedPermissionsList = new ArrayList<>();
        for (String permission : permissions) {
            if (!isPermissionGranted(permission)) {
                deniedPermissionsList.add(permission);
            }
        }

        return deniedPermissionsList.toArray(new String[deniedPermissionsList.size()]);
    }

    private boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
//                Uri selectedImage = data.getData();
                assert data != null;
                ClipData mClipData = data.getClipData();

                if (mClipData != null) {

                    if (mClipData.getItemCount() <= 5) {
                        multipleAttachmentSend(mClipData);
                    } else if (mClipData.getItemCount() > 5) {
                        for (int i = 0; i < 5; i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            sendAttachment_onChat(item);
//                getAtachementLink(filePhoto);
                            Log.e(TAG, "onActivityResult: imageuri-> " + item);
                        }

                        Toast.makeText(this, "Can select only five image in one time.", Toast.LENGTH_SHORT).show();
                    }


                } else if (data.getData() != null) {


                    Uri selectedImage = data.getData();
                    Log.e("multipleimage", "onActivityResult:Image uri----->" + selectedImage);
                    // display your image
//                    String selectedFilePath = FilePath.getPath(this, selectedImage);
                    String selectedFilePath = UriUtils.getPathFromUri(this, selectedImage);
                    assert selectedImage != null;
                    assert selectedFilePath != null;
//                    attachmentlayoutShow(new File(selectedFilePath));
                    Log.e(TAG, "onActivityResult:selectedfilepath----->" + selectedFilePath);
                    countattach = countattach + 1;
//                    sendAttachment_onChat(new File(selectedFilePath));
                    attachmentlayoutShow(new File(selectedFilePath));
                }

            }
            Log.e("broadcastdata", "onActivityResult: count-> " + countattach);
        }
    }


    /**
     * sendmultiple image
     *
     * @param mClipData
     */
    private void multipleAttachmentSend(ClipData mClipData) {


        for (int i = 0; i < mClipData.getItemCount(); i++) {

            ClipData.Item item = mClipData.getItemAt(i);
            sendAttachment_onChat(item);
        }
    }

    /**
     * demo attachment send with in chat (upload and auto send)
     *
     * @param item
     */
    private void sendAttachment_onChat(ClipData.Item item) {


        Log.e("multipleimage", "multipleAttachmentSend: " + " item-> " + item);
        Uri selectedImage = item.getUri();
//                        String selectedFilePath = FilePath.getPath(this, selectedImage);
        String selectedFilePath = UriUtils.getPathFromUri(this, selectedImage);
        assert selectedImage != null;
        assert selectedFilePath != null;
        countattach = countattach + 1;
        attachmentlayoutShow(new File(selectedFilePath));
        Log.e(TAG, "onActivityResult: imageuri-> " + selectedImage);


//        getAtachementLink(file);

    }


    private void getAtachementLink(File imagefile) {

        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {

                Log.e(TAG, "getAtachementLink: file--> " + imagefile);
                String[] tags = new String[]{"fun", "notfun"};
                QBContent.uploadFileTask(imagefile, false, null, new QBProgressCallback() {
                    @Override
                    public void onProgressUpdate(int i) {
                        Log.e(TAG, "onProgressUpdate: -> " + i);

                        if (i != 100) {
//                            textView.setText(i + "%");


                        } else {

                        }
                    }
                }).performAsync(new QBEntityCallback<QBFile>() {
                    @Override
                    public void onSuccess(QBFile qbFile, Bundle bundle) {


//                        QBChatMessage chatMessage = new QBChatMessage();
//                        chatMessage.setSaveToHistory(true);
                        String type = "photo";
                        if (qbFile.getContentType().contains(QBAttachment.IMAGE_TYPE)) {
                            type = QBAttachment.IMAGE_TYPE;
                        } else if (qbFile.getContentType().contains(QBAttachment.VIDEO_TYPE)) {
                            type = QBAttachment.VIDEO_TYPE;
                        }

                        // attach a photo
                        QBAttachment attachment = new QBAttachment(type);
                        attachment.setId(qbFile.getUid().toString());
                        attachment.setSize(qbFile.getSize());
                        attachment.setName(qbFile.getName());
                        attachment.setUrl(qbFile.getPrivateUrl());
//                        attachment.setContentType(qbFile.getContentType());
                        Log.e("entry-> Adapter-> ", "onSuccess: " + attachment + " type " + type + " seturl-> "
                                + qbFile.getPrivateUrl() + " ");

//
//                        textView.setVisibility(View.GONE);
//                        grayoverlay.setVisibility(View.GONE);
//                        cancle.setVisibility(View.VISIBLE);
////                        Log.e(TAG, "onSuccess: ", );

//                        SendAttachmentOnly(attachment);
//                        savefileinStorage(attachment);
//                        chatMessage.addAttachment(attachment);

                        Log.e(TAG, "onSuccess: QbFile uid-> " + qbFile.getUid() + " QBFile id-> " + qbFile.getId() + " QBFile-> " + qbFile);

                        try {
                            copy(imagefile,
                                    new File(Chat_Activity.this.getFilesDir(), "Videos/"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(QBResponseException e) {

                        Log.e(TAG, "onError: onAttachment--> " + e.toString());


                    }
                });
            }
        });
    }


    public void copy(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }


    private void savefileinStorage(File file) {
        try {
            InputStream in = new FileInputStream(file);
            FileOutputStream output = new FileOutputStream(file);

            if (file != null) {

                output.close();
                Log.e("outputStream", "doInBackground: output-> " + output);
            } else {
                Log.e("outputStream", "doInBackground: is null ");
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void sendattachment(QBAttachment qbAttachment, int size) {
        Log.e("multipleimage", "sendattachment: " + size + " attacmentdatasize-> " + attachment_adapter.size()+
                " data----> "+qbAttachment.getUrl()+" url path--> "+qbAttachment.getName());
        SendAttachmentOnly(qbAttachment, size);
//        attachment_adapter.removePosition(size);


    }

    private void SendAttachmentOnly(QBAttachment qbAttachment, int size) {
        QBChatMessage chatMessage = new QBChatMessage();
        chatMessage.setDateSent(System.currentTimeMillis() / 1000);
        chatMessage.setSenderId(qbUser.getId());
        Log.e("check", "sendMsssageButtonClicked: " + QBChatService.getInstance());
        chatMessage.setSaveToHistory(true);
        chatMessage.setMarkable(true);
        chatMessage.addAttachment(qbAttachment);
        try {
            qbChatDialog.sendMessage(chatMessage);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }


        adapter.addMessage(chatMessage);
//        qbChatrecent.add(chatMessage);
//        adapter.notifyDataSetChanged();
        Log.e(TAG, "SendAttachmentOnly: filename -> " + qbAttachment.getUrl());
//    attachment_adapter.remove(new File(qbAttachment.getUrl()));
//    attachment_adapter.notifyDataSetChanged();

//        if (qbChatrecent.size() > 1) {
//        Log.e(TAG, "SendAttachmentOnly: adapter size-> "+adapter.getsizemessage() );
//            rv.smoothScrollToPosition(adapter.getsizemessage() - 1);
////        }


//        attachment_adapter.removePosition(size);
    }


    /**
     * demo attachemnt end ^^^
     */

    private static class AllDialogsMessageListener implements QBChatDialogMessageListener {
        @Override
        public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
            Log.e("Check", "AllDialogsMessageListener onSuccess: ");


        }

        @Override
        public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

        }
    }


    /**
     * not using now only for back up
     **/
    private void getAtachementLink2(File imagefile) {

        Log.e(TAG, "getAtachementLink: file--> " + imagefile);
        String[] tags = new String[]{"fun", "notfun"};
        QBContent.uploadFileTask(imagefile, false, null, new QBProgressCallback() {
            @Override
            public void onProgressUpdate(int i) {
                Log.e(TAG, "onProgressUpdate: -> " + i);
            }
        }).performAsync(new QBEntityCallback<QBFile>() {
            @Override
            public void onSuccess(QBFile qbFile, Bundle bundle) {

                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setSaveToHistory(true);
                String type = "file";
                if (qbFile.getContentType().contains(QBAttachment.IMAGE_TYPE)) {
                    type = QBAttachment.IMAGE_TYPE;
                } else if (qbFile.getContentType().contains(QBAttachment.VIDEO_TYPE)) {
                    type = QBAttachment.VIDEO_TYPE;
                }
                // attach a photo
                QBAttachment attachment = new QBAttachment(type);
                attachment.setId(qbFile.getId().toString());
                chatMessage.addAttachment(attachment);


//                attachment holding
//                QBChatMessageHolder.getInstance().putMessage(qbChatDialog.getDialogId(), chatMessage);
//                ArrayList<QBChatMessage> messages = QBChatMessageHolder.getInstance().getChatMessageByDialogId(qbChatDialog.getDialogId());
////                adapter = new ChatMessageAdapter_2(getBaseContext(), messages);
////                rv.setAdapter(adapter);
                Log.e(TAG, "onSuccess: QbFile uid-> " + qbFile.getUid() + " QBFile id-> " + qbFile.getId() + " QBFile-> " + qbFile);
                dialog.dismiss();

            }


            @Override
            public void onError(QBResponseException e) {
                dialog.dismiss();
                Log.e(TAG, "onError: onAttachment--> " + e.toString());
            }
        });
    }

    private void messageListener() {
        qbChatDialog.addMessageListener(messagelistenerObject);
    }


//    private class PushBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String message = intent.getStringExtra(ConstantString.EXTRA_FCM_MESSAGE);
//            Log.v(TAG, "Received broadcast " + intent.getAction() + " with data: " + message);
//            loadDialogsFromQb(false, false);
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
//        qbChatDialog.removeMessageListrener(messagelistenerObject);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListeners();
        if (ChatHelper.getInstance().isLogged()) {
            if (qbChatDialog == null) {
                qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(ConstantString.DIALOG_EXTRA);
                chatSevice = QBChatService.getInstance();
                rtcClient = QBRTCClient.getInstance(getApplicationContext());
            }
            returnToChat();
        } else {
//            showProgressDialog(R.string.dlg_loading);
            ChatHelper.getInstance().loginToChat(SharedPreferanceManager.getInstance(Chat_Activity.this).getQbUser(), new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    Log.e(TAG, "onSuccess: chathelper");
                    returnToChat();

                }

                @Override
                public void onError(QBResponseException e) {

                    Log.e(TAG, "onError: " + e.toString());
                }
            });
        }
        Log.e(TAG, "onResume: " + ChatHelper.getInstance().getclient().isLoggedIn());

    }


    private void returnToChat() {
        qbChatDialog.initForChat(QBChatService.getInstance());
        if (!qbChatDialog.isJoined()) {
            try {
                qbChatDialog.join(new DiscussionHistory());
                chatSevice = QBChatService.getInstance();
            } catch (Exception e) {
                finish();
            }
        }

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (qbChatDialog == null) {
            try {
                qbChatDialog = QbDialogHolder.getInstance().getChatDialogById(savedInstanceState.getString(ConstantString.DIALOG_EXTRA));
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    /**
     * Attachement test demo
     */
    private void attachmentlayoutShow(File imageuri) {
        attachment_adapter.add(imageuri);
        Log.e("multipleimage", "attachmentlayoutShow: filename-> " + imageuri + " size ->" + attachment_adapter.size());


    }


    public void attachmentlayouthide() {
        show_attachment.clear();

        attachment_adapter.notifyDataSetChanged();
        ll_attachment_preview_container.setVisibility(View.GONE);
    }

    /**
     * send only chat
     **/
    private void simplechatflow() {


        if (ChatHelper.getInstance().isLogged()) {
            QBChatMessage chatMessage = new QBChatMessage();
            chatMessage.setBody(messageArea.getText().toString().trim());
            chatMessage.setDateSent(System.currentTimeMillis() / 1000);
            chatMessage.setSenderId(qbUser.getId());
            Log.e("check", "sendMsssageButtonClicked: " + QBChatService.getInstance());
            chatMessage.setSaveToHistory(true);
            chatMessage.setMarkable(true);

            Log.e("privatechat", "simplechatflow: is login" + QBChatService.getInstance().isLoggedIn());
            Log.e("privatechat", "simplechatflow: simple chat-> " + messageArea.getText().toString());

            try {
                Log.e("privatechat", "sendMsssageButtonClicked: " + messageArea.getText().toString());
                Log.e("privatechat", "sendMsssageButtonClicked:qbchatDialog->  " + qbChatDialog + "   message---> " + chatMessage);

                if (qbChatDialog != null) {
                    Log.e(TAG, "sendMsssageButtonClicked:qbchatDialog->  " + qbChatDialog);
                    Log.e("sushilchat-> ", "simplechatflow_data \n\n\n\n" + chatMessage + "\n\n\n\n\n");
                    qbChatDialog.sendMessage(chatMessage);
                }

//            QBChatMessageHolder.getInstance().putMessages(qbChatDialog.getDialogId(), qbChatrecent);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
                Log.e(TAG, "$$$$$$sendMsssageButtonClicked: " + e.toString());
            }

            qbChatrecent.add(chatMessage);
            Log.e("check", "sendMsssageButtonClicked: " + qbChatrecent);
            adapter.notifyDataSetChanged();
            if (qbChatrecent.size() > 1) {
                rv.smoothScrollToPosition(qbChatrecent.size() - 1);
            }
//              Remove text from edit text
            messageArea.setText("");
            messageArea.setFocusable(true);
        } else {
            Log.d(TAG, "Relogin to Chat only");
//            dialog = new ProgressDialog(Chat_Activity.this);
//            dialog.setMessage("Please Wait...");
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();
            Loading_dialog loading_dialog = new Loading_dialog(Chat_Activity.this);
            loading_dialog.showDialog(0);
            ChatHelper.getInstance().loginToChat(qbUser, new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {


                    Log.d(TAG, "Relogin Successfull");
                    Log.e("sushilchat-> ", "Simplechatflow-> notlogin so login");
                    simplechatflow();
                    loading_dialog.hideDialog();

                }

                @Override
                public void onError(QBResponseException e) {
                    Log.d(TAG, "Relogin Error: " + e.getMessage());

                }
            });
        }
    }

    /**
     * send attachment
     */
    private void sendattachment_chat(Map<File, QBAttachment> allattachment) {

        Log.e("privatechat", "sendattachment: is login " + QBChatService.getInstance().isLoggedIn());
        Log.e("attachmentCount", "sendattachment_chat: " + allattachment.size());
        if (ChatHelper.getInstance().isLogged()) {
            Log.e(TAG, "sendattachment_chat: attachment **--->" + allattachment.entrySet());
            for (Map.Entry<File, QBAttachment> entry : allattachment.entrySet()) {
                Log.e("nofound-->", "sendattachment_chat: " + entry);


                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setDateSent(System.currentTimeMillis() / 1000);
                chatMessage.setSenderId(qbUser.getId());
                Log.e("check", "sendMsssageButtonClicked: " + QBChatService.getInstance());
                chatMessage.setSaveToHistory(true);
                chatMessage.setMarkable(true);
                chatMessage.addAttachment(entry.getValue());

                Log.e("value_data", "sendattachment_chat: " + entry.getValue());


                Log.e("privatechat", "sendattachment_chat:\n\n\n\n\n\n\n\n " + chatMessage + " \n\n\n\n\n\n\n\n");

                try {
                    qbChatDialog.sendMessage(chatMessage);
                    Log.e("check ", "sendattachment_chat: " + qbChatDialog);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }

                qbChatrecent.add(chatMessage);

                adapter.notifyDataSetChanged();
                if (qbChatrecent.size() > 1) {
                    rv.smoothScrollToPosition(qbChatrecent.size() - 1);
                }

//            show_attachment.remove(entry.getKey());

//                    attachment_adapter.remove(entry.getKey());


//                attachment_adapter.notifyDataSetChanged();
            }
            attachment_adapter.cleaArraylist();
            show_attachment.clear();
            attachmentlayouthide();
        } else {
//            dialog = new ProgressDialog(Chat_Activity.this);
//        dialog.setMessage("Please Wait...");
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
            Log.d(TAG, "Relogin to Chat attachement");
            ChatHelper.getInstance().loginToChat(qbUser, new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    Log.d(TAG, "Relogin Successfull");

                    sendattachment_chat(allattachment);
//                    dialog.dismiss();
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.e(TAG, "Relogin Error: " + e.getMessage());

                }
            });
        }


//        attachmentlayouthide();

//        dialog = new ProgressDialog(Chat_Activity.this);
//        dialog.setMessage("Please Wait...");
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSIONS_FOR_SAVE_FILE_IMAGE_REQUEST && grantResults.length > 0 && grantResults[0] != -1) {
//            imagepicker();
//        }
//    }


//    private void sendAttachemntonly(){
//        if(ChatHelper.getInstance().isLogged()){
//
//            QBChatMessage chatMessage = new QBChatMessage();
//            chatMessage.setBody(messageArea.getText().toString());
//            chatMessage.setDateSent(System.currentTimeMillis() / 1000);
//            chatMessage.setSenderId(qbUser.getId());
//
//
//
//        }else{
//            Log.e(TAG, "sendAttachemntonly: reloading" );
//            ChatHelper.getInstance().loginToChat(qbUser, new QBEntityCallback<Void>() {
//                @Override
//                public void onSuccess(Void aVoid, Bundle bundle) {
//                    Log.d(TAG, "Relogin Successfull");
//                }
//
//                @Override
//                public void onError(QBResponseException e) {
//
//                }
//            });
//        }
//    }


    /**
     * video calling
     */
    private void inComingVideoCalls() {
        // Add signalling manager
        chatSevice.getVideoChatWebRTCSignalingManager().addSignalingManagerListener(new QBVideoChatSignalingManagerListener() {
            @Override
            public void signalingCreated(QBSignaling qbSignaling, boolean createdLocally) {
                if (!createdLocally) {
                    Log.e(TAG, "signalingCreated: + signaling is created ");
                    rtcClient.addSignaling(qbSignaling);
                }
            }
        });
        // handing the video call on receive
        BaseSession = new QBRTCClientVideoTracksCallbacks() {
            @Override
            public void onLocalVideoTrackReceive(com.quickblox.videochat.webrtc.BaseSession baseSession, QBRTCVideoTrack qbrtcVideoTrack) {
//                    fillVideoView(localView,qbrtcVideoTrack);
            }

            @Override
            public void onRemoteVideoTrackReceive(com.quickblox.videochat.webrtc.BaseSession baseSession, QBRTCVideoTrack qbrtcVideoTrack, Integer userID) {
//                    fillVideoView(surfaceView,qbrtcVideoTrack);
            }
        };

        // Listener for handling the incoming call
        QBRTCClient.getInstance(this).addSessionCallbacksListener(clientSessionCallbacks);
        rtcClient.prepareToProcessCalls();
    }

    QBRTCClientSessionCallbacks clientSessionCallbacks = new QBRTCClientSessionCallbacks() {
        @Override
        public void onReceiveNewSession(QBRTCSession qbrtcSession) {

            currentSession = qbrtcSession;
            Log.e("loginchat", "onDestroy: current session-> "+currentSession+" Qbrtc-> param-> "+qbrtcSession );

//                Map<String, String> userInfo = new HashMap<>();
//                userInfo = qbrtcSession.getUserInfo();
//                data = (HashMap<String, String>) qbrtcSession.getUserInfo();
//                qbrtcSession1 = qbrtcSession;
////                Intent intent = new Intent(Chat_Activity.this, CallActivityCallActivity.class);
//                intent = new Intent(Chat_Activity.this, VideoCallService.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("service", true);
//                intent.putExtra("id", "" + qbrtcSession.getCallerID());
//                intent.putExtra("session", String.valueOf(qbrtcSession));
//                intent.putExtra("username", qbChatDialog.getName());
////                Log.e("chatvclistener", "onReceiveNewSession: userinfo---> " + qbrtcSession.getUserInfo());
//////                intent.putExtra("username"+qbrtcSession.getUserInfo());
////                Log.e(TAG, "onReceiveNewSession: username--> " + qbrtcSession.getUserInfo() + " data-> " + data);
//////                intent.putExtra("Username",qbrtcSession.getUserInfo().get("name"));
////                Log.e(TAG, "onReceiveNewSession: " + " " + qbrtcSession.getCallerID());
//                startService(intent);
////                qbrtcSession.acceptCall(qbrtcSession.getUserInfo());
////                qbrtcSession.acceptCall(userInfo);
////                qbrtcSession.rejectCall(userInfo);
////                Toast.makeText(Chat_Activity.this, "onReceiveNewSession .", Toast.LENGTH_SHORT).show();
            Map<String, String> userInfo = new HashMap<>();
            userInfo = qbrtcSession.getUserInfo();
            data = (HashMap<String, String>) qbrtcSession.getUserInfo();
            qbrtcSession1 = qbrtcSession;
//                Intent intent = new Intent(Chat_Activity.this, CallActivityCallActivity.class);
//                intent = new Intent(Chat_Activity.this, VideoCallService.class);
            Intent intent = null;
            if (qbrtcSession.getConferenceType().getValue() == QB_CONFERENCE_TYPE_VIDEO.getValue()) {
                intent = new Intent(Chat_Activity.this, CallActivity.class);
//
//                  intent.putExtra("type","video");
            } else if (qbrtcSession.getConferenceType().getValue() == QB_CONFERENCE_TYPE_AUDIO.getValue()) {
//                    intent.putExtra("type","audio");
                intent = new Intent(Chat_Activity.this, VoiceCallActivity.class);
            }
//                Intent intent = null;
//                Log.e("TAG", "onReceiveNewSession: call type "+serviceIntent.getStringExtra("type") );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("service", true);
            intent.putExtra("id", "" + qbrtcSession.getCallerID());
            intent.putExtra("session", String.valueOf(qbrtcSession));
            intent.putExtra("username", qbChatDialog.getName());
//                Log.e("chatvclistener", "onReceiveNewSession: userinfo---> " + qbrtcSession.getUserInfo());
////                intent.putExtra("username"+qbrtcSession.getUserInfo());
//                Log.e(TAG, "onReceiveNewSession: username--> " + qbrtcSession.getUserInfo() + " data-> " + data);
////                intent.putExtra("Username",qbrtcSession.getUserInfo().get("name"));
//                Log.e(TAG, "onReceiveNewSession: " + " " + qbrtcSession.getCallerID());
//
//               startService(intent);
//                qbrtcSession.acceptCall(qbrtcSession.getUserInfo());
//                qbrtcSession.acceptCall(userInfo);
//                qbrtcSession.rejectCall(userInfo);
//                Toast.makeText(Chat_Activity.this, "onReceiveNewSession .", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }

        @Override
        public void onUserNoActions(QBRTCSession qbrtcSession, Integer integer) {
//                Toast.makeText(Chat_Activity.this, "onUserNoActions.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSessionStartClose(QBRTCSession qbrtcSession) {
//                Toast.makeText(Chat_Activity.this, " onSessionStartClose.", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onUserNotAnswer(QBRTCSession qbrtcSession, Integer integer) {
//                Toast.makeText(Chat_Activity.this, "onUserNotAnswer.", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCallRejectByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {
//                Toast.makeText(Chat_Activity.this, "onCallRejectByUser.", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCallAcceptByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {
            qbrtcSession.addVideoTrackCallbacksListener(BaseSession);
            qbrtcSession.removeVideoTrackCallbacksListener(BaseSession);
//                Toast.makeText(Chat_Activity.this, "onCallAcceptByUser.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onReceiveHangUpFromUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {
//                Toast.makeText(Chat_Activity.this, "onReceiveHangUpFromUser.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSessionClosed(QBRTCSession qbrtcSession) {
//                Toast.makeText(Chat_Activity.this, "onSessionClosed.", Toast.LENGTH_SHORT).show();
//                if ()
//                stopService(intent);
        }
    };

    public void audiocall(View view) {
        Log.e(TAG, "audiocall: permission-> " + mikePermission());
        if (mikePermission()) {
            Intent intent = new Intent(this, VoiceCallActivity.class);
            intent.putExtra("user", qbChatDialog.getRecipientId());
            intent.putExtra("username", qbChatDialog.getName());
            intent.putExtra("isoutgoing", true);
            intent.putExtra("type", "audio");
            startActivity(intent);
        }
    }

    public void videocall(View view) {
//        Log.e("permission", "videocall:mikepermission->  " + mikePermission() + " camera permission-> " + cameraPermission());
//        Log.e(TAG, "videocall: caller name -> " + qbChatDialog.getName());

        if (cameraPermission()) {
            Intent intent = new Intent(this, CallActivity.class);
            intent.putExtra("user", qbChatDialog.getRecipientId());
            intent.putExtra("username", qbChatDialog.getName());
            intent.putExtra("isoutgoing", true);
            intent.putExtra("type", "video");
            startActivity(intent);
        }

//        videoCall();
//        // Create collection of opponents ID
//        List<Integer> opponents = new ArrayList<>();
////        for (QBUser user : ) {
//        opponents.add(qbChatDialog.getUserId());
//        Log.e(TAG, "videocall: "+qbChatDialog.getUserId() );
////        opponents.add(chatSevice.getUser().getId());
////        }
//
//
//// There are two call types: Audio or Video Call
////        QBRTCTypes.QBConferenceType qbConferenceType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
//// or
////        QBRTCTypes.QBConferenceType qbConferenceType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;
//        QBRTCTypes.QBConferenceType qbConferenceType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;
//
//    // Init session
//        QBRTCSession session = QBRTCClient.getInstance(this).createNewSessionWithOpponents(opponents, qbConferenceType);
//
//
//        // You can set any string key and value in user info
//       // Then retrieve this data from sessions which is returned in callbacks
//        // and parse them as you wish
//
//        Map<String, String> userInfo = new HashMap<>();
//        userInfo.put(qbChatDialog.getUserId().toString(), qbChatDialog.getName());
//
//// Start call
//        session.startCall(userInfo);
//        SetCallButtonsDialing(session,userInfo);
//        StartDialRinging();
    }

    private boolean cameraPermission() {
        boolean isAllowed = false;
        if (ContextCompat.checkSelfPermission(Chat_Activity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (mikePermission()) {
                isAllowed = true;
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        }
        return isAllowed;
    }

    private boolean mikePermission() {
        boolean isAllowed = false;
        if (ContextCompat.checkSelfPermission(Chat_Activity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(Chat_Activity.this, "You already granted the permission", Toast.LENGTH_SHORT).show();
            isAllowed = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        }
        return isAllowed;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_CAMERA || requestCode == MY_PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(Chat_Activity.this, "permission granted", Toast.LENGTH_SHORT).show();
                if (requestCode == MY_PERMISSION_REQUEST_CAMERA) {
                    mikePermission();
                }
            } else {
                Toast.makeText(Chat_Activity.this, "permission not granted", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == MY_PERMISSION_REQUEST_CAMERA || requestCode == MY_PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(Chat_Activity.this, "permission granted", Toast.LENGTH_SHORT).show();
                if (requestCode == MY_PERMISSION_REQUEST_CAMERA) {
                    mikePermission();
                }
                if (requestCode == MY_PERMISSIONS_REQUEST_RECORD_AUDIO) {
                    cameraPermission();
                }
            } else {
                Toast.makeText(Chat_Activity.this, "permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void signIn(final QBUser user) {
        Log.e(TAG, "signIn: " + user);
        dialog = new ProgressDialog(Chat_Activity.this);
        dialog.setMessage("Please Wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
//        showProgressDialog(R.string.dlg_login);
        ChatHelper.getInstance().login(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser userFromRest, Bundle bundle) {
                Log.e(TAG, "onSuccess: signin " + user);
                Log.e(TAG, "onSuccess: email-> " + user.getLogin() + " emailcallback-> " + userFromRest.getLogin());
                if (userFromRest.getLogin().equalsIgnoreCase(user.getLogin())) {
//                    loginToChat(user);
                    Log.e(TAG, "onSuccess: signin " + user);
                    loginToChat(user);
//                    ChatHelper.getInstance().getclient().setReconnectionAllowed(true);
                    dialog.dismiss();

                } else {
                    //Need to set password NULL, because server will update user only with NULL password
                    user.setPassword(null);
                    updateUser(user);
                }
            }

            @Override
            public void onError(QBResponseException e) {
                dialog.dismiss();
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
                dialog.dismiss();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: loginchat-> " + e.toString());
//                hideProgressDialog();
//                showErrorSnackbar(R.string.login_chat_login_error, e, null);
            }
        });
    }


    private void oldchatloading() {
        loding_message_layout_id.setVisibility(View.VISIBLE);
        messageArea.setEnabled(false);
    }

    private void oldchatloading_complete() {
        loding_message_layout_id.setVisibility(View.GONE);
        messageArea.setEnabled(true);
    }

    private void somecheck() {
        if (!QBDialogType.PRIVATE.equals(qbChatDialog.getType()) && !qbChatDialog.isJoined()) {
            try {
                qbChatDialog.join(new DiscussionHistory());
            } catch (XMPPException | SmackException e) {
                Log.d(TAG, e.getMessage());
            }
            Log.e(TAG, "somecheck: some chat to load");
            return;
        }
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted1");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted1");
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted2");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted2");
            return true;
        }
    }


    /**
     * QBMessagesStatusListener functions
     */
    @Override
    public void processMessageDelivered(String messageID, String dialogID, Integer userID) {
        if (qbChatDialog.getDialogId().equals(dialogID) && userID != null) {
            adapter.updateStatusDelivered(messageID, userID);
        }
    }

    @Override
    public void processMessageRead(String messageID, String dialogID, Integer userID) {

        if (qbChatDialog.getDialogId().equals(dialogID) && userID != null) {
            adapter.updateStatusRead(messageID, userID);
        }
    }


    /**
     * message Listener
     */

    private class SystemMessagesListener implements QBSystemMessageListener {
        @Override
        public void processMessage(final QBChatMessage qbChatMessage) {
            Log.d(TAG, "System Message Received: " + qbChatMessage.getId());
//            dialogsManager.onSystemMessageReceived(qbChatMessage);
        }

        @Override
        public void processError(QBChatException e, QBChatMessage qbChatMessage) {
            Log.d(TAG, "System Messages Error: " + e.getMessage() + "With MessageID: " + qbChatMessage.getId());
        }
    }
}

