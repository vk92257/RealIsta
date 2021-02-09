package com.example.myapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

public class QbDialogs_userholderArray implements Parcelable {

    private ArrayList<QBChatDialog> qbChatDialogs;
    private ArrayList<Chat_parameter> chat_parameters;

    public QbDialogs_userholderArray() {
    }

    protected QbDialogs_userholderArray(Parcel in) {
        chat_parameters = in.createTypedArrayList(Chat_parameter.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(chat_parameters);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QbDialogs_userholderArray> CREATOR = new Creator<QbDialogs_userholderArray>() {
        @Override
        public QbDialogs_userholderArray createFromParcel(Parcel in) {
            return new QbDialogs_userholderArray(in);
        }

        @Override
        public QbDialogs_userholderArray[] newArray(int size) {
            return new QbDialogs_userholderArray[size];
        }
    };

    public ArrayList<QBChatDialog> getQbChatDialogs() {
        return qbChatDialogs;
    }

    public void setQbChatDialogs(ArrayList<QBChatDialog> qbChatDialogs) {
        this.qbChatDialogs = qbChatDialogs;
    }

    public ArrayList<Chat_parameter> getChat_parameters() {
        return chat_parameters;
    }

    public void setChat_parameters(ArrayList<Chat_parameter> chat_parameters) {
        this.chat_parameters = chat_parameters;
    }
}
