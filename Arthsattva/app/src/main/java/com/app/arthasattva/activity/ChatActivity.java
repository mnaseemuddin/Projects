package com.app.arthasattva.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.app.arthasattva.Api.DBConstants;
import com.app.arthasattva.Notifications.InializeNotification;
import com.app.arthasattva.Notifications.NotificationRequest;
import com.app.arthasattva.Notifications.Notification_Const;
import com.app.arthasattva.Notifications.Sender;
import com.app.arthasattva.R;
import com.app.arthasattva.adapter.Chat.ImageChatHolder;
import com.app.arthasattva.adapter.Chat.TextChatHolder;
import com.app.arthasattva.databinding.ActivityChatBinding;
import com.app.arthasattva.databinding.ImageChatLayoutBinding;
import com.app.arthasattva.databinding.TextChatLayoutBinding;
import com.app.arthasattva.model.Chat.ChatModel;
import com.app.arthasattva.model.ProfilePOJO;
import com.app.arthasattva.utils.Commn;
import com.app.arthasattva.utils.ConstantsKey;
import com.app.arthasattva.utils.Permissions;
import com.app.arthasattva.utils.SessionManager;
import com.mikelau.croperino.Croperino;
import com.mikelau.croperino.CroperinoConfig;
import com.mikelau.croperino.CroperinoFileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private String user_id, user_name, user_image;
    private String mMessage;
    private ChatActivity activity;
    private Context context;
    private int MESSAGE_TYPE;
    private String file_name;
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private String message_id;
    //database
    private DatabaseReference messagesRef;
    private FirebaseDatabase database;
    //load chat
    private FirebaseRecyclerOptions<ChatModel> chatOptions;
    private FirebaseRecyclerAdapter<ChatModel, RecyclerView.ViewHolder> chat_adapter;
    //
    private boolean isNotify = false;
    private FirebaseFirestore firebaseFirestore;
    private String notification_type = "";
    private File filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        context = activity = this;
        iniFirebase();
        getSessionData();
        getIntentData();
        addTextChander();
        handleClick();
        loadChat();
    }

    private void getSessionData() {

        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);

    }

    private void iniFirebase() {
        database = FirebaseDatabase.getInstance();
        messagesRef = database.getReference();

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void handleClick() {

        binding.ivBack.setOnClickListener(view -> onBackPressed());
        binding.ivSendMsg.setOnClickListener(view -> {
            mMessage = binding.etSaySomething.getText().toString();

            if (TextUtils.isEmpty(mMessage.trim())) {
                Commn.myToast(context, "can't send empty messageeee");

            } else {

                MESSAGE_TYPE = DBConstants.TEXT_TYPE;
                file_name = "text";
                notification_type = Notification_Const.NORMAL_NOTIFICATION_TYPE;
                // readyToChat();
                startChat();
            }
        });

        binding.ivSendAttach.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= 23) {
                checkStoragePermission();
            } else {
                ChooseImageDialog();
            }
        });
    }

    private void ChooseImageDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
        builderSingle.setTitle("Choose Media");
        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(activity,
                        android.R.layout.simple_list_item_1);
        arrayAdapter.add("Camera");
        arrayAdapter.add("Gallery");

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            dialog.dismiss();
            if (strName.equals("Camera")) {
                openCamera();
            } else {
                openGalleryImages();
            }
        });
        builderSingle.show();
    }

    private void openGalleryImages() {


        Croperino.prepareGallery(activity);
    }

    private void openCamera() {

        Croperino.prepareCamera(activity);

    }

    private void checkStoragePermission() {
        TedPermission.with(context)
                .setPermissionListener(storageSharelistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Permissions.storage_permissions)
                .check();


    }

    PermissionListener storageSharelistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            ChooseImageDialog();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {

            checkStoragePermission();
        }
    };

    private void compressImage() {

        ImageCompression imageCompression = new ImageCompression(context);
        imageCompression.execute(filePath.getAbsolutePath());


    }

    @SuppressLint("StaticFieldLeak")
    public class ImageCompression extends AsyncTask<String, Void, String> {

        private Context context;

        public ImageCompression(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0 || strings[0] == null)
                return null;

            return Commn.compressImage(strings[0], context);
        }

        protected void onPostExecute(String imagePath) {
            // imagePath is path of new compressed image.
            Commn.showDebugLog("my compressed path:" + imagePath + " ");
            if (filePath != null) {
                sendImage(imagePath);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CroperinoConfig.REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    filePath = CroperinoFileUtil.getTempFile();
                    Commn.showDebugLog("camera_picked_path:" + filePath.getAbsolutePath() + "");
                    compressImage();
                }
                break;
            case CroperinoConfig.REQUEST_PICK_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    CroperinoFileUtil.newGalleryFile(data, activity);
                    filePath = CroperinoFileUtil.getTempFile();
                    Commn.showDebugLog("gallery_picked_path:" + filePath.getAbsolutePath() + "");
                    compressImage();
                }
                break;

            default:
                break;
        }

    }

    private void sendImage(String path) {
        if (!activity.isFinishing()) {
            Commn.showDialog(activity);
        }
        if (profilePOJO.getArthsId() != null) {
            file_name = profilePOJO.getArthsId() + "-" + System.currentTimeMillis() + ".png";
        } else {
            file_name = System.currentTimeMillis() + ".png";
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        final StorageReference ImagesRef = storageRef.child(DBConstants.ConversationData).child(profilePOJO.getUserId())
                .child(file_name);

        final UploadTask uploadTask = ImagesRef.putFile(Uri.fromFile(new File(path)));
        uploadTask.addOnFailureListener(exception -> Commn.showDebugLog("whatTheFuck:" + exception.toString())).addOnProgressListener(taskSnapshot -> {
        }).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    Log.i("problem", task.getException().toString());
                }
                return ImagesRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    if (!activity.isFinishing()) {
                        Commn.hideDialog(activity);
                    }
                    Uri downloadUri = task.getResult();

                    Commn.showDebugLog("seeThisUrl:" + downloadUri.toString());// This is the one you should store

                    mMessage = downloadUri.toString();
                    MESSAGE_TYPE = DBConstants.IMAGE_TYPE;
                    notification_type = Notification_Const.IMAGE_NOTIFICATION_TYPE;
                    startChat();


                } else {
                    Log.i("wentWrong", "downloadUri failure");
                }
            });
        });

    }


    private void startChat() {

        isNotify = true;
        messagesRef = database.getReference();

        String currentUserRef = DBConstants.User_Messages + "/" + profilePOJO.getUserId() + "/" + user_id;
        String chat_user_Ref = DBConstants.User_Messages + "/" + user_id + "/" + profilePOJO.getUserId();

        DatabaseReference user_message_push = messagesRef.child(DBConstants.User_Messages).
                child(profilePOJO.getUserId()).child(user_id).push();
        message_id = user_message_push.getKey();

        Map messageMap = new HashMap();
        messageMap.put(DBConstants.messageid, message_id);
        messageMap.put(DBConstants.filename, file_name);
        messageMap.put(DBConstants.message, mMessage);
        messageMap.put(DBConstants.seen, "false");
        messageMap.put(DBConstants.messageType, MESSAGE_TYPE);
        messageMap.put(DBConstants.timestamp, ServerValue.TIMESTAMP);
        messageMap.put(DBConstants.user_id, profilePOJO.getUserId());

        Map messageUserMap = new HashMap();
        messageUserMap.put(currentUserRef + "/" + message_id, messageMap);
        messageUserMap.put(chat_user_Ref + "/" + message_id, messageMap);


        binding.etSaySomething.setText("");
        messagesRef.updateChildren(messageUserMap, (databaseError, databaseReference) -> {

            updateCurrentChat();
            sendNotification();

        });


    }

    private void updateCurrentChat() {
        Map<String, Object> currentUser = new HashMap<>();
        currentUser.put(DBConstants.user_id, profilePOJO.getUserId());
        currentUser.put(DBConstants.timestamp, FieldValue.serverTimestamp());
        currentUser.put(DBConstants.message, mMessage);
        currentUser.put(DBConstants.messageid, message_id);

        Map<String, Object> anotherUser = new HashMap<>();
        anotherUser.put(DBConstants.user_id, user_id);
        anotherUser.put(DBConstants.timestamp, FieldValue.serverTimestamp());
        anotherUser.put(DBConstants.message, mMessage);
        anotherUser.put(DBConstants.messageid, message_id);

        firebaseFirestore.collection(DBConstants.current_chat + "/" + profilePOJO.getUserId() + "/" + DBConstants.all_chat)
                .document(user_id)
                .set(anotherUser);

        firebaseFirestore.collection(DBConstants.current_chat + "/" + user_id + "/" + DBConstants.all_chat)
                .document(profilePOJO.getUserId())
                .set(currentUser);

    }

    private void sendNotification() {

        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(user_id)
                .collection(DBConstants.Tokens)
                .document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            if (task.getResult().exists()) {
                                if (task.getResult().getString(DBConstants.user_token) != null) {
                                    String token = task.getResult().getString(DBConstants.user_token);
                                    Commn.showDebugLog("got that user token:" + token);
                                    sendPushNotification(token);
                                }

                            }

                        }
                    }
                });
    }

    private void sendPushNotification(String token) {
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setMessage(mMessage + "");
        notificationRequest.setNotification_type(notification_type + "");
        notificationRequest.setMantra_id(profilePOJO.getArthsId() + "");
        notificationRequest.setUser_id(profilePOJO.getUserId() + "");
        notificationRequest.setContext_message("");
        notificationRequest.setAlert_type(Commn.CHAT_TYPE);
        notificationRequest.setNotification_data("");
        Sender sender = new Sender(notificationRequest, token);
        Commn.showDebugLog("notification_send_model:" + new Gson().toJson(sender.getData()));
        new InializeNotification().sendNotification(sender);
    }


    private void addTextChander() {

        binding.etSaySomething.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length() > 0) {
                    binding.ivSendAttach.setVisibility(View.GONE);
                } else {
                    binding.ivSendAttach.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void loadChat() {

        iniChatFirebaseOptions();

        chat_adapter = new FirebaseRecyclerAdapter<ChatModel, RecyclerView.ViewHolder>(chatOptions) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i, @NonNull ChatModel chatModel) {
                Commn.showDebugLog("text:" + chatModel.getMessage() + "");
                switch (chatModel.getMessageType()) {

                    case 0:

                        TextChatLayoutBinding text_chat_binding = ((TextChatHolder) holder).getTextChatBinding();
                        if (profilePOJO.getUserId().equalsIgnoreCase(chatModel.getUser_id())) {

                            setTextCurrentUser(text_chat_binding);

                        } else {

                            setTextRecieverContent(text_chat_binding);

                        }
                        setText(chatModel.getMessage(), text_chat_binding);
                        text_chat_binding.tvMessageTime.setText(Commn.getTimeAgo(chatModel.getTimestamp()));

                        break;
                    case 1:


                        ImageChatLayoutBinding imageChatLayoutBinding = ((ImageChatHolder) holder).getImage_binding();
                        if (profilePOJO.getUserId().equalsIgnoreCase(chatModel.getUser_id())) {

                            setImageCurrentUser(imageChatLayoutBinding);

                        } else {

                            setImageRecieverContent(imageChatLayoutBinding);

                        }
                        Commn.showDebugLog("images:" + chatModel.getMessage() + "");
                        setImage(chatModel.getMessage(), imageChatLayoutBinding);
                        imageChatLayoutBinding.tvMessageTime.setText(Commn.getTimeAgo(chatModel.getTimestamp()));

                        break;

                    default:
                        break;
                }
            }

            @Override
            public int getItemViewType(int position) {

                ChatModel model = getItem(position);
                switch (model.getMessageType()) {
                    case 0:
                        return DBConstants.TEXT_TYPE;

                    case 1:
                        return DBConstants.IMAGE_TYPE;
                    default:
                        return -1;
                }


            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                switch (viewType) {
                    case 0:
                        View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_chat_layout, parent, false);
                        return new TextChatHolder(textView);


                    case 1:
                        View imageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_chat_layout, parent, false);
                        return new ImageChatHolder(imageView);
                }
                return null;
            }
        };

        chat_adapter.startListening();
        binding.rvChat.setAdapter(chat_adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);

        binding.rvChat.setLayoutManager(layoutManager);

        binding.rvChat.setHasFixedSize(true);
        binding.rvChat.setAdapter(chat_adapter);
        binding.rvChat.setNestedScrollingEnabled(false);
        chat_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = chat_adapter.getItemCount();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                Commn.showDebugLog("countttmess:" + friendlyMessageCount + "");
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    binding.rvChat.scrollToPosition(positionStart);
                }
            }
        });

    }

    private void setImageRecieverContent(ImageChatLayoutBinding imageChatLayoutBinding) {

        imageChatLayoutBinding.llImageLayout.setGravity(Gravity.START);
        imageChatLayoutBinding.tvMessageTime.setGravity(Gravity.START);
    }

    private void setImageCurrentUser(ImageChatLayoutBinding imageChatLayoutBinding) {

        imageChatLayoutBinding.llImageLayout.setGravity(Gravity.END);

        imageChatLayoutBinding.tvMessageTime.setGravity(Gravity.END);
    }

    private void setTextRecieverContent(TextChatLayoutBinding textChatLayoutBinding) {
        textChatLayoutBinding.tvMessage.setBackground(getResources().getDrawable(R.drawable.chat_recieve_bg));
        textChatLayoutBinding.tvMessage.setTextColor(getResources().getColor(R.color.black));
        textChatLayoutBinding.tvMessageTime.setTextColor(getResources().getColor(R.color.dark_grey));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 10, 100, 10);
        textChatLayoutBinding.llTextLayout.setLayoutParams(params);

    }

    private void setTextCurrentUser(TextChatLayoutBinding textChatLayoutBinding) {
        textChatLayoutBinding.tvMessage.setBackground(getResources().getDrawable(R.drawable.chat_sent_bg));
        textChatLayoutBinding.tvMessage.setTextColor(getResources().getColor(R.color.white));
        textChatLayoutBinding.tvMessageTime.setTextColor(getResources().getColor(R.color.dark_grey));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(100, 10, 15, 10);
        textChatLayoutBinding.llTextLayout.setLayoutParams(params);
    }


    private void setImage(String message, ImageChatLayoutBinding imageChatLayoutBinding) {

        Glide.with(getApplicationContext())
                .load(message).placeholder(R.drawable.app_logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageChatLayoutBinding.ivImage);


    }

    private void setText(String message, TextChatLayoutBinding textChatLayoutBinding) {
        textChatLayoutBinding.tvMessage.setText(message);

    }

    private void iniChatFirebaseOptions() {
        Commn.showDebugLog("user_id" + user_id + "");
        messagesRef = database.getReference().child(DBConstants.User_Messages).child(profilePOJO.getUserId()).child(user_id);
        chatOptions = new FirebaseRecyclerOptions.Builder<ChatModel>()
                .setQuery(messagesRef, ChatModel.class).build();
    }

    private void getIntentData() {
        if (getIntent().hasExtra(DBConstants.user_id)) {
            user_id = getIntent().getStringExtra(DBConstants.user_id);
            getUserInfo();
        }
    }
    private void getUserInfo() {
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(user_id)
                .get()
                .addOnCompleteListener(task -> {


                    if (task.getResult().exists()) {
                        if (task.getResult().getString(DBConstants.image) != null) {
                            user_image = task.getResult().getString(DBConstants.image);
                            user_name = task.getResult().getString(DBConstants.name);
                            binding.tvUsername.setText(user_name);
                            Glide.with(context).load(user_image).
                                    placeholder(R.drawable.placeholder_user)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivUserImage);
                            Commn.showDebugLog("user_image:"+user_image);


                        }


                    }

                });
    }

}