package com.app.arthasattva.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.google.firebase.auth.FirebaseAuth;
import com.app.arthasattva.R;
import com.app.arthasattva.databinding.ActivitySettingsBinding;
import com.app.arthasattva.utils.SessionManager;

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


        binding.labledNotification.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn) {
                    binding.labledNotification.setColorOn(context.getResources().getColor(R.color.low_app_color));
                } else {
                    binding.labledNotification.setColorOn(context.getResources().getColor(R.color.darkGrey));
                }
            }
        });




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
