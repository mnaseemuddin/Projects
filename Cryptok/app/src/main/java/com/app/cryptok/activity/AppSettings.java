package com.app.cryptok.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.databinding.ActivitySettingsBinding;
import com.app.cryptok.utils.SessionManager;

public class AppSettings extends AppCompatActivity {

    private ActivitySettingsBinding binding;

    private Context context;
    private AppSettings activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        context = activity = this;
        handleClick();
    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(view -> onBackPressed());
        binding.tvLogout.setOnClickListener(view -> logoutFromApp());

        binding.labledNotification.setOnToggledListener((toggleableView, isOn) -> {
            if (isOn) {
                binding.labledNotification.setColorOn(context.getResources().getColor(R.color.low_app_color));
            } else {
                binding.labledNotification.setColorOn(context.getResources().getColor(R.color.darkGrey));
            }
        });

        binding.tvAboutUs.setOnClickListener(view -> FirebaseFirestore.getInstance()
                .collection(DBConstants.App_Guidelines + "/"
                        + DBConstants.guidlines_table_key + "/" +
                        DBConstants.help)
                .document(DBConstants.help_document)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.getResult().exists()) {
                        if (task.getResult().getString(DBConstants.about_us) != null) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(task.getResult().getString(DBConstants.about_us)));
                            if (intent != null) {
                                startActivity(intent);
                            }
                        }
                    }
                }));

    }

    public void logoutFromApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Are you sure!\nYou want to logout from this app.");
        builder.setTitle("Logout Alert");
        builder.setCancelable(false);
        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            SessionManager sessionManager = new SessionManager(context);
            sessionManager.clearData();
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().signOut();
            }
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        // Intent intent=new Intent(context,)

    }
}
