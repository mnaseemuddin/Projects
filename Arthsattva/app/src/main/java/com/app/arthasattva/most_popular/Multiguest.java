package com.app.arthasattva.most_popular;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.app.arthasattva.Api.DBConstants;
import com.app.arthasattva.R;
import com.app.arthasattva.databinding.HotLiveLayoutBinding;


import com.app.arthasattva.adapter.LiveStreamAdapter.HotLiveHolder;

import com.app.arthasattva.model.ProfilePOJO;
import com.app.arthasattva.utils.BaseFragment;
import com.app.arthasattva.utils.SessionManager;
import com.app.arthasattva.view_live_module.MultiLiveModel;

import pl.droidsonroids.gif.GifImageView;


public class Multiguest extends BaseFragment {
    @Override
    protected int setLayout() {
        return R.layout.frag_multiguest_live;
    }

    Context context;
    Activity activity;
    RecyclerView rv_lets_party;
    RecyclerView rv_hot;
    TextView tv_seeAll;
    private GifImageView iv_coming_soon;

    //firebase
    private FirebaseRecyclerOptions<MultiLiveModel> firebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<MultiLiveModel, HotLiveHolder> adapter;

    private FirebaseDatabase database;
    private ProfilePOJO profilePOJO;
    private SessionManager sessionManager;

    public Multiguest() {

    }

    @Override
    protected void onLaunch() {
        context = activity = getActivity();
        tv_seeAll = find(R.id.tv_seeAll);

        rv_hot = find(R.id.rv_hot);
        iv_coming_soon = find(R.id.iv_coming_soon);


        rv_hot.setLayoutManager(new GridLayoutManager(getActivity(), 2));

       // getLiveVideos();
    }

    private void getLiveVideos() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference current_stream_Ref = database.getReference().child(DBConstants.Multiple_Streams);
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<MultiLiveModel>().setQuery(current_stream_Ref, MultiLiveModel.class).build();

        adapter = new FirebaseRecyclerAdapter<MultiLiveModel, HotLiveHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull HotLiveHolder hotLiveHolder, int position, @NonNull MultiLiveModel hotLiveModel) {

                HotLiveLayoutBinding holder_bind=hotLiveHolder.getHolder_binding();
                holder_bind.tvLiveTitle.setText(hotLiveModel.getStream_title());
                holder_bind.ivLivePreview.setColorFilter(0xff555555, PorterDuff.Mode.MULTIPLY);
                Glide.with(context).load(hotLiveModel.getStream_image()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder_bind.ivLivePreview);

                hotLiveHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });


            }

            @NonNull
            @Override
            public HotLiveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_live_layout, parent, false);
                return new HotLiveHolder(textView);
            }
        };
        adapter.startListening();
        rv_hot.setAdapter(adapter);
    }


}
