package com.app.cryptok.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.activity.AppSettings;
import com.app.cryptok.activity.EditProfileActivity;
import com.app.cryptok.activity.LevelActivity;
import com.app.cryptok.activity.MainActivity;
import com.app.cryptok.activity.MessageUsersActivity;
import com.app.cryptok.activity.MyConnectionsActivity;
import com.app.cryptok.activity.MyPostsActivity;
import com.app.cryptok.LiveShopping.Activities.ShoppingZoneActivity;
import com.app.cryptok.databinding.FragMyProfileBinding;
import com.app.cryptok.model.Follow.FollowModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;
import com.app.cryptok.wallet_module.WalletActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyProfile extends Fragment {

    public MyProfile() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_my_profile, container, false);
        return binding.getRoot();
    }

    private FragMyProfileBinding binding;
    Toolbar toolbar;
    MainActivity mainActivity;
    ProfilePOJO profilePOJO;
    SessionManager sessionManager;
    private FirebaseFirestore firebaseFirestore;
    private FollowModel followModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = (MainActivity) getActivity();

        toolbar = view.findViewById(R.id.toolbar);
        mainActivity.setSupportActionBar(toolbar);
//        mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        sessionManager = new SessionManager(requireActivity());
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniFirebase();
        new Handler().postDelayed(() -> {
            if (profilePOJO != null) {
                getInfo();
            }

        }, 200);
        getFans();
        handleClick();
    }

    private void handleClick() {

        binding.llFollowing.setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), MyConnectionsActivity.class);
            intent.putExtra(DBConstants.connection_type, DBConstants.following_type);
            intent.putExtra(DBConstants.user_id, profilePOJO.getUserId());
            startActivity(intent);
        });
        binding.llFollowers.setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), MyConnectionsActivity.class);
            intent.putExtra(DBConstants.connection_type, DBConstants.follower_type);
            intent.putExtra(DBConstants.user_id, profilePOJO.getUserId());
            startActivity(intent);
        });

        binding.llFriends.setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), MyConnectionsActivity.class);
            intent.putExtra(DBConstants.connection_type, DBConstants.friends_type);
            intent.putExtra(DBConstants.user_id, profilePOJO.getUserId());
            startActivity(intent);
        });

        binding.llSettings.setOnClickListener(view -> {

            Intent intent = new Intent(getActivity(), AppSettings.class);
            startActivity(intent);

        });

        binding.llLever.setOnClickListener(view -> {

            Intent intent = new Intent(getActivity(), LevelActivity.class);
            startActivity(intent);

        });

        binding.llWallet.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), WalletActivity.class);
            startActivity(intent);
        });


        binding.llMyPost.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MyPostsActivity.class);
            startActivity(intent);
        });
        binding.llMessage.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MessageUsersActivity.class);
            startActivity(intent);
        });
        binding.llHelp.setOnClickListener(view -> {
            HelpBottomSheet helpBottomSheet = new HelpBottomSheet();
            helpBottomSheet.show(getChildFragmentManager(), "");
        });

        binding.ivProfilePic.setOnClickListener(view -> startActivity(new Intent(mainActivity, EditProfileActivity.class)));

        binding.llHelpFeedback.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(Commn.play_store_base_url + binding.getRoot().getContext().getPackageName()));
            startActivity(intent);

        });
        binding.llLikeUs.setOnClickListener(view -> {

            firebaseFirestore.collection(DBConstants.App_Guidelines + "/" +
                    DBConstants.guidlines_table_key + "/" + DBConstants.social)
                    .document(DBConstants.social_document)
                    .get()
                    .addOnCompleteListener(task -> {

                        if (task.getResult().exists()) {
                            if (task.getResult().getString(DBConstants.like_us) != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(task.getResult().getString(DBConstants.like_us)));
                                if (intent != null) {
                                    startActivity(intent);
                                }
                            }

                        }
                    });

        });
        binding.llLikeInstagram.setOnClickListener(view -> {

            firebaseFirestore.collection(DBConstants.App_Guidelines + "/" +
                    DBConstants.guidlines_table_key + "/" + DBConstants.social)
                    .document(DBConstants.social_document)
                    .get()
                    .addOnCompleteListener(task -> {

                        if (task.getResult().exists()) {
                            if (task.getResult().getString(DBConstants.follow_us) != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(task.getResult().getString(DBConstants.follow_us)));
                                if (intent != null) {
                                    startActivity(intent);
                                }

                            }

                        }
                    });

        });
        binding.llShoppingZone.setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), ShoppingZoneActivity.class);
            startActivity(intent);
        });
    }

    private void iniFirebase() {

        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    private void getFans() {

        String currentUserRef = DBConstants.UserInfo + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Followers;
        String following_Ref = DBConstants.UserInfo + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Following;
        firebaseFirestore.collection(currentUserRef)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        int count = value.size();
                        binding.tvFansCount.setText(Commn.prettyCount(Long.parseLong(String.valueOf(count))));
                        Commn.showDebugLog("snapshot eXISTS: all_fans:" + count + "");
                    } else {
                        Commn.showDebugLog("snapshot NOT eXISTS:");
                        binding.tvFansCount.setText("0");
                    }
                });

        firebaseFirestore.collection(following_Ref)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        int count = value.size();
                        binding.tvFollowingCount.setText(Commn.prettyCount(Long.parseLong(String.valueOf(count))));
                        Commn.showDebugLog("snapshot eXISTS: all_following:" + count + "");
                    } else {
                        Commn.showDebugLog("following snapshot NOT eXISTS:");
                        binding.tvFollowingCount.setText("0");
                    }
                });

        String another_user = DBConstants.UserInfo + "/" + profilePOJO.getUserId() + "/" + DBConstants.Friends;

        firebaseFirestore.collection(another_user)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {

                        int count = value.size();
                        binding.tvFriendsCount.setText(Commn.prettyCount(Long.parseLong(String.valueOf(count))));
                        Commn.showDebugLog("snapshot eXISTS: all_friends:" + count + "");
                    } else {
                        Commn.showDebugLog("all_friends snapshot NOT eXISTS:");
                        binding.tvFriendsCount.setText("0");
                    }
                });


    }

    private void getInfo() {

        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .addSnapshotListener((value, error) -> {

                    if (value.exists()) {
                        Log.e("cjdcnjncjnn",profilePOJO+"");
                        ProfilePOJO profilePOJO = value.toObject(ProfilePOJO.class);
                        binding.tvName.setText(profilePOJO.getName() + "");
                        binding.tvId.setText("ID: " + profilePOJO.getSuper_live_id() + "");
                        if (MyProfile.this.getActivity() != null) {
                            Glide.with(MyProfile.this.getActivity()).load(profilePOJO.getImage()).placeholder(R.drawable.placeholder_user).into(binding.ivProfilePic);

                        }
                        binding.tvAge.setText(String.valueOf(Commn.getAge(profilePOJO.getDob())) + "");
                        if (profilePOJO.getGender() != null) {
                            if (DBConstants.male.equalsIgnoreCase(profilePOJO.getGender())) {
                                Glide.with(getApplicationContext()).load(R.drawable.male).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivGender);
                            }
                            if (DBConstants.female.equalsIgnoreCase(profilePOJO.getGender())) {
                                Glide.with(getApplicationContext()).load(R.drawable.female).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivGender);
                            }
                        }
                        if (profilePOJO.getCurrent_level() != null) {
                            binding.tvTotalBeans.setText(Commn.prettyCount(Long.parseLong(String.valueOf(profilePOJO.getTotal_recieved_beans()))));
                        }

                        binding.tvSentBeans.setText(Commn.prettyCount(Long.parseLong(String.valueOf(profilePOJO.getTotal_sent_diamonds()))));

                        if (profilePOJO.getCurrent_level() != null) {
                            binding.tvCurrentLevel.setText("Level : " + profilePOJO.getCurrent_level() + "");
                        }
                    }

                });
        firebaseFirestore.collection(DBConstants.UserInfo
                + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Coins_Info)
                .document(profilePOJO.getUserId())
                .addSnapshotListener((value, error) -> {
                    assert value != null;
                    if (value.exists()) {

                        if (value.getLong(DBConstants.current_coins) != null) {
                            int current_coins = value.getLong(DBConstants.current_coins).intValue();
                            Commn.showDebugLog("current_coins:" + current_coins + "");
                            binding.tvCurrentCoins.setText(String.valueOf(current_coins));
                        } else {
                            createDiamonds();
                        }
                        if (value.getLong(DBConstants.current_recieved_beans) == null) {
                            creatUpdateBeans();
                        }

                    } else {
                        createCoinsInfo();
                        binding.tvCurrentCoins.setText("0");
                    }
                });

    }

    private void createDiamonds() {
        Map<String, Object> current_coins_map = new HashMap<>();
        current_coins_map.put(DBConstants.current_coins, 0);
        firebaseFirestore.collection(DBConstants.UserInfo
                + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Coins_Info)
                .document(profilePOJO.getUserId())
                .update(current_coins_map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Commn.showDebugLog("createDiamonds field added");
                    }
                });
    }

    private void creatUpdateBeans() {
        Map<String, Object> current_coins_map = new HashMap<>();
        current_coins_map.put(DBConstants.current_recieved_beans, 0);
        firebaseFirestore.collection(DBConstants.UserInfo
                + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Coins_Info)
                .document(profilePOJO.getUserId())
                .update(current_coins_map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Commn.showDebugLog("current_recieved_beans field added");
                    }
                });
    }

    private void createCoinsInfo() {
        //creating record
        Map<String, Object> current_coins_map = new HashMap<>();
        current_coins_map.put(DBConstants.current_recieved_beans, 0);
        current_coins_map.put(DBConstants.current_coins, 0);
        firebaseFirestore.collection(DBConstants.UserInfo + "/" + profilePOJO.getUserId()
                + "/" + DBConstants.User_Coins_Info)
                .document(profilePOJO.getUserId())
                .set(current_coins_map)
                .addOnCompleteListener(task2 -> {
                    Commn.showDebugLog("added_beans_successfully:ye table exist nahi thi,abhi create ki h humne");

                });
    }

}
