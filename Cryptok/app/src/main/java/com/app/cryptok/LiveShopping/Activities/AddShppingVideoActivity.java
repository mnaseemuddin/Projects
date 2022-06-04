package com.app.cryptok.LiveShopping.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.Api.ProgressRequestBody;
import com.app.cryptok.R;
import com.app.cryptok.databinding.ActivityAddShppingVideoBinding;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.CustomDialogBuilder;
import com.app.cryptok.utils.Permissions;
import com.app.cryptok.utils.SessionManager;
import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.TranscoderOptions;
import com.otaliastudios.transcoder.strategy.DefaultVideoStrategy;
import com.otaliastudios.transcoder.strategy.size.AtMostResizer;
import com.otaliastudios.transcoder.strategy.size.FractionResizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import wseemann.media.FFmpegMediaMetadataRetriever;

import static com.app.cryptok.utils.Commn.getRealPathFromURI_API19;

public class AddShppingVideoActivity extends AppCompatActivity {
    private ActivityAddShppingVideoBinding binding;
    private AddShppingVideoActivity activity;
    private Context context;
    private String VIDEO_BITRATE = "650000";
    long fileSizeInMB;
    long LONG_BITRATE = 650000;
    private String video_Path;
    private File video_file, outputFile;
    private String mVideo_name, mVideoPath = "";
    private String image_thumbg = "";
    private CompositeDisposable disposable = new CompositeDisposable();
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private String image_thumb_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_shpping_video);
        context = activity = this;
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        handleClick();
    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.rlAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkStoragePermission();
            }
        });

        binding.btnAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUpload();
            }
        });
    }

    private void validateUpload() {
        mVideo_name = binding.etVideoName.getText().toString();
        if (TextUtils.isEmpty(mVideo_name)) {
            binding.etVideoName.setError("Enter Video name");
            return;
        }
        if (mVideoPath.isEmpty()) {
            Commn.myToast(context, "choose video...");
            return;
        }

        iniUploadVideo();
    }

    private void iniUploadVideo() {
        image_thumbg = getPath().getPath().concat("/" + image_thumb_name);
        video_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                getString(R.string.app_name) + "/" + Commn.videos;
        video_file = new File(video_Path);

        if (!video_file.mkdirs()) {
            video_file.mkdirs();
        }

        if (video_file.isDirectory()) {

            outputFile = new File(video_file, String.valueOf(System.currentTimeMillis()) + ".mp4");
            if (mVideoPath != null) {
                runOnUiThread(this::startCompress);

            }
        } else {
            video_file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/" + Commn.videos);
            video_file.mkdirs();
            outputFile = new File(video_file, String.valueOf(System.currentTimeMillis()) + ".mp4");
            if (mVideoPath != null) {
                runOnUiThread(this::startCompress);

            }
        }
    }

    private void checkStoragePermission() {
        TedPermission.with(context)
                .setPermissionListener(stoargelistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Permissions.storage_permissions)
                .check();

    }

    PermissionListener stoargelistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            openPicker();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            checkStoragePermission();

        }
    };

    private void openPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 2);
        } else {
            Toast.makeText(
                    this,
                    "No Gallery APP installed",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 2) {
            // Open Editor with some uri in this case with an image selected from the system gallery.
            Uri selectedImage = data.getData();
            try {
                String realPath = getRealPathFromURI_API19(context, selectedImage);

                File file = new File(realPath);
                if (!file.exists()) {
                    return;
                }
                // Get length of file in bytes
                long fileSizeInBytes = file.length();
                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                long fileSizeInKB = fileSizeInBytes / 1024;
                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                long fileSizeInMB = fileSizeInKB / 1024;
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(this, Uri.fromFile(new File(realPath)));
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInMilliSec = Long.parseLong(time);

                if (fileSizeInMB > 200) {
                    Commn.myToast(context, "Video size is too big please choose another video...");
                } else {
                    Log.d("video_path", "" + selectedImage);

                    mVideoPath = realPath;
                    commpressVideo();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Log.d("error", "something big");
            }
        }

    }

    private void commpressVideo() {
        Commn.showDebugLog("choosed_video:" + mVideoPath);

        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this, Uri.fromFile(new File(mVideoPath)));
            if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE) != null) {
                VIDEO_BITRATE = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
            } else {
                LONG_BITRATE = 650000;
            }
            if (retriever != null) {
                retriever.release();
            }
            getThumb();
            Commn.showDebugLog("VIDEO_BITRATE:" + "original_bitrate=" + VIDEO_BITRATE + "");
            if (!TextUtils.isEmpty(VIDEO_BITRATE.trim()) && VIDEO_BITRATE != null) {
                int bitrate = Integer.parseInt(VIDEO_BITRATE);
                int percent = (bitrate * 20) / 100;
                int calulate_bitrate = bitrate - percent;
                LONG_BITRATE = (long) calulate_bitrate;
                Commn.showDebugLog("VIDEO_BITRATE:" + "customized_bitrate=" + LONG_BITRATE + "");
            } else {
                LONG_BITRATE = 650000;
            }

        } catch (Exception e) {
            e.printStackTrace();
            LONG_BITRATE = 650000;
        }


    }

    private void getThumb() {
        image_thumb_name = String.valueOf(System.currentTimeMillis()) + ".jpg";
        File thumbFile = new File(getPath(), image_thumb_name);
        try {
            FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
            retriever.setDataSource(mVideoPath);
            FileOutputStream stream = new FileOutputStream(thumbFile);
            Bitmap bmThumbnail;
            bmThumbnail = retriever.getFrameAtTime(1);
            if (bmThumbnail != null) {
                Commn.showDebugLog("thumb not null by mediaretirver");
                bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            } else {
                Commn.showDebugLog("thumb null by mediaretirver");
            }
            stream.flush();
            stream.close();
            retriever.release();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            String image_thumbg = filesDir.getPath();
            Commn.showDebugLog("my_image_path:" + image_thumbg + "");
        }
        return filesDir;
    }

    private CustomDialogBuilder customDialogBuilder;

    private void startCompress() {
        if (customDialogBuilder == null) {

            customDialogBuilder = new CustomDialogBuilder(activity);

        }
        customDialogBuilder.showLoadingDialog();
        customDialogBuilder.showPercentageLoading();
        customDialogBuilder.tv_percentage.setVisibility(View.VISIBLE);

        DefaultVideoStrategy strategy = new DefaultVideoStrategy.Builder()
                .addResizer(new FractionResizer(0.7f))
                .addResizer(new AtMostResizer(1000))
                .bitRate(LONG_BITRATE)
                .build();

        TranscoderOptions.Builder options = Transcoder.into(outputFile.getAbsolutePath());
        options.addDataSource(mVideoPath);
        options.setVideoTrackStrategy(strategy);
        options.setListener(new TranscoderListener() {
            @Override
            public void onTranscodeProgress(double progress) {
                double innpres = progress * 100;
                Commn.showDebugLog("MediaTranscoder:" + "onTranscodeProgress:" + innpres + "");
                if (customDialogBuilder.tv_percentage != null) {
                    customDialogBuilder.tv_percentage.setText("");
                    customDialogBuilder.tv_percentage.setText(String.valueOf("Processing Video..." + (int) innpres) + "%");
                }
            }

            @Override
            public void onTranscodeCompleted(int successCode) {
                Commn.showDebugLog("MediaTranscoder:" + "onTranscodeCompleted:");

                mVideoPath = outputFile.getAbsolutePath();

                startUploadVideo();

            }

            @Override
            public void onTranscodeCanceled() {

            }

            @Override
            public void onTranscodeFailed(@NonNull Throwable exception) {
                Log.d("MediaTranscoder", "onTranscodeFailed:" + exception.getMessage() + "");
            }

        }).transcode();
    }

    private void startUploadVideo() {
        prepareVideoThumbMultipart();
        prepareVideoMultipart();
        HashMap<String, RequestBody> params = new HashMap<>();
        params.put(DBConstants.user_id, toRequestBody(profilePOJO.getUserId() + ""));
        params.put("video_title", toRequestBody(mVideo_name + ""));

        disposable.add(MyApi.initRetrofit().uploadVideo(params, videoBody, videoThumbBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null && dates.getStatus() != null && !dates.getStatus().isEmpty()) {
                        Commn.myToast(context, dates.getMessage() + "");
                        if ("1".equalsIgnoreCase(dates.getStatus())) {

                            customDialogBuilder.hideLoadingDialog();
                            onBackPressed();
                        }
                        Commn.showDebugLog("uploadMultipleMedia_response:" + new Gson().toJson(dates));

                    }
                }));
    }

    MultipartBody.Part videoThumbBody;
    MultipartBody.Part videoBody;

    public RequestBody toRequestBody(String value) {
        Commn.showDebugLog("toRequestBody:update_user:" + value);
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    private void prepareVideoThumbMultipart() {
        if (image_thumbg != null) {
            File file_thumb = new File(image_thumbg + "");
            if (file_thumb.exists()) {
                videoThumbBody = videoThumbMultipart("video_thumb", file_thumb);
            } else {
                videoThumbBody = null;
            }
        } else {
            videoThumbBody = null;
        }

    }

    private void prepareVideoMultipart() {
        if (mVideoPath != null) {
            File file_thumb = new File(mVideoPath + "");
            if (file_thumb.exists()) {
                videoBody = videoMultipart("videos", file_thumb);
            } else {
                videoBody = null;
            }
        } else {
            videoBody = null;
        }
    }

    @NonNull
    private MultipartBody.Part videoThumbMultipart(String partName, File fileUri) {

        Commn.showDebugLog("prepareFilePart:" + fileUri.getAbsolutePath());
        ProgressRequestBody requestBody = new ProgressRequestBody(fileUri, percentage -> {
            try {
                Commn.showDebugLog("onProgress:" + percentage + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, fileUri.getName(), requestBody);
    }

    @NonNull
    private MultipartBody.Part videoMultipart(String partName, File fileUri) {

        Commn.showDebugLog("prepareFilePart:" + fileUri.getAbsolutePath());
        ProgressRequestBody requestBody = new ProgressRequestBody(fileUri, percentage -> {
            try {
                customDialogBuilder.tv_percentage.setText(String.valueOf("Uploading Video..." + (int) percentage) + "%");
                Commn.showDebugLog("onProgress:" + percentage + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, fileUri.getName(), requestBody);
    }
}