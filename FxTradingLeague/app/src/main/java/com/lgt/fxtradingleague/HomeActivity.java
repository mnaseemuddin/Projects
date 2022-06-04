package com.lgt.fxtradingleague;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.Adapter.MainViewPagerAdapter;
import com.lgt.fxtradingleague.FragmentBottomMenu.HomeFragment;
import com.lgt.fxtradingleague.FragmentBottomMenu.MoreFragment;
import com.lgt.fxtradingleague.FragmentBottomMenu.MyContestFragment;
import com.lgt.fxtradingleague.FragmentBottomMenu.NewHomeFragment;
import com.lgt.fxtradingleague.FragmentBottomMenu.ProfileFragment;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lgt.fxtradingleague.Adapter.SectionsPagerAdapter;
import com.lgt.fxtradingleague.RazorPayments.AddPaymentActivity;
import com.lgt.fxtradingleague.TrakNPayPackage.PaymetPackageActivity;
import com.lgt.fxtradingleague.databinding.ActivityHomeBinding;
import com.lgt.fxtradingleague.databinding.ApplicationDownloadLayooutBinding;
import com.lgt.fxtradingleague.databinding.DialogUpdateBinding;
import com.lgt.fxtradingleague.databinding.UnknownSourcesBinding;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.lgt.fxtradingleague.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.fxtradingleague.Extra.ExtraData.HOME_SLIDER_API;

public class HomeActivity extends AppCompatActivity {
    private boolean isUpdateAvailable = false;
    Context context;
    HomeActivity activity;
    public static SessionManager sessionManager;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    //download app
    File download_directry;
    Dialog dialog;
    private DialogUpdateBinding dialogUpdateBinding;
    public AlertDialog download_dialog, unknown_sources_dialog;

    private UnknownSourcesBinding unknownSourcesBinding;
    private ApplicationDownloadLayooutBinding downloadLayooutBinding;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        context = activity = this;
        sessionManager = new SessionManager();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Initialization();
        handleClick();
        iniTabLayout();
        //getSlider();
    }

    private void iniTabLayout() {

        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewpager.setAdapter(adapter);

        binding.viewpager.setOffscreenPageLimit(4);
        for (int i = 0; i <= 4; i++) {
            switch (i) {
                case 0:
                    binding.tabs.addTab(binding.tabs.newTab().setIcon(R.drawable.home));
                    TabLayout.Tab tab = binding.tabs.getTabAt(0);
                    if (tab != null && tab.getIcon() != null) {
                        tab.getIcon().setTint(ContextCompat.getColor(context, R.color.colorPrimary));
                    }
                    break;
                case 1:
                    binding.tabs.addTab(binding.tabs.newTab().setIcon(R.drawable.league_icon));
                    TabLayout.Tab tab1 = binding.tabs.getTabAt(i);
                    if (tab1 != null && tab1.getIcon() != null) {
                        tab1.getIcon().setTint(ContextCompat.getColor(context, R.color.gray));
                    }

                    break;
                case 2:
                    binding.tabs.addTab(binding.tabs.newTab().setIcon(R.drawable.contest));
                    TabLayout.Tab tab2 = binding.tabs.getTabAt(i);
                    if (tab2 != null && tab2.getIcon() != null) {
                        tab2.getIcon().setTint(ContextCompat.getColor(context, R.color.gray));
                    }
                    break;
                case 3:
                    binding.tabs.addTab(binding.tabs.newTab().setIcon(R.drawable.userprofile));
                    TabLayout.Tab tab3 = binding.tabs.getTabAt(i);
                    if (tab3 != null && tab3.getIcon() != null) {
                        tab3.getIcon().setTint(ContextCompat.getColor(context, R.color.gray));
                    }
                    break;
                case 4:
                    binding.tabs.addTab(binding.tabs.newTab().setIcon(R.drawable.more_d));
                    TabLayout.Tab tab4 = binding.tabs.getTabAt(i);
                    if (tab4 != null && tab4.getIcon() != null) {
                        tab4.getIcon().setTint(ContextCompat.getColor(context, R.color.gray));
                    }
                    break;
            }
        }
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab != null) {

                    switch (tab.getPosition()) {
                        case 0:
                            binding.viewpager.setCurrentItem(0);
                            if (tab.getIcon() != null)
                                tab.getIcon().setTint(ContextCompat.getColor(context, R.color.colorPrimary));
                            break;

                        case 1:

                            binding.viewpager.setCurrentItem(1);
                            if (tab.getIcon() != null)
                                tab.getIcon().setTint(ContextCompat.getColor(context, R.color.colorPrimary));
                            break;
                        case 2:

                            binding.viewpager.setCurrentItem(2);
                            if (tab.getIcon() != null)
                                tab.getIcon().setTint(ContextCompat.getColor(context, R.color.colorPrimary));

                            break;
                        case 3:

                            binding.viewpager.setCurrentItem(3);
                            if (tab.getIcon() != null)
                                tab.getIcon().setTint(ContextCompat.getColor(context, R.color.colorPrimary));

                            break;

                        case 4:

                            binding.viewpager.setCurrentItem(4);
                            if (tab.getIcon() != null)
                                tab.getIcon().setTint(ContextCompat.getColor(context, R.color.colorPrimary));

                            break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                    case 0:

                        if (tab.getIcon() != null)
                            tab.getIcon().setTint(ContextCompat.getColor(context, R.color.gray));

                        break;

                    case 1:

                        if (tab.getIcon() != null)
                            tab.getIcon().setTint(ContextCompat.getColor(context, R.color.gray));
                        break;
                    case 2:

                        if (tab.getIcon() != null)
                            tab.getIcon().setTint(ContextCompat.getColor(context, R.color.gray));
                        break;
                    case 3:

                        if (tab.getIcon() != null)
                            tab.getIcon().setTint(ContextCompat.getColor(context, R.color.gray));
                        break;

                    case 4:
                        if (tab.getIcon() != null)
                            tab.getIcon().setTint(ContextCompat.getColor(context, R.color.gray));
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        startFirstFrame();
    }

    private void handleClick() {
        binding.imNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, NotificationActivity.class);
                startActivity(i);
            }
        });
        binding.RLAddCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AddPaymentActivity.class));
            }
        });
    }

    private void Initialization() {
        dialog = new Dialog(this);
        dialogUpdateBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_update
                , (ViewGroup) binding.getRoot(), false);
        dialog.setContentView(dialogUpdateBinding.getRoot());
        Animation shake = AnimationUtils.loadAnimation(activity, R.anim.shake);
        binding.imNotification.startAnimation(shake);

       /* if (tab.getPosition() == 0) {
            replaceFragment(new NewHomeFragment());
            head.setVisibility(View.VISIBLE);
            sliderLayout.setVisibility(View.GONE);
        } else if (tab.getPosition() == 1) {
            replaceFragment(new HomeFragment());
            head.setVisibility(View.VISIBLE);
            //  main_tabs.setVisibility(View.VISIBLE);
            sliderLayout.setVisibility(View.VISIBLE);
        } else if (tab.getPosition() == 2) {
            replaceFragment(new MyContestFragment());
            head.setVisibility(View.VISIBLE);
            //  main_tabs.setVisibility(View.GONE);
            sliderLayout.setVisibility(View.GONE);
        } else if (tab.getPosition() == 3) {
            replaceFragment(new ProfileFragment());
            head.setVisibility(View.GONE);
            // main_tabs.setVisibility(View.GONE);
            sliderLayout.setVisibility(View.GONE);
        } else if (tab.getPosition() == 4) {
            replaceFragment(new MoreFragment());
            head.setVisibility(View.VISIBLE);
            //  main_tabs.setVisibility(View.GONE);
            sliderLayout.setVisibility(View.GONE);
        }*/
    }

    private void startFirstFrame() {

        binding.head.setVisibility(View.VISIBLE);
       // binding.slider.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

/*
    private void getSlider() {
        final Map<String, String> file_maps = new HashMap<String, String>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HOME_SLIDER_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getSlider", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray innerData = jsonObject.getJSONArray("data");
                        for (int i = 0; i < innerData.length(); i++) {
                            JSONObject inData = innerData.getJSONObject(i);
                            String tbl_slider_id = inData.getString("tbl_slider_id");
                            String image = inData.getString("image");
                            file_maps.put(tbl_slider_id, image);
                        }
                        showSliderData(file_maps);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getSlider", "" + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
*/

/*
    private void showSliderData(Map<String, String> file) {
        for (String name : file.keySet()) {
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            // initialize a SliderLayout

            textSliderView.image(file.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {

                        }
                    });
            binding.slider.addSlider(textSliderView);
            binding.slider.setDuration(9000);
        }
    }
*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void Dialog(String UpdateorInstall, String WhatsnewHead) {

        if (dialog == null) {
            return;
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        //  tv_UpdateNote.setText(Update_Note);
        dialogUpdateBinding.tvUpdateApp.setText(UpdateorInstall);
        dialogUpdateBinding.tvWhatsNewHead.setText(WhatsnewHead);

        dialogUpdateBinding.tvDClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog.dismiss();
                Toast.makeText(HomeActivity.this, "This update is mandatory", Toast.LENGTH_SHORT).show();
            }
        });

        dialogUpdateBinding.tvUpdateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions();
                } else {
                    readyToStartDownload();
                }
            }
        });
    }

    private boolean checkPermissions() {

        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String p : permissions) {
                result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((HomeActivity.this),
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), 100);
            return false;
        } else {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            readyToStartDownload();

        }
        return true;
    }

    private void readyToStartDownload() {
        download_directry = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!getPackageManager().canRequestPackageInstalls()) {

                showToAllowUnknownSources();

            } else {
                dialog.dismiss();
                if (!download_directry.mkdirs()) {

                    Log.e("file_path", download_directry.getAbsoluteFile() + "");
                    InializeDownloadDialog(download_directry);
                } else {
                    download_directry = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name));
                    Log.e("file_pathfirst", download_directry.getAbsoluteFile() + "");
                    InializeDownloadDialog(download_directry);
                }
            }

        } else {
            dialog.dismiss();

            if (!download_directry.mkdirs()) {
                Log.e("file_path", download_directry.getAbsoluteFile() + "");
                InializeDownloadDialog(download_directry);
            }
        }
    }

    private void showToAllowUnknownSources() {

        unknownSourcesBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.unknown_sources
                , (ViewGroup) binding.getRoot(), false);
        unknown_sources_dialog = new AlertDialog.Builder(this).create();
        unknown_sources_dialog.setView(unknownSourcesBinding.getRoot());
        unknown_sources_dialog.setCanceledOnTouchOutside(false);
        unknown_sources_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        unknown_sources_dialog.show();

        unknownSourcesBinding.tvCancelAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (download_dialog != null) {
                    download_dialog.dismiss();
                }

                unknown_sources_dialog.dismiss();

            }
        });
        unknownSourcesBinding.tvGoToAllowUnknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unknown_sources_dialog.dismiss();
                startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));

            }
        });

    }

    private void InializeDownloadDialog(final File download_directry) {
        showDownloadDialog();

        int downloadId = PRDownloader.download(Config.apk_download_url, String.valueOf(download_directry.getAbsoluteFile()), getString(R.string.app_name) + ".apk")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                        Log.e("start_download", "started");

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                        long total_size = (progress.totalBytes / 1024) / 1024;
                        long current_progress_size = (progress.currentBytes / 1024) / 1024;
                        downloadLayooutBinding.tvTotalSize.setText(String.valueOf(total_size + " mb"));
                        downloadLayooutBinding.tvCurrentSizeProgress.setText(String.valueOf(current_progress_size + " mb"));
                        double progress_percentage = (100.0 * progress.currentBytes) / progress.totalBytes;

                        downloadLayooutBinding.downloadProgress.setProgress((int) ((progress.currentBytes * 100l) / progress.totalBytes));
                        downloadLayooutBinding.downloadProgress.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
                        downloadLayooutBinding.tvDownloadPercentage.setText(String.valueOf((int) progress_percentage) + "%");
                    }
                }).start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        download_dialog.dismiss();
                        ShowToast(activity, String.valueOf(download_directry.getAbsoluteFile()));

                        ReadyToInstall();
                    }

                    @Override
                    public void onError(Error error) {

                    }


                });

    }

    private void ReadyToInstall() {

        File file = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/" + getString(R.string.app_name) + ".apk");
        if (file.exists()) {

            Log.e("file_directry_exists", "exists");
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkUri = FileProvider.getUriForFile(activity, getPackageName() + ".provider", file);
                intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent.setData(apkUri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                Uri apkUri = Uri.fromFile(file);
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            activity.startActivity(intent);
        }

    }

    private void showDownloadDialog() {
        downloadLayooutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.application_download_layoout
                , (ViewGroup) binding.getRoot(), false);
        download_dialog = new AlertDialog.Builder(this).create();
        download_dialog.setView(downloadLayooutBinding.getRoot());
        download_dialog.setCanceledOnTouchOutside(false);
        download_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        download_dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.

                readyToStartDownload();

            } else {
                readyToStartDownload();
            }
            return;
        }
    }
}


