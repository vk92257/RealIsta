package com.example.myapplication.chatModule.Chat_Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.callbacks.NotifiyLongPress;
import com.example.myapplication.clientsprofile.clients_activity.Chat_Activity;
import com.example.myapplication.pojo.Chat_parameter;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.TimeUtils;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;
import java.util.List;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ChatDialogHolder> {

    private Context context;
    private List<QBChatDialog> dialogs;
    private ArrayList<Chat_parameter> chat_parameters_array;


//    TAGS for intent
private NotifiyLongPress notifiyLongPress;

    private final String TAG = "DialogAdapter";

    public DialogAdapter(Context context, List<QBChatDialog> dialogs) {
        this.context = context;
        this.dialogs = dialogs;
    }

    @NonNull
    @Override
    public ChatDialogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatDialogHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.demo_chatlist_show, parent, false));
    }

    public void setChatuserParam(ArrayList<Chat_parameter> chat_parameters_array) {
        this.chat_parameters_array = chat_parameters_array;
    }


    public void setOnlongClick(NotifiyLongPress notifiyLongPress){
        this.notifiyLongPress = notifiyLongPress ;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChatDialogHolder holder, int position) {
        holder.username.setText(dialogs.get(position).getName());

        Log.e("chat_param", "onBindViewHolder: chat_parameters_array-> "+chat_parameters_array+
                " size-> "+chat_parameters_array.size() );
        if (ishaveChatParameters()) {
            for (int i = 0; i < chat_parameters_array.size(); i++) {
                Chat_parameter chat_parameter = chat_parameters_array.get(i);
                Log.e("chat_param", "onBindViewHolder: dialogs-> " + dialogs.get(position).getName() + " chat_parameter-> " + chat_parameter.getName());
                if (dialogs.get(position).getName().equals(chat_parameter.getName())) {
                    Log.e("chat_param", "onBindViewHolder: dialogs-> " + dialogs.get(position).getName() + " chat_parameter-> " + chat_parameter.getName());
                    Glide.with(context)
                            .load(chat_parameter.getProfileImg())
                            .placeholder(R.drawable.ic_person)
                            .into(holder.userimage);

                    holder.chatclick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, Chat_Activity.class);
                            intent.putExtra(ConstantString.DIALOG_EXTRA, dialogs.get(position));
                            Log.e(TAG, "onClick: " + dialogs.get(position) + dialogs.get(position).getOccupants().get(0));
                            if (ishaveChatParameters()) {
                                intent.putExtra(ConstantString.CHAT_RCV_USER, chat_parameter);
                            }

                            holder.unreadmessage.setVisibility(View.GONE);
                            context.startActivity(intent);
                        }
                    });




                    holder.chatclick.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            Log.e(TAG, "onLongClick: notification-> "+notifiyLongPress );
                            notifiyLongPress.OnlongpressNotify(position);

                            return true;
                        }
                    });
                }
            }
        }



        int count = dialogs.get(position).getUnreadMessageCount();
        if (count > 0) {
            holder.unreadmessage.setVisibility(View.VISIBLE);
            holder.unreadmessage.setText(String.valueOf(count));
        } else {
            holder.unreadmessage.setVisibility(View.GONE);
        }

        Log.e(TAG, "onBindViewHolder:count->  "+dialogs.get(position).getUnreadMessageCount() );
        Log.e("lastmessage", "onBindViewHolder: dialog-> "+dialogs.get(position).getLastMessage() +" dialogs-> "+dialogs.get(position));

        checkforLastMsg(holder,dialogs.get(position));

//        if (dialogs.get(position) != null && dialogs.get(position).getLastMessage() != null
//                &&
//
//                !dialogs.get(position).getLastMessage().equals("")
//        ) {
//
//            checkforLastMsgfile(holder,dialogs.get(position));
//
//
//        } else if(dialogs.get(position) != null && dialogs.get(position).getUnreadMessageCount() <=0){
//            holder.imageholder.setVisibility(View.GONE);
//            holder.lastmessage.setVisibility(View.GONE);
//        }
//        else if(){
//            holder.imageholder.setVisibility(View.VISIBLE);
//            holder.lastmessage.setVisibility(View.GONE);
//        }


    }


    @SuppressLint("ResourceAsColor")
    private void checkforLastMsg(ChatDialogHolder holder, QBChatDialog qbChatDialog){
       holder.timetxt.setVisibility(View.VISIBLE);
        holder.timetxt.setText(TimeUtils.getTimewitham_pm(qbChatDialog.getLastMessageDateSent()));
        if(qbChatDialog != null && qbChatDialog.getLastMessage() != null){
            if(qbChatDialog.getLastMessage().trim().length() >24){
                holder.lastmessage.setText(qbChatDialog.getLastMessage().toString().substring(0,20)+"....");
            }
            else{
                holder.lastmessage.setText(qbChatDialog.getLastMessage().toString().trim());
            }
        }
        else {
            holder.lastmessage.setText("Attachment");
            holder.lastmessage.setTextColor(context.getResources().getColor(R.color.blueattachemnt));
        }
    }

    private void checkforLastMsgfile(ChatDialogHolder holder, QBChatDialog qbChatDialog) {


        boolean image = QBAttachment.IMAGE_TYPE.equalsIgnoreCase(String.valueOf(qbChatDialog.getType()));
        boolean video = QBAttachment.VIDEO_TYPE.equalsIgnoreCase(String.valueOf(qbChatDialog.getType()));
        boolean audio = QBAttachment.AUDIO_TYPE.equalsIgnoreCase(String.valueOf(qbChatDialog.getType()));
        boolean file = String.valueOf(qbChatDialog.getType()).equals("file") ||String.valueOf(qbChatDialog.getType()).contains("file") || String.valueOf(qbChatDialog.getType()).equals("");


        holder.imageholder.setVisibility(View.GONE);
        holder.lastmessage.setVisibility(View.VISIBLE);
        if(image){
            holder.imageholder.setVisibility(View.VISIBLE);

        }
        else if(video){
            holder.imageholder.setVisibility(View.VISIBLE);
            holder.imageholder.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_video_player, 0, 0, 0);
        }
        else if(file){
            holder.imageholder.setVisibility(View.VISIBLE);
            holder.imageholder.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_placeholder, 0, 0, 0);


        }
        else if(audio){
            holder.imageholder.setVisibility(View.VISIBLE);
            holder.imageholder.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file, 0, 0, 0);


        }else if(qbChatDialog.getLastMessage().trim().length() >24){
            holder.lastmessage.setText(qbChatDialog.getLastMessage().toString().substring(0,20)+"....");
        }
        else{
            holder.lastmessage.setText(qbChatDialog.getLastMessage().toString().trim());
        }



    }


    public void updateDialog(String dialogId, QBChatMessage qbChatMessage){
        for(int i=0;i<getItemCount();i++){
            if(dialogs.get(i).getDialogId().equals(dialogId)){
                QBChatDialog chatDialog = dialogs.get(i);

                Log.e(TAG, "updateDialog:chatdialog "+chatDialog.getName()+" -> "+chatDialog.getLastMessage() );
                chatDialog.setLastMessage(qbChatMessage.getBody());
                chatDialog.setUnreadMessageCount(chatDialog.getUnreadMessageCount()+1);
                chatDialog.setDialogId(dialogId);

                dialogs.set(i,chatDialog);
                notifyItemChanged(i);
            }
        }
    }


    private boolean ishaveChatParameters() {
        return chat_parameters_array != null && !chat_parameters_array.isEmpty();
    }

    @Override
    public int getItemCount() {
        return dialogs.size();
    }

    public QBChatDialog getDialogfrompostion(int position){
        return dialogs.get(position);
    }

    static class ChatDialogHolder extends RecyclerView.ViewHolder {

        TextView username;
        CircularImageView userimage;
        LinearLayout chatclick;
        TextView lastmessage;
        TextView unreadmessage;
        TextView imageholder;
        TextView timetxt;

        public ChatDialogHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            userimage = itemView.findViewById(R.id.userimage);
            chatclick = itemView.findViewById(R.id.chatclick);
            lastmessage = itemView.findViewById(R.id.lastmessage_id);
            unreadmessage = itemView.findViewById(R.id.unreadnotfication_id);
            imageholder = itemView.findViewById(R.id.imageholder);
            timetxt = itemView.findViewById(R.id.timetxt);
        }
    }

//private void setUnreadMessageCount(){
////    QBRestChatService.getChatDialogById(dialog.getDialogId()).performAsync(new QBEntityCallback<QBChatDialog>() {
//    QBRestChatService.getChatDialogById("").performAsync(new QBEntityCallback<QBChatDialog>() {
//        @Override
//        public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
//            // get unread messages count
//            int unreadMessageCount = qbChatDialog.getUnreadMessageCount();
//        }
//
//        @Override
//        public void onError(QBResponseException e) {
//
//        }
            //    });
//}


}
