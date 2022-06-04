package com.app.cryptok.utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.app.cryptok.Api.SingletonRequestQueue;
import com.app.cryptok.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Commn {
    public static ProgressDialog progressDialog;

    public static SharedPreferences.Editor editor;
    public static String MODEL = "MODEL";
    public static String PRODUCT_MODEL = "PRODUCT_MODEL";
    public static String EDIT_TYPE = "EDIT_TYPE";
    public static String ADD_TYPE = "ADD_TYPE";
    public static String CHOOSE_TYPE = "CHOOSE_TYPE";
    public static String CHOOSE_ADDRESS = "CHOOSE_ADDRESS";
    public static String NORMAL_TYPE = "NORMAL_TYPE";
    public static String TYPE = "TYPE";
    public static String CHAT_TYPE = "CHAT_TYPE";
    public static String LIVE_TYPE = "LIVE_TYPE";
    public static String FOLLOW_TYPE = "FOLLOW_TYPE";
    public static String POST_LIKE_TYPE = "POST_LIKE_TYPE";
    public static String POST_COMMENT_TYPE = "POST_COMMENT_TYPE";
    public static String video_path = "video_path";
    public static String videos = "videos";
    public static String tbl_sound_id = "tbl_sound_id";
    public static String sound_uri = "sound_uri";
    public static String sound_name = "sound_name";
    public static String audio_type = "audio_type";
    public static String URL_TYPE = "URL_TYPE";
    public static String STORAGE_TYPE = "STORAGE_TYPE";
    public static String GOOGLE_TYPE = "GOOGLE_TYPE";
    public static String FACEBOOK_TYPE = "FACEBOOK_TYPE";
    public static String MOBILE_TYPE = "NORMAL";
    public static String Users_Info = "Users_Info";
    public static String Stream_Title = "Stream_Title";
    public static String USER_MOBILE = "USER_MOBILE";
    public static String RECIEVE_GIFT = "RECIEVE_GIFT";
    public static String SEND_GIFT = "SEND_GIFT";
    public static String SHARE = "SHARE";
    public static String WATCH_TYPE = "WATCH_TYPE";
    public static int TEXT_TYPE = 0;
    public static String user_id = "user_id";
    public static String user_name = "user_name";
    public static String STREAM_BASE_URL = "http://play.streamaxia.com/streamaxia/";
    public static String RTMP_BASE_URL = "rtmp://rtmp.streamaxia.com/streamaxia/";
    public static String placeholder_profile = "https://icons-for-free.com/iconfiles/png/512/avatar+person+profile+user+icon-1320086059654790795.png";
    public static String play_store_base_url = "https://play.google.com/store/apps/details?id=";
    //
    //trip hitory to trip details keys//
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String dummy_icon = "http://dhamakaapp.com/uploads/logo/watermark.png";

    public static void showShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static ProgressDialog progress = null;

    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showErrorLog(String message) {
        Log.e("common_log", message);
    }

    public static void showDebugLog(String message) {
        Log.d("common_log", message + "");
    }

    public static void showVerbosLog(String message) {
        Log.v("CommanError", message);
    }


    public static Boolean isValidTextInput(TextInputEditText editText, String message) {
        if (editText.getText().toString().equals("")) {
            editText.requestFocus();
            editText.setError(message);
            return false;
        }
        return true;
    }

    public static String getTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date(time);

        return formatter.format(date);
    }


    public static Boolean isValidInputEdit(EditText editText, String message) {
        if (editText.getText().toString().equals("")) {
            editText.requestFocus();
            editText.setError(message);
            return false;
        }
        return true;
    }

    public static Boolean isValidMobile(TextInputEditText editText) {
        if (editText.getText().toString().equals("")) {
            editText.requestFocus();
            editText.setError("Please Enter Valid Mobile Number");
            return false;
        }
        return true;
    }

    public static Boolean isValidMobileEdit(EditText editText) {
        if (editText.getText().toString().equals("")) {
            editText.requestFocus();
            editText.setError("Please Enter Valid Mobile Number");
            return false;
        }
        return true;
    }

    public static void myToast(Context context, String msg) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

    }

    public static void showDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

    }

    public static void hideDialog(Context context) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

    }

    public static int getAge(String dobString) {

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month + 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }


        return age;
    }

    public static void showCompressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public static void hideCompressDialog(Context context) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    public static String convertDuration(long start, long end) {

        long cal = end - start;
        long Minutes = (cal / 1000) / 60;
        long seconds = (cal / 1000) % 60;

        String converted = String.format("%d:%02d", Minutes, seconds);
        return converted;
    }

    public static void requestQueue(Context context, StringRequest stringRequest) {

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(context).getRequestQueue();
        requestQueue.add(stringRequest);

    }

    public static Bitmap getThumnail(Uri uri) {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(String.valueOf(uri), new HashMap<String, String>());
        try {
            final byte[] coverImage = mediaMetadataRetriever.getEmbeddedPicture();
            bitmap = BitmapFactory.decodeByteArray(coverImage, 0, coverImage.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a min ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " min ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static boolean checkInternetConnection(Context context) {

        //Check internet connection:
        boolean connected = true;
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //Means that we are connected to a network (mobile or wi-fi)
            connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        }


        return connected;
    }

    public static void showProgress(Context mContext) {
        try {
            if (mContext != null) {

                if (progress != null && progress.isShowing()) {
                    return;
                }

                progress = ProgressDialog.show(mContext, null, null);
                progress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progress.setContentView(R.layout.progress_loading);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideProgress() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }

    }


    //filter names
    public static String black_and_white_filter = "Black and white";
    public static String Vignette_filter = "Vignette";
    public static String Grayscale_filter = "Grayscale";
    public static String Documentry_filter = "Documentry";
    public static String Sepia_filter = "Sepia";
    public static String tint_filter = "Tint";
    public static String auto_filter = "Auto Mix";
    public static String cross_filter = "Cross Process";

    //

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                // This is for checking Main Memory
                if ("primary".equalsIgnoreCase(type)) {
                    if (split.length > 1) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    } else {
                        return Environment.getExternalStorageDirectory() + "/";
                    }
                    // This is for checking SD Card
                } else {
                    return "storage" + "/" + docId.replace(":", "/");
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                String fileName = getFilePath(context, uri);
                if (fileName != null) {
                    return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                }

                String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    id = id.replaceFirst("raw:", "");
                    File file = new File(id);
                    if (file.exists())
                        return id;
                }

                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getFilePath(Context context, Uri uri) {

        Cursor cursor = null;
        final String[] projection = {
                MediaStore.MediaColumns.DISPLAY_NAME
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, null, null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String prettyCount(Number number) {

        try {
            char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
            long numValue = number.longValue();
            int value = (int) Math.floor(Math.log10(numValue));
            int base = value / 3;
            if (value >= 3 && base < suffix.length) {
                double value2 = numValue / Math.pow(10, base * 3);
                if (String.valueOf(value2).contains(".")) {
                    String num = String.valueOf(value2).split("\\.")[String.valueOf(value2).split("\\.").length - 1];
                    if (num.contains("0")) {
                        return new DecimalFormat("#0").format(value2) + suffix[base];
                    } else {
                        return new DecimalFormat("#0.0").format(value2) + suffix[base];
                    }
                } else {
                    return new DecimalFormat("#0").format(value2) + suffix[base];
                }
            } else {
                return new DecimalFormat("#,##0").format(numValue);
            }
        } catch (Exception e) {
            return String.valueOf(number);
        }
    }


    private static float maxHeight = 1280.0f;
    private static float maxWidth = 1280.0f;

    public static String compressImage(String imagePath, Context context) {
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        if (bmp != null) {
            bmp.recycle();
        }

        ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filepath = getFilename(context);
        try {
            out = new FileOutputStream(filepath);

            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filepath;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static String getFilename(Context context) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/DCIM/"
                + context.getApplicationContext().getPackageName()
                + "/Files/Compressed");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        String mImageName = "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        String uriString = (mediaStorageDir.getAbsolutePath() + "/" + mImageName);

        return uriString;

    }

    //to add levels notificationModel
  /*  private void addLevelsData() {
        CollectionReference reference = firebaseFirestore.collection(DBConstants.all_levels_points);
        for (int i = 1; i < 61; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("each", 1000);
            map.put("level", String.valueOf(i));
            map.put("point", 1000);
            map.put("point_id", String.valueOf(i));
            reference.document(String.valueOf(i))
                    .set(map)
                    .addOnCompleteListener(task -> {
                        Commn.showDebugLog("adding_levels_data:");
                    });
        }
    }*/




}

