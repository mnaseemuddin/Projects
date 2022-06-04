package com.lgt.fxtradingleague.Extra;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.lgt.fxtradingleague.HomeActivity;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.RazorPayments.AddPaymentActivity;

public class ViewDialog {

    public void showDialog(final Activity activity, String msg, final String status) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.conformation_alert);
        dialog.setCancelable(false);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (status.equalsIgnoreCase("2")){
                    activity.startActivity(new Intent(activity, AddPaymentActivity.class));
                    activity.finishAffinity();
                } else if(status.equalsIgnoreCase("1")) {
                    activity.startActivity(new Intent(activity, HomeActivity.class));
                    activity.finishAffinity();
                }else{
                    activity.onBackPressed();
                }
            }
        });
        dialog.show();
    }
}
