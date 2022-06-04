package com.app.cryptok.fragment.stream;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.adapter.LiveStreamAdapter.AllJoinedStreamHolder;
import com.app.cryptok.databinding.FragmentAllViewersForHostBinding;
import com.app.cryptok.fragment.Profile.BottomUserProfile;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.model.StreamViewersModel;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

public class AllViewersForHost extends BottomSheetDialogFragment {

    private FirebaseRecyclerOptions<StreamViewersModel> viewers_firebase_options;
    private FirebaseRecyclerAdapter<StreamViewersModel, AllJoinedStreamHolder> viewers_adapter;
    private FragmentAllViewersForHostBinding binding;
    private String user_id;
    ProfilePOJO profilePOJO;
    private SessionManager sessionManager;
    public AllViewersForHost() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(DBConstants.user_id)) {
                user_id = getArguments().getString(DBConstants.user_id);
                Commn.showErrorLog("for_all_viewers:" + user_id + "");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_viewers_for_host, container, false);

        return binding.getRoot();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sessionManager = new SessionManager(binding.getRoot().getContext());
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        getViewersList();
    }


    private void getViewersList() {

        inViewersFirebaseOptionsBottom();
        viewers_adapter = new FirebaseRecyclerAdapter<StreamViewersModel, AllJoinedStreamHolder>(viewers_firebase_options) {
            @Override
            protected void onBindViewHolder(@NonNull AllJoinedStreamHolder holder, int i, @NonNull StreamViewersModel model) {

                Glide.with(binding.getRoot().getContext()).load(model.getUser_image()).placeholder(R.drawable.placeholder_user)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_user_image);

                holder.tv_user_name.setText(model.user_name);

                Commn.showErrorLog("all_viewers:" + new Gson().toJson(model) + "");
                holder.iv_user_image.setOnClickListener(view -> {
                    openViewBottomSheet(model.getUser_id());
                });

            }

            @NonNull
            @Override
            public AllJoinedStreamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View TextChatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_viewers_list_bottom, parent, false);
                return new AllJoinedStreamHolder(TextChatView);
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);
                Commn.showErrorLog("onError:" + error.getDetails() + "," + error.getMessage() + "," + error.toException().getMessage() + "");
            }

            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull DataSnapshot snapshot, int newIndex, int oldIndex) {
                super.onChildChanged(type, snapshot, newIndex, oldIndex);
                Commn.showErrorLog("onChildChanged:" + snapshot.getChildrenCount() + "");
            }
        };
        viewers_adapter.startListening();
        binding.rvAllViewersList.setAdapter(viewers_adapter);


    }

    private void openViewBottomSheet(String user_id) {
        Bundle bundle = new Bundle();
        bundle.putString(DBConstants.user_id, user_id);
        BottomUserProfile bottomUserProfile = new BottomUserProfile();
        bottomUserProfile.setArguments(bundle);
        bottomUserProfile.show(getChildFragmentManager(), "");
    }


    private void inViewersFirebaseOptionsBottom() {

        DatabaseReference viewers_ref = FirebaseDatabase.getInstance().getReference().child(DBConstants.Current_Viewer).child(user_id);
        viewers_firebase_options = new FirebaseRecyclerOptions.Builder<StreamViewersModel>().setQuery(viewers_ref, StreamViewersModel.class).build();

        viewers_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    binding.tvTotalViewers.setText("Total Viewers : " + String.valueOf(snapshot.getChildrenCount()) + "");


                } else {
                    binding.tvTotalViewers.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}