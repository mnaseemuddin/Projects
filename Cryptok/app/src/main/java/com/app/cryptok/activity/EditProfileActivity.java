package com.app.cryptok.activity;

import static com.app.cryptok.activity.LoginActivity.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.databinding.ActivityEditProfileBinding;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class EditProfileActivity extends AppCompatActivity {

    private String mName;
    private FirebaseFirestore database;
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private EditProfileActivity activity;
    private Context context;
    private boolean isUploading = false;
    //private File filePath;
    private Uri filePath;
    private ActivityEditProfileBinding binding;
    private PopupMenu popupMenu;
    Calendar myCalendar;
    private boolean isFirst_Image = false;
    private boolean is_Second_Image = false;
    private boolean is_Third_Image = false;
    private String image_id = "";
    public static final int PICK_IMAGE = 1;
    private boolean isProfile = false;
    Boolean isImageAvailable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        context = activity = this;
        iniFirebase();
        handleClick();
        getInfo();
    }

    private void iniFirebase() {
        database = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(this);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);

    }

    private void handleClick() {

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.btUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mName = binding.etUserName.getText().toString();
                updateInfo();

            }
        });


        binding.llImageOne.setOnClickListener(view -> {
            isFirst_Image = true;
            is_Second_Image = false;
            is_Third_Image = false;
            image_id = "1";
            if (!isUploading) {

                checkStoragePermission();
                //showChangeImageDialog();
            } else {
                Commn.myToast(context, "already uploading,please wait..");
            }
        });

        binding.llImageTwo.setOnClickListener(view -> {
            isFirst_Image = false;
            is_Second_Image = true;
            is_Third_Image = false;
            image_id = "2";
            if (!isUploading) {
                checkStoragePermission();

            } else {
                Commn.myToast(context, "already uploading,please wait..");


            }
        });
        binding.llImageThree.setOnClickListener(view -> {
            isFirst_Image = false;
            is_Second_Image = false;
            is_Third_Image = true;
            image_id = "3";
            if (!isUploading) {
                checkStoragePermission();

            } else {
                Commn.myToast(context, "already uploading,please wait..");
            }
        });


        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }

        iniCalender();
        binding.tvDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog();
            }
        });
        binding.tvGender.setOnClickListener(view -> showGenderPopup());

    }

    private void pickImage() {
        Options options = Options.init()
                .setRequestCode(1) //Request code for activity results
                .setCount(1) //Number of images to restict selection count
                .setFrontfacing(false) //Front Facing camera on start
                .setSpanCount(4) //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.Picture) //Option to select only pictures or videos or both
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);
        Pix.start(activity, options);
    }

    private void checkStoragePermission() {

        Dexter.withActivity(EditProfileActivity.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).
                withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        Log.e(TAG, "onPermissionsChecked:dsada " + report + "");
                        if (report.areAllPermissionsGranted()) {
                            pickImage();
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(EditProfileActivity.this, "Please allow all permissions from setting", Toast.LENGTH_SHORT).show();
                            showSettingsDialog();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "All permissions are required", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }

                }).onSameThread().check();


    }

    private void iniCalender() {
        myCalendar = Calendar.getInstance();
        myCalendar.add(Calendar.YEAR, -18);
    }

    private void showGenderPopup() {
        popupMenu = new PopupMenu(activity, binding.tvGender);
        popupMenu.getMenu().add("Male");
        popupMenu.getMenu().add("Female");

        popupMenu.setOnMenuItemClickListener(item -> {

            binding.tvGender.setText(item.getTitle() + "");
            return true;
        });
        popupMenu.show();
    }

    private void datePickerDialog() {
        //Goes 10 Year Back in time ^^

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, date
                , myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
        );
        long upperLimit = myCalendar.getTimeInMillis();
        datePickerDialog.getDatePicker().setMaxDate(upperLimit);

        datePickerDialog.show();

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.tvDob.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateInfo() {

        database.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        Map<String, Object> update = new HashMap<>();
                        update.put(DBConstants.name, mName);
                        update.put(DBConstants.bio, binding.etBio.getText().toString() + "");
                        update.put(DBConstants.gender, binding.tvGender.getText().toString() + "");
                        update.put(DBConstants.hometown, binding.tvHometown.getText().toString() + "");
                        update.put(DBConstants.dob, binding.tvDob.getText().toString() + "");
                        database.collection(DBConstants.UserInfo)
                                .document(profilePOJO.getUserId())
                                .update(update);
                        Commn.myToast(context, "updated");


                    }
                });

    }

    private void getInfo() {
        database.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        if (value.exists()) {
                            ProfilePOJO profilePOJO = value.toObject(ProfilePOJO.class);

                            if (profilePOJO != null) {
                                sessionManager.setString(ConstantsKey.PROFILE_KEY, new Gson().toJson(profilePOJO));

                                binding.etUserName.setText(profilePOJO.getName());
                                binding.tvBuzoId.setText(profilePOJO.getSuper_live_id());
                                binding.etHometown.setText(profilePOJO.getHometown());
                                binding.etBio.setText(profilePOJO.getBio());
                                binding.tvDob.setText(profilePOJO.getDob());
                                binding.tvGender.setText(profilePOJO.getGender());

                            }

                        }
                    }

                });

        getAllImages();

    }

    private void getAllImages() {
        String ref = DBConstants.UserInfo + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Images;

        database.collection(ref)
                .document("1")
                .addSnapshotListener((value, error) -> {

                    if (value.exists()) {
                        binding.ivAddOne.setVisibility(View.GONE);
                        if (value.getString(DBConstants.image) != null) {
                            String image = value.getString(DBConstants.image);
                            Glide.with(getApplicationContext())
                                    .load(image).diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivImageOne);

                            updateMainProfile(image);
                        }

                    } else {
                        binding.ivAddOne.setVisibility(View.GONE);
                        Glide.with(getApplicationContext()).load(profilePOJO.getImage())
                                .thumbnail(0.05f)
                                .transition(DrawableTransitionOptions.withCrossFade()).
                                placeholder(R.drawable.placeholder_user).into(binding.ivImageOne);
                    }
                });
        database.collection(ref)
                .document("2")
                .addSnapshotListener((value, error) -> {

                    if (value.exists()) {
                        binding.ivAddTwo.setVisibility(View.GONE);
                        if (value.getString(DBConstants.image) != null) {
                            String image = value.getString(DBConstants.image);
                            Glide.with(getApplicationContext())
                                    .load(image).diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .thumbnail(0.05f)
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(binding.ivImageTwo);
                        }

                    } else {
                        binding.ivAddTwo.setVisibility(View.VISIBLE);
                    }
                });
        database.collection(ref)
                .document("3")
                .addSnapshotListener((value, error) -> {

                    if (value.exists()) {

                        binding.ivAddThree.setVisibility(View.GONE);
                        if (value.getString(DBConstants.image) != null) {
                            String image = value.getString(DBConstants.image);
                            Glide.with(getApplicationContext())
                                    .load(image).diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .thumbnail(0.05f)
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(binding.ivImageThree);

                        }

                    } else {
                        binding.ivAddThree.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void updateMainProfile(String image) {
        database.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        Map<String, Object> update = new HashMap<>();
                        update.put(DBConstants.image, image + "");
                        database.collection(DBConstants.UserInfo)
                                .document(profilePOJO.getUserId())
                                .update(update);
                        Commn.myToast(context, "updated");
                    }
                });
    }

    private void showSettingsDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", (getPackageName()), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Commn.showDebugLog("selected_phto:" + new Gson().toJson(returnValue));
            if (returnValue != null) {
                if (returnValue.size() > 0) {

                    compreessImage(returnValue.get(0));
                }
            }
        }
    }

    private void compreessImage(String image) {
        String compressed_path = Commn.compressImage(image, context);
        Commn.showDebugLog("compressed_path:" + compressed_path + "");
        if (isFirst_Image) {
            Glide.with(getApplicationContext())
                    .load(compressed_path + "")
                    .into(binding.ivAddOne);
        }
        if (is_Second_Image) {
            Glide.with(getApplicationContext())
                    .load(compressed_path + "")
                    .into(binding.ivAddTwo);
        }
        if (is_Third_Image) {
            Glide.with(getApplicationContext())
                    .load(compressed_path + "")
                    .into(binding.ivAddThree);
        }
        uploadImage(compressed_path);
    }

    public File getPath() {
        String state = Environment.getExternalStorageState();
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            filesDir = getExternalFilesDir(null);
        } else {
            // Load another directory, probably local memory
            filesDir = getFilesDir();
        }
        if (filesDir != null) {
            String realPATH = filesDir.getPath();
            Commn.showDebugLog("compress path:" + realPATH + " ");
        }
        return filesDir;
    }

    private void uploadImage(String bitmap) { if (!activity.isFinishing()) {
        Commn.showDialog(activity);
    }
        Commn.showDebugLog("from glide:" + bitmap + " ");
        String post_image_name = profilePOJO.getUserId() + ".png";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference dataRef = storageRef.child(DBConstants.User_Profile_Images).child(profilePOJO.getUserId())
                .child(image_id).child(post_image_name);

        final UploadTask uploadTask;
        uploadTask = dataRef.putFile(Uri.fromFile(new File(bitmap)));

        uploadTask.addOnFailureListener(e -> Commn.showDebugLog("whatTheFuck:" + e.toString() + "")).addOnProgressListener(taskSnapshot -> {
            //calculating progress percentage
            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

            //displaying percentage in progress dialog

            Commn.myToast(context, "Uploading image : " + String.valueOf(((int) progress) + "%..."));

            isUploading = true;
        }).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    Log.i("problem", task.getException().toString());
                }

                return dataRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    isUploading = false;
                    if (!activity.isFinishing()) {
                        Commn.hideDialog(activity);
                    }
                    Uri downloadUri = task.getResult();

                    Commn.showDebugLog("seeThisUri" + downloadUri.toString());// This is the one you should store

                    String POST_URL = downloadUri.toString();

                    updateImage(POST_URL);

                } else {
                    Log.i("wentWrong", "downloadUri failure");
                }
            });
        });

    }

    private void updateImage(String post_url) {

        String ref = DBConstants.UserInfo + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Images;
        if (isFirst_Image) {
            database.collection(DBConstants.UserInfo)
                    .document(profilePOJO.getUserId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.getResult().exists()) {
                            Map<String, Object> update = new HashMap<>();
                            update.put(DBConstants.image, post_url + "");
                            database.collection(DBConstants.UserInfo)
                                    .document(profilePOJO.getUserId())
                                    .update(update);
                            Commn.myToast(context, "updated");
                        }
                    });
        }
        Map<String, Object> update = new HashMap<>();
        update.put(DBConstants.image, post_url + "");
        update.put(DBConstants.image_id, image_id + "");
        database.collection(ref)
                .document(image_id)
                .set(update)
                .addOnCompleteListener(task ->
                        Commn.showErrorLog("image uploaded:" + image_id + ""));

    }
}