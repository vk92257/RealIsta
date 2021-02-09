package com.example.myapplication.chatModule.Chat_Adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.chatModule.ChatHelper;
import com.example.myapplication.chatModule.holder.QbDialogHolder;
import com.github.library.bubbleview.BubbleTextView;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.content.model.QBFile;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;


public class ChatMessageAdapter_2 extends RecyclerView.Adapter<ChatMessageAdapter_2.ChatmessageHolder> {

    private Context context;
    private ArrayList<QBChatMessage> qbChatMessages;

    public static final int CUSTOM_VIEW_TYPE = -1;
    public static final int TYPE_TEXT_RIGHT = 1;
    public static final int TYPE_TEXT_LEFT = 2;
    public static final int TYPE_ATTACH_RIGHT = 3;
    public static final int TYPE_ATTACH_LEFT = 4;
    public static final int TYPE_NOTIFICATION_CENTER = 5;

    private static String TAG = "ChatMessageAdapter";
//    private QBChatService  qbChatService = QBChatService.getInstance();
    protected QBUser currentUser;

    public ChatMessageAdapter_2(Context context, ArrayList<QBChatMessage> qbChatMessages) {
        this.context = context;
        currentUser = ChatHelper.getInstance().getclient().getUser();
        this.qbChatMessages = qbChatMessages;
    }
    private SparseIntArray containerLayoutRes = new SparseIntArray() {
        {
            put(TYPE_TEXT_RIGHT, R.layout.list_send_message);
            put(TYPE_TEXT_LEFT, R.layout.list_recv_message);
            put(TYPE_ATTACH_RIGHT, R.layout.list_send_message);
            put(TYPE_ATTACH_LEFT, R.layout.list_recv_message);

        }
    };

    @NonNull
    @Override
    public ChatmessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        Log.e(TAG, "onCreateViewHolder: " + viewType);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (viewType == 1) {
            view = inflater.inflate(R.layout.list_send_message, null);
            TextView bubbleTextView = (TextView) view.findViewById(R.id.message_content);
            bubbleTextView.setText(qbChatMessages.get(viewType).getBody());
        } else {

            view = inflater.inflate(R.layout.list_recv_message, null);
            TextView nametxt = (TextView) view.findViewById(R.id.nametxt);
            nametxt.setText("sushil chhetri");
            TextView  bubbleTextView = (TextView) view.findViewById(R.id.message_content);
            bubbleTextView.setText(qbChatMessages.get(viewType).getBody());
//                TextView textView = (TextView)view.findViewById(R.id.message_user);
//                textView.setText(QBUsersHolder.getInstance().getUserById(qbChatMessages.get(position).getSenderId()).getFullName());
        }



        if(viewType == TYPE_TEXT_RIGHT){
            view = inflater.inflate(R.layout.list_send_message, null);
            LinearLayout Attachment_layout = (LinearLayout) view.findViewById(R.id.ll_attachment_body_container);
            Attachment_layout.setVisibility(View.GONE);

            TextView bubbleTextView = (TextView) view.findViewById(R.id.message_content);
            bubbleTextView.setText(qbChatMessages.get(viewType).getBody());
        }
        else if(viewType == TYPE_TEXT_LEFT){
            view = inflater.inflate(R.layout.list_recv_message, null);
            TextView nametxt = (TextView) view.findViewById(R.id.nametxt);
            nametxt.setText("sushil chhetri");
            TextView  bubbleTextView = (TextView) view.findViewById(R.id.message_content);
            bubbleTextView.setText(qbChatMessages.get(viewType).getBody());


        }
        else if(viewType == TYPE_ATTACH_RIGHT){
            view = inflater.inflate(R.layout.list_send_message, null);

            LinearLayout bubbleTextView = (LinearLayout) view.findViewById(R.id.ll_message_body_container);
            bubbleTextView.setVisibility(View.GONE);
//            QBAttachment attachment =getAttachment(0);
//            String imageUrl = QBFile.getPrivateUrlForUID(attachment.getId());

            ImageView imageView = (ImageView)view.findViewById(R.id.attachment_content);
            Glide.with(context)
                    .load(R.drawable.model10)
                    .into(imageView);

        }

        else if(viewType == TYPE_ATTACH_LEFT){

        }



        return new ChatmessageHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatmessageHolder holder, int position) {
        holder.message_content.setText(qbChatMessages.get(position).getBody());

    }

    @Override
    public int getItemViewType(int position) {

        QBChatMessage chatMessage = getItem(position);
        int p=CUSTOM_VIEW_TYPE ;


        Log.e(TAG, "getItemViewType: qbcatservice-> "+ChatHelper.getInstance().getclient().getUser()+
                " \n\n\n\n\n getchathelper+ "+qbChatMessages.get(position)+" qbchatservice-> "+ChatHelper.getInstance().getclient()+
                "\n\n\n\n\n\n wbchatlkjd-> "+ChatHelper.getInstance().getclient().getUser()+
                " \n\n\n\n\n "+currentUser.getId()
                );
        if(currentUser.getId() != null){
//            if (qbChatMessages.get(position).getSenderId().equals(currentUser.getId())) {
//                p = 1;
//            } else {
//                p = 0;
//            }

            Log.e("typeposition", "getItemViewType: "+position );

            assert chatMessage != null;
            if (chatMessage.getAttachments() != null && chatMessage.getAttachments().size() > 0) {
                QBAttachment attachment = getAttachment(position);
                boolean photo = QBAttachment.PHOTO_TYPE.equalsIgnoreCase(attachment.getType());
                boolean image = QBAttachment.IMAGE_TYPE.equalsIgnoreCase(attachment.getType());
                boolean video = QBAttachment.VIDEO_TYPE.equalsIgnoreCase(attachment.getType());
                boolean audio = QBAttachment.AUDIO_TYPE.equalsIgnoreCase(attachment.getType());
                boolean file = attachment.getType().equals("file") || attachment.getType().contains("file") || attachment.getType().equals("");

                if (photo || image || video || audio || file) {
                    if (isIncoming(chatMessage)) {
                        p = TYPE_ATTACH_LEFT;
                    } else {
                        p = TYPE_ATTACH_RIGHT;
                    }
                }
            }
            else if(isIncoming(chatMessage)){
                p = TYPE_TEXT_LEFT;
            }
            else{
                p = TYPE_TEXT_RIGHT;
            }

        }


        return p;
    }

    private QBChatMessage getItem(int position) {
        if (position <= getItemCount() - 1) {
            return qbChatMessages.get(position);
        } else {
            return null;
        }
    }

    private QBAttachment getAttachment(int position) {
        QBChatMessage chatMessage = getItem(position);
        if (chatMessage != null && chatMessage.getAttachments() != null && chatMessage.getAttachments().iterator().hasNext()) {
            return chatMessage.getAttachments().iterator().next();
        } else {
            return null;
        }
    }

    private boolean isIncoming(QBChatMessage chatMessage) {

        return chatMessage.getSenderId() != null && !chatMessage.getSenderId().equals(currentUser.getId());
    }
    @Override
    public int getItemCount() {
        return qbChatMessages.size();
    }

    static class ChatmessageHolder extends RecyclerView.ViewHolder {

        TextView message_content;

        public ChatmessageHolder(@NonNull View itemView) {
            super(itemView);

            message_content = itemView.findViewById(R.id.message_content);
        }
    }
}
