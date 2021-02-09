package com.example.myapplication.utils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.chatModule.ChatHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.quickblox.chat.QBChatService;

import org.jivesoftware.smack.SmackException;

import java.util.Objects;

public class BottomSheettl extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "logout is click";
    public LinearLayout linearLayout;
    public BottomSheettl() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheetformenutl,
                container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initvalue(view);
    }

    public void initvalue(View view){
         view.findViewById(R.id.logoutbtnbottom).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.logoutbtnbottom:
                    Log.d(TAG, "onClick:logout ");
                    logoutallsession();
                    break;

            }
    }

    private void logoutallsession(){

        Log.e(TAG, "logoutallsession: logout........" );
        SharedPreferanceManager.getInstance(getActivity()).logout();
//        ChatHelper.getInstance().logoutChatSetvice();
        ChatHelper.getInstance().destoryChatService();
        SharedPreferanceManager.getInstance(getActivity()).removeQbUser();

        Objects.requireNonNull(getActivity()).finish();
    }

}
