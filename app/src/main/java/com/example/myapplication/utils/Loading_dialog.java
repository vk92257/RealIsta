package com.example.myapplication.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myapplication.R;


public class Loading_dialog {
    private Dialog dialog;
    private Context context;

    public Loading_dialog(Context context) {
        this.context = context;
    }

    public void showDialog(int raw){
        dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        LottieAnimationView lotanim = dialog.findViewById(R.id.loading_anim);
        if(raw != 0){
            lotanim.setAnimation(raw);
        }



        final Window window = dialog.getWindow();

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.alpha(12)));
        dialog.setCancelable(false);
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }


}
