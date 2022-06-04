package com.app.arthasattva.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.app.arthasattva.Api.DBConstants;
import com.app.arthasattva.R;
import com.app.arthasattva.adapter.MessageUsersAdapter;
import com.app.arthasattva.databinding.InflateMessagesBinding;
import com.app.arthasattva.databinding.MessagesLayoutActivityBinding;
import com.app.arthasattva.model.Chat.CurrentChat;
import com.app.arthasattva.model.ProfilePOJO;
import com.app.arthasattva.utils.Commn;
import com.app.arthasattva.utils.ConstantsKey;
import com.app.arthasattva.utils.SessionManager;

import static com.app.arthasattva.Api.DBConstants.user_id;
import static com.app.arthasattva.utils.Commn.CHAT_TYPE;

public class MessageUsersActivity extends AppCompatActivity {

    ProfilePOJO profilePOJO;
    private SessionManager sessionManager;
    Context context;
    Activity activity;
    private MessagesLayoutActivityBinding binding;
    private CollectionReference user_info;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerOptions<CurrentChat> options;
    private FirestoreRecyclerAdapter<CurrentChat, MessageUsersAdapter> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.messages_layout_activity);
        context = activity = this;

        setInfo();
        handleClick();
        iniFirebase();

        //call addObserve Function in main thread
        runOnUiThread(this::loadCurrentUsers);

        //new Thread(this::loadCurrentUsers).start();

    }

    private void setInfo() {
        binding.defaultConvers.tvNameBuzoService.setText(getString(R.string.app_name) + " " + "Services");
        binding.defaultConvers.tvMessageBuzoService.setText("This service provided by " + getString(R.string.app_name));
        binding.defaultConvers.tvName.setText(getString(R.string.app_name) + " Team");
        binding.defaultConvers.tvMessage.setText("This is the " + getString(R.string.app_name) + " Team");
    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(view -> {

            onBackPressed();

        });
    }

    private void iniFirebase() {
        sessionManager = new SessionManager(this);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        firebaseFirestore = FirebaseFirestore.getInstance();
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);
    }

    private void loadCurrentUsers() {

        iniFirebaseOptions();
        adapter = new FirestoreRecyclerAdapter<CurrentChat, MessageUsersAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MessageUsersAdapter holder, int position, @NonNull CurrentChat model) {
                try {

                    InflateMessagesBinding holder_binding = holder.getHolder_binding();

                    holder_binding.tvTimeBefore.setText(Commn.getTimeAgo(model.getTimestamp().getTime()));
                    holder_binding.tvMessage.setText(model.getMessage());
                    holder_binding.ivUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, AnotherUserProfile.class);
                            intent.putExtra(user_id, model.getUser_id());
                            startActivity(intent);
                        }
                    });
                    holder_binding.llInfo.setOnClickListener(view -> {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra(user_id, model.getUser_id());
                        intent.putExtra(Commn.TYPE, CHAT_TYPE);
                        context.startActivity(intent);
                    });
                    getUserInfo(holder_binding, model.getUser_id());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @NonNull
            @Override
            public MessageUsersAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_messages, parent, false);
                return new MessageUsersAdapter(textView);
            }
        };
        binding.rvCurrentChat.setAdapter(adapter);

    }

    private void getUserInfo(InflateMessagesBinding holder_binding, String user_id) {
        user_info.document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        String image = task.getResult().getString(DBConstants.image);
                        String name = task.getResult().getString(DBConstants.name);
                        if (image != null) {

                            Glide.with(context).load(image).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.fake_user_icon)
                                    .into(holder_binding.ivUser);

                        }

                        if (name != null) {
                            holder_binding.tvName.setText(name + "");
                        }
                    }
                });

    }

    private void iniFirebaseOptions() {

        Query query = firebaseFirestore.collection(DBConstants.current_chat
                + "/" + profilePOJO.getUserId() + "/" + DBConstants.all_chat)
                .orderBy(DBConstants.timestamp, Query.Direction.DESCENDING)
                .limit(30);

        options = new FirestoreRecyclerOptions.Builder<CurrentChat>()
                .setLifecycleOwner(this)
                .setQuery(query, CurrentChat.class)
                .build();
    }

}
