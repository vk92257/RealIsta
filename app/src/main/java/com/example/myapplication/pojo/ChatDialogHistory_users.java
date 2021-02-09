package com.example.myapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.quickblox.chat.model.QBChatDialog;

public class ChatDialogHistory_users implements Parcelable {

    private QBChatDialog qbChatDialog;
    private Chat_parameter chat_parameter;
    private int recipentid;

    public ChatDialogHistory_users() {
    }

    protected ChatDialogHistory_users(Parcel in) {
        chat_parameter = in.readParcelable(Chat_parameter.class.getClassLoader());
        recipentid = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(chat_parameter, flags);
        dest.writeInt(recipentid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChatDialogHistory_users> CREATOR = new Creator<ChatDialogHistory_users>() {
        @Override
        public ChatDialogHistory_users createFromParcel(Parcel in) {
            return new ChatDialogHistory_users(in);
        }

        @Override
        public ChatDialogHistory_users[] newArray(int size) {
            return new ChatDialogHistory_users[size];
        }
    };

    public QBChatDialog getQbChatDialog() {
        return qbChatDialog;
    }

    public void setQbChatDialog(QBChatDialog qbChatDialog) {
        this.qbChatDialog = qbChatDialog;
    }

    public Chat_parameter getChat_parameter() {
        return chat_parameter;
    }

    public void setChat_parameter(Chat_parameter chat_parameter) {
        this.chat_parameter = chat_parameter;
    }

    public int getRecipentid() {
        return recipentid;
    }

    public void setRecipentid(int recipentid) {
        this.recipentid = recipentid;
    }
}
