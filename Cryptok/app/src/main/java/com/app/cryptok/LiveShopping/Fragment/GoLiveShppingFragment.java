package com.app.cryptok.LiveShopping.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.LiveShopping.Activities.LiveShoppingActivity;
import com.app.cryptok.LiveShopping.Activities.ProductListActivity;
import com.app.cryptok.LiveShopping.Activities.ProductVideosActivity;
import com.app.cryptok.databinding.FragmentGoLiveShppingBinding;
import com.app.cryptok.LiveShopping.Model.LiveShoppingModel;
import com.app.cryptok.LiveShopping.Model.ProductModel;
import com.app.cryptok.LiveShopping.Model.ProductVideosModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.FastClickUtil;
import com.app.cryptok.utils.SessionManager;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;


public class GoLiveShppingFragment extends Fragment {
    private FragmentGoLiveShppingBinding binding;
    private CompositeDisposable disposable = new CompositeDisposable();
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private ProductVideosModel.VideosDatum selected_video_model;
    private ArrayList<ProductModel.ProductsDatum> selected_product_model = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private String mStreamTitle = "", stream_id;
    private String mStream_type;

    public GoLiveShppingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_go_live_shpping, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniViews();
        getVideosName();
        handleClick();
    }

    private void iniViews() {
        sessionManager = new SessionManager(binding.getRoot().getContext());
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        LocalBroadcastManager.getInstance(binding.getRoot().getContext()).registerReceiver(GetSelectedVideo, new IntentFilter(Commn.CHOOSE_TYPE));

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void handleClick() {
        binding.tvStreamType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                openStreamTypePopup();
            }
        });
        binding.tvVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                Intent intent = new Intent(binding.getRoot().getContext(), ProductVideosActivity.class);
                intent.putExtra(Commn.TYPE, Commn.CHOOSE_TYPE);
                startActivity(intent);
            }
        });
        binding.tvSelectProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                Intent intent = new Intent(binding.getRoot().getContext(), ProductListActivity.class);
                intent.putExtra(Commn.TYPE, Commn.CHOOSE_TYPE);
                startActivity(intent);
            }
        });
        binding.btnGoLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }

                validateSelection();
            }
        });
    }

    private void validateSelection() {
        mStreamTitle = binding.etLiveTitle.getText().toString();
        if (TextUtils.isEmpty(binding.tvSelectProduct.getText().toString())) {
            Commn.myToast(binding.getRoot().getContext(), "Select Product");
            return;
        }
        if (TextUtils.isEmpty(binding.tvStreamType.getText().toString())) {
            Commn.myToast(binding.getRoot().getContext(), "Select Stream Type");
            return;
        }
        if (mStream_type.equalsIgnoreCase(DBConstants.Uploaded_Video_Type)) {
            if (TextUtils.isEmpty(binding.tvVideos.getText().toString())) {
                Commn.myToast(binding.getRoot().getContext(), "Select Video");
                return;
            }
        }


        goLiveStream();
    }

    CollectionReference stream_ref;

    private void goLiveStream() {

        stream_id = String.valueOf(System.currentTimeMillis());
        LiveShoppingModel shoppingModel = new LiveShoppingModel();
        shoppingModel.setStream_id(stream_id + "");

        if (DBConstants.Uploaded_Video_Type.equalsIgnoreCase(mStream_type)) {
            shoppingModel.setStream_url(selected_video_model.getVideo() + "");
            shoppingModel.setVIDEO_MODEL(new Gson().toJson(selected_video_model) + "");
        } else {
            shoppingModel.setStream_url(stream_id + "");
            shoppingModel.setVIDEO_MODEL("");
        }
        shoppingModel.setStream_title(mStreamTitle + "");
        shoppingModel.setCountry_name(profilePOJO.getCountry_name() + "");
        shoppingModel.setStream_type(mStream_type + "");
        shoppingModel.setUser_id(profilePOJO.getUserId() + "");
        shoppingModel.setPRODUCT_MODEL(new Gson().toJson(selected_product_model) + "");


        Intent intent = new Intent(binding.getRoot().getContext(), LiveShoppingActivity.class);
        intent.putExtra(DBConstants.LiveShoppingModel, new Gson().toJson(shoppingModel));
        startActivity(intent);

    }

    private void openStreamTypePopup() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), binding.tvVideos);
        popupMenu.getMenu().add("Live Streaming");
        popupMenu.getMenu().add("Uploaded Video");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(item -> {
            binding.tvStreamType.setText(item.getTitle().toString());
            if (item.getTitle().equals("Live Streaming")) {
                mStream_type = DBConstants.Live_Streaming_Type;
                binding.tvVideos.setVisibility(View.GONE);
            }
            if (item.getTitle().equals("Uploaded Video")) {
                mStream_type = DBConstants.Uploaded_Video_Type;
                binding.tvVideos.setVisibility(View.VISIBLE);
                getVideosName();
            }
            return true;
        });
    }

    public BroadcastReceiver GetSelectedVideo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction() != null) {
                    if (Commn.CHOOSE_TYPE.equalsIgnoreCase(intent.getAction())) {
                        if (intent.hasExtra(Commn.MODEL)) {
                            selected_video_model = new Gson().fromJson(intent.getStringExtra(Commn.MODEL), ProductVideosModel.VideosDatum.class);
                            binding.tvVideos.setText(selected_video_model.getVideoTitle() + "");
                        }
                        if (intent.hasExtra(Commn.PRODUCT_MODEL)) {
                            selected_product_model = new Gson().fromJson(intent.getStringExtra(Commn.PRODUCT_MODEL),
                                    new TypeToken<ArrayList<ProductModel.ProductsDatum>>() {
                                    }.getType());


                            if (selected_product_model.size() > 1) {
                                binding.tvSelectProduct.setText(selected_product_model.get(0).getProName() + " and more");
                            } else {
                                binding.tvSelectProduct.setText(selected_product_model.get(0).getProName() + "");
                            }

                        }
                    }

                }
            }
        }
    };

    private void getVideosName() {


    }
}