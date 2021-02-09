package com.example.myapplication.clientsprofile.clients_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.chatModule.ChatHelper;
import com.example.myapplication.pojo.AllProposal_Pojo;
import com.example.myapplication.pojo.Chat_parameter;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.Loading_dialog;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Clients_ViewDetailedProposal extends AppCompatActivity {

    private CircularImageView profileimage;
    private TextView name;
    private TextView professiontitle;
    private TextView location;
    private TextView proposedbudget;
    private TextView coverletter;
    private AllProposal_Pojo deatilpojo;
    private LinearLayout attachments_ll;
    private ListView attachments;
    private Loading_dialog lodDialog;

    private Chat_parameter chat_recever;


    private static final String TAG = "Clients_viewsDEtailedproposal";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
//        deatilpojo = Objects.requireNonNull(getIntent().getExtras()).getParcelable("pojoall_proposal");

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.clients_viewdetailedproposal);
        initview();
        Log.d(TAG, "onCreate: detailpojo-> "+deatilpojo);
        deatilpojo = getIntent().getParcelableExtra("pojoall_proposal");
        if (deatilpojo != null) {
            setview(deatilpojo);
        }

        if (getIntent().getParcelableExtra(ConstantString.CHAT_RCV_USER) != null) {
            chat_recever = getIntent().getParcelableExtra(ConstantString.CHAT_RCV_USER);
        }
    }

    public void back(View view) {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void chat(View view) {
        createchatsession(chat_recever);
    }

    public void viewprofile(View view) {
        if(deatilpojo != null){
            Intent intent = new Intent(this, View_proposal_userProfile.class);
            intent.putExtra("user_id",deatilpojo.getUserId());
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }

    private void initview() {
        profileimage = findViewById(R.id.profileimage);
        name = findViewById(R.id.name);
        professiontitle = findViewById(R.id.professiontitle);
        location = findViewById(R.id.location);
        proposedbudget = findViewById(R.id.proposedbudget);
        coverletter = findViewById(R.id.coverletter);
        attachments_ll = findViewById(R.id.attachments_ll);
        attachments = findViewById(R.id.attachments);

        lodDialog = new Loading_dialog(this);

    }

    @SuppressLint("SetTextI18n")
    private void setview(AllProposal_Pojo deatilpojo) {
        Log.d(TAG, "setview: name->  "+deatilpojo.getName()+
                " \n image-> "+deatilpojo.getProfile_img()+
                "\n proffesiontitla-> "+deatilpojo.getProffesion()+
                "\n "+deatilpojo.getCover_letter()+
                " \n location-> "+deatilpojo.getCountry()+" | "+deatilpojo.getState()+
                " \n Proposed buget-> "+deatilpojo.getPurposalRate());
        Glide.with(this)
                .load(deatilpojo.getProfile_img()).error(R.mipmap.ic_launcher_r)
                .into(profileimage);
        name.setText(deatilpojo.getName());
        professiontitle.setText(deatilpojo.getProffesion());
        location.setText(deatilpojo.getCity()+", "+deatilpojo.getState()+" | "+deatilpojo.getCountry());
        coverletter.setText(deatilpojo.getCover_letter());

        proposedbudget.setText("Proposed budget: $"+deatilpojo.getPurposalRate()+" per day");
        if (!deatilpojo.getAttachment().isEmpty() && deatilpojo.getAttachment().get(0) != null) {
            attachments_ll.setVisibility(View.VISIBLE);
            ArrayList<String> linked = new ArrayList<>();
            linked.addAll(deatilpojo.getAttachment());

            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.listview_attachments_item, linked);
            attachments.setAdapter(arrayAdapter);
            attachments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(linked.get(i)));
                    startActivity(in);
                }
            });
        } else {
            attachments_ll.setVisibility(View.GONE);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    private void createchatsession(Chat_parameter chatparam) {
        lodDialog.showDialog(R.raw.chat_load);
        ChatHelper.getInstance().loadUserByLogin(chatparam.getId(), new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {

                ChatHelper.getInstance().createPrivateChat(user, "new Chat", new QBEntityCallback<QBChatDialog>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {

                        Log.e(TAG, "<---------------- onSuccess: dialog session created ------> ");
                        lodDialog.hideDialog();

                        Intent intent = new Intent(Clients_ViewDetailedProposal.this, Chat_Activity.class);
                        intent.putExtra(ConstantString.DIALOG_EXTRA, qbChatDialog);
                        intent.putExtra(ConstantString.CHAT_RCV_USER, chatparam);
                        startActivity(intent);

                    }

                    @Override
                    public void onError(QBResponseException e) {

                        Log.e(TAG, "onError:----------------- " + e.toString());
                        lodDialog.hideDialog();
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError:loaduser --------------------> " + e.toString());
                lodDialog.hideDialog();
            }
        });
    }

    }
