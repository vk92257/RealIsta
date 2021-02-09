package com.example.myapplication.modelsprofile.models_fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.example.myapplication.callbacks.NotifiyLongPress;
import com.example.myapplication.chatModule.ChatHelper;
import com.example.myapplication.chatModule.Chat_Adapter.DialogAdapter;
import com.example.myapplication.chatModule.callback.QbChatDialogMessageListenerImp;
import com.example.myapplication.chatModule.chatutils.FcmConsts;
import com.example.myapplication.chatModule.manager.DialogsManager;
import com.example.myapplication.pojo.ChatDialogHistory_users;
import com.example.myapplication.pojo.Chat_parameter;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.pojo.QbDialogs_userholderArray;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.core.server.Performer;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.todkars.shimmer.ShimmerRecyclerView;

import org.jivesoftware.smack.ConnectionListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Model_Chats extends Fragment implements DialogsManager.ManagingDialogsCallbacks,NotifiyLongPress {

    private LinearLayout titlebar;
    private TextView title;
    private LinearLayout nosavedjobslayout;
    private ShimmerRecyclerView rv;
    private LinearLayout nochatsfoundlayout, recyclercontain;
    private RelativeLayout loding_layout;
    public static final int DIALOGS_PER_PAGE = 50;
    private static String TAG = "Model_Chats_fragment";
    private ArrayList<Chat_parameter> chat_parameters_array;
    private ArrayList<String> ids = new ArrayList<>();
    private CountDownLatch latch;

    private ConnectionListener chatConnectionListener;
    private QBSystemMessagesManager systemMessagesManager;
    private QBIncomingMessagesManager incomingMessagesManager;
    private QBChatDialogMessageListener allDialogsMessagesListener = new AllDialogsMessageListener();

    private BroadcastReceiver pushBroadcastReceiver;

    private SystemMessagesListener systemMessagesListener = new SystemMessagesListener();

    private DialogsManager dialogsManager = new DialogsManager();

    private    DialogAdapter dialogAdapter;
    private ArrayList< ChatDialogHistory_users > chatDialogHArray;

    private ArrayList<QBChatDialog> qbDialogHistory;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.model_chat_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);

        chatDialogHArray = new ArrayList<>();
        qbDialogHistory = new ArrayList<>();
        Log.e(TAG, "onViewCreated: hascontaindialog-> "+SharedPreferanceManager.getInstance(getContext()).hascontaindialog() );
//        getuserfromdialog(SharedPreferanceManager.getInstance(getContext()).getchatDialogs());
//        loadChatDialogs(true);
        if(SharedPreferanceManager.getInstance(getContext()).hascontainDialogAndChatparam()){
            Log.e(TAG, "onViewCreated: not doing background" );
//            loadChatDialogs(false);
            QbDialogs_userholderArray qbDialogs_userholderArray = SharedPreferanceManager.getInstance(getContext()).getChatDialogHistory();
            dialogAdapter = new DialogAdapter(getActivity(), qbDialogs_userholderArray.getQbChatDialogs());
            dialogAdapter.setOnlongClick(this);
            dialogAdapter.setChatuserParam(qbDialogs_userholderArray.getChat_parameters());
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
            rv.setAdapter(dialogAdapter);
            loadingmsg_complete();

            loadChatDialogs(true);
        }
        else{
//            Log.e(TAG, "onViewCreated: doing in background" );
            loadChatDialogs(false);
//            getuserfromdialog(SharedPreferanceManager.getInstance(getContext()).getchatDialogs());
        }

    }

    private void initview(View view) {
        titlebar = view.findViewById(R.id.titlebar);
        title = view.findViewById(R.id.title);
        nosavedjobslayout = view.findViewById(R.id.nosavedjobslayout);
        rv = view.findViewById(R.id.rv);
        nochatsfoundlayout = view.findViewById(R.id.nochatsfoundlayout);

        loding_layout = view.findViewById(R.id.loding_layout);
        recyclercontain = view.findViewById(R.id.recyclercontain);
        chat_parameters_array = new ArrayList<>();

//        loading message
        loadingmessage();


        dialogAdapter = new DialogAdapter(getContext(),new ArrayList<>());
        dialogAdapter.setOnlongClick((NotifiyLongPress) this);
    }

    @Override
    public void onStart() {
        super.onStart();
//        rv.showShimmer();

    }

    @Override
    public void onResume() {
        super.onResume();
        registerQbChatListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(pushBroadcastReceiver);

    }

    /**notification chat listeners*/
    private void registerQbChatListeners() {
//    ChatHelper.getInstance().addConnectionListener();

        try {
            systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
            incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        }catch(Exception e){
            Log.e(TAG, "registerQbChatListeners: error-> "+e.toString() );
            return;
        }

        if(systemMessagesManager != null){
            systemMessagesManager.addSystemMessageListener(systemMessagesListener);
        }

        if(incomingMessagesManager != null){
            incomingMessagesManager.addDialogMessageListener(allDialogsMessagesListener);
        }


        dialogsManager.addManagingDialogsCallbackListener(this);

        pushBroadcastReceiver = new PushBroadcastReceiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(pushBroadcastReceiver,
                new IntentFilter(FcmConsts.ACTION_NEW_FCM_EVENT));
    }

    /**
     * Extra
     */
//
//    private void loadChatDialogs() {
//
//        Log.e(TAG, "loadChatDialogs: ");
//        QBRequestGetBuilder getBuilder = new QBRequestGetBuilder();
//        getBuilder.setLimit(DIALOGS_PER_PAGE);
//        ArrayList<QBChatDialog> dialogs = new ArrayList<>(QbDialogHolder.getInstance().getDialogs().values());
//
//
////        QBRestChatService.createChatDialog(dialogs)
////        ChatHelper.getInstance().getDialogs(getBuilder, new QBEntityCallback<ArrayList<QBChatDialog>>() {
////            @Override
////            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
////                if (dialogs.size() < DIALOGS_PER_PAGE) {
////                    hasMoreDialogs = false;
////                }
////                if (clearDialogHolder) {
////                    QbDialogHolder.getInstance().clear();
////                    hasMoreDialogs = true;
////                }
////                QbDialogHolder.getInstance().addDialogs(dialogs);
////                updateDialogsAdapter();
////
////                DialogJoinerAsyncTask joinerTask = new DialogJoinerAsyncTask(DialogsActivity.this, dialogs);
////                joinerTasksSet.add(joinerTask);
////                joinerTask.execute();
////
////                disableProgress();
////                if (hasMoreDialogs) {
////                    loadDialogsFromQb(true, false);
////                }
////            }
////
////            @Override
////            public void onError(QBResponseException e) {
////
////            }
////        });
//
//        QBRestChatService.getChatDialogs(null,getBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
//            @Override
//            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
//                Log.e(TAG, "onSuccess: qbchatDialogs"+qbChatDialogs + " qbchatDialogs size-> "+qbChatDialogs.size() );
//                rv.hideShimmer();
//                if(qbChatDialogs.size() >0){
//                    hidenoPerson_chatlayout();
//                    DialogAdapter dialogAdapter = new DialogAdapter(getActivity(),qbChatDialogs);
//                    rv.setLayoutManager(new LinearLayoutManager(getContext()));
//                    rv.setAdapter(dialogAdapter);
//                    dialogAdapter.notifyDataSetChanged();
//                }
//                else{
//                    shownoPerson_chatlayout();
//                }
//
//
//            }
//
//            @Override
//            public void onError(QBResponseException e) {
//
//                Log.e(TAG, "onError: "+e.getMessage());
//            }
//        });
//    }

    /**
     * Get All chat dialog (1-1 dialogs)
     */
    private void loadChatDialogs(boolean inbackground) {

        Log.e(TAG, "loadChatDialogs: ");
        QBRequestGetBuilder getBuilder = new QBRequestGetBuilder();
        getBuilder.setLimit(DIALOGS_PER_PAGE);

        QBRestChatService.getChatDialogs(null, getBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {

                Log.e(TAG, "onSuccess: dialogs-> " + qbChatDialogs);
//                rv.showShimmer();
                if (qbChatDialogs.size() > 0) {
                    hidenoPerson_chatlayout();

                    Log.e(TAG, "onSuccess: inbackground "+inbackground );
//                   if(inbackground){
//                       Log.e(TAG, "onSuccess: startbackground--> " );
//                       SharedPreferanceManager.getInstance(getContext()).save_chatDialogs(qbChatDialogs);
//                    }
//                    else{
                        getuserfromdialog(qbChatDialogs);
//                    }


                } else {
//                    loadingmsg_complete();
                    shownoPerson_chatlayout();
                }


            }

            @Override
            public void onError(QBResponseException e) {

                Log.e("error", "model_chats onError: " + e.getMessage());
                Toast.makeText(getContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * getuserfrom dialog
     */
    private void getuserfromdialog(ArrayList<QBChatDialog> qbChatDialogs) {
        Log.e(TAG, "onSuccess: qbchatDialogs" + qbChatDialogs + " qbchatDialogs size-> " + qbChatDialogs.size());
//        rv.hideShimmer();
        if (qbChatDialogs.size() > 0) {
            hidenoPerson_chatlayout();
            getuserdetails(qbChatDialogs);

        } else {
//            loadingmsg_complete();
            shownoPerson_chatlayout();

        }
    }


    private void getuserdetails(ArrayList<QBChatDialog> qbChatDialogs) {


        for (QBChatDialog qbChatDialog : qbChatDialogs) {
            getuserprofile(qbChatDialog, qbChatDialogs);
        }


    }


    public void getuserprofile(QBChatDialog qbChatDialog, ArrayList<QBChatDialog> qbChatDialogs) {

        Log.e(TAG, "getuserprofile: name->  " + qbChatDialog.getName()+" getRecipientId-> "+qbChatDialog.getRecipientId());

        Performer<QBUser> user = QBUsers.getUser(qbChatDialog.getRecipientId());
        user.performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {
                Log.e(TAG, "onSuccess: username-=>" + user.getFullName() + " email-> " + user.getEmail());
//                ids.add(user.getLogin());
//                if(ids.size() == qbChatDialogs.size()){
//                    Log.e("idsdata", "onSuccess: ids-> "+ids+" qbchatdialogs-> "+qbChatDialogs );
//                }

//                getapiuserdetail(user.getLogin(), qbChatDialogs);
                getapiuserdetail(user.getLogin(), qbChatDialogs,qbChatDialog);
            }

            @Override
            public void onError(QBResponseException e) {

                Log.e("error", "onError: e-> "+e.toString() );
            }
        });


//        QBUsers.getUsersByIDs(qbChatDialog.getRecipientId(),new QBPagedRequestBuilder())
//
//      QBUser qbUser =   QBUsers.getUser(qbChatDialog.getUserId());

    }


    private void getapiuserdetail(String userid, ArrayList<QBChatDialog> qbChatDialogs, QBChatDialog qbChatDialog) {
        Log.e(TAG, "\n\n\n\n\ngetuserprofile: userid-> \n\n\n\n\n" + userid);
        Log.e("userdata", "\n\n\n\n\n\n\n\ngetapiuserdetail:arraylist--> " + qbChatDialogs + "\n\n\n\n\n\n\n\n\n\n");
        qbDialogHistory.clear();

        StringRequest loginstringRequest = new StringRequest(Request.Method.POST,
                ApiConstant.GET_CHAT_PARAMETER,
                (Response.Listener<String>) response -> {
                    Log.d(TAG, "setlogin: response=> " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                            JSONObject detailobjec = jsonObject.getJSONObject(ConstantString.DETAIL_TAG);

                            Log.e("userdata", "setlogin: id " + detailobjec.getString("id"));
                            Log.e("userdata", "setlogin: name-> " + detailobjec.getString("name"));
                            Log.e("userdata", "setlogin: profile_img-> " + detailobjec.getString("profile_img"));
                            Chat_parameter chat_parameter = new Chat_parameter();
                            chat_parameter.setId(detailobjec.getString("id"));
                            chat_parameter.setName(detailobjec.getString("name"));
                            chat_parameter.setProfileImg(detailobjec.getString("profile_img"));

                            chat_parameters_array.add(chat_parameter);




                            ChatDialogHistory_users chatDialogHistoryUsers = new ChatDialogHistory_users();
                            chatDialogHistoryUsers.setQbChatDialog(qbChatDialog);
                            if(qbChatDialog.getRecipientId() < 0){
                                getuserdetails(qbChatDialogs);
                                return;
                            }

                            chatDialogHistoryUsers.setRecipentid(qbChatDialog.getRecipientId());
                            chatDialogHistoryUsers.setChat_parameter(chat_parameter);
                            chatDialogHArray.add(chatDialogHistoryUsers);

                            qbDialogHistory.add(qbChatDialog);



                            if (chat_parameters_array != null && !chat_parameters_array.isEmpty() &&
                                    chat_parameters_array.size() == qbChatDialogs.size()) {
                               dialogAdapter = new DialogAdapter(getActivity(),qbDialogHistory );
                               dialogAdapter.setOnlongClick(this);
                                dialogAdapter.setChatuserParam(chat_parameters_array);
                                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                                rv.setAdapter(dialogAdapter);
                                dialogAdapter.notifyDataSetChanged();
                                loadingmsg_complete();
//                                Log.e("Dialogchat", "getapiuserdetail: chatDialogHArray-> Recipient id-> "+chatDialogHArray.get(0).getRecipentid()
//                                        +" chatDialogHarray Chatparam-> "+chatDialogHArray.get(0).getChat_parameter().getName()+" profile-> "+chatDialogHArray.get(0).getChat_parameter().getId()+
//                                        " chatDialogHArray.get(0)--> "+chatDialogHArray.get(0).getQbChatDialog().getName());
//                                SharedPreferanceManager.getInstance(getContext()).save_chatDialogs(qbDialogHistory);

                                QbDialogs_userholderArray qbDialogs_userholderArray = new QbDialogs_userholderArray();
                                qbDialogs_userholderArray.setQbChatDialogs(qbDialogHistory);
                                qbDialogs_userholderArray.setChat_parameters(chat_parameters_array);

//
                                if(SharedPreferanceManager.getInstance(getContext()).has(SharedPreferanceManager.USER_DIALOGARRAY) &&
                                        SharedPreferanceManager.getInstance(getContext()).has(SharedPreferanceManager.USER_CHATPARAM)){

                                    Log.e("contain", "getapiuserdetail: hascontains" );
                                    SharedPreferanceManager.getInstance(getContext()).clear_chatDialogArrayOnly();
                                    SharedPreferanceManager.getInstance(getContext()).saveArray_DialogHistory(qbDialogs_userholderArray);
                                }
                                else{
                                    Log.e("contain", "getapiuserdetail:has not contains " );
                                    SharedPreferanceManager.getInstance(getContext()).saveArray_DialogHistory(qbDialogs_userholderArray);
                                }
                            }

                        } else {
                            loadingmsg_complete();
                            loadingmessage();
                            showvaldationError(jsonObject.getString(ConstantString.RESPONSE_MESSAGE), R.raw.onboard);

                        }

                    } catch (JSONException e) {
                        loadingmsg_complete();
                        e.printStackTrace();
                    }

                },
                error -> {


                    Log.d("error", "model_chat setlogin: error-> " + error.getMessage());
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


                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(getActivity()).getUserData();
                Log.d(TAG, "getHeaders: token value -> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> loginparam = new HashMap<>();
                loginparam.put("id", userid);

                return loginparam;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(loginstringRequest);
    }


    private void shownoPerson_chatlayout() {
        rv.setVisibility(View.GONE);
        recyclercontain.setVisibility(View.GONE);
        nochatsfoundlayout.setVisibility(View.VISIBLE);
        loding_layout.setVisibility(View.GONE);
    }

    private void hidenoPerson_chatlayout() {
        rv.setVisibility(View.VISIBLE);
        recyclercontain.setVisibility(View.VISIBLE);
        nochatsfoundlayout.setVisibility(View.GONE);
    }


    private void loadingmessage() {
        rv.setVisibility(View.GONE);
        loding_layout.setVisibility(View.VISIBLE);
    }

    private void loadingmsg_complete() {
        rv.setVisibility(View.VISIBLE);
        loding_layout.setVisibility(View.GONE);
    }

    private void showvaldationError(String msg, int errorimage) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg, errorimage);
        bottomSheet_for_error.setCancelable(false);
//        bottomSheet_for_error.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        bottomSheet_for_error.setlottiimage(errorimage);
        bottomSheet_for_error.show(getFragmentManager(), "error bottom");
    }


    /*System message Listener*/
    private class SystemMessagesListener implements QBSystemMessageListener {
        @Override
        public void processMessage(final QBChatMessage qbChatMessage) {
            dialogsManager.onSystemMessageReceived(qbChatMessage);
        }

        @Override
        public void processError(QBChatException ignored, QBChatMessage qbChatMessage) {

        }
    }


    /*process dialog message Listener*/
    private class AllDialogsMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(final String dialogId, final QBChatMessage qbChatMessage, Integer senderId) {
            Log.d(TAG, "Processing received Message: " + qbChatMessage.getBody()+" message user-> "+qbChatMessage.getId());
            if (!senderId.equals(ChatHelper.getCurrentUser(getContext()).getId())) {
                dialogsManager.onGlobalMessageReceived(dialogId, qbChatMessage);
                dialogAdapter.updateDialog(dialogId,qbChatMessage);
            }
        }
    }

    @Override
    public void onDialogCreated(QBChatDialog chatDialog) {

    }

    @Override
    public void onDialogUpdated(String chatDialog) {

    }

    @Override
    public void onNewDialogLoaded(QBChatDialog chatDialog) {

    }



    /**Push Broadcast reciver*/
    private class PushBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(FcmConsts.EXTRA_FCM_MESSAGE);
            Log.v(TAG, "Received broadcast " + intent.getAction() + " with data: " + message);
//            loadDialogsFromQb(false, false);
        }
    }


    @Override
    public void OnlongpressNotify(int position) {
        QBChatDialog chatDialog = dialogAdapter.getDialogfrompostion(position);
//        Toast.makeText(getContext(), "position-> "+position+" name-> "+chatDialog.getName(), Toast.LENGTH_SHORT).show();
    }
}
