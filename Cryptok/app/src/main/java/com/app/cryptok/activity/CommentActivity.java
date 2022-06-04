package com.app.cryptok.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.Interface.OnCommentSelection;
import com.app.cryptok.Notifications.InializeNotification;
import com.app.cryptok.Notifications.NotificationRequest;
import com.app.cryptok.Notifications.Notification_Const;
import com.app.cryptok.Notifications.Sender;
import com.app.cryptok.R;
import com.app.cryptok.adapter.Comment.CommentImageHolder;
import com.app.cryptok.adapter.Comment.CommentTextHolder;
import com.app.cryptok.adapter.Comment.GifAdapter;
import com.app.cryptok.databinding.ActivityCommentBinding;
import com.app.cryptok.databinding.CommentImagesLayoutBinding;
import com.app.cryptok.databinding.CommentTextLayoutBinding;
import com.app.cryptok.model.Comment.CommentModel;
import com.app.cryptok.model.Comment.GifModel;
import com.app.cryptok.model.Comment.ReplyCommentModel;
import com.app.cryptok.model.PostsModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {
    private ActivityCommentBinding binding;
    private String POST_ID, USER_ID, post_image = "";
    private LinearLayoutManager layoutManager;
    private FirebaseFirestore firebaseFirestore;
    private String mComment;
    private boolean isReplyType = false;
    //gif
    private List<GifModel> gifModelList = new ArrayList<>();
    private GifAdapter gifAdapter;
    private OnCommentSelection onCommentSelection;
    //
    ProfilePOJO profilePOJO;
    private SessionManager sessionManager;
    public static int COMMENT_TYPE;
    //getComments
    private DatabaseReference comment_Ref;
    private FirebaseDatabase database;
    FirebaseRecyclerOptions<CommentModel> firestoreRecyclerOptions;
    private FirebaseRecyclerAdapter<CommentModel, RecyclerView.ViewHolder> comment_adapter;
    CollectionReference user_info;
    private CommentActivity activity;
    private Context context;
    private PostsModel postsModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment);
        context = activity = this;
        iniData();
        iniViews();
        getData();
    }

    private void getData() {

        handleClick();
        getComments();
    }

    private void iniViews() {

        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvComments.setHasFixedSize(true);
        binding.rvComments.setLayoutManager(layoutManager);

        binding.rvGifList.setHasFixedSize(true);
        binding.rvGifList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }

    private void iniData() {
        database = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        comment_Ref = database.getReference();

        if (getIntent().hasExtra(Commn.MODEL)) {
            postsModel = new Gson().fromJson(getIntent().getStringExtra(Commn.MODEL), PostsModel.class);
            POST_ID = postsModel.getPost_id();
            USER_ID = postsModel.getUser_id();
            post_image = postsModel.getPost_image();
        }
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }


    private void getComments() {

        iniFirebaseOptions();
        user_info = FirebaseFirestore.getInstance().collection(DBConstants.UserInfo);
        comment_adapter = new FirebaseRecyclerAdapter<CommentModel, RecyclerView.ViewHolder>(firestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i, @NonNull CommentModel model) {
                switch (model.getComment_type()) {
                    case 0:

                        CommentTextLayoutBinding commentTextLayoutBind = ((CommentTextHolder) holder).getCommentTextLayoutBind();
                        commentTextLayoutBind.tvComment.setText(model.getComment());
                        commentTextLayoutBind.tvCommentTime.setText(Commn.getTimeAgo(model.getTimestamp()));
                        commentTextLayoutBind.tvComment.setText(model.getComment());
                        Commn.showDebugLog("imageee:" + model.getComment() + "");

                        commentTextLayoutBind.tvCommentReply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(context, ReplyComments.class);
                                intent.putExtra(Commn.MODEL, new Gson().toJson(model));

                                startActivity(intent);
                            }
                        });

                        commentTextLayoutBind.tvViewOneReplies.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(context, ReplyComments.class);
                                intent.putExtra(Commn.MODEL, new Gson().toJson(model));
                                startActivity(intent);
                            }
                        });
                        getTextTypeInfo(commentTextLayoutBind, model.getUser_id());

                        getTextReplies(model, commentTextLayoutBind);
                        commentTextLayoutBind.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                PopupMenu popupMenu = new PopupMenu(context, commentTextLayoutBind.getRoot());
                                if (profilePOJO.getUserId().equalsIgnoreCase(postsModel.getUser_id())||
                                        profilePOJO.getUserId().equalsIgnoreCase(model.getUser_id())) {
                                    popupMenu.getMenu().add("Delete Comment");

                                }
                                popupMenu.getMenu().add("Reply");
                                popupMenu.show();
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getTitle().toString()) {
                                            case "Delete Comment":
                                                deleteComment(model);
                                                break;
                                            case "Reply":

                                                Intent intent = new Intent(context, ReplyComments.class);
                                                intent.putExtra(Commn.MODEL, new Gson().toJson(model));
                                                startActivity(intent);
                                                break;
                                        }
                                        return true;
                                    }
                                });

                                return true;
                            }
                        });

                        break;
                    case 1:
                        CommentImagesLayoutBinding commentImagesLayoutBind = ((CommentImageHolder) holder).getCommentImagesLayoutBind();
                        getImageInfo(commentImagesLayoutBind, model.getUser_id());

                        commentImagesLayoutBind.tvUserName.setText(model.getUser_name());
                        commentImagesLayoutBind.tvCommentTime.setText(Commn.getTimeAgo(model.getTimestamp()));

                        Glide.with(getApplicationContext()).load(model.getUser_image()).placeholder(R.drawable.placeholder_user)
                                .diskCacheStrategy(DiskCacheStrategy.ALL).into((commentImagesLayoutBind.ivCommentUser));

                        Glide.with(getApplicationContext()).asGif().load(model.getComment()).placeholder(R.drawable.placeholder_user)
                                .diskCacheStrategy(DiskCacheStrategy.ALL).into((commentImagesLayoutBind.ivCommentImage));
                        Commn.showDebugLog("imageee:" + model.getComment() + "");

                        commentImagesLayoutBind.tvCommentReply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, ReplyComments.class);
                                intent.putExtra(Commn.MODEL, new Gson().toJson(model));
                                startActivity(intent);
                            }
                        });
                        commentImagesLayoutBind.tvViewOneReplies.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, ReplyComments.class);
                                intent.putExtra(Commn.MODEL, new Gson().toJson(model));
                                startActivity(intent);
                            }
                        });

                        getImageTypeReplies(model, commentImagesLayoutBind);
                        commentImagesLayoutBind.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                PopupMenu popupMenu = new PopupMenu(context, commentImagesLayoutBind.getRoot());
                                if (profilePOJO.getUserId().equalsIgnoreCase(postsModel.getUser_id())||
                                profilePOJO.getUserId().equalsIgnoreCase(model.getUser_id())) {
                                    popupMenu.getMenu().add("Delete Comment");

                                }
                                popupMenu.getMenu().add("Reply");
                                popupMenu.show();
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getTitle().toString()) {
                                            case "Delete Comment":
                                                deleteComment(model);
                                                break;
                                            case "Reply":

                                                Intent intent = new Intent(context, ReplyComments.class);
                                                intent.putExtra(Commn.MODEL, new Gson().toJson(model));
                                                startActivity(intent);
                                                break;
                                        }
                                        return true;
                                    }
                                });

                                return true;
                            }
                        });

                        break;
                    default:
                        break;
                }

            }

            @Override
            public int getItemViewType(int position) {
                CommentModel messageModel = getItem(position);

                switch (messageModel.getComment_type()) {
                    case 0:
                        return DBConstants.TEXT_COMMENT;
                    case 1:
                        return DBConstants.GIF_COMMENT;
                    default:
                        return -1;
                }


            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                switch (viewType) {
                    case 0:
                        View TextChatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_text_layout, parent, false);
                        return new CommentTextHolder(TextChatView);
                    case 1:
                        View ImageChatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_images_layout, parent, false);
                        return new CommentImageHolder(ImageChatView);
                }
                return null;
            }

        };
        comment_adapter.startListening();
        binding.rvComments.setAdapter(comment_adapter);
        binding.rvComments.setNestedScrollingEnabled(false);
        comment_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int friendlyMessageCount = comment_adapter.getItemCount();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();

                Log.e("countttmess", friendlyMessageCount + "");
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    binding.rvComments.scrollToPosition(positionStart);
                }
            }
        });

    }

    private void deleteComment(CommentModel model) {
        DatabaseReference comment_Ref = database.getReference().child(DBConstants.Post_Comments).child(POST_ID)
                .child(model.getComment_id());

        comment_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getImageInfo(CommentImagesLayoutBinding CommentImagesLayoutBinding, String user_id) {
        user_info.document(user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            String image = task.getResult().getString(DBConstants.image);
                            String name = task.getResult().getString(DBConstants.name);
                            if (image != null) {
                                Glide.with(context).load(image).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.fake_user_icon)
                                        .into(CommentImagesLayoutBinding.ivCommentUser);
                            }

                            if (name != null) {
                                CommentImagesLayoutBinding.tvUserName.setText(name + "");
                            }
                        }
                    }
                });

    }

    private void getTextTypeInfo(CommentTextLayoutBinding holder, String user_id) {

        user_info.document(user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            String image = task.getResult().getString(DBConstants.image);
                            String name = task.getResult().getString(DBConstants.name);
                            if (image != null) {
                                Glide.with(context).load(image)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.fake_user_icon)
                                        .into(holder.ivCommentUser);
                            }

                            if (name != null) {
                                holder.tvUserName.setText(name + "");
                            }
                        }
                    }
                });

    }

    private void getTextReplies(CommentModel model, CommentTextLayoutBinding holder) {
        DatabaseReference messageRef = database.getReference().child(DBConstants.Post_Comments_Reply);

        Query lastMessageQuery = messageRef.child(model.getComment_id()).limitToLast(1);

        lastMessageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {
                    holder.llReplyLayout.setVisibility(View.VISIBLE);
                    holder.tvViewOneReplies.setVisibility(View.VISIBLE);
                    ReplyCommentModel model1 = dataSnapshot.getValue(ReplyCommentModel.class);
                    getReplyTextTypeInfo(holder, model1.getUser_id());
                    holder.tvReplyComment.setText(model1.getComment());
                    holder.tvCommentTime.setText(Commn.getTimeAgo(model1.getTimestamp()));


                } else {
                    holder.llReplyLayout.setVisibility(View.GONE);
                    holder.tvViewOneReplies.setVisibility(View.GONE);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getReplyTextTypeInfo(CommentTextLayoutBinding holder, String user_id) {
        user_info.document(user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            String image = task.getResult().getString(DBConstants.image);
                            String name = task.getResult().getString(DBConstants.name);
                            if (image != null) {

                                Glide.with(getApplicationContext())
                                        .load(image)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .thumbnail(0.09f)
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .into(holder.ivReplycommentUser);

                            }


                            if (name != null) {
                                holder.tvReplyuserName.setText(name + "");
                            }
                        }
                    }
                });
    }

    private void getImageTypeReplies(CommentModel model, CommentImagesLayoutBinding holder) {
        DatabaseReference messageRef = database.getReference().child(DBConstants.Post_Comments_Reply);

        Query lastMessageQuery = messageRef.child(model.getComment_id()).limitToLast(1);

        lastMessageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {
                    holder.llReplyLayout.setVisibility(View.VISIBLE);
                    holder.tvViewOneReplies.setVisibility(View.VISIBLE);
                    ReplyCommentModel model1 = dataSnapshot.getValue(ReplyCommentModel.class);

                    getReplyImageInfo(holder, model1.getUser_id());
                 /*   holder.tv_reply_user_name.setText(model1.getUser_name());

                    Glide.with(getApplicationContext()).load(model1.getUser_image()).placeholder(R.drawable.placeholder_user)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_replycomment_user);*/
                    Glide.with(getApplicationContext()).load(model1.getComment()).placeholder(R.drawable.placeholder_user)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivReplyCommentImage);
                } else {
                    holder.llReplyLayout.setVisibility(View.GONE);
                    holder.tvViewOneReplies.setVisibility(View.GONE);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getReplyImageInfo(CommentImagesLayoutBinding holder, String user_id) {
        user_info.document(user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            String image = task.getResult().getString(DBConstants.image);
                            String name = task.getResult().getString(DBConstants.name);
                            if (image != null) {
                                Glide.with(getApplicationContext()).load(image).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.fake_user_icon)
                                        .into(holder.ivReplycommentUser);
                            }

                            if (name != null) {
                                holder.tvReplyUserName.setText(name + "");
                            }
                        }
                    }
                });

    }


    private void iniFirebaseOptions() {
        DatabaseReference comment_Ref = database.getReference().child(DBConstants.Post_Comments).child(POST_ID);
        Commn.showDebugLog("post_id:" + POST_ID);
        firestoreRecyclerOptions = new FirebaseRecyclerOptions.Builder<CommentModel>().setQuery(comment_Ref, CommentModel.class)
                .build();
    }

    private void handleClick() {
        binding.etAddComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

              /*  if (s.toString().length() == 0) {
                    iv_add_comment.setColorFilter(ContextCompat.getColor(activity), R.color.dark_grey), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    iv_add_comment.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);

                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.ivAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mComment = binding.etAddComment.getText().toString();
                if (TextUtils.isEmpty(mComment)) {
                    Commn.myToast(context, "should not be empty..");
                } else {
                    COMMENT_TYPE = DBConstants.TEXT_COMMENT;

                    addComment();
                }
            }
        });
        binding.tvGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setGifVisible();
                final String searchTerm = "funny";
                String query_url = MyApi.GIF_Url + searchTerm + "&key=" + MyApi.GIF_API_KEY + "&limit=" + 15;
                gifApi(query_url, searchTerm);

                // load the results for the user
                Log.v("my_gifff", "Search Results: " + query_url + "");
            }
        });
        binding.etGifSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() > 0) {
                    final String searchTerm = s.toString();
                    String query_url = MyApi.GIF_Url + searchTerm + "&key=" + MyApi.GIF_API_KEY + "&limit=" + 15;

                    gifApi(query_url, searchTerm);
                }
                if (s.toString().length() == 0) {
                    final String searchTerm = "funny";
                    String query_url = MyApi.GIF_Url + searchTerm + "&key=" + MyApi.GIF_API_KEY + "&limit=" + 15;

                    gifApi(query_url, searchTerm);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.ivBackGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonGifVisible();
            }
        });
        this.onCommentSelection = new OnCommentSelection() {
            @Override
            public void OnGifSelection(String gif_url) {
                COMMENT_TYPE = DBConstants.GIF_COMMENT;
                nonGifVisible();

                mComment = gif_url;
                addComment();
                Commn.showDebugLog("you selected gif");
            }

            @Override
            public void OnReplySend(boolean shouldReply, String username, String comment_id) {

            }


        };
    }

    private void addComment() {
        String cmnt_ref = DBConstants.Post_Comments + "/" + POST_ID;

        DatabaseReference user_message_push = comment_Ref.child(DBConstants.Post_Comments).
                child(POST_ID).push();

        String comment_id = user_message_push.getKey();

        Map firstMap = new HashMap();
        firstMap.put(DBConstants.user_image, profilePOJO.getImage());
        firstMap.put(DBConstants.user_name, profilePOJO.getName());
        firstMap.put(DBConstants.comment, mComment);
        firstMap.put(DBConstants.comment_type, COMMENT_TYPE);
        firstMap.put(DBConstants.timestamp, ServerValue.TIMESTAMP);
        firstMap.put(DBConstants.post_id, POST_ID);
        firstMap.put(DBConstants.user_id, profilePOJO.getUserId());
        firstMap.put(DBConstants.comment_id, comment_id);

        Map finalMap = new HashMap();
        finalMap.put(cmnt_ref + "/" + comment_id, firstMap);

        binding.etAddComment.setText("");
        comment_Ref.updateChildren(finalMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                if (!profilePOJO.getUserId().equalsIgnoreCase(USER_ID)) {
                    pushNotification();
                }

                if (error != null) {

                    Commn.showDebugLog("error occured in add comment:" + error.getMessage() + "");
                }
            }
        });
    }

    private void pushNotification() {
        NotificationRequest notificationRequest = new NotificationRequest();
        if (null != profilePOJO.getName()) {
            if (!profilePOJO.getName().isEmpty()) {
                notificationRequest.setContext_message("<strong>" + profilePOJO.getName() + "</strong>" + " commented on your post");
            } else {
                notificationRequest.setContext_message("<strong>" + profilePOJO.getSuper_live_id() + "</strong>" + " commented on your post ");
            }
        } else {
            notificationRequest.setContext_message("<strong>" + profilePOJO.getSuper_live_id() + "</strong>" + " commented on your post ");
        }
        notificationRequest.setNotification_type(Notification_Const.IMAGE_NOTIFICATION_TYPE);
        notificationRequest.setSuper_live_id(profilePOJO.getSuper_live_id() + "");
        notificationRequest.setUser_id(profilePOJO.getUserId() + "");
        notificationRequest.setMessage(post_image + "");
        notificationRequest.setAlert_type(Commn.POST_COMMENT_TYPE);
        notificationRequest.setNotification_data(POST_ID);

        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(USER_ID)
                .collection(DBConstants.Tokens)
                .document(USER_ID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            if (task.getResult().exists()) {
                                if (task.getResult().getString(DBConstants.user_token) != null) {
                                    String token = task.getResult().getString(DBConstants.user_token);
                                    Commn.showDebugLog("got that user token:" + token);
                                    Sender sender = new Sender(notificationRequest, token);
                                    Commn.showDebugLog("notification_send_model:" + new Gson().toJson(sender.getData()));
                                    new InializeNotification().sendNotification(sender);
                                }

                            }

                        }
                    }
                });
    }

    private void gifApi(String query_url, String search_key) {
        gifModelList.clear();
        binding.gifProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, query_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                binding.gifProgress.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("media");
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            JSONObject jsonObject11 = jsonArray1.getJSONObject(j);
                            JSONObject tingif = jsonObject11.getJSONObject("tinygif");
                            String url = tingif.getString("url");
                            Log.e("my_final_gif", url);
                            gifModelList.add(new GifModel(url));
                        }
                    }
                    gifAdapter = new GifAdapter(gifModelList, context, onCommentSelection);
                    binding.rvGifList.setAdapter(gifAdapter);
                    gifAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Commn.requestQueue(context, stringRequest);
    }

    private void setGifVisible() {

        binding.LLBottom.setVisibility(View.GONE);
        binding.llGifEdittext.setVisibility(View.VISIBLE);
        binding.etGifSearch.requestFocus();
    }

    private void nonGifVisible() {
        binding.LLBottom.setVisibility(View.VISIBLE);
        binding.llGifEdittext.setVisibility(View.GONE);
        //  et_add_comment.requestFocus();
    }


}