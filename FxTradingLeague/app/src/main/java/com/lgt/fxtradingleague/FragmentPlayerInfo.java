package com.lgt.fxtradingleague;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.lgt.fxtradingleague.Adapter.AdapterPlayerInformation;
import Model.ModelPlayerInformation;


public class FragmentPlayerInfo extends Fragment {

    private RecyclerView rvPlayerInformation;
    private AdapterPlayerInformation adapterPlayerInformation;

    private TextView tvSelectedBy, tvPointsPlayerInfo, tvCredits,
            tvActualStarting, tvPointsStarting, tvRunsActual,
            tvRunPoints, tvFoursActual, tvFoursPoints, tvCatchesActual,tvCatchesPoints,tvSixACtual, tvSixPoints, tvSrActuals, tvSrPoints, tvFiftyActual, tvFiftyPoints, tvDuckActual,
            tvDuckPoints, tvWktsActual, tvWktsPoints,
            tvMaidenActual, tvMaidenPoints;

    private static List<ModelPlayerInformation> listdasd = new ArrayList<>();
    private TextView tvPlayerNameInfo;

    public FragmentPlayerInfo() {
    }


    public static FragmentPlayerInfo newInstance(int val, List<ModelPlayerInformation> list, int currentTab) {
        FragmentPlayerInfo fragment = new FragmentPlayerInfo();
        Log.e("asdasdasdasdasasd", list + "");

        listdasd = list;
        Bundle args = new Bundle();
        args.putInt("someInt", val);
        args.putInt("currentTab", currentTab);
        fragment.setArguments(args);

        Log.e("asdasrerrrew", currentTab + "");
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_info, container, false);
        int val = getArguments().getInt("someInt", 0);

        rvPlayerInformation = view.findViewById(R.id.rvPlayerInformation);
        tvPlayerNameInfo = view.findViewById(R.id.tvPlayerNameInfo);

        tvSelectedBy = view.findViewById(R.id.tvSelectedBy);
        tvPointsPlayerInfo = view.findViewById(R.id.tvPointsPlayerInfo);
        tvCredits = view.findViewById(R.id.tvCredits);
        tvActualStarting = view.findViewById(R.id.tvActualStarting);
        tvPointsStarting = view.findViewById(R.id.tvPointsStarting);
        tvRunsActual = view.findViewById(R.id.tvRunsActual);
        tvRunPoints = view.findViewById(R.id.tvRunPoints);
        tvFoursActual = view.findViewById(R.id.tvFoursActual);
        tvFoursPoints = view.findViewById(R.id.tvFoursPoints);
        tvSixACtual = view.findViewById(R.id.tvSixACtual);
        tvSixPoints = view.findViewById(R.id.tvSixPoints);
        tvSrActuals = view.findViewById(R.id.tvSrActuals);
        tvSrPoints = view.findViewById(R.id.tvSrPoints);
        tvFiftyActual = view.findViewById(R.id.tvFiftyActual);
        tvFiftyPoints = view.findViewById(R.id.tvFiftyPoints);
        tvDuckActual = view.findViewById(R.id.tvDuckActual);
        tvDuckPoints = view.findViewById(R.id.tvDuckPoints);
        tvWktsActual = view.findViewById(R.id.tvWktsActual);
        tvWktsPoints = view.findViewById(R.id.tvWktsPoints);
        tvMaidenActual = view.findViewById(R.id.tvMaidenActual);
        tvMaidenPoints = view.findViewById(R.id.tvMaidenPoints);


        tvCatchesActual = view.findViewById(R.id.tvCatchesActual);
        tvCatchesPoints = view.findViewById(R.id.tvCatchesPoints);

        Log.e("dasdsadsadadas", listdasd.size() + "Val-->" + val);

        Log.e("sdarerwerewr", PagerAdapterPlayerInformation.currentTab + "");


        tvPlayerNameInfo.setText(listdasd.get(val).getName());
        tvPointsPlayerInfo.setText(listdasd.get(val).getPoints());
        tvActualStarting.setText(listdasd.get(val).getStarting11());
        tvRunsActual.setText(listdasd.get(val).getRun());
        tvFoursActual.setText(listdasd.get(val).getFour());
        tvSixACtual.setText(listdasd.get(val).getSix());
        tvSrActuals.setText(listdasd.get(val).getSr());
        tvFiftyActual.setText(listdasd.get(val).getFifty());
        tvDuckActual.setText(listdasd.get(val).getDuck());
        tvWktsActual.setText(listdasd.get(val).getWickets());
        tvMaidenActual.setText(listdasd.get(val).getMaidenover());
        tvCatchesActual.setText(listdasd.get(val).getCatches());

        adapterPlayerInformation = new AdapterPlayerInformation(getActivity(), listdasd);

        rvPlayerInformation.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvPlayerInformation.hasFixedSize();
        rvPlayerInformation.setNestedScrollingEnabled(false);
        rvPlayerInformation.setAdapter(adapterPlayerInformation);

        adapterPlayerInformation.notifyDataSetChanged();


        Log.e("asdrewrwefdsfsdf", val + "");


        return view;
    }

}
