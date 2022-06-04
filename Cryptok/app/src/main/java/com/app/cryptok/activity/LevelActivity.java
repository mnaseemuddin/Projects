package com.app.cryptok.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.adapter.HowToUpdateAdapter;
import com.app.cryptok.adapter.PendantUsersAdapter;
import com.app.cryptok.databinding.ActivityLevelBinding;
import com.app.cryptok.model.LevelPOJO;
import com.app.cryptok.model.Levels.LevelsPointsModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LevelActivity extends AppCompatActivity {

    private ProfilePOJO profilePOJO;
    HowToUpdateAdapter updateAdapter;
    PendantUsersAdapter usersAdapter;
    Activity activity;
    Context context;
    // BubbleSeekBar seekBar;
    ArrayList<LevelPOJO> levelPOJOS = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    SessionManager sessionManager;
    private ActivityLevelBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_level);

        context = activity = this;
        loadData();
        iniFirebase();
        handleClick();
        loadLevelsInfo();
    }

    private void iniFirebase() {
        sessionManager = new SessionManager(getApplicationContext());
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);

        firebaseFirestore = FirebaseFirestore.getInstance();
        Glide.with(getApplicationContext())
                .load(profilePOJO.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivProfilePic);
    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void loadData() {
        usersAdapter = new PendantUsersAdapter(this);
        binding.rvUsers.setAdapter(usersAdapter);
        levelPOJOS.add(setLevel(R.drawable.check_in, "Check In", "Up to 20 Exp. per day"));
        levelPOJOS.add(setLevel(R.drawable.watch_live, "Watch Live", "Up to 30 Exp. per day/Once a day"));
        levelPOJOS.add(setLevel(R.drawable.share_live, "Share Live Room", "Up to 30 Exp. per day/Three times a day"));
        levelPOJOS.add(setLevel(R.drawable.share_gift, "Share Gift", "Share 1 diamond and you may gain 5 Exp."));
        levelPOJOS.add(setLevel(R.drawable.recieved_gift, "Recieved Gift", "Recieve 1 bean and you may gain 3 Exp"));
        updateAdapter = new HowToUpdateAdapter(this, levelPOJOS);
        binding.rvLevel.setAdapter(updateAdapter);
    }

    private void loadLevelsInfo() {

        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        if (value.exists()) {

                            int points = value.getLong(DBConstants.point).intValue();
                            Commn.showDebugLog("my current points:" + points + "");
                            binding.levelsProgress.setProgress(points);
                            binding.tvCurrentLevelPoints.setText(String.valueOf(points));
                            getLevel(points);


                        }
                    }

                });

    }

    private void getLevel(int points) {

        firebaseFirestore.collection(DBConstants.all_levels_points)
                .orderBy(DBConstants.point, Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo(DBConstants.point, points)
                .limit(1)
                .addSnapshotListener((value, error) -> {

                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        LevelsPointsModel model = snapshot.toObject((LevelsPointsModel.class));
                        Commn.showDebugLog("my filtererere:" + new Gson().toJson(model) + "");
                       /* if (points > model.getPoint()) {
                            binding.tvNextLevelPoints.setVisibility(View.GONE);
                            binding.tvDivide.setVisibility(View.GONE);
                        } else {
                            binding.tvNextLevelPoints.setVisibility(View.VISIBLE);
                            binding.tvDivide.setVisibility(View.VISIBLE);
                        }*/
                        binding.tvCurrentLevel.setText("Lv" + model.getLevel() + "");
                        binding.tvCurrentLevelTop.setText("Level : " + model.getLevel() + "");
                        binding.tvNextLevelPoints.setText(model.getPoint() + "");
                        binding.levelsProgress.setMax(model.getPoint());
                        if (model.getLevel() != null) {
                            sessionManager.saveMyLevel(model.getLevel() + "");
                        }

                        updateMyLevel(model.getLevel() + "");

                    }
                });

    }

    private void updateMyLevel(String level) {

        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            if (task.getResult().exists()) {
                                if (task.getResult().getString(ConstantsKey.current_level) != null) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put(ConstantsKey.current_level, level);
                                    firebaseFirestore.collection(DBConstants.UserInfo)
                                            .document(profilePOJO.getUserId())
                                            .update(map);
                                }

                            }
                        }
                    }
                });
    }

    private LevelPOJO setLevel(int image, String title, String desc) {
        LevelPOJO levelPOJO = new LevelPOJO();
        levelPOJO.image = image;
        levelPOJO.title = title;
        levelPOJO.desc = desc;
        return levelPOJO;
    }
}
