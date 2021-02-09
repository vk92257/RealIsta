package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Chat_module.ListUserActivity;
import com.example.myapplication.R;
import com.example.myapplication.chatModule.ChatHelper;
import com.example.myapplication.clientsprofile.clients_activity.Chat_Activity;
import com.example.myapplication.clientsprofile.clients_activity.Clients_HiringNowActivity;
import com.example.myapplication.clientsprofile.clients_activity.Clients_ViewDetailedProposal;
import com.example.myapplication.clientsprofile.clients_activity.View_proposal_userProfile;
import com.example.myapplication.pojo.AllProposal_Pojo;
import com.example.myapplication.pojo.Chat_parameter;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.Loading_dialog;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class AllProposals_Adapter extends RecyclerView.Adapter<AllProposals_Adapter.Proposals_ViewHolder> {
    private ArrayList<AllProposal_Pojo> pojoAll_proposal;
    private Context context;
    private String TAG = "Allproposals";

    private Loading_dialog lodDialog;


    public AllProposals_Adapter(ArrayList<AllProposal_Pojo> pojoAll_proposal, Context context) {
        this.pojoAll_proposal = pojoAll_proposal;
        this.context = context;
        lodDialog = new Loading_dialog(context);
    }

    @NonNull
    @Override
    public Proposals_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Proposals_ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_clientsseeingproposal_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Proposals_ViewHolder holder, int position) {
//        holder.profileimage.

        Log.d(TAG, "onBindViewHolder:proposal rate->  " + pojoAll_proposal.get(position).getPurposalRate());
        Glide.with(context)
                .load(pojoAll_proposal.get(position).getProfile_img()).error(R.mipmap.ic_launcher_r)
                .into(holder.profileimage);

        String name = pojoAll_proposal.get(position).getName();
        holder.name.setText(name.trim().substring(0, 1).toUpperCase() + name.trim().substring(1));

        String professiontitle = pojoAll_proposal.get(position).getProffesion();
        holder.professiontitle.setText(professiontitle.trim().substring(0, 1).toUpperCase() +
                professiontitle.trim().substring(1));
        holder.location.setText(pojoAll_proposal.get(position).getCity() + " | " + pojoAll_proposal.get(position).getCountry());
        holder.proposedbudget.setText("Proposed budget : $" + pojoAll_proposal.get(position).getPurposalRate() + " per day");
        holder.skills.setText(jsonarrayconvert((ArrayList<String>) pojoAll_proposal.get(position).getSkills()));
        holder.coverletter.setText(pojoAll_proposal.get(position).getCover_letter());
//            holder.chatwithhim.sett

        Chat_parameter chat_parameter = new Chat_parameter();
        chat_parameter.setId(pojoAll_proposal.get(position).getUserId());
        chat_parameter.setName(pojoAll_proposal.get(position).getName());
        chat_parameter.setProfileImg(pojoAll_proposal.get(position).getProfile_img());


        holder.viewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, View_proposal_userProfile.class);
                intent.putExtra("user_id", pojoAll_proposal.get(position).getUserId());
                context.startActivity(intent);

//                context.getApplicationContext().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });


        holder.chatwithhim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: " + pojoAll_proposal);
                Log.e(TAG, "onClick: " + pojoAll_proposal.get(position).getId() + " " + pojoAll_proposal.get(position).getUserId());

                createchatsession(chat_parameter);
            }
        });

        holder.hireartist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Clients_HiringNowActivity.class);
                intent.putExtra("jobid", pojoAll_proposal.get(position).getId());
                intent.putExtra("proposalid", pojoAll_proposal.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(intent);
            }
        });

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Clients_ViewDetailedProposal.class);
                intent.putExtra("pojoall_proposal", pojoAll_proposal.get(position));
                intent.putExtra(ConstantString.CHAT_RCV_USER,chat_parameter);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return pojoAll_proposal.size();
    }

    static class Proposals_ViewHolder extends RecyclerView.ViewHolder {
        CircularImageView profileimage;
        TextView name;
        TextView professiontitle;
        TextView location;
        TextView proposedbudget;
        TextView skills;
        TextView coverletter;
        ImageView chatwithhim;
        TextView viewprofile;
        TextView viewproposal;
        AppCompatButton hireartist;
        CardView card;

        public Proposals_ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileimage = itemView.findViewById(R.id.profileimage);
            name = itemView.findViewById(R.id.name);
            professiontitle = itemView.findViewById(R.id.professiontitle);
            location = itemView.findViewById(R.id.location);
            proposedbudget = itemView.findViewById(R.id.proposedbudget);
            skills = itemView.findViewById(R.id.skills);
            coverletter = itemView.findViewById(R.id.coverletter);
            chatwithhim = itemView.findViewById(R.id.chatwithhim);
            viewprofile = itemView.findViewById(R.id.viewprofile);
            viewproposal = itemView.findViewById(R.id.viewproposal);
            hireartist = itemView.findViewById(R.id.hireartist);
            card = itemView.findViewById(R.id.card);


        }
    }

    private StringBuilder jsonarrayconvert(ArrayList<String> jsondata) {
        StringBuilder locationstring = new StringBuilder();

        for (int i = 0; i < jsondata.size(); i++) {

            locationstring.append(jsondata.get(i));
            if (i < jsondata.size() - 1) {
                locationstring.append(", ");
            }
        }

        if (locationstring.length() <= 0) {
            locationstring.append("NA");
            return locationstring;
        }
        return locationstring;
    }


    /**
     * connect chat session and create private dialog quickblox
     *
     */

    private void createchatsession(Chat_parameter chatparam) {
        lodDialog.showDialog(R.raw.chat_load);
        ChatHelper.getInstance().loadUserByLogin(chatparam.getId(), new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {

                ChatHelper.getInstance().createPrivateChat(user, "new Chat", new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {

                        Log.e(TAG, "<---------------- onSuccess: dialog session created ------> ");
                        lodDialog.hideDialog();

                        Intent intent = new Intent(context, Chat_Activity.class);
                        intent.putExtra(ConstantString.DIALOG_EXTRA,qbChatDialog);
                        intent.putExtra(ConstantString.CHAT_RCV_USER, chatparam);
                        context.startActivity(intent);

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
