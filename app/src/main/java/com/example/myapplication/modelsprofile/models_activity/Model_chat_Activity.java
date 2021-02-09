package com.example.myapplication.modelsprofile.models_activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.chatModule.Chat_Adapter.ChatMessageAdapter_2;
import com.example.myapplication.utils.ConstantString;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;

public class Model_chat_Activity extends AppCompatActivity {

    private CircularImageView profileimage;
    private TextView title;
    private RecyclerView rv;
    private EditText messageArea;
    private QBChatDialog qbChatDialog;
    private ChatMessageAdapter_2 adapter;
    private ArrayList<QBChatMessage> qbChatrecent;
    private FrameLayout sendbtn_attachment;

    private static final String VIDEO_OR_IMAGE_MIME = "image/* video/*";
    private static final String IMAGE_MIME = "image/*";
    private static  String TAG = "Model_chat_Activity";
    public static final int PERMISSIONS_FOR_SAVE_FILE_IMAGE_REQUEST = 1010;
    public static final int GALLERY_REQUEST_CODE = 183;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatactivitylayout);
        initviews();
        initChatDialog();
        retrievemessage();
    }

    private void initviews() {
        qbChatDialog = (QBChatDialog)getIntent().getSerializableExtra(ConstantString.DIALOG_EXTRA);

        profileimage = findViewById(R.id.profileimage);
        title = findViewById(R.id.title);
        rv = findViewById(R.id.rv);
        messageArea = findViewById(R.id.messageArea);
        sendbtn_attachment  = findViewById(R.id.sendbtn_attachment);


    }
    private void initChatDialog(){


    }
    private void retrievemessage() {

    }
    public void audiocall(View view){

    }

    public void videocall(View view){

    }

    public void sendMsssageButtonClicked(View view){

    }

    public void sendAttachment(View view){


    }


}
