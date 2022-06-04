package com.app.arthasattva.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.app.arthasattva.Api.DBConstants;
import com.app.arthasattva.R;
import com.app.arthasattva.adapter.SearchHolder;
import com.app.arthasattva.databinding.ActivitySearchBinding;
import com.app.arthasattva.model.ProfilePOJO;
import com.app.arthasattva.utils.Commn;
import com.app.arthasattva.utils.ConstantsKey;
import com.app.arthasattva.utils.SessionManager;

import static com.app.arthasattva.Api.DBConstants.user_id;

public class SearchActivity extends AppCompatActivity {

    private ProfilePOJO profilePOJO;
    private SessionManager sessionManager;
    private FirebaseFirestore firebaseFirestore;
    private FirestorePagingOptions<ProfilePOJO> firebaseRecyclerOptions;
    private FirestorePagingAdapter<ProfilePOJO, SearchHolder> adapter;

    private Context context;
    private SearchActivity activity;
    private String searchKey;
    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        context = activity = this;
        iniFirebase();
        iniPojo();
        iniViews();
        handleClick();
        addtextChanger();
    }


    private void iniFirebase() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void iniPojo() {
        sessionManager = new SessionManager(this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }

    private void iniViews() {

        inILayout();
        binding.tvSearchTitle.setText(getString(R.string.app_name) + " Members");
        binding.etSearch.setHint("Enter " + getString(R.string.app_name) + " Id");
    }

    private void inILayout() {
        binding.rvUsersList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        binding.rvUsersList.setHasFixedSize(true);
    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.etSearch.setText("");

            }
        });
    }

    private void addtextChanger() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() > 0) {
                    binding.ivClear.setVisibility(View.VISIBLE);

                } else {
                    binding.ivClear.setVisibility(View.GONE);
                }
                searchKey = editable.toString();
                searchUsers();
            }
        });


    }

    private void searchUsers() {
        iniStreamFirebaseOptions();
        adapter = new FirestorePagingAdapter<ProfilePOJO, SearchHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull SearchHolder hotLiveHolder, int position, @NonNull ProfilePOJO hotLiveModel) {

                if ("".equalsIgnoreCase(hotLiveModel.getName())) {
                    hotLiveHolder.tv_buzo_id.setText(hotLiveModel.getArthsId() + "");
                    hotLiveHolder.tv_user_name.setVisibility(View.GONE);
                } else {
                    hotLiveHolder.tv_buzo_id.setText(hotLiveModel.getArthsId() + "");
                    hotLiveHolder.tv_user_name.setText(hotLiveModel.getName());
                }


                Glide.with(context).load(hotLiveModel.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.placeholder_user)
                        .into(hotLiveHolder.iv_user_image);

                hotLiveHolder.itemView.setOnClickListener(view -> {

                    Intent intent = new Intent(context, AnotherUserProfile.class);
                    intent.putExtra(user_id, hotLiveModel.getUserId());
                    startActivity(intent);

                });


            }

            @NonNull
            @Override
            public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_users_list, parent, false);
                return new SearchHolder(textView);
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                super.onLoadingStateChanged(state);
                switch (state) {

                    case ERROR:
                        Commn.showDebugLog("PAGING log:error loading data:");
                        binding.llDataUi.setVisibility(View.GONE);
                        binding.tvNoUsers.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                        break;
                    case LOADED:

                        binding.progressBar.setVisibility(View.GONE);
                        binding.tvNoUsers.setVisibility(View.GONE);
                        binding.llDataUi.setVisibility(View.VISIBLE);
                        Commn.showDebugLog("PAGING log:total_items loaded:" + getItemCount() + "");

                        break;
                    case FINISHED:

                        binding.progressBar.setVisibility(View.GONE);

                        if (getItemCount() == 0) {
                            binding.llDataUi.setVisibility(View.GONE);
                            binding.tvNoUsers.setVisibility(View.VISIBLE);
                        } else {
                            binding.llDataUi.setVisibility(View.VISIBLE);
                        }

                        Commn.showDebugLog("PAGING log:All data loaded:" + getItemCount() + "");
                        break;
                    case LOADING_MORE:

                        Commn.showDebugLog("PAGING log:loading next page");
                        break;
                    case LOADING_INITIAL:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        binding.tvNoUsers.setVisibility(View.GONE);
                        binding.llDataUi.setVisibility(View.GONE);
                        Commn.showDebugLog("PAGING log:loading initial data");
                        break;
                }
            }
        };
        binding.rvUsersList.setAdapter(adapter);

    }

    private void iniStreamFirebaseOptions() {

        Commn.showDebugLog("searchKey:" + searchKey);
        Query query = firebaseFirestore.collection(DBConstants.UserInfo)
                .orderBy(DBConstants.arths_id)
                .orderBy(DBConstants.name)
                .startAt(searchKey)
                .endAt(searchKey + '~');

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        firebaseRecyclerOptions = new FirestorePagingOptions.Builder<ProfilePOJO>()
                .setLifecycleOwner(this)
                .setQuery(query, config, ProfilePOJO.class)
                .build();

    }
}