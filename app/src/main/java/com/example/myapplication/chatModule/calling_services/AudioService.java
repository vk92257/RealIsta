package com.example.myapplication.chatModule.calling_services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.speech.tts.Voice;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;
import com.example.myapplication.chatModule.CallActivity;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBSignaling;
import com.quickblox.chat.QBWebRTCSignaling;
import com.quickblox.chat.listeners.QBVideoChatSignalingManagerListener;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AudioService extends Service implements QBRTCClientSessionCallbacks {
    private static final String NOTIFICATION_CHANNEL_ID ="notificaiton" ;
    private String default_notification_channel_id="serviceNotification";
    private Timer mTimer = null;
    private Handler mHandler = new Handler();
    public static final int notify = 300000;
    Intent serviceIntent;

    public AudioService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIntent = intent;
//        startMyOwnForeground();
        try {
            Log.e("service", "start");
            if (mTimer != null) {
                  mTimer.cancel();
                Log.wtf("service", "All ready started");
            } else {
                mTimer = new Timer();   //recreate new
                mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);
//                startMyOwnForeground();
//                LoginChatService();
//                ProcessCalls();
//                InitSignalling();
//                QBRTCClient.getInstance(this).addSessionCallbacksListener(this);
//                makeCall();
            }
        }catch (Exception e){
            Log.wtf("ex",""+e);
        }
        return START_NOT_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startMyOwnForeground() {
        String channelName = "My Background Service";
        Intent intent  = new Intent(this, VoiceCallActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationChannel chan = new NotificationChannel(getPackageName(), channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getPackageName());
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_logout)
                .setContentTitle("Voice call with "+serviceIntent.getStringExtra("username")+" is running.")
                .setContentIntent(fullScreenPendingIntent)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    private class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    startMyOwnForeground();
                    // display toast
//                    Toast.makeText(VideoCallService.this, "Service is running", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

//    private void InitSignalling() {
//        QBChatService.getInstance().getVideoChatWebRTCSignalingManager()
//                .addSignalingManagerListener(new QBVideoChatSignalingManagerListener() {
//                    @Override
//                    public void signalingCreated(QBSignaling qbSignaling, boolean createdLocally) {
//                        if (!createdLocally) {
//                            QBRTCClient.getInstance(NofificationService.this).addSignaling((QBWebRTCSignaling) qbSignaling);
//                        }
//                    }
//                });
//    }
    public  void LoginChatService()
    {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

//                    QBUser user = new QBUser(logins.getString(TRUE_ME_USERNEME, ""), logins.getString("password", ""));
                    QBUser user = SharedPreferanceManager.getInstance(getApplicationContext()).getQbUser();
                    SharedPreferences s=getSharedPreferences("QBid",0);
//                    user.setId(Integer.valueOf(s.getString("id","")));
                    user.setId(Integer.valueOf(SharedPreferanceManager.getInstance(getApplicationContext()).get("id")));
                    if(!QBChatService.getInstance().isLoggedIn()) {
                        QBChatService.getInstance().login(user);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        thread.start();
    }

    private void ProcessCalls() {
        QBRTCClient.getInstance(this).prepareToProcessCalls();
    }

    @Override
    public void onReceiveNewSession(QBRTCSession qbrtcSession) {

       Intent intent = new Intent(this, CallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("service", true);
        intent.putExtra("id", "" + qbrtcSession.getCallerID());
        intent.putExtra("session", String.valueOf(qbrtcSession));
        intent.putExtra("username",serviceIntent.getStringExtra("username"));
        startActivity(intent);

    }

    @Override
    public void onUserNoActions(QBRTCSession qbrtcSession, Integer integer) {

    }

    @Override
    public void onSessionStartClose(QBRTCSession qbrtcSession) {

    }

    @Override
    public void onUserNotAnswer(QBRTCSession qbrtcSession, Integer integer) {

    }

    @Override
    public void onCallRejectByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {

    }

    @Override
    public void onCallAcceptByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {

    }

    @Override
    public void onReceiveHangUpFromUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {

    }

    @Override
    public void onSessionClosed(QBRTCSession qbrtcSession) {

    }



    private void makeCall() {
        Intent intent = new Intent(this, CallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("service", true);
//        intent.putExtra("id", "" + qbrtcSession.getCallerID());
//        intent.putExtra("session", String.valueOf(qbrtcSession));
        Log.e("TAG", "onReceiveNewSession: user name is "+serviceIntent.getStringExtra("username"));
        intent.putExtra("username",serviceIntent.getStringExtra("username"));
        startActivity(intent);
    }

}
