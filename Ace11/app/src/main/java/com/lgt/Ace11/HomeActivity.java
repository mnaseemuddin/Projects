package com.lgt.Ace11;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.BuildConfig;
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
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.Adapter.CustomViewPager;
import com.lgt.Ace11.Adapter.MainViewPagerAdapter;
import com.lgt.Ace11.FragmentBottomMenu.HomeNewLayoutFragment;
import com.lgt.Ace11.FragmentBottomMenu.MyContestFragment;
import com.lgt.Ace11.FragmentBottomMenu.ProfileFragment;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lgt.Ace11.MyAccount.MyAccountActivity;
import com.lgt.Ace11.TrakNPayPackage.PaymetPackageActivity;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.lgt.Ace11.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.Ace11.Config.UPDATEAPP;
import static com.lgt.Ace11.Constants.UPDATEAPPTYPE;

public class HomeActivity extends AppCompatActivity implements ResponseManager {
    TextView tv_InviteFriendText, tv_UserEmail;
    LinearLayout ll_EditProfile, ll_balance, ll_EarnMoney, ll_FindPeople, ll_Matches, ll_howToPlay, ll_FantasyPointSystem, ll_InviteFriends, ll_chat_help_desk_container;

    APIRequestManager apiRequestManager;
    ResponseManager responseManager;

    private boolean isUpdateAvailable = false;

    private TextView tv_UpdateApp;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.home_icon,
            R.drawable.my_contect_icon,
            R.drawable.profile_icon,
            R.drawable.more_icon
    };
    Context context;
    HomeActivity activity;
    private LinearLayout container;
    public static CustomViewPager custom_view_pager_container;
    //Auto Login
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;
    public static GoogleApiClient mGoogleApiClient;
    private TextView tv_HeaderName;
    RelativeLayout head;
    Typeface LatoBold, LatoRegular, Ravenscroft;
    public static SessionManager sessionManager;
    ImageView im_Notification, im_AppIcon, im_walletIcons;
    int progressStatus = 0;

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog pDialog;

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    private SliderLayout sliderLayout;

    //download app
    File download_directry;
    Dialog dialog;
    public AlertDialog download_dialog, unknown_sources_dialog;
    private ProgressBar download_progress;
    private TextView tv_download_percentage, tv_total_size, tv_current_size_progress, tv_cancel_allow, tv_go_to_allow_unknown, tv_ProfileName;
    int INSTALL_PACKAGE_CODE = 33;
    private String Update_Note;
    //
    //private ViewPager viewPager;
    //private SectionsPagerAdapter sectionsPagerAdapter;
    //  private TabLayout main_tabs;
    RelativeLayout RLAddCash;
    CircleImageView civ_UserProfileImage, civ_UserHeadProfileImage;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    private int checkBack;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = activity = this;
        sessionManager = new SessionManager();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        generateKeyHash();
        Initialization();
    }

    String Imageurl;

    private void Initialization() {
        dialog = new Dialog(this);
        // side bar
        tv_InviteFriendText = findViewById(R.id.tv_InviteFriendText);
        ll_balance = findViewById(R.id.ll_balance);
        ll_EditProfile = findViewById(R.id.ll_EditProfile);
        custom_view_pager_container = findViewById(R.id.custom_view_pager_container);
        ll_EarnMoney = findViewById(R.id.ll_EarnMoney);
        ll_FindPeople = findViewById(R.id.ll_FindPeople);
        ll_Matches = findViewById(R.id.ll_Matches);
        ll_howToPlay = findViewById(R.id.ll_howToPlay);
        tv_UserEmail = findViewById(R.id.tv_UserEmail);
        ll_FantasyPointSystem = findViewById(R.id.ll_FantasyPointSystem);
        ll_InviteFriends = findViewById(R.id.ll_InviteFriends);
        ll_chat_help_desk_container = findViewById(R.id.ll_chat_help_desk_container);
        clickSideBarEvent();
        dialog.setContentView(R.layout.dialog_update);
        sliderLayout = findViewById(R.id.slider);
        tv_ProfileName = findViewById(R.id.tv_ProfileName);
        im_walletIcons = findViewById(R.id.im_walletIcons);
        civ_UserProfileImage = findViewById(R.id.civ_UserProfileImage);
        civ_UserHeadProfileImage = findViewById(R.id.civ_UserHeadProfileImage);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // getSlider();
        //viewPager = (ViewPager) findViewById(R.id.viewpager);
        ///sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        //viewPager.setAdapter(sectionsPagerAdapter);

        apiRequestManager = new APIRequestManager(activity);
        responseManager = this;
        head = findViewById(R.id.head);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        im_Notification = findViewById(R.id.im_Notification);
        im_AppIcon = findViewById(R.id.im_AppIcon);
        RLAddCash = findViewById(R.id.RLAddCash);

        tv_UpdateApp = dialog.findViewById(R.id.tv_UpdateApp);

        Animation shake = AnimationUtils.loadAnimation(activity, R.anim.shake);
        im_Notification.startAnimation(shake);

        im_Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, NotificationActivity.class);
                startActivity(i);
            }
        });
        im_walletIcons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, MyAccountActivity.class);
                startActivity(i);
            }
        });
        RLAddCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // drawer.closeDrawer(GravityCompat.START);
                drawer.openDrawer(GravityCompat.START);
                // showAlertDialog();
            }
        });

        setProfileDetails();

        InitializeDownloadManager();

        // CheckUpdateVersionCode
        callCheckUpdateVersion(true);


        Ravenscroft = Typeface.createFromAsset(this.getAssets(), "Ravenscroft.ttf");
        //tv_HeaderName.setTypeface(Ravenscroft);
        String Name = sessionManager.getUser(context).getName();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        tabLayout = findViewById(R.id.tabs);
        initTabLayout();
        // container = findViewById(R.id.fragment_container);
        // setupTabIcons1();


        // replaceFragment(new HomeNewLayoutFragment());
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                if (i == 0) {
//                    replaceFragment(new HomeNewLayoutFragment());
//                }
//
//                if (i == 1) {
//                    replaceFragment(new MyContestFragment());
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });


//        tabLayout.setOnTabSelectedListener(
//                new TabLayout.OnTabSelectedListener() {
//                    @Override
//                    public void onTabSelected(TabLayout.Tab tab) {
//                        int tabIconColor = ContextCompat.getColor(activity, R.color.startcolor);
//                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
//                        if (tab.getPosition() == 0) {
//                            replaceFragment(new HomeNewLayoutFragment());
//                            head.setVisibility(View.VISIBLE);
//                        } else if (tab.getPosition() == 1) {
//                            replaceFragment(new MyContestFragment());
//                            head.setVisibility(View.VISIBLE);
//                        } else if (tab.getPosition() == 2) {
//                            replaceFragment(new ProfileFragment());
//                            head.setVisibility(View.VISIBLE);
//                        }
//                        /* else {
//                            replaceFragment(new MoreFragment());
//                            head.setVisibility(View.VISIBLE);
//                            //  main_tabs.setVisibility(View.GONE);
//                            //sliderLayout.setVisibility(View.GONE);
//                        }*/
//                    }
//
//                    @Override
//                    public void onTabUnselected(TabLayout.Tab tab) {
//
//                        int tabIconColor = ContextCompat.getColor(activity, R.color.tabtextunselected);
//                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
//                    }
//
//                    @Override
//                    public void onTabReselected(TabLayout.Tab tab) {
//
//                    }
//                }
//        );
    }

    private void initTabLayout() {

        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        custom_view_pager_container.setAdapter(adapter);
       // tabLayout.setupWithViewPager(custom_view_pager_container);
        custom_view_pager_container.setOffscreenPageLimit(2);

        for (int i = 0; i <= 2; i++) {
            switch (i) {
                case 0:
                    tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home_icon));
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    assert tab != null;
                    tab.setText("Home");
                    head.setVisibility(View.VISIBLE);
                    if (tab != null && tab.getIcon() != null) {
                        tab.getIcon().setTint(ContextCompat.getColor(context, R.color.colorPrimary));
                    }
                    break;
                case 1:
                    tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.my_contect_icon));
                    TabLayout.Tab tabOne = tabLayout.getTabAt(i);
                    assert tabOne != null;
                    tabOne.setText("My Contest");
                    head.setVisibility(View.VISIBLE);

                    break;
                case 2:
                    tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profile_icon));
                    TabLayout.Tab tabTwo = tabLayout.getTabAt(i);
                    assert tabTwo != null;
                    tabTwo.setText("Profile");
                    head.setVisibility(View.VISIBLE);

                    break;
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab != null) {


                    switch (tab.getPosition()) {
                        case 0:
                            custom_view_pager_container.setCurrentItem(0);
                            tab.setIcon(R.drawable.home_icon);
                            if (tab.getIcon() != null)
                                tab.getIcon().setTint(ContextCompat.getColor(context, R.color.colorPrimary));
                            break;
                        case 1:
                            custom_view_pager_container.setCurrentItem(1);
                            tab.setIcon(R.drawable.my_contect_icon);
                            if (tab.getIcon() != null)
                                tab.getIcon().setTint(ContextCompat.getColor(context, R.color.colorPrimary));
                            break;
                        case 2:
                            custom_view_pager_container.setCurrentItem(2);
                            tab.setIcon(R.drawable.profile_icon);
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
                        tab.setIcon(R.drawable.home_icon);
                        if (tab.getIcon() != null)
                            tab.getIcon().setTint(ContextCompat.getColor(context, R.color.dark_gray));
                        break;
                    case 1:
                        tab.setIcon(R.drawable.my_contect_icon);
                        if (tab.getIcon() != null)
                            tab.getIcon().setTint(ContextCompat.getColor(context, R.color.dark_gray));
                        break;
                    case 2:
                        tab.setIcon(R.drawable.profile_icon);
                        if (tab.getIcon() != null)
                            tab.getIcon().setTint(ContextCompat.getColor(context, R.color.dark_gray));
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        custom_view_pager_container.setCurrentItem(0);
                        tab.setIcon(R.drawable.home_icon);
                        if (tab.getIcon() != null)
                            tab.getIcon().setTint(ContextCompat.getColor(context, R.color.colorPrimary));
                            // tab.getIcon().setTint(ContextCompat.getColor(context, R.color.dark_gray));
                        break;
                    case 1:
                        custom_view_pager_container.setCurrentItem(1);
                        tab.setIcon(R.drawable.my_contect_icon);
                        if (tab.getIcon() != null)
                           tab.getIcon().setTint(ContextCompat.getColor(context, R.color.dark_gray));
                        break;
                    case 2:
                        custom_view_pager_container.setCurrentItem(2);
                        tab.setIcon(R.drawable.profile_icon);
                        if (tab.getIcon() != null)
                            tab.getIcon().setTint(ContextCompat.getColor(context, R.color.dark_gray));
                        break;
                }
            }
        });
        custom_view_pager_container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab = tabLayout.getTabAt(0);
                TabLayout.Tab tab2 = tabLayout.getTabAt(1);
                TabLayout.Tab tab3 = tabLayout.getTabAt(2);
                assert tab != null;
                switch (position) {
                    case 0:
                        if (tab2 != null && tab2.getIcon() != null) {
                            tab2.getIcon().setTint(ContextCompat.getColor(context, R.color.dark_gray));
                            tab.getIcon().setTint(ContextCompat.getColor(context, R.color.colorPrimary));
                            tab3.getIcon().setTint(ContextCompat.getColor(context, R.color.dark_gray));

                            int wantedTabIndex = 0;
                            int wantedTabIndex1 = 1;
                            int wantedTabIndex2 = 2;

                            TextView tabTextView = (TextView)(((LinearLayout)((LinearLayout)tabLayout.
                                    getChildAt(0)).getChildAt(wantedTabIndex)).getChildAt(1));

                            TextView tabTextView2 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.
                                    getChildAt(0)).getChildAt(wantedTabIndex1)).getChildAt(1));

                            TextView tabTextView3 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.
                                    getChildAt(0)).getChildAt(wantedTabIndex2)).getChildAt(1));

                            tabTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                            tabTextView2.setTextColor(ContextCompat.getColor(context, R.color.dark_gray));
                            tabTextView3.setTextColor(ContextCompat.getColor(context, R.color.dark_gray));

                        }


                        break;
                    case 1:
                        if (tab2 != null && tab2.getIcon() != null) {
                            tab2.getIcon().setTint(ContextCompat.getColor(context, R.color.colorPrimary));

                            tab.getIcon().setTint(ContextCompat.getColor(context, R.color.dark_gray));
                            tab3.getIcon().setTint(ContextCompat.getColor(context, R.color.dark_gray));

                            int wantedTabIndex = 0;
                            int wantedTabIndex1 = 1;
                            int wantedTabIndex2 = 2;

                            TextView tabTextView = (TextView)(((LinearLayout)((LinearLayout)tabLayout.
                                    getChildAt(0)).getChildAt(wantedTabIndex)).getChildAt(1));

                            TextView tabTextView2 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.
                                    getChildAt(0)).getChildAt(wantedTabIndex1)).getChildAt(1));

                            TextView tabTextView3 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.
                                    getChildAt(0)).getChildAt(wantedTabIndex2)).getChildAt(1));

                            tabTextView.setTextColor(ContextCompat.getColor(context, R.color.dark_gray));
                            tabTextView2.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                            tabTextView3.setTextColor(ContextCompat.getColor(context, R.color.dark_gray));

                        }

                        break;
                    case 2:
                        if (tab2 != null && tab2.getIcon() != null) {
                            tab2.getIcon().setTint(ContextCompat.getColor(context, R.color.dark_gray));

                            tab.getIcon().setTint(ContextCompat.getColor(context, R.color.dark_gray));
                            tab3.getIcon().setTint(ContextCompat.getColor(context, R.color.colorPrimary));

                            int wantedTabIndex = 0;
                            int wantedTabIndex1 = 1;
                            int wantedTabIndex2 = 2;

                            TextView tabTextView = (TextView)(((LinearLayout)((LinearLayout)tabLayout.
                                    getChildAt(0)).getChildAt(wantedTabIndex)).getChildAt(1));

                            TextView tabTextView2 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.
                                    getChildAt(0)).getChildAt(wantedTabIndex1)).getChildAt(1));

                            TextView tabTextView3 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.
                                    getChildAt(0)).getChildAt(wantedTabIndex2)).getChildAt(1));

                            tabTextView.setTextColor(ContextCompat.getColor(context, R.color.dark_gray));
                            tabTextView2.setTextColor(ContextCompat.getColor(context, R.color.dark_gray));
                            tabTextView3.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

                        }

                        break;
                }



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


//    public void goToMyContest() {
//        replaceFragment(new MyContestFragment());
//    }

    private void clickSideBarEvent() {
        ll_EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, EditProfileActivity.class);
                startActivity(i);
            }
        });
        tv_InviteFriendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, MyAccountActivity.class);
                startActivity(i);
            }
        });
        ll_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, MyAccountActivity.class);
                startActivity(i);
            }
        });
        ll_EarnMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Upcoming...", Toast.LENGTH_SHORT).show();
            }
        });
        ll_FindPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Upcoming...", Toast.LENGTH_SHORT).show();
            }
        });
        ll_InviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, InviteFriendsActivity.class);
                startActivity(i);
            }
        });
        ll_FantasyPointSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading", "FANTASY POINT SYSTEM");
                i.putExtra("URL", Config.FANTASYPOINTSYSTEMURL);
                startActivity(i);
            }
        });
        ll_Matches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Upcoming...", Toast.LENGTH_SHORT).show();
            }
        });
        ll_howToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading", "HOW TO PLAY");
                i.putExtra("URL", Config.HOWTOPLAYURL);
                startActivity(i);
            }
        });
        ll_chat_help_desk_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading", "HELP DESK");
                i.putExtra("URL", Config.HELPDESKURL);
                startActivity(i);
            }
        });
    }

    private void setProfileDetails() {
        if (sessionManager.getUser(this) != null) {
            Imageurl = HomeActivity.sessionManager.getUser(this).getImage();
            if (HomeActivity.sessionManager.getUser(this).getEmail() != null) {
                tv_UserEmail.setText("" + HomeActivity.sessionManager.getUser(this).getEmail());
            } else {
                tv_UserEmail.setText("best11@gmail.com");
            }
            Glide.with(context).load(Config.ProfileIMAGEBASEURL + Imageurl).into(civ_UserProfileImage);
            Glide.with(context).load(Config.ProfileIMAGEBASEURL + Imageurl).into(civ_UserHeadProfileImage);
            tv_ProfileName.setText("Hello, " + sessionManager.getUser(this).getName());
            Log.e("asasasas", "" + Imageurl);
            if (!Imageurl.equalsIgnoreCase("default.jpg")) {
                Glide.with(context).load(Config.ProfileIMAGEBASEURL + Imageurl).into(civ_UserProfileImage);
                Glide.with(context).load(Config.ProfileIMAGEBASEURL + Imageurl).into(civ_UserHeadProfileImage);
                Log.e("asasasas2222", "" + Imageurl);
            } else if (Imageurl.equalsIgnoreCase("default.jpg")) {
                Glide.with(context).load(Config.ProfileIMAGEBASEURL + "default.jpg").into(civ_UserProfileImage);
                Glide.with(context).load(Config.ProfileIMAGEBASEURL + "default.jpg").into(civ_UserHeadProfileImage);
                Log.e("asasasas3333", "" + Imageurl);
            } else {
                Glide.with(context).load(Imageurl).into(civ_UserProfileImage);
                Glide.with(context).load(Imageurl).into(civ_UserHeadProfileImage);
                Log.e("asasasas4444", "" + Imageurl);
            }
        } else {
            Toast.makeText(context, "Something Wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_layout, null);
        LinearLayout llcoin = dialogView.findViewById(R.id.ll_bit_coin);
        ImageView cancel_btn = dialogView.findViewById(R.id.iv_cancel_btn);
        LinearLayout lltrackpay = dialogView.findViewById(R.id.ll_track_and_pay_cash);
        builder1.setView(dialogView);
        builder1.setCancelable(false);
        final AlertDialog alert11 = builder1.create();
        alert11.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        lltrackpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Payment", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(activity, PaymetPackageActivity.class);
                startActivity(i);
                alert11.dismiss();
            }
        });

        llcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, AddCashActivity.class);
                startActivity(i);
                alert11.dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert11.dismiss();
            }
        });
        alert11.show();
    }

    private void InitializeDownloadManager() {

        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void callCheckUpdateVersion(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(UPDATEAPP,
                    createRequestJson(), context, activity, UPDATEAPPTYPE,
                    isShowLoader, responseManager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
            Log.e("hgsfdgshd", sessionManager.getUser(context).getUser_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void getSlider() {
        Map<String, Integer> file_maps = new HashMap<String, Integer>();
      /*  file_maps.put("Hannibal", "https://images.squarespace-cdn.com/content/v1/5aded13a45776e718e5f7ee2/1524845452743-FQIIXMWLU7UH4BS83NZ8/ke17ZwdGBToddI8pDm48kKSiaXFwbmXNlzvOvdvHftcUqsxRUqqbr1mOJYKfIPR7LoDQ9mXPOjoJoqy81S2I8N_N4V1vUb5AoIIIbLZhVYxCRW4BPu10St3TBAUQYVKcrgo5nrgbvIf89WC3de0eUrReULnC1FcM8Z_f4CgCiqmyHM717DYEIMNfs9pdtYPc/new%2Bbanner2.jpg?format=1500w");
        file_maps.put("Big Bang Theory", "https://www.northsydney.nsw.gov.au/files/assets/public/images/sports/cricket_2016banner.jpg?w=735");*/

        file_maps.put("a", R.drawable.banner);
        file_maps.put("b", R.drawable.banner2);

        for (String name : file_maps.keySet()) {
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            // initialize a SliderLayout

            textSliderView.image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {


                        }
                    });

            sliderLayout.addSlider(textSliderView);
            sliderLayout.setDuration(9000);

        }
    }

    private void setupTabIcons1() {
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#ff1616"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#626262"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#626262"), PorterDuff.Mode.SRC_IN);
    }


//    public void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment);
//        transaction.commit();
//    }
   /* private void replaceFragment1(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }*/


    private void generateKeyHash() {
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                // ilIC6xl+7OAefINJUy/P50DXtTg=
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        //String MobileVName = "1.3";
        Log.e("uopupoppppo", result + "");
        if (type.equals(UPDATEAPPTYPE)) {
            try {
                Update_Note = result.getString("note");
                String OldV = result.getString("old_version");
                String NewV = result.getString("new_version");

                Log.e("versionnn", OldV);
                Log.e("newversionn", NewV);

                String MobileVName = com.lgt.Ace11.BuildConfig.VERSION_NAME;

                Log.e("mobileversionname", BuildConfig.VERSION_NAME + "");

                if (!MobileVName.equals(NewV)) {

                    Dialog("Update", "What's new");
                } else {
                    Log.e("appppversionissna", "dasdasdasdasd");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {

    }


    public void Dialog(String UpdateorInstall, String WhatsnewHead) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update);

        final TextView tv_DClose = dialog.findViewById(R.id.tv_DClose);
        final TextView tv_UpdateNote = dialog.findViewById(R.id.tv_UpdateNote);
        tv_UpdateApp = dialog.findViewById(R.id.tv_UpdateApp);
        final TextView tv_WhatsNewHead = dialog.findViewById(R.id.tv_WhatsNewHead);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        tv_UpdateNote.setText(Update_Note);
        tv_UpdateApp.setText(UpdateorInstall);
        tv_WhatsNewHead.setText(WhatsnewHead);

        tv_DClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog.dismiss();
                Toast.makeText(HomeActivity.this, "This update is mandatory", Toast.LENGTH_SHORT).show();
            }
        });

        tv_UpdateApp.setOnClickListener(new View.OnClickListener() {
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

    private void checkPermissions() {

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
        } else {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            readyToStartDownload();

        }
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

        View customView = LayoutInflater.from(this).inflate(R.layout.unknown_sources, null);
        unknown_sources_dialog = new AlertDialog.Builder(this).create();
        unknown_sources_dialog.setView(customView);
        unknown_sources_dialog.setCanceledOnTouchOutside(false);

        tv_cancel_allow = customView.findViewById(R.id.tv_cancel_allow);
        tv_go_to_allow_unknown = customView.findViewById(R.id.tv_go_to_allow_unknown);

        unknown_sources_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        unknown_sources_dialog.show();

        tv_cancel_allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (download_dialog != null) {
                    download_dialog.dismiss();
                }

                unknown_sources_dialog.dismiss();

            }
        });
        tv_go_to_allow_unknown.setOnClickListener(new View.OnClickListener() {
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
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onProgress(Progress progress) {

                        long total_size = (progress.totalBytes / 1024) / 1024;
                        long current_progress_size = (progress.currentBytes / 1024) / 1024;
                        tv_total_size.setText(String.valueOf(total_size + " mb"));
                        tv_current_size_progress.setText(String.valueOf(current_progress_size + " mb"));
                        double progress_percentage = (100.0 * progress.currentBytes) / progress.totalBytes;
                        download_progress.setProgress((int) ((progress.currentBytes * 100l) / progress.totalBytes));
                        download_progress.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
                        tv_download_percentage.setText(String.valueOf((int) progress_percentage) + "%");
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

        View customView = LayoutInflater.from(this).inflate(R.layout.application_download_layoout, null);
        download_dialog = new AlertDialog.Builder(this).create();
        download_dialog.setView(customView);
        download_dialog.setCanceledOnTouchOutside(false);

        download_progress = customView.findViewById(R.id.download_progress);
        tv_download_percentage = customView.findViewById(R.id.tv_download_percentage);
        tv_total_size = customView.findViewById(R.id.tv_total_size);
        tv_current_size_progress = customView.findViewById(R.id.tv_current_size_progress);


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


