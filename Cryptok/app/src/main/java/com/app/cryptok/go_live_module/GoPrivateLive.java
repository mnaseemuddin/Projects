package com.app.cryptok.go_live_module;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.app.cryptok.R;
import com.app.cryptok.activity.LiveActivity;
import com.app.cryptok.databinding.FragmentGoPrivateLiveBinding;
import com.app.cryptok.go_live_module.Model.GoLiveModel;
import com.app.cryptok.utils.Commn;


public class GoPrivateLive extends Fragment {

    private FragmentGoPrivateLiveBinding binding;
    private boolean isFilled_mandotory = false;
    private String entry_diamonds;
    private String total_peoples_allow;

    public GoPrivateLive() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_go_private_live, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handleClick();
    }

    private void handleClick() {
        binding.entryDiamonds.tvSuggestion1.setOnClickListener(view -> setSuggestionBg1());
        binding.entryDiamonds.tvSuggestion2.setOnClickListener(view -> setSuggestionBg2());
        binding.entryDiamonds.tvSuggestion3.setOnClickListener(view -> setSuggestionBg3());
        binding.entryDiamonds.tvSuggestion4.setOnClickListener(view -> setSuggestionBg4());

        binding.allowPeoples.tvAllowSuggestion1.setOnClickListener(view -> {
            setAllowSuggestion1();
        });
        binding.allowPeoples.tvAllowSuggestion2.setOnClickListener(view -> {
            setAllowSuggestion2();
        });
        binding.allowPeoples.tvAllowSuggestion3.setOnClickListener(view -> {
            setAllowSuggestion3();
        });
        binding.allowPeoples.tvAllowSuggestion4.setOnClickListener(view -> {
            setAllowSuggestion4();
        });
        binding.allowPeoples.tvAllowSuggestion5.setOnClickListener(view -> {
            setAllowSuggestion5();
        });
        /*binding.allowPeoples.tvAllowSuggestion6.setOnClickListener(view -> {
            setAllowSuggestion6();
        });*/
        binding.btnGoLive.setOnClickListener(view -> {
            startValidate();
        });
    }

    private void startValidate() {
        total_peoples_allow = binding.etPeoplesAllow.getText().toString().trim();
        entry_diamonds = binding.etEntryDiamonds.getText().toString().trim();

        isFilled_mandotory = true;
        if (TextUtils.isEmpty(total_peoples_allow)) {
            isFilled_mandotory = false;
            binding.etPeoplesAllow.setError("please Enter People's allow");
            binding.etPeoplesAllow.requestFocus();
            Commn.myToast(binding.getRoot().getContext(), "please Enter People's allow");
        }
        if (TextUtils.isEmpty(entry_diamonds)) {
            isFilled_mandotory = false;
            binding.etEntryDiamonds.setError("please Enter Entry Diamonds");
            binding.etEntryDiamonds.requestFocus();
            Commn.myToast(binding.getRoot().getContext(), "please Enter Entry Beans");
        }
        if (isFilled_mandotory) {
            goLive();
        }
    }

    private void goLive() {
        GoLiveModel model = new GoLiveModel();
        model.setEntry_diamonds(entry_diamonds);
        model.setStream_title(binding.etLiveTitle.getText().toString() + "");
        model.setTotal_peoples_allow(total_peoples_allow);
        model.setStream_type(LiveConst.PRIVATE_STREAM);

        Intent intent = new Intent(binding.getRoot().getContext(), LiveActivity.class);
        intent.putExtra(LiveConst.GO_LIVE_PARAMS, new Gson().toJson(model) + "");
        startActivity(intent);
    }

    private void setAllowSuggestion6() {

        binding.allowPeoples.tvAllowSuggestion1.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion2.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion3.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion4.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion5.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        //binding.allowPeoples.tvAllowSuggestion6.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
    }

    private void setAllowSuggestion5() {
        binding.etPeoplesAllow.setText(binding.allowPeoples.tvAllowSuggestion5.getText().toString());
        binding.allowPeoples.tvAllowSuggestion1.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion2.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion3.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion4.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion5.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
        //binding.allowPeoples.tvAllowSuggestion6.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
    }

    private void setAllowSuggestion4() {
        binding.etPeoplesAllow.setText(binding.allowPeoples.tvAllowSuggestion4.getText().toString());
        binding.allowPeoples.tvAllowSuggestion1.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion2.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion3.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion4.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
        binding.allowPeoples.tvAllowSuggestion5.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
       // binding.allowPeoples.tvAllowSuggestion6.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
    }

    private void setAllowSuggestion3() {
        binding.etPeoplesAllow.setText(binding.allowPeoples.tvAllowSuggestion3.getText().toString());
        binding.allowPeoples.tvAllowSuggestion1.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion2.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion3.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
        binding.allowPeoples.tvAllowSuggestion4.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion5.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
       // binding.allowPeoples.tvAllowSuggestion6.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
    }

    private void setAllowSuggestion2() {
        binding.etPeoplesAllow.setText(binding.allowPeoples.tvAllowSuggestion2.getText().toString());
        binding.allowPeoples.tvAllowSuggestion1.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion2.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
        binding.allowPeoples.tvAllowSuggestion3.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion4.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion5.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
       // binding.allowPeoples.tvAllowSuggestion6.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
    }

    private void setAllowSuggestion1() {

        binding.allowPeoples.tvAllowSuggestion1.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
        binding.allowPeoples.tvAllowSuggestion2.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion3.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion4.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.allowPeoples.tvAllowSuggestion5.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        //binding.allowPeoples.tvAllowSuggestion6.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));

    }

    private void setSuggestionBg4() {
        binding.etEntryDiamonds.setText(binding.entryDiamonds.tvSuggestion4.getText().toString());
        binding.entryDiamonds.tvSuggestion1.setBackgroundResource(R.drawable.et_bg);
        binding.entryDiamonds.tvSuggestion2.setBackgroundResource(R.drawable.et_bg);
        binding.entryDiamonds.tvSuggestion3.setBackgroundResource(R.drawable.et_bg);
        binding.entryDiamonds.tvSuggestion4.setBackgroundResource(R.drawable.entry_beans_bg);

        //change text color
        binding.entryDiamonds.tvSuggestion4.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.entryDiamonds.tvSuggestion3.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
        binding.entryDiamonds.tvSuggestion2.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
        binding.entryDiamonds.tvSuggestion1.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));

    }

    private void setSuggestionBg3() {
        binding.etEntryDiamonds.setText(binding.entryDiamonds.tvSuggestion3.getText().toString());
        binding.entryDiamonds.tvSuggestion1.setBackgroundResource(R.drawable.et_bg);
        binding.entryDiamonds.tvSuggestion2.setBackgroundResource(R.drawable.et_bg);
        binding.entryDiamonds.tvSuggestion3.setBackgroundResource(R.drawable.entry_beans_bg);
        binding.entryDiamonds.tvSuggestion4.setBackgroundResource(R.drawable.et_bg);

        //change text color
        binding.entryDiamonds.tvSuggestion3.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.entryDiamonds.tvSuggestion4.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
        binding.entryDiamonds.tvSuggestion2.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
        binding.entryDiamonds.tvSuggestion1.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
    }

    private void setSuggestionBg2() {
        binding.etEntryDiamonds.setText(binding.entryDiamonds.tvSuggestion2.getText().toString());
        binding.entryDiamonds.tvSuggestion1.setBackgroundResource(R.drawable.et_bg);
        binding.entryDiamonds.tvSuggestion2.setBackgroundResource(R.drawable.entry_beans_bg);
        binding.entryDiamonds.tvSuggestion3.setBackgroundResource(R.drawable.et_bg);
        binding.entryDiamonds.tvSuggestion4.setBackgroundResource(R.drawable.et_bg);

        //change text color
        binding.entryDiamonds.tvSuggestion2.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.entryDiamonds.tvSuggestion4.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
        binding.entryDiamonds.tvSuggestion3.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
        binding.entryDiamonds.tvSuggestion1.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
    }

    private void setSuggestionBg1() {
        binding.etEntryDiamonds.setText(binding.entryDiamonds.tvSuggestion1.getText().toString());
        binding.entryDiamonds.tvSuggestion1.setBackgroundResource(R.drawable.entry_beans_bg);
        binding.entryDiamonds.tvSuggestion2.setBackgroundResource(R.drawable.et_bg);
        binding.entryDiamonds.tvSuggestion3.setBackgroundResource(R.drawable.et_bg);
        binding.entryDiamonds.tvSuggestion4.setBackgroundResource(R.drawable.et_bg);

        //change text color
        binding.entryDiamonds.tvSuggestion1.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        binding.entryDiamonds.tvSuggestion2.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
        binding.entryDiamonds.tvSuggestion3.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
        binding.entryDiamonds.tvSuggestion4.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.colorPrimary));
    }
}