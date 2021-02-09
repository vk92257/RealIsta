package com.example.myapplication.Chat_module.AdapterChat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

public class ChatDialogAdapter extends BaseAdapter {

    private Context context ;
    private ArrayList<QBChatDialog> qbChatDialogs;

    public ChatDialogAdapter(Context context, ArrayList<QBChatDialog> qbChatDialogs) {
        this.context = context;
        this.qbChatDialogs = qbChatDialogs;
    }

    @Override
    public int getCount() {
        return qbChatDialogs.size();
    }

    @Override
    public Object getItem(int position) {
        return qbChatDialogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view  = layoutInflater.inflate(R.layout.list_chat_demo,null);

            TextView chatTitle ,chatmessage;
            chatTitle = view.findViewById(R.id.list_chat_dialog_title);
            chatmessage = view.findViewById(R.id.list_chat_dialog_message);
            chatTitle.setText(qbChatDialogs.get(position).getName());
        }

        return view;
    }
}
