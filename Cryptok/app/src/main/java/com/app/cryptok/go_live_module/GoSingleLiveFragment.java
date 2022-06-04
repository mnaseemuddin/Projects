package com.app.cryptok.go_live_module;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.app.cryptok.R;
import com.app.cryptok.activity.LiveActivity;
import com.app.cryptok.databinding.GoSingleLiveFragmentBinding;
import com.app.cryptok.go_live_module.Model.GoLiveModel;

public class GoSingleLiveFragment extends Fragment {

    private GoSingleLiveFragmentBinding binding;
    private String title = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.go_single_live_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handleClick();

    }

    private void handleClick() {
        binding.btnGoLive.setOnClickListener(view -> {
            title = binding.etLiveTitle.getText().toString();
            GoLiveModel model = new GoLiveModel();
            model.setEntry_diamonds("");
            model.setStream_title(title + "");
            model.setTotal_peoples_allow("");
            model.setStream_type(LiveConst.PUBLIC_STREAM);

            Intent intent = new Intent(binding.getRoot().getContext(), LiveActivity.class);
            intent.putExtra(LiveConst.GO_LIVE_PARAMS, new Gson().toJson(model) + "");
            startActivity(intent);
        });
    }

}
