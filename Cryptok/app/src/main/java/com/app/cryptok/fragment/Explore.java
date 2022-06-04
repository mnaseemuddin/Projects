package com.app.cryptok.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.activity.AnotherUserProfile;
import com.app.cryptok.activity.MainActivity;
import com.app.cryptok.activity.SearchActivity;
import com.app.cryptok.adapter.CountryListAdapter;
import com.app.cryptok.adapter.ExploreHolder;
import com.app.cryptok.databinding.ExploreStreamLayoutBinding;
import com.app.cryptok.databinding.FragExploreBinding;
import com.app.cryptok.model.CountryModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import static com.app.cryptok.Api.DBConstants.user_id;


public class Explore extends Fragment {

    public Explore() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_explore, container, false);
        return binding.getRoot();
    }

    Toolbar toolbar;
    MainActivity mainActivity;
    private FragExploreBinding binding;

    Context context;
    Activity activity;
    //user info
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    //use info
    //iniCountryFirebase
    private FirebaseFirestore firebaseFirestore;
    FirestorePagingOptions<CountryModel> firebaseRecyclerOptions;
    FirestorePagingAdapter<CountryModel, CountryListAdapter> countryAdapter;

    //iniCountryFirebase

    //iniCountryFirebase

    FirestorePagingOptions<ProfilePOJO> streams_options;
    FirestorePagingAdapter<ProfilePOJO, ExploreHolder> streams_adapter;
    //iniCountryFirebase
    int current_position=-1;
    private String current_country="India";

    //iniLiveFirebase
    //getUserInfo
    private CollectionReference user_info;
    private boolean isIndiaSelected=true;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        context = activity = getActivity();

        iniViews(view);


    }

    private void iniViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        mainActivity.setSupportActionBar(toolbar);
        mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        firebaseFirestore = FirebaseFirestore.getInstance();
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);

      //  getCountry();
        getLiveStreams();
        handleClick();
    }

    private void handleClick() {
        binding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mainActivity, SearchActivity.class));
            }
        });


        binding.tvDefaultCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isIndiaSelected=true;

                current_position=-1;
                countryAdapter.notifyDataSetChanged();
                binding.tvDefaultCountry.setBackgroundResource(R.drawable.dark_gray_bg);
                binding.tvDefaultCountry.setTextColor(getResources().getColor(R.color.white));
                current_country = "India";
                getLiveStreams();
            }
        });
    }

    private void setIndiaDEFAULT() {
        binding.tvDefaultCountry.setBackgroundResource(R.drawable.dark_gray_bg);
        binding.tvDefaultCountry.setTextColor(getResources().getColor(R.color.white));
        current_country = "India";
        getLiveStreams();
    }

    private void getLiveStreams() {

        Commn.showDebugLog("selected_country:" + current_country + "");
        iniStreamsFirebaseOptions();
        streams_adapter = new FirestorePagingAdapter<ProfilePOJO, ExploreHolder>(streams_options) {
            @Override
            protected void onBindViewHolder(@NonNull ExploreHolder hotLiveHolder, int position, @NonNull ProfilePOJO hotLiveModel) {

                ExploreStreamLayoutBinding holder_bind = hotLiveHolder.getHolder_binding();
                if (hotLiveModel.getName().equals("")) {
                    holder_bind.tvLiveTitle.setText(hotLiveModel.getSuper_live_id());
                } else {
                    holder_bind.tvLiveTitle.setText(hotLiveModel.getName());
                }
                Glide.with(context).load(hotLiveModel.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.fake_user_icon)
                        .into(holder_bind.ivLivePreview);
                //    hotLiveHolder.stream_thumb.setColorFilter(0xff555555, PorterDuff.Mode.MULTIPLY);

                holder_bind.ivLiveView.setVisibility(View.GONE);


                hotLiveHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, AnotherUserProfile.class);

                        intent.putExtra(user_id, hotLiveModel.getUserId());

                        startActivity(intent);
                     /*   Intent intent = new Intent(context, SingleLiveStreamPreview.class);
                        intent.putExtra(DBConstants.stream_title, hotLiveModel.getStream_title());
                        intent.putExtra(DBConstants.stream_url, hotLiveModel.getStream_url());
                        intent.putExtra(DBConstants.stream_id, hotLiveModel.getStream_id());
                        Log.d("my_stream_url", hotLiveModel.getStream_url() + "");
                        intent.putExtra(DBConstants.user_id, hotLiveModel.getUser_id());
                        intent.putExtra(DBConstants.stream_image, hotLiveModel.getStream_image());
                        intent.putExtra(DBConstants.stream_user_name, hotLiveModel.getStream_user_name());
                        startActivity(intent);*/
                    }
                });

                //getUserInfo(holder_bind,hotLiveModel.getUserId());
            }

            @Override
            protected void onError(@NonNull Exception e) {
                super.onError(e);
                Commn.showErrorLog("getLiveStreams:on country selection:" + e.getMessage() + "");
            }

            @NonNull
            @Override
            public ExploreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_stream_layout, parent, false);
                return new ExploreHolder(textView);
            }
        };

        binding.rvLive.setAdapter(streams_adapter);
    }


    private void iniStreamsFirebaseOptions() {

        Query query = firebaseFirestore.collection(DBConstants.UserInfo)
               // .whereEqualTo(DBConstants.country_name, current_country)
                .whereNotEqualTo(DBConstants.userId, profilePOJO.getUserId());

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        streams_options = new FirestorePagingOptions.Builder<ProfilePOJO>()
                .setLifecycleOwner(this)
                .setQuery(query, config, ProfilePOJO.class)
                .build();
    }

    private void getCountry() {
        setIndiaDEFAULT();
        iniFirebaseOptions();

        countryAdapter = new FirestorePagingAdapter<CountryModel, CountryListAdapter>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull CountryListAdapter holder, int position, @NonNull CountryModel model) {

                holder.tv_country.setText(model.getCountry_name());

                holder.tv_country.setOnClickListener(view -> {

                    isIndiaSelected = false;
                    current_position = position;

                    setIndiaUnselected();
                    notifyDataSetChanged();

                });
                if (current_position == position) {

                    if (!isIndiaSelected) {
                        Commn.showDebugLog("india selected");
                        current_country = model.getCountry_name();
                        getLiveStreams();
                        holder.itemView.setBackgroundResource(R.drawable.dark_gray_bg);
                        holder.tv_country.setTextColor(getResources().getColor(R.color.white));
                    }

                } else {

                    holder.itemView.setBackgroundResource(R.drawable.low_dark_bg);
                    holder.tv_country.setTextColor(getResources().getColor(R.color.black));

                }
            }

            @NonNull
            @Override
            public CountryListAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_country_name, parent, false);
                return new CountryListAdapter(view);
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                super.onLoadingStateChanged(state);
                switch (state) {

                    case ERROR:
                        Commn.showDebugLog("PAGING log:error loading notificationModel:");
                        break;
                    case LOADED:

                        //progress_bar.setVisibility(View.GONE);
                        binding.tvDefaultCountry.setVisibility(View.VISIBLE);

                        Commn.showDebugLog("PAGING log:total_items loaded:" + getItemCount() + "");
                        break;
                    case FINISHED:
                        binding.tvDefaultCountry.setVisibility(View.VISIBLE);
                        // progress_bar.setVisibility(View.GONE);
                        Commn.showDebugLog("PAGING log:All notificationModel loaded:");
                        break;
                    case LOADING_MORE:

                        Commn.showDebugLog("PAGING log:loading next page");
                        break;
                    case LOADING_INITIAL:
                        Commn.showDebugLog("PAGING log:loading initial notificationModel");
                        break;
                }
            }
        };

        binding.rvCountries.setAdapter(countryAdapter);

    }

    private void setIndiaUnselected() {
        binding.tvDefaultCountry.setBackgroundResource(R.drawable.low_dark_bg);
        binding.tvDefaultCountry.setTextColor(getResources().getColor(R.color.black));
    }

    private void iniFirebaseOptions() {

        CollectionReference reference = firebaseFirestore.collection(DBConstants.All_Countries);

        Query query = reference.whereNotEqualTo(DBConstants.country_name, "India");

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        firebaseRecyclerOptions = new FirestorePagingOptions.Builder<CountryModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, CountryModel.class)
                .build();


    }
}