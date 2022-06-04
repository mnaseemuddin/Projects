package com.app.cryptok.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.app.cryptok.R;
import com.app.cryptok.databinding.DailogLoaderBinding;


public class CustomDialogBuilder {
    private Context mContext;
    private Dialog mBuilder = null;

    public CustomDialogBuilder(Context context) {
        this.mContext = context;
        if (mContext != null) {
            mBuilder = new Dialog(mContext);
            mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mBuilder.setCancelable(false);
            mBuilder.setCanceledOnTouchOutside(false);
            if (mBuilder != null && mBuilder.getWindow() != null) {
                mBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }



    public TextView tv_percentage;
    public void showLoadingDialog() {
        if (mContext == null)
            return;

        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        DailogLoaderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dailog_loader, null, false);
        Animation rotateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
        Animation reverseAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_reverse);
        binding.ivParent.startAnimation(rotateAnimation);
        binding.ivChild.startAnimation(reverseAnimation);
        mBuilder.setContentView(binding.getRoot());
        tv_percentage=mBuilder.findViewById(R.id.tv_percentage);
    }

    public void showPercentageLoading(){
        if (mContext == null)
            return;
        if (mBuilder==null)
            return;

        mBuilder.show();
    }

    public void hideLoadingDialog() {
        try {
            mBuilder.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnResultButtonClick {
        void onButtonClick(boolean success);
    }

    public interface OnDismissListener {
        void onPositiveDismiss();

        void onNegativeDismiss();
    }

    public interface OnCoinDismissListener {
        void onCancelDismiss();

        void on5Dismiss();

        void on10Dismiss();

        void on20Dismiss();

    }

}