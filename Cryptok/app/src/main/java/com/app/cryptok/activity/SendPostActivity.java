package com.app.cryptok.activity;

import static com.app.cryptok.activity.LoginActivity.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import com.app.cryptok.Api.DBConstants;

import com.app.cryptok.R;
import com.app.cryptok.adapter.ImagePreviewAdapter;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.BaseActivity;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikelau.croperino.Croperino;
import com.mikelau.croperino.CroperinoConfig;
import com.mikelau.croperino.CroperinoFileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class SendPostActivity extends BaseActivity implements ImagePreviewAdapter.OnRemove {
    @Override
    protected int setLayout() {
        return R.layout.activity_send_post;
    }

    private ImagePreviewAdapter adapter;
    private ArrayList<String> imageList = new ArrayList<>();
    LinearLayout ll_gallery, ll_camera;
    private TextView tv_post_image,tv_photo_gallery;
    Context context;
    Activity activity;
    RecyclerView rv_preview_image;
    private ImageView iv_back;
    private EditText et_post_caption;
    private String CAPTION = "";
    ProfilePOJO profilePOJO;
    private SessionManager sessionManager;
    private String POST_ID;
    private String POST_URL = " ", Imagepaths = "";

    private FirebaseFirestore firebaseFirestore;
    private boolean isUploading = false;
    File filePath;

    @Override
    protected void onLaunch() {
        context = activity = this;

        iniViews();

        handleClick();

    }

    private void handleClick() {
        ll_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageList.size() > 0) {

                    Commn.myToast(context, "you cannot upload more than one");
                } else {
                    //openGallery();
                    checkStoragePermission();
                }
            }
        });
     tv_post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isUploading) {
                    Commn.showDebugLog("started posting...");
                    if (Imagepaths != null) {
                        CAPTION = et_post_caption.getText().toString();
                        startPost(Imagepaths);
                        Log.e("checkrealpath", Imagepaths);
                    } else {
                        Commn.myToast(context, "you have choose image");
                        Commn.showDebugLog("something is wrong on starting posting...");
                    }
                } else {
                    Commn.myToast(context, "please wait ,post already uploading...");
                }

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void checkStoragePermission() {

        Dexter.withActivity(SendPostActivity.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).
                withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        Log.e(TAG, "onPermissionsChecked:dsada " + report + "");
                        if (report.areAllPermissionsGranted()) {
                            pickImage();
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(SendPostActivity.this, "Please allow all permissions from setting", Toast.LENGTH_SHORT).show();
                            showSettingsDialog();
                        } else {
                            Toast.makeText(SendPostActivity.this, "All permissions are required", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }

                }).onSameThread().check();

    }

    private void pickImage() {
        Options options = Options.init()
                .setRequestCode(1) //Request code for activity results
                .setCount(1) //Number of images to restict selection count
                .setFrontfacing(false) //Front Facing camera on start
                .setSpanCount(4) //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.Picture) //Option to select only pictures or videos or both
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);
        Pix.start((FragmentActivity) activity, options);
    }

    private void showSettingsDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SendPostActivity.this);
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

                    Imagepaths = returnValue.get(0);

                    adapter = new ImagePreviewAdapter(context, returnValue, this);
                    rv_preview_image.setAdapter(adapter);
                    tv_photo_gallery.setText("Change Image");
                    tv_post_image.setVisibility(View.VISIBLE);

                    // compreessImage(returnValue.get(0));
                }
            }
        }
    }

    private void startPost(String imagepaths) {
        String compressed_path = Commn.compressImage(imagepaths, context);
        Commn.showDebugLog("compressed_path:" + compressed_path + "");

        uploadImage(compressed_path);

    }

    public class ImageCompression extends AsyncTask<String, Void, String> {

        private Context context;

        public ImageCompression(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0 || strings[0] == null)
                return null;

            return Commn.compressImage(strings[0], context);
        }

        protected void onPostExecute(String imagePath) {
            // imagePath is path of new compressed image.
            Commn.showDebugLog("my compressed path:" + imagePath + " ");
            if (filePath != null) {
                uploadImage(imagePath);
            }
        }
    }

    private void uploadImage(String bitmap) {
        String post_image_name = System.currentTimeMillis() + "-" + profilePOJO.getUserId() + ".png";
        if (post_image_name == null) {
            post_image_name = generatePostId() + ".png";
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference dataRef = storageRef.child(DBConstants.User_Posts_Storage).child(profilePOJO.getUserId())
                .child(post_image_name);


        final UploadTask uploadTask = dataRef.putFile(Uri.fromFile(new File(bitmap)));

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("whatTheFuck:", e.toString());
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                //calculating progress percentage
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                //displaying percentage in progress dialog
                Commn.myToast(context, "Uploading post : " + String.valueOf(((int) progress) + "%..."));

                isUploading = true;
            }
        }).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.i("problem", task.getException().toString());
                    }

                    return dataRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        isUploading = false;

                        Uri downloadUri = task.getResult();

                        Log.i("seeThisUri", downloadUri.toString());// This is the one you should store

                        POST_URL = downloadUri.toString();

                        addPost();

                    } else {
                        Log.i("wentWrong", "downloadUri failure");
                    }
                }
            });
        });

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

    private void addPost() {
        CollectionReference collectionReference = firebaseFirestore.collection(DBConstants.User_Posts);
        DocumentReference Id_Ref = firebaseFirestore.collection(DBConstants.User_Posts).document();
        POST_ID = Id_Ref.getId() + "";

        Map<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_image, profilePOJO.getImage());
        map.put(DBConstants.user_name, profilePOJO.getName());
        map.put(DBConstants.timestamp, FieldValue.serverTimestamp());
        map.put(DBConstants.post_caption, CAPTION + "");
        map.put(DBConstants.post_id, POST_ID);
        map.put(DBConstants.user_id, profilePOJO.getUserId());
        map.put(DBConstants.post_image, POST_URL);

        collectionReference.document(POST_ID).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    startActivity(new Intent(activity, MainActivity.class));
                    finishAffinity();
                    Commn.myToast(context, "Post Uploaded..");
                    Commn.showDebugLog("image upload ho gya....");
                } else {
                    Commn.showDebugLog("something is wrong:" + task.getException() + "");
                }
            }
        });

    }

    private String generatePostId() {
        String post_id = UUID.randomUUID().toString();
        return post_id;
    }

    private void openCamera() {
        iniCrop();
        Croperino.prepareCamera(activity);
    }

    private void iniCrop() {
        //Initialize on every usage
        new CroperinoConfig("IMG_" + System.currentTimeMillis() + ".jpg", "/MikeLau/Pictures", "/sdcard/MikeLau/Pictures");
        CroperinoFileUtil.verifyStoragePermissions(activity);
        CroperinoFileUtil.setupDirectory(activity);

        // Croperino.prepareChooser(activity, "Capture photo...", ContextCompat.getColor(activity, android.R.color.background_dark));
    }

    private void iniViews() {
        sessionManager = new SessionManager(this);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Log.d("infoo", profilePOJO.getName() + "");
        rv_preview_image = find(R.id.rv_preview_image);
        rv_preview_image.setLayoutManager(new GridLayoutManager(context, 2));
        ll_camera = find(R.id.ll_camera);
        ll_gallery = find(R.id.ll_gallery);
        tv_post_image = find(R.id.tv_post_image);
        et_post_caption = find(R.id.et_post_caption);
        iv_back = find(R.id.iv_back);
        tv_photo_gallery = find(R.id.tv_photo_gallery);

    }

    @Override
    public void onRemove(int position) {
        tv_post_image.setVisibility(View.GONE);
        imageList.remove(position);
        adapter.notifyDataSetChanged();

    }
}

