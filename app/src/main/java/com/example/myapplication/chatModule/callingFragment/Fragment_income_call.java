package com.example.myapplication.chatModule.callingFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class Fragment_income_call extends Fragment {
    private LinearLayout layout_info_about_call;
    private ImageView image_caller_avatar;
    private TextView call_type;
    private TextView text_also_on_call;
    private TextView text_other_inc_users;
    private ImageButton image_button_reject_call;
    private ImageButton image_button_accept_call;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_income_call, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initviews(view);
    }
    private void initviews(View view){
        layout_info_about_call = view.findViewById(R.id.layout_info_about_call);
        image_caller_avatar = view.findViewById(R.id.image_caller_avatar);
        call_type = view.findViewById(R.id.call_type);
        text_also_on_call = view.findViewById(R.id.text_also_on_call);
        text_other_inc_users = view.findViewById(R.id.text_other_inc_users);
        image_button_reject_call = view.findViewById(R.id.image_button_reject_call);
        image_button_accept_call = view.findViewById(R.id.image_button_accept_call);
    }
}
