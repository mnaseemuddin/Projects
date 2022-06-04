package com.app.arthasattva.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.app.arthasattva.Api.DBConstants;
import com.app.arthasattva.Api.MyApi;
import com.app.arthasattva.Interface.OnCommentSelection;
import com.app.arthasattva.R;
import com.app.arthasattva.adapter.Comment.CommentImageHolder;
import com.app.arthasattva.adapter.Comment.CommentTextHolder;
import com.app.arthasattva.adapter.Comment.GifAdapter;
import com.app.arthasattva.model.Comment.GifModel;
import com.app.arthasattva.model.Comment.ReplyCommentModel;
import com.app.arthasattva.model.ProfilePOJO;
import com.app.arthasattva.utils.Commn;
import com.app.arthasattva.utils.ConstantsKey;
import com.app.arthasattva.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyComments extends AppCompatActivity {

    private ImageView iv_back;


    private String comment_id, USER_ID, POST_ID;
    private int comment_type = 0;
    private RecyclerView rv_comments;
    private LinearLayoutManager layoutManager;

    private TextView tv_nocomment, tv_total_comments, tv_gif;
    private EditText et_add_comment;
    private ImageView iv_add_comment;
    private String mComment;
    private boolean isReplyType = false;
    private RelativeLayout LL_Bottom;
    //gif
    private LinearLayout ll_gif_edittext;
    private RecyclerView rv_gif_list;
    private ImageView iv_back_gif;
    private EditText et_gif_search;
    private List<GifModel> gifModelList = new ArrayList<>();
    private ProgressBar gif_progress;
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
    private CircleImageView iv_comment_user;
    private ImageView iv_comment_image;
    private TextView tv_user_name, tv_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_comments);
        context = activity = this;
        iniData();

        iniViews();
        iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
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
        rv_comments = findViewById(R.id.rv_comments);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        rv_comments.setHasFixedSize(true);
        rv_comments.setLayoutManager(layoutManager);

        et_add_comment = findViewById(R.id.et_add_comment);
        iv_add_comment = findViewById(R.id.iv_add_comment);
        tv_nocomment = findViewById(R.id.tv_nocomment);
        tv_total_comments = findViewById(R.id.tv_total_comments);
        tv_gif = findViewById(R.id.tv_gif);
        LL_Bottom = findViewById(R.id.LL_Bottom);
        gif_progress = findViewById(R.id.gif_progress);
        et_gif_search = findViewById(R.id.et_gif_search);
        iv_back_gif = findViewById(R.id.iv_back_gif);
        ll_gif_edittext = findViewById(R.id.ll_gif_edittext);

        rv_gif_list = findViewById(R.id.rv_gif_list);
        rv_gif_list.setHasFixedSize(true);
        rv_gif_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }

    private void iniData() {
        database = FirebaseDatabase.getInstance();
        comment_Ref = database.getReference();

        if (getIntent().hasExtra(DBConstants.comment_id)) {
            comment_id = getIntent().getStringExtra(DBConstants.comment_id);
            USER_ID = getIntent().getStringExtra(DBConstants.user_id);
            comment_type = getIntent().getIntExtra(DBConstants.comment_type, 0);
            POST_ID = getIntent().getStringExtra(DBConstants.post_id);

            String user_name = getIntent().getStringExtra(DBConstants.user_name);
            String user_image = getIntent().getStringExtra(DBConstants.user_image);
            String comment = getIntent().getStringExtra(DBConstants.comment);

            iniMainCommentViews(user_name, user_image);

            if (comment_type == DBConstants.TEXT_COMMENT) {

                textTypeView();
                tv_comment.setVisibility(View.VISIBLE);
                iv_comment_image.setVisibility(View.GONE);
                tv_comment.setText(comment);
                Commn.showDebugLog("gif wala comment");
            } else {
                Commn.showDebugLog("text wala comment");
                iv_comment_image = findViewById(R.id.iv_comment_image);
                imageTypeView();
                Glide.with(getApplicationContext()).asGif().load(comment).placeholder(R.drawable.placeholder_user)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_comment_image);
            }
        }
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }

    private void imageTypeView() {
        tv_comment.setVisibility(View.GONE);
        iv_comment_image.setVisibility(View.VISIBLE);
    }

    private void textTypeView() {
        tv_comment.setVisibility(View.VISIBLE);
        iv_comment_image.setVisibility(View.GONE);
    }

    private void iniMainCommentViews(String user_name, String user_image) {
        tv_comment = findViewById(R.id.tv_comment);
        iv_comment_image = findViewById(R.id.iv_comment_image);
        iv_comment_user = findViewById(R.id.iv_comment_user);
        tv_user_name = findViewById(R.id.tv_user_name);
        Glide.with(getApplicationContext()).asGif().load(user_image).placeholder(R.drawable.placeholder_user)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_comment_user);
        tv_user_name.setText(user_name);
    }


    private void getComments() {

        iniFirebaseOptions();
        user_info = FirebaseFirestore.getInstance().collection(DBConstants.UserInfo);
        comment_adapter = new FirebaseRecyclerAdapter<ReplyCommentModel, RecyclerView.ViewHolder>(firestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i, @NonNull ReplyCommentModel model) {


                switch (model.getComment_type()) {
                    case 0:

                        ((CommentTextHolder) holder).tv_comment.setText(model.getComment());

                        ((CommentTextHolder) holder).tv_comment_time.setText(Commn.getTimeAgo(model.getTimestamp()));
                        ((CommentTextHolder) holder).tv_comment.setText(model.getComment());



                        Commn.showDebugLog("imageee:" + model.getComment() + "");


                        ((CommentTextHolder) holder).tv_comment_reply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                replyComment(model.getComment_id(), model.getComment_type());
                            }
                        });

                        getTextTypeInfo((CommentTextHolder) holder, model.getUser_id());
                        break;
                    case 1:

                        getImageInfo((CommentImageHolder) holder, model.getUser_id());

                        ((CommentImageHolder) holder).tv_comment_time.setText(Commn.getTimeAgo(model.getTimestamp()));


                        Glide.with(getApplicationContext()).asGif().load(model.getComment()).placeholder(R.drawable.placeholder_user)
                                .diskCacheStrategy(DiskCacheStrategy.ALL).into(((CommentImageHolder) holder).iv_comment_image);
                        Commn.showDebugLog("imageee:" + model.getComment() + "");

                        ((CommentImageHolder) holder).tv_comment_reply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                replyComment(model.getComment_id(), model.getComment_type());
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
                        View TextChatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_text_layout, parent, false);
                        return new CommentTextHolder(TextChatView);
                    case 1:
                        View ImageChatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_image_layout, parent, false);
                        return new CommentImageHolder(ImageChatView);
                }
                return null;
            }

        };
        comment_adapter.startListening();

        rv_comments.setAdapter(comment_adapter);

        rv_comments.setNestedScrollingEnabled(false);
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
                    rv_comments.scrollToPosition(positionStart);
                }
            }
        });

    }

    private void getImageInfo(CommentImageHolder holder, String user_id) {
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
                                        .into(holder.iv_comment_user);
                            }

                            if (name != null) {
                                holder.tv_user_name.setText(name+ "");
                            }
                        }
                    }
                });

    }

    private void getTextTypeInfo(CommentTextHolder holder, String user_id) {
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
                                        .into(holder.iv_comment_user);
                            }

                            if (name != null) {
                                holder.tv_user_name.setText(name+ "");
                            }
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

        et_add_comment.setText("");
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
        et_add_comment.addTextChangedListener(new TextWatcher() {
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
        iv_add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mComment = et_add_comment.getText().toString();
                if (TextUtils.isEmpty(mComment)) {
                    Commn.myToast(context, "should not be empty..");
                } else {
                    COMMENT_TYPE = DBConstants.TEXT_COMMENT;

                    replyComment();
                }
            }
        });
        tv_gif.setOnClickListener(new View.OnClickListener() {
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
        et_gif_search.addTextChangedListener(new TextWatcher() {
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
        iv_back_gif.setOnClickListener(new View.OnClickListener() {
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

        et_add_comment.setText("");
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
        gif_progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, query_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                gif_progress.setVisibility(View.GONE);
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
                    rv_gif_list.setAdapter(gifAdapter);
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

        LL_Bottom.setVisibility(View.GONE);
        ll_gif_edittext.setVisibility(View.VISIBLE);
        et_gif_search.requestFocus();
    }

    private void nonGifVisible() {
        LL_Bottom.setVisibility(View.VISIBLE);
        ll_gif_edittext.setVisibility(View.GONE);
        //  et_add_comment.requestFocus();
    }

}