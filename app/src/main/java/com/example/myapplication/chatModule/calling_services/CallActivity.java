package com.example.myapplication.chatModule.calling_services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.chatModule.calling_services.VideoCallService;
import com.example.myapplication.chatModule.calling_services.videoService;
import com.example.myapplication.clientsprofile.clients_activity.Chat_Activity;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBSignaling;
import com.quickblox.chat.QBWebRTCSignaling;
import com.quickblox.chat.listeners.QBVideoChatSignalingManagerListener;
import com.quickblox.videochat.webrtc.AppRTCAudioManager;
import com.quickblox.videochat.webrtc.BaseSession;
import com.quickblox.videochat.webrtc.QBMediaStreamManager;
import com.quickblox.videochat.webrtc.QBRTCAudioTrack;
import com.quickblox.videochat.webrtc.QBRTCCameraVideoCapturer;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientAudioTracksCallback;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks;
import com.quickblox.videochat.webrtc.view.QBRTCSurfaceView;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;


import org.webrtc.CameraVideoCapturer;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.quickblox.videochat.webrtc.QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
import static com.quickblox.videochat.webrtc.QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;

public class CallActivity extends AppCompatActivity implements QBRTCClientSessionCallbacks, QBRTCClientVideoTracksCallbacks, QBRTCClientAudioTracksCallback {
    private int userid;
    private String username;
    private Boolean isOutgoing, micE = true, vidE = true;
    private QBRTCSurfaceView surfaceView, remoteview;
    private MediaPlayer mp;
    private QBRTCSession currentsession;
    private AppRTCAudioManager appRTCAudioManager;
    private QBMediaStreamManager mediaStreamManager;
    private ImageView mic, video, cameraSwitch, speaker;
    private ImageView talking;
    private ToggleButton toggleSpeaker;
    private ToggleButton toggleCamera;
    private ToggleButton toggleMic;
    private ToggleButton toggleCameraSwitch;
    private boolean isCurrentCameraFront = true;
    private NotificationManager mNotificationManager;
    private Notification notification;
    private Intent videoCallIntent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startMyOwnForeground();
         videoCallIntent = new Intent(this, videoService.class);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_call);
        video = (ImageView) findViewById(R.id.video);
        toggleSpeaker = findViewById(R.id.toggle_speaker);
        toggleCamera = findViewById(R.id.toggle_camera);
        cameraSwitch = (ImageView) findViewById(R.id.camera_switch);
        toggleCameraSwitch = findViewById(R.id.toggle_camera_switch);
        speaker = findViewById(R.id.speaker);
        talking = findViewById(R.id.talking);
        mic = findViewById(R.id.mic);
        toggleMic = findViewById(R.id.toggle_mic);
        userid = getIntent().getIntExtra("user", 1);
        username = getIntent().getStringExtra("username");
        isOutgoing = getIntent().getBooleanExtra("isoutgoing", false);
        videoCallIntent.putExtra("username",username);
        ProcessCalls();
        InitSignalling();
        initAudioManager();

        if (isOutgoing) {
            CallUser(getIntent().getStringExtra("type"));
            if (username == null) {
                SetCallerName("Unknown User");
            } else

                SetCallerName(username + "");
        }

        if (getIntent().getBooleanExtra("service", false)) {
            //            SetLayoutForReceiveCall(QBChatService.getInstance().getSession(),DataHolder.getInstance().getUserInfo());
//            SetCallerName(getIntent().getStringExtra("id"));
            Log.e("calling activity ", "onCreate: username-> " + username);
            SetCallerName(username + "");
            videoCallIntent.putExtra("username",username);
//            Log.e("TAG", "onCreate: "+getIntent().getExtras().get("session"));
            QBRTCSession qbrtcSession = Chat_Activity.qbrtcSession1;
            Log.e("TAG", "onCreate: " + Chat_Activity.qbrtcSession1);

            if (qbrtcSession != null)
                SetLayoutForReceiveCall(qbrtcSession, qbrtcSession.getUserInfo());
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mp = MediaPlayer.create(getApplicationContext(), notification);
            mp.start();
        }

        toggleMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (micE) {
                    micE = false;
                    AudioManage();
                } else {
                    micE = true;
                    AudioManage();
                }
            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (micE) {
                    micE = false;
                    AudioManage();
                } else {
                    micE = true;
                    AudioManage();
                }
            }
        });


        cameraSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCamera();
            }
        });

        toggleCameraSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCamera();
            }
        });

        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchAudio();
            }
        });

        toggleSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchAudio();
            }
        });


        toggleCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vidE) {
                    vidE = false;
                    VideoManage();
                } else {
                    vidE = true;
                    VideoManage();
                }
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vidE) {
                    vidE = false;
                    VideoManage();
                } else {
                    vidE = true;
                    VideoManage();
                }
            }
        });

        remoteview = (QBRTCSurfaceView) findViewById(R.id.opponentView);
        surfaceView = (QBRTCSurfaceView) findViewById(R.id.localView);
        surfaceView.setMirror(true);
        surfaceView.requestLayout();
        QBRTCClient.getInstance(this).addSessionCallbacksListener(this);

    }


    private void switchCamera() {
        if (currentsession != null) {
            QBRTCCameraVideoCapturer videoCapturer = (QBRTCCameraVideoCapturer) currentsession.getMediaStreamManager().getVideoCapturer();
            videoCapturer.switchCamera(cameraSwitchHandler);
        }

    }

    CameraVideoCapturer.CameraSwitchHandler cameraSwitchHandler = new CameraVideoCapturer.CameraSwitchHandler() {
        @Override
        public void onCameraSwitchDone(boolean b) {
//            Log.e("TAG", "onCameraSwitchDone: camera switch is done");
            toggleCamera(isCurrentCameraFront);
        }

        @Override
        public void onCameraSwitchError(String s) {
            Log.e("TAG", "onCameraSwitchError: " + s);
        }
    };

    private void toggleCamera(boolean isCurrentCameraFront) {
        if (isCurrentCameraFront) {
            this.isCurrentCameraFront = false;
            cameraSwitch.setImageResource(R.drawable.ic_camera_rear);
        }
        if (!isCurrentCameraFront) {
            cameraSwitch.setImageResource(R.drawable.ic_camera_front);
            this.isCurrentCameraFront = true;
        }
    }

    private void SetCallerName(String callerID) {
        TextView callerName = (TextView) findViewById(R.id.callername);
        TextView callertime = (TextView) findViewById(R.id.callTime);
        callerName.setText(callerID + "");
        if (isOutgoing) {
            callertime.setText("Calling...");
        }

    }

    private void InitSignalling() {
        QBChatService.getInstance().getVideoChatWebRTCSignalingManager()
                .addSignalingManagerListener(new QBVideoChatSignalingManagerListener() {
                    @Override
                    public void signalingCreated(QBSignaling qbSignaling, boolean createdLocally) {

                        if (!createdLocally) {
                            QBRTCClient.getInstance(CallActivity.this).addSignaling((QBWebRTCSignaling) qbSignaling);
                        }
                    }
                });

    }

    private void ProcessCalls() {
        QBRTCClient.getInstance(this).prepareToProcessCalls();
    }

    private void CallUser(String type) {
        //Set conference type
        // There are two types of calls:
        // - QB_CONFERENCE_TYPE_VIDEO - for video call;
        // - QB_CONFERENCE_TYPE_AUDIO - for audio call;
//        mic.setVisibility(View.GONE);
        toggleMic.setVisibility(View.GONE);
//        video.setVisibility(View.GONE);
        toggleCamera.setVisibility(View.GONE);
        cameraSwitch.setVisibility(View.GONE);
        toggleCameraSwitch.setVisibility(View.GONE);
        QBRTCTypes.QBConferenceType qbConferenceType = null;
        if (type.equalsIgnoreCase("audio")) {
            video.setVisibility(View.GONE);
            toggleCamera.setVisibility(View.GONE);
            cameraSwitch.setVisibility(View.GONE);
            toggleCameraSwitch.setVisibility(View.GONE);
            talking.setVisibility(View.VISIBLE);
            qbConferenceType = QB_CONFERENCE_TYPE_AUDIO;
        } else if (type.equalsIgnoreCase("video")) {
            qbConferenceType = QB_CONFERENCE_TYPE_VIDEO;
            talking.setVisibility(View.GONE);
        }

//        QBRTCTypes.QBConferenceType qbConferenceType = QB_CONFERENCE_TYPE_VIDEO;
        //Initiate opponents list
        List<Integer> opponents = new ArrayList<Integer>();
        opponents.add(userid); //12345 - QBUser ID
        //Set user information
        // User can set any string key and value in user info
        // Then retrieve this data from sessions which is returned in callbacks
        // and parse them as he wish
        Map<String, String> userInfo = new HashMap<>();
        Log.i("TAG", "onReceiveNewSession:--> " + username);
        userInfo.put("username", username);

//Init session
//         QBRTCSession session =

        currentsession = QBRTCClient.getInstance(this).createNewSessionWithOpponents(opponents, qbConferenceType);

        //Start call

        currentsession.startCall(userInfo);


        currentsession.addEventsCallback(this);
        SetCallButtonsDialing(currentsession, userInfo);
        Log.e("TAG", "CallUser: Service is starting ");

        StartDialRinging();
    }

    private void SetCallButtonsDialing(final QBRTCSession session, final Map<String, String> userInfo) {
        FrameLayout call = (FrameLayout) findViewById(R.id.CallerLayout);
        call.setVisibility(View.VISIBLE);
        Log.e("TAG", "onReceiveNewSession: " + session.getConferenceType().getValue() + "==" + QB_CONFERENCE_TYPE_AUDIO.getValue());
        if (session.getConferenceType().getValue() == QB_CONFERENCE_TYPE_VIDEO.getValue()) {
            session.addVideoTrackCallbacksListener(this);
        }
        if (session.getConferenceType().getValue() == QB_CONFERENCE_TYPE_AUDIO.getValue()) {
            session.addAudioTrackCallbacksListener(this);
            Log.e("TAG", "onReceiveNewSession: inside  " + session.getConferenceType().getValue() + "==" + QB_CONFERENCE_TYPE_AUDIO.getValue());
            talking.setVisibility(View.VISIBLE);
            video.setVisibility(View.GONE);
            toggleCamera.setVisibility(View.GONE);
            cameraSwitch.setVisibility(View.GONE);
            toggleCameraSwitch.setVisibility(View.GONE);
        }

        findViewById(R.id.Endcall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.hangUp(userInfo);
                if (mNotificationManager != null)
                    mNotificationManager.cancel(2);

                CallActivity.this.finish();
            }
        });
    }



    public void initAudioManager() {
        appRTCAudioManager = AppRTCAudioManager.create(this);
        appRTCAudioManager.setOnWiredHeadsetStateListener(new AppRTCAudioManager.OnWiredHeadsetStateListener() {
            @Override
            public void onWiredHeadsetStateChanged(boolean plugged, boolean hasMicrophone) {
                Log.e("Headset  Plugged", "Unplugged");
            }
        });

        appRTCAudioManager.setBluetoothAudioDeviceStateListener(new AppRTCAudioManager.BluetoothAudioDeviceStateListener() {
            @Override
            public void onStateChanged(boolean connected) {
                Log.e("Bluetooth   Connected", "Disconnected");
            }
        });

        appRTCAudioManager.start(new AppRTCAudioManager.AudioManagerEvents() {
            @Override
            public void onAudioDeviceChanged(AppRTCAudioManager.AudioDevice audioDevice, Set<AppRTCAudioManager.AudioDevice> set) {
                Log.e("AudioDevice", "" + audioDevice);
            }
        });

        if (currentsession != null && currentsession.getConferenceType() == QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO) {
            appRTCAudioManager.selectAudioDevice(AppRTCAudioManager.AudioDevice.EARPIECE);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        Intent intent = new Intent(CallActivity.this, CallActivity.class);
        PendingIntent pendingLaunchIntent = PendingIntent.getActivity(this, 0, intent, 0);
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(getPackageName(), channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert mNotificationManager != null;
        mNotificationManager.createNotificationChannel(chan);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getPackageName());
        notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_logout)
                .setContentTitle(getResources().getString(R.string.app_name) + " is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(pendingLaunchIntent)
                .build();

//        mNotificationManager.notify(2,notification);

//        Intent intent = new Intent(getApplicationContext(), CallActivity.class);
//        PendingIntent pendingLaunchIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        String channelName = "My Background Service";
//        NotificationChannel chan = new NotificationChannel(getPackageName(), channelName, NotificationManager.IMPORTANCE_NONE);
//        chan.setLightColor(Color.BLUE);
//        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//         mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        assert mNotificationManager != null;
//        mNotificationManager.createNotificationChannel(chan);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getPackageName());
//         notification = notificationBuilder.setOngoing(true)
//                .setSmallIcon(R.drawable.ic_logout)
//                .setContentTitle(getResources().getString(R.string.app_name) + " is running in background")
//                .setColor(ContextCompat.getColor(this, R.color.white_green))
//                .setColorized(true)
//                .setContentIntent(pendingLaunchIntent)
//                .setPriority(NotificationManager.IMPORTANCE_MIN)
//                .setCategory(Notification.CATEGORY_SERVICE)
//                .build();
//            mNotificationManager.notify(2,notification);

//        startFore(2, notification);

//        mNotificationManager.notify(2, notification);
    }

    public void switchAudio() {
        Log.v("TAG", "onSwitchAudio(), SelectedAudioDevice() = " + appRTCAudioManager.getSelectedAudioDevice());
        if (appRTCAudioManager.getSelectedAudioDevice() != AppRTCAudioManager.AudioDevice.SPEAKER_PHONE) {
            appRTCAudioManager.selectAudioDevice(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);
            speaker.setImageResource(R.drawable.ic_sound);
        } else {
            if (appRTCAudioManager.getAudioDevices().contains(AppRTCAudioManager.AudioDevice.BLUETOOTH)) {
                appRTCAudioManager.selectAudioDevice(AppRTCAudioManager.AudioDevice.BLUETOOTH);
            } else if (appRTCAudioManager.getAudioDevices().contains(AppRTCAudioManager.AudioDevice.WIRED_HEADSET)) {
                appRTCAudioManager.selectAudioDevice(AppRTCAudioManager.AudioDevice.WIRED_HEADSET);
            } else {
                appRTCAudioManager.selectAudioDevice(AppRTCAudioManager.AudioDevice.EARPIECE);
                speaker.setImageResource(R.drawable.ic_soundoff);

            }
        }
    }

    public void AudioManage() {
//        Toast.makeText(this, "mediaStreamManager"+mediaStreamManager, Toast.LENGTH_SHORT).show();
        if (mediaStreamManager != null) {
            if (mediaStreamManager.isAudioEnabled()) {
                mediaStreamManager.setAudioEnabled(false);
                mic.setImageResource(R.drawable.ic_mic_off_black_24dp);
            } else {
                mediaStreamManager.setAudioEnabled(true);
                mic.setImageResource(R.drawable.ic_mic_black_24dp);
            }
        }
    }


    public void VideoManage() {
//        Toast.makeText(this, "video clicked", Toast.LENGTH_SHORT).show();
        if (mediaStreamManager != null) {
            if (mediaStreamManager.isVideoEnabled()) {
                mediaStreamManager.setVideoEnabled(false);
                video.setImageResource(R.drawable.ic_videocam_off_black_24dp);
            } else {
                mediaStreamManager.setVideoEnabled(true);
                video.setImageResource(R.drawable.ic_videocam_black_24dp);
            }
        }
    }


    @Override
    public void onReceiveNewSession(QBRTCSession qbrtcSession) {
        qbrtcSession.addVideoTrackCallbacksListener(this);
        Map<String, String> userInfo = qbrtcSession.getUserInfo();
        Log.e("CallActivity", "onReceiveNewSession: " + userInfo.get("username"));


        currentsession = qbrtcSession;

        if (currentsession.getConferenceType().getValue() == QB_CONFERENCE_TYPE_VIDEO.getValue())
            appRTCAudioManager.selectAudioDevice(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);
        SetLayoutForReceiveCall(qbrtcSession, userInfo);
    }


    private void SetLayoutForReceiveCall(final QBRTCSession qbrtcSession, final Map<String, String> userInfo) {
        final FrameLayout receive = (FrameLayout) findViewById(R.id.answerlayout);
        receive.setVisibility(View.VISIBLE);
        qbrtcSession.addVideoTrackCallbacksListener(this);
        final ImageView calll = (ImageView) findViewById(R.id.answerCall);
        calll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Accept incoming call
                startService(videoCallIntent);
                qbrtcSession.acceptCall(userInfo);
                receive.setVisibility(View.GONE);
                SetCallButtonsDialing(qbrtcSession, userInfo);
//                mic.setVisibility(View.VISIBLE);
                talking.setVisibility(View.GONE);
                toggleMic.setVisibility(View.VISIBLE);
                if (qbrtcSession.getConferenceType().getValue() == QB_CONFERENCE_TYPE_VIDEO.getValue()) {
//                    video.setVisibility(View.VISIBLE);
                    toggleCamera.setVisibility(View.VISIBLE);
//                    cameraSwitch.setVisibility(View.VISIBLE);
                    toggleCameraSwitch.setVisibility(View.VISIBLE);
                }
                if (mNotificationManager != null)
                    mNotificationManager.notify(2, notification);
                StartTimer();
                if (mp != null && mp.isPlaying()) {
                    mp.stop();
                }
            }
        });


        findViewById(R.id.rejectcall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qbrtcSession.rejectCall(userInfo);
                CallActivity.this.finish();
                if (mp != null && mp.isPlaying()) {
                    mp.stop();
                }
            }
        });
    }

    private void StartTimer() {
//        mic.setVisibility(View.VISIBLE);
        toggleMic.setVisibility(View.VISIBLE);

        final TextView timer = (TextView) findViewById(R.id.callTime);
        timer.setText("00:00");
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            private long startTime = System.currentTimeMillis();

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            int millis = (int) (System.currentTimeMillis() - startTime) / 1000;
                            int min = millis / 60;
                            int sec = millis % 60;
                            timer.setText((min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec));
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void onUserNoActions(QBRTCSession qbrtcSession, Integer integer) {
//        Toast.makeText(this, "no action by user", Toast.LENGTH_SHORT).show();
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
    }

    @Override
    public void onSessionStartClose(QBRTCSession qbrtcSession) {
        Log.e("TAG", "onSessionStartClose: ");
        qbrtcSession.addVideoTrackCallbacksListener(this);
        try {
            qbrtcSession.getMediaStreamManager().setVideoCapturer(new QBRTCCameraVideoCapturer(this, null));
            mediaStreamManager = qbrtcSession.getMediaStreamManager();
            currentsession = qbrtcSession;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUserNotAnswer(QBRTCSession qbrtcSession, Integer integer) {
//        Toast.makeText(this, "No answer", Toast.LENGTH_SHORT).show();
        if (mp != null && mp.isPlaying()) {
            mp.stop();
//            stopService(intent);
        }
        finish();
    }

    @Override
    public void onCallRejectByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {
//        Toast.makeText(this, "Call rejected", Toast.LENGTH_SHORT).show();
        if (mp != null && mp.isPlaying()) {
            mp.stop();

//            stopService(intent);
        }
        finish();
    }

    @Override
    public void onCallAcceptByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {
        qbrtcSession.addVideoTrackCallbacksListener(this);
       startService(videoCallIntent);

        if (notification != null)
            mNotificationManager.notify(2, notification);

        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }

        if (qbrtcSession.getConferenceType().getValue() == QB_CONFERENCE_TYPE_VIDEO.getValue()) {
//            video.setVisibility(View.VISIBLE);
            toggleCamera.setVisibility(View.VISIBLE);
//            cameraSwitch.setVisibility(View.VISIBLE);
            toggleCameraSwitch.setVisibility(View.VISIBLE);
        }

        StartTimer();
    }

    @Override
    public void onReceiveHangUpFromUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {
        if (mp != null && mp.isPlaying()) {
            mp.stop();

        }
//        Toast.makeText(this, "Call ended by user", Toast.LENGTH_SHORT).show();
        finish();
//        stopService(intent);
    }

    @Override
    public void onSessionClosed(QBRTCSession qbrtcSession) {
        if (mp != null && mp.isPlaying()) {
            mp.stop();

        }
        if (mNotificationManager != null)
            mNotificationManager.cancel(2);

        //        stopService(intent);
    }

    public void StartDialRinging() {
        try {
            mp = MediaPlayer.create(getApplicationContext(), R.raw.beep);
            mp.setLooping(true);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocalVideoTrackReceive(BaseSession baseSession, QBRTCVideoTrack qbrtcVideoTrack) {
        currentsession = (QBRTCSession) baseSession;
        fillVideoView(surfaceView, qbrtcVideoTrack);
        updateVideoView(surfaceView);
        surfaceView.setMirror(true);
        surfaceView.requestLayout();

    }

    @Override
    public void onRemoteVideoTrackReceive(BaseSession baseSession, QBRTCVideoTrack qbrtcVideoTrack, Integer integer) {
//        qbrtcVideoTrack.addRenderer(new VideoRenderer(remoteview));


//
        fillVideoView(remoteview, qbrtcVideoTrack);
        updateVideoView(remoteview);
        remoteview.setMirror(true);
        remoteview.requestLayout();
//        mediaStreamManager = Client_chat_Activity.qbrtcSession1.getMediaStreamManager();
        mediaStreamManager = baseSession.getMediaStreamManager();
        remoteview.requestLayout();

    }

    private void fillVideoView(QBRTCSurfaceView videoView, QBRTCVideoTrack videoTrack) {
        // To remove renderer if Video Track already has another one
        videoTrack.cleanUp();
        if (videoView != null) {
            videoTrack.addRenderer(videoView);
            updateVideoView(videoView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentsession != null) {
           currentsession.rejectCall(currentsession.getUserInfo());
            currentsession.removeEventsCallback(this);
            currentsession.removeVideoTrackCallbacksListener(this);
            currentsession.removeAudioTrackCallbacksListener(this);
        }
        if (mNotificationManager != null) {
            mNotificationManager.cancel(2);
            notification = null;
            mNotificationManager = null;
        }
    stopService(videoCallIntent);
    }

    private void updateVideoView(SurfaceViewRenderer videoView) {
        RendererCommon.ScalingType scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;
        videoView.setScalingType(scalingType);
        videoView.setMirror(false);
        videoView.requestLayout();
    }

    @Override
    public void onLocalAudioTrackReceive(BaseSession baseSession, QBRTCAudioTrack qbrtcAudioTrack) {

    }

    @Override
    public void onRemoteAudioTrackReceive(BaseSession baseSession, QBRTCAudioTrack qbrtcAudioTrack, Integer integer) {
        mediaStreamManager = baseSession.getMediaStreamManager();
    }
}

