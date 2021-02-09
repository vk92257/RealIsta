package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

public class BottomSheet_for_error extends BottomSheetDialogFragment {
    private TextView errotext;
    private MaterialButton errorbtn;
    private String txt_error_String;
    private LottieAnimationView animationView;
    private static String TAG="BottomSheet_for_errr";


    private String btntxt="";
    private int lottianimation=-1;
    public BottomSheet_for_error(String txt_error_String) {
        this.txt_error_String = txt_error_String;
    }
    public BottomSheet_for_error(String txt_error_String, int lottianimation){
        this.txt_error_String = txt_error_String;
        this.lottianimation = lottianimation;
    }

    public BottomSheet_for_error(String txt_error_String, String btntxt, int lottianimation) {
        this.txt_error_String = txt_error_String;
        this.btntxt = btntxt;
        this.lottianimation = lottianimation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);
//        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_for_error,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundResource(android.R.color.transparent);
//        view.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

//        view.setScrollBarStyle(R.style.R.styles.BottomSheetDialog);
        initview(view);
        setdata();
    }
    private void setdata(){
//        errorbtn.setText("this is text");
        errotext.setText(txt_error_String);
        if(  !btntxt.isEmpty()){
            errorbtn.setText(btntxt);
        }
        if(lottianimation >0){
            animationView.setAnimation(lottianimation);
        }
        errorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void initview(View view){
      errotext = view.findViewById(R.id.errortxt);
      errorbtn = view.findViewById(R.id.errobtn);
      animationView = view.findViewById(R.id.erroranimation);

    }
    public void setlottiimage(int err){

    }
}
