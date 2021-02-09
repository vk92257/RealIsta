package com.example.myapplication.clientsprofile.clients_fragment;

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
import com.example.myapplication.chatModule.ChatHelper;
import com.example.myapplication.chatModule.Chat_Adapter.DialogAdapter;
import com.example.myapplication.chatModule.holder.QbDialogHolder;
import com.example.myapplication.pojo.Chat_parameter;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.core.server.Performer;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.todkars.shimmer.ShimmerRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.quickblox.users.QBUsers.updateUser;

public class Chats extends Fragment {
    //    private QBUser  user;
    private static final String TAG = "Chats_Fragmetn_client";

    //    public Chats(QBUser user) {
//        this.user = user;
//    }
    private LinearLayout titlebar;
    private LinearLayout nosavedjobslayout;
    private TextView title;
    private ShimmerRecyclerView rv;
    private LinearLayout nochatsfoundlayout;
    private RelativeLayout loding_layout;
    private static final int DIALOGS_PER_PAGE = 50;
    private LinearLayout recyclercontain;
    private ArrayList<Chat_parameter> chat_parameters_array;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clients_messagesfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intiviews(view);
//        loadChatDialogs();

    }


    @Override
    public void onStart() {
        super.onStart();

        loadChatDialogs();


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void intiviews(View view) {
        titlebar = view.findViewById(R.id.titlebar);
        title = view.findViewById(R.id.title);
        nosavedjobslayout = view.findViewById(R.id.nosavedjobslayout);
        rv = view.findViewById(R.id.rv);
        recyclercontain = view.findViewById(R.id.recyclercontain);

        nochatsfoundlayout = view.findViewById(R.id.nochatsfoundlayout);
        titlebar = view.findViewById(R.id.titlebar);
        titlebar = view.findViewById(R.id.titlebar);
        loding_layout = view.findViewById(R.id.loding_layout);
//        LottieAnimationView lottieAnimationView = view.findViewById(R.id.message_loading);
//        lottieAnimationView.setAnimation(R.raw.message_loading);

//        loading message
        loadingmessage();

        ArrayList<QBChatDialog> dialogs = new ArrayList<>(QbDialogHolder.getInstance().getDialogs().values());
        chat_parameters_array = new ArrayList<>();
    }


    private void loadChatDialogs() {
        rv.showShimmer();
        Log.e(TAG, "loadChatDialogs: " + QBChatService.getInstance().isLoggedIn());
        if (!QBChatService.getInstance().isLoggedIn()) {
            QBUser user = SharedPreferanceManager.getInstance(getContext()).getQbUser();
            signIn(user);

        } else {
            createchatbuilder();
        }


    }


    private void createchatbuilder() {
        QBRequestGetBuilder getBuilder = new QBRequestGetBuilder();
        getBuilder.setLimit(DIALOGS_PER_PAGE);

        QBRestChatService.getChatDialogs(null, getBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                Log.e(TAG, "onSuccess: qbchatDialogs" + qbChatDialogs + " qbchatDialogs size-> " + qbChatDialogs.size());
                rv.hideShimmer();
//                getuserdetails(qbChatDialogs);
                if (qbChatDialogs.size() > 0) {
                    Log.e(TAG, "onSuccess: chate is not empty");
                    hidenoPerson_chatlayout();

                    getuserfromdialog(qbChatDialogs);


//                    DialogAdapter dialogAdapter = new DialogAdapter(getActivity(), qbChatDialogs);
//                    rv.setLayoutManager(new LinearLayoutManager(getContext()));
//                    rv.setAdapter(dialogAdapter);
//                    dialogAdapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "onSuccess: chat diloag is empty" );
//                    loadingmsg_complete();
                    shownoPerson_chatlayout();
                }

            }

            @Override
            public void onError(QBResponseException e) {

                Log.e("error", "onError: " + e.getMessage());
                Toast.makeText(getContext(),"Please check your internet connection.",Toast.LENGTH_SHORT).show();
            }
        });
    }



    /**
     *
     * getuserfrom dialog
     *
     * */



    private void getuserfromdialog(ArrayList<QBChatDialog> qbChatDialogs) {
        Log.e(TAG, "onSuccess: qbchatDialogs" + qbChatDialogs + " qbchatDialogs size-> " + qbChatDialogs.size());
        rv.hideShimmer();
        if (qbChatDialogs.size() > 0) {
            hidenoPerson_chatlayout();
            getuserdetails(qbChatDialogs);

        } else {
            shownoPerson_chatlayout();
        }
    }

    private void getuserdetails(ArrayList<QBChatDialog> qbChatDialogs){

        for(QBChatDialog qbChatDialog : qbChatDialogs){
            Log.e(TAG, "getuserdetails:\n\n\n\n\n\n "+qbChatDialog+" \n\n\n\n\n\n\n" );
//            getuserprofile(qbChatDialog.getId().toString());
            getuserprofile(qbChatDialog,qbChatDialogs);
        }
//        DialogAdapter dialogAdapter = new DialogAdapter(getActivity(), qbChatDialogs);
//        rv.setLayoutManager(new LinearLayoutManager(getContext()));
//        rv.setAdapter(dialogAdapter);
//        dialogAdapter.notifyDataSetChanged();
    }

    private void signIn(final QBUser user) {
//        showProgressDialog(R.string.dlg_login);
        ChatHelper.getInstance().login(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser userFromRest, Bundle bundle) {
                Log.e(TAG, "onSuccess: signin " + user);
                Log.e(TAG, "onSuccess: email-> " + user.getLogin() + " emailcallback-> " + userFromRest.getLogin());
                if (userFromRest.getLogin().equalsIgnoreCase(user.getLogin())) {
//                    loginToChat(user);
                    Log.e(TAG, "onSuccess: signin " + user);
//                    loginToChat(user);
                    createchatbuilder();

                } else {
                    //Need to set password NULL, because server will update user only with NULL password
                    user.setPassword(null);
                    updateUser(user);
                }
            }

            @Override
            public void onError(QBResponseException e) {
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
                Log.e(TAG, "onSuccess: loginto chat successfull");

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: loginchat-> " + e.toString());
//                hideProgressDialog();
//                showErrorSnackbar(R.string.login_chat_login_error, e, null);
            }
        });
    }


    private void shownoPerson_chatlayout() {
        rv.setVisibility(View.GONE);
        recyclercontain.setVisibility(View.GONE);
        nochatsfoundlayout.setVisibility(View.VISIBLE);
        loding_layout.setVisibility(View.GONE);
    }

    private void hidenoPerson_chatlayout() {
        rv.setVisibility(View.VISIBLE);
        nochatsfoundlayout.setVisibility(View.GONE);
    }

    public void getuserprofile(QBChatDialog qbChatDialog,ArrayList<QBChatDialog> qbChatDialogs) {

        Log.e(TAG, "getuserprofile: String user id-->"+qbChatDialog.getRecipientId() );
        Performer<QBUser> user = QBUsers.getUser(qbChatDialog.getRecipientId());
        user.performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {
                Log.e(TAG, "onSuccess: username-=>" + user.getFullName() + " email-> " + user.getEmail());
                getapiuserdetail(user.getLogin(),qbChatDialogs);

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("error", "onError: getuserprofile" );

            }
        });


 }



    private void getapiuserdetail(String userid,ArrayList<QBChatDialog> qbChatDialogs) {


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
                            if (chat_parameters_array != null && !chat_parameters_array.isEmpty() &&
                                    chat_parameters_array.size() == qbChatDialogs.size()) {
                                DialogAdapter dialogAdapter = new DialogAdapter(getActivity(), qbChatDialogs);
                                dialogAdapter.setChatuserParam(chat_parameters_array);
                                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                                rv.setAdapter(dialogAdapter);

                                dialogAdapter.notifyDataSetChanged();
                                loadingmsg_complete();
                            }


                        } else {
                            loadingmsg_complete();
                            showvaldationError(jsonObject.getString(ConstantString.RESPONSE_MESSAGE), R.raw.onboard);

                        }

                    } catch (JSONException e) {
                        loadingmsg_complete();
                        e.printStackTrace();
                    }

                },
                error -> {

                    Log.d("error", "setlogin: error-> " + error.getMessage());
//                    showvaldationError(error.getMessage(), R.raw.onboard);
                    if( error instanceof NetworkError) {
                        showvaldationError("Network Error please check internet connection", R.raw.onboard);
                    } else if( error instanceof ServerError) {
                        //handle if server error occurs with 5** status code
                        showvaldationError("Server side error", R.raw.onboard);
                    } else if( error instanceof AuthFailureError) {
                        //handle if authFailure occurs.This is generally because of invalid credentials
                        showvaldationError("please check your credentials ", R.raw.onboard);
                    } else if( error instanceof ParseError) {
                        //handle if the volley is unable to parse the response data.
                        showvaldationError(" Unable to parse the response data ", R.raw.onboard);
                    } else if( error instanceof NoConnectionError) {
                        //handle if no connection is occurred
                        showvaldationError(" No Connection to server ", R.raw.onboard);
                    } else if( error instanceof TimeoutError) {
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


    private void loadingmessage(){
        rv.setVisibility(View.GONE);
        loding_layout.setVisibility(View.VISIBLE);
    }

    private void loadingmsg_complete(){
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

}
