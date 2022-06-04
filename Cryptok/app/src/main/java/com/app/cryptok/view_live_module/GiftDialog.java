package com.app.cryptok.view_live_module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.R;
import com.app.cryptok.activity.PurchaseCoinsActivity;
import com.app.cryptok.adapter.ItemClickListner;
import com.app.cryptok.databinding.InflateLiveGiftBottomBinding;
import com.app.cryptok.go_live_module.GiftFragment;
import com.app.cryptok.go_live_module.LiveGiftAdapter;
import com.app.cryptok.go_live_module.LiveGiftPOJO;
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
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.app.cryptok.activity.SingleLiveStreamPreview.giftDialog;
import static com.app.cryptok.go_live_module.LiveClickConstants.AUDIO_FRAG;
import static com.app.cryptok.go_live_module.LiveClickConstants.LIVE_FRAG;
import static com.app.cryptok.go_live_module.LiveClickConstants.MULTIGUEST_FRAG;

public class GiftDialog extends BottomSheetDialogFragment implements ItemClickListner {

    ItemClickListner itemClickListner;
    private String selected_quantity = "1";
    private FirebaseFirestore firebaseFirestore;
    private FirebaseDatabase database;
    private String stream_user_id, stream_id;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListner) {
            itemClickListner = (ItemClickListner) context;
        }
    }

    private List<LiveGiftPOJO> liveGiftPOJOS = new ArrayList<>();
    private LiveGiftAdapter giftAdapter;
    private InflateLiveGiftBottomBinding binding;
    ProfilePOJO profilePOJO;
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.inflate_live_gift_bottom, container, false);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        liveGiftDialog();
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(DBConstants.stream_id)) {
                stream_id = getArguments().getString(DBConstants.stream_id);
                stream_user_id = getArguments().getString(DBConstants.user_id);
            }
        }
        sessionManager = new SessionManager(Objects.requireNonNull(getActivity()));
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        iniFirebase();
        new Handler(Looper.getMainLooper()).postDelayed(() -> getGift(), 100);

        handleClick();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager.getInstance(binding.getRoot().getContext()).registerReceiver(giftBroadcast, new IntentFilter(DBConstants.SEND_GIFT_ADAPTER));
    }

    private void handleClick() {
        binding.tvGiftQuantity.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(binding.getRoot().getContext(), binding.tvGiftQuantity);
            popupMenu.getMenu().add("1");
            popupMenu.getMenu().add("10");
            popupMenu.getMenu().add("99");
            popupMenu.getMenu().add("188");
            popupMenu.getMenu().add("999");

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    selected_quantity = menuItem.getTitle().toString();
                    binding.tvGiftQuantity.setText(selected_quantity);
                    return true;
                }
            });
            popupMenu.show();
        });

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void iniFirebase() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
    }


    private void iniLAYOUT() {
        giftAdapter = new LiveGiftAdapter(getActivity(), liveGiftPOJOS, itemClickListner);
        binding.rvGiftList.setAdapter(giftAdapter);
    }


    private void liveGiftDialog() {
        try {
            // live_gift_dialog.setContentView(R.layout.inflate_live_gift_bottom);

         /*   setGiftViewPager(giftViewPager);
            giftTabLayout.setupWithViewPager(giftViewPager);*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getGift() {
        iniLAYOUT();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, MyApi.diamond_list_api, response -> {

            binding.progressBar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(response);

                Commn.showDebugLog("diamond_list_api:"+response);
                String status = jsonObject.getString("status");

                if ("1".equalsIgnoreCase(status)) {

                    JSONArray jsonArray = jsonObject.getJSONArray("stiker_data");

                    for (int i = 0; i < jsonArray.length(); i++) {


                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String tbl_product_id = jsonObject1.getString("tbl_product_id");
                        String name = jsonObject1.getString("name");
                        String image = jsonObject1.getString("image");
                        String value = jsonObject1.getString("value");

                        liveGiftPOJOS.add(new LiveGiftPOJO(tbl_product_id, image, name, value));

                    }
                    giftAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {
            binding.progressBar.setVisibility(View.GONE);
        });
        Commn.requestQueue(getActivity(), stringRequest);

    }

    private void setGiftViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new GiftFragment(MULTIGUEST_FRAG, this), "Hot");
        adapter.addFragment(new GiftFragment(LIVE_FRAG, this), "Popular");
        adapter.addFragment(new GiftFragment(AUDIO_FRAG, this), "Lucky");
        adapter.addFragment(new GiftFragment(AUDIO_FRAG, this), "Crazy");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void itemClick(int position, String image) {


    }

    private void sendGift(String image, String value) {
        DatabaseReference reference = database.getReference();
        String ref = DBConstants.Gift_Stream + "/" + stream_user_id + "/" + stream_id;
        String current_stream = DBConstants.Current_Gift_Stream + "/" + stream_user_id + "/" + stream_id;

        DatabaseReference key = reference.child(DBConstants.Gift_Stream).child(stream_user_id)
                .child(stream_id).push();


        if (value.contains("[a-zA-Z]+")) {
            value = "5";
            Commn.myToast(binding.getRoot().getContext(), "gift value is not in number format,so default value is 5");
        }

        Map map = new HashMap();

        map.put(DBConstants.user_id, profilePOJO.getUserId());
        map.put(DBConstants.gift, image);
        map.put(DBConstants.gift_quantity, selected_quantity);
        map.put(DBConstants.user_name, profilePOJO.getName() + "");
        map.put(DBConstants.gift_value, value);


        Map finalMap = new HashMap();
        Map finalMap2 = new HashMap();
        finalMap.put(ref + "/" + key.getKey(), map);
        finalMap2.put(current_stream + "/" + key.getKey(), map);

        reference.updateChildren(finalMap, (error, ref1) -> {

            updateGift();

        });
        reference.updateChildren(finalMap2, (error, ref1) -> {


        });


    }

    public BroadcastReceiver giftBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                if (intent.getAction().equalsIgnoreCase(DBConstants.SEND_GIFT_ADAPTER)) {

                    if (intent.hasExtra(DBConstants.SEND_GIFT_ADAPTER)) {
                        String image = intent.getStringExtra(DBConstants.SEND_GIFT_ADAPTER);
                        String value = intent.getStringExtra(DBConstants.SEND_GIFT_VALUE);
                        Commn.showDebugLog("got gift:" + image + "");
                        Glide.with(binding.getRoot().getContext()).load(image).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivSelectedGift);
                        binding.tvSendGift.setOnClickListener(view -> {

                            int total_value = Integer.parseInt(value) * Integer.parseInt(selected_quantity);

                            checkWalletCoin(value, total_value, image);


                        });
                    }

                }
            }

        }
    };

    private void checkWalletCoin(String value, int total_value, String image) {

        firebaseFirestore.collection(DBConstants.UserInfo
                + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Coins_Info)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        int current_coins = task.getResult().getLong(DBConstants.current_coins).intValue();
                        Commn.showDebugLog("current_coins:" + current_coins + ",selected_coins:" + total_value + "");
                        if (total_value <= current_coins) {

                            iniSendGift(value, image);

                        } else {

                            lowCoins();
                        }
                    } else {

                        lowCoins();
                        Commn.showDebugLog("current_coins:" + "0" + "");

                    }
                });


    }

    private void iniSendGift(String value, String image) {
        sendGift(image, value);
        Intent intent2 = new Intent(DBConstants.SEND_GIFT);
        intent2.putExtra(DBConstants.SEND_GIFT, image);
        intent2.putExtra(DBConstants.SEND_GIFT_QUANTITY, selected_quantity);
        intent2.putExtra(DBConstants.SEND_GIFT_VALUE, value);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent2);
        if (giftDialog != null) {
            if (giftDialog.isVisible()) {
                giftDialog.dismiss();
            }
        }
    }

    private void lowCoins() {

        final SweetAlertDialog dialog = new SweetAlertDialog(binding.getRoot().getContext(), SweetAlertDialog.ERROR_TYPE);

        dialog.setTitleText("low coins !")
                .setContentText("please add coins to send this gift...")
                .show();

        dialog.setCancelClickListener(sweetAlertDialog -> {
            dialog.dismiss();
        });

        dialog.setConfirmClickListener(sweetAlertDialog -> {
            dialog.dismiss();

            Intent intent = new Intent(binding.getRoot().getContext(), PurchaseCoinsActivity.class);
            binding.getRoot().getContext().startActivity(intent);
        });
    }

    int update_Send_gift = 5;

    private void updateGift() {
        if (sessionManager.getPoints() != null) {
            update_Send_gift = sessionManager.getPoints().getOn_send_gift();
        } else {
            update_Send_gift = 5;
        }
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put(DBConstants.point, task.getResult().getLong(DBConstants.point).intValue() + update_Send_gift);
                        firebaseFirestore.collection(DBConstants.UserInfo)
                                .document(profilePOJO.getUserId())
                                .update(map)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Commn.showDebugLog("send gift points updated success:");
                                    } else {
                                        Commn.showDebugLog("send gift points not updated:");
                                    }
                                });

                    }
                });

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(binding.getRoot().getContext()).unregisterReceiver(giftBroadcast);

    }
}
