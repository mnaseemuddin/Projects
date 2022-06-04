package com.app.cryptok.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.Interface.OnCommentSelection;
import com.app.cryptok.R;
import com.app.cryptok.adapter.Comment.CommentImageHolder;
import com.app.cryptok.adapter.Comment.CommentTextHolder;
import com.app.cryptok.adapter.Comment.GifAdapter;
import com.app.cryptok.databinding.ActivityReplyCommentsBinding;
import com.app.cryptok.databinding.CommentImagesLayoutBinding;
import com.app.cryptok.databinding.CommentTextLayoutBinding;
import com.app.cryptok.model.Comment.CommentModel;
import com.app.cryptok.model.Comment.GifModel;
import com.app.cryptok.model.Comment.ReplyCommentModel;
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

public class ReplyComments extends AppCompatActivity {
    private ActivityReplyCommentsBinding binding;
    private String comment_id, USER_ID, POST_ID;
    private int comment_type = 0;
    private LinearLayoutManager layoutManager;
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
    FirebaseRecyclerOptions<ReplyCommentModel> firestoreRecyclerOptions;
    private FirebaseRecyclerAdapter<ReplyCommentModel, RecyclerView.ViewHolder> comment_adapter;

    private ReplyComments activity;
    private Context context;
    CollectionReference user_info;
    //get main comment
    private CommentModel commentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reply_comments);
        context = activity = this;
        iniData();
        iniViews();


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
        comment_Ref = database.getReference();

        if (getIntent().hasExtra(Commn.MODEL)) {
            commentModel = new Gson().fromJson(getIntent().getStringExtra(Commn.MODEL), CommentModel.class);
            comment_id = commentModel.getComment_id();
            USER_ID = commentModel.getUser_id();
            comment_type = commentModel.getComment_type();
            POST_ID = commentModel.getPost_id();
            String comment = commentModel.getComment();

            iniMainCommentViews();

            if (comment_type == DBConstants.TEXT_COMMENT) {

                textTypeView();
                binding.tvComment.setVisibility(View.VISIBLE);
                binding.ivCommentImage.setVisibility(View.GONE);
                binding.tvComment.setText(comment);
                Commn.showDebugLog("gif wala comment");
            } else {
                Commn.showDebugLog("text wala comment");
                imageTypeView();
                Glide.with(getApplicationContext())
                        .asGif().load(comment)
                        .placeholder(R.drawable.placeholder_user)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivCommentImage);
            }
        }
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }

    private void imageTypeView() {
        binding.tvComment.setVisibility(View.GONE);
        binding.ivCommentImage.setVisibility(View.VISIBLE);
    }

    private void textTypeView() {
        binding.tvComment.setVisibility(View.VISIBLE);
        binding.ivCommentImage.setVisibility(View.GONE);
    }

    private void iniMainCommentViews() {
        FirebaseFirestore.getInstance().collection(DBConstants.UserInfo).document(USER_ID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            String image = task.getResult().getString(DBConstants.image);
                            String name = task.getResult().getString(DBConstants.name);
                            String id = task.getResult().getString(DBConstants.super_live_id);
                            if (image != null) {
                                Glide.with(getApplicationContext())
                                        .load(image)
                                        .placeholder(R.drawable.placeholder_user)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(binding.ivCommentUser);
                            }

                            if (name != null) {
                                if (!name.isEmpty()) {
                                    binding.tvUserName.setText(name);
                                } else {
                                    binding.tvUserName.setText(id);
                                }

                            } else {
                                binding.tvUserName.setText(id);

                            }
                        }
                    }
                });


    }


    private void getComments() {

        iniFirebaseOptions();
        user_info = FirebaseFirestore.getInstance().collection(DBConstants.UserInfo);
        comment_adapter = new FirebaseRecyclerAdapter<ReplyCommentModel, RecyclerView.ViewHolder>(firestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i, @NonNull ReplyCommentModel model) {


                switch (model.getComment_type()) {
                    case 0:
                        CommentTextLayoutBinding commentTextLayoutBind = ((CommentTextHolder) holder).getCommentTextLayoutBind();
                        commentTextLayoutBind.tvComment.setText(model.getComment());
                        commentTextLayoutBind.tvCommentTime.setText(Commn.getTimeAgo(model.getTimestamp()));
                        commentTextLayoutBind.tvComment.setText(model.getComment());
                        Commn.showDebugLog("imageee:" + model.getComment() + "");
                        commentTextLayoutBind.tvCommentReply.setVisibility(View.GONE);

                        Commn.showDebugLog("imageee:" + model.getComment() + "");
                        getTextTypeInfo(commentTextLayoutBind, model.getUser_id());

                        commentTextLayoutBind.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                PopupMenu popupMenu = new PopupMenu(context, commentTextLayoutBind.getRoot());
                                if (profilePOJO.getUserId().equalsIgnoreCase(commentModel.getUser_id())||
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

                        commentImagesLayoutBind.tvCommentTime.setText(Commn.getTimeAgo(model.getTimestamp()));


                        Glide.with(getApplicationContext())
                                .asGif().load(model.getComment())
                                .placeholder(R.drawable.placeholder_user)
                                .diskCacheStrategy(DiskCacheStrategy.ALL).into((commentImagesLayoutBind.ivCommentImage));
                        Commn.showDebugLog("imageee:" + model.getComment() + "");

                        commentImagesLayoutBind.tvCommentReply.setVisibility(View.GONE);
                        commentImagesLayoutBind.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                PopupMenu popupMenu = new PopupMenu(context, commentImagesLayoutBind.getRoot());
                                if (profilePOJO.getUserId().equalsIgnoreCase(commentModel.getUser_id())||
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
                ReplyCommentModel messageModel = getItem(position);

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
    private void deleteComment(ReplyCommentModel model) {
        DatabaseReference comment_Ref = database.getReference().child(DBConstants.Post_Comments_Reply).child(comment_id)
                .child(model.getReply_id());

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
    private void getImageInfo(CommentImagesLayoutBinding holder, String user_id) {
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
                                        .into(holder.ivCommentUser);
                            }

                            if (name != null) {
                                holder.tvUserName.setText(name + "");
                            }
                        }
                    }
                });

    }

    private void getTextTypeInfo(CommentTextLayoutBinding holder, String user_id) {
        user_info.document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        String image = task.getResult().getString(DBConstants.image);
                        String name = task.getResult().getString(DBConstants.name);
                        if (image != null) {
                            Glide.with(context).load(image).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.fake_user_icon)
                                    .into(holder.ivCommentUser);
                        }
                        if (name != null) {
                            holder.tvUserName.setText(name + "");
                        }
                    }
                });

    }


    private void replyComment(String comment_id, int comment_type) {

        DatabaseReference replyRef = database.getReference();

        String cmnt_ref = DBConstants.Post_Comments_Reply + "/" + comment_id;

        DatabaseReference user_message_push = replyRef.child(DBConstants.Post_Comments_Reply).
                child(comment_id).push();

        String reply_id = user_message_push.getKey();

        Map firstMap = new HashMap();
        firstMap.put(DBConstants.user_image, profilePOJO.getImage());
        firstMap.put(DBConstants.user_name, profilePOJO.getName());
        firstMap.put(DBConstants.comment, mComment);
        firstMap.put(DBConstants.comment_type, comment_type);
        firstMap.put(DBConstants.timestamp, ServerValue.TIMESTAMP);
        firstMap.put(DBConstants.post_id, this.comment_id);
        firstMap.put(DBConstants.user_id, profilePOJO.getUserId());
        firstMap.put(DBConstants.comment_id, comment_id);
        firstMap.put(DBConstants.reply_id, reply_id);

        Map finalMap = new HashMap();
        finalMap.put(cmnt_ref + "/" + reply_id, firstMap);

        binding.etAddComment.setText("");
        replyRef.updateChildren(finalMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {

                    Commn.showDebugLog("error occured in add comment:" + error.getMessage() + "");
                }
            }
        });


    }

    private void iniFirebaseOptions() {
        DatabaseReference comment_Ref = database.getReference().child(DBConstants.Post_Comments_Reply).child(comment_id);
        Commn.showDebugLog("comment_id:" + comment_id);
        firestoreRecyclerOptions = new FirebaseRecyclerOptions.Builder<ReplyCommentModel>().setQuery(comment_Ref, ReplyCommentModel.class)
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

                    replyComment();
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
                replyComment();
                Commn.showDebugLog("you selected gif");
            }

            @Override
            public void OnReplySend(boolean shouldReply, String username, String comment_id) {

            }


        };
    }

    private void replyComment() {
        DatabaseReference replyRef = database.getReference();
        String cmnt_ref = DBConstants.Post_Comments_Reply + "/" + comment_id;
        DatabaseReference user_message_push = replyRef.child(DBConstants.Post_Comments_Reply).
                child(comment_id).push();
        String reply_id = user_message_push.getKey();
        Map firstMap = new HashMap();
        firstMap.put(DBConstants.user_image, profilePOJO.getImage());
        firstMap.put(DBConstants.user_name, profilePOJO.getName());
        firstMap.put(DBConstants.comment, mComment);
        firstMap.put(DBConstants.comment_type, COMMENT_TYPE);
        firstMap.put(DBConstants.timestamp, ServerValue.TIMESTAMP);
        firstMap.put(DBConstants.post_id, POST_ID);
        firstMap.put(DBConstants.user_id, profilePOJO.getUserId());
        firstMap.put(DBConstants.comment_id, comment_id);
        firstMap.put(DBConstants.reply_id, reply_id);

        Map finalMap = new HashMap();
        finalMap.put(cmnt_ref + "/" + reply_id, firstMap);

        binding.etAddComment.setText("");
        replyRef.updateChildren(finalMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {

                    Commn.showDebugLog("error occured in add comment:" + error.getMessage() + "");
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