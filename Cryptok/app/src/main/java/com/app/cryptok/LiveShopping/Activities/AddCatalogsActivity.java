package com.app.cryptok.LiveShopping.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.Api.ProgressRequestBody;
import com.app.cryptok.R;
import com.app.cryptok.LiveShopping.Adapter.SelectedImagesAdapter;
import com.app.cryptok.activity.ViewImageActivity;
import com.app.cryptok.LiveShopping.Adapter.ProductImageAdapter;
import com.app.cryptok.databinding.ActivityAddCatalogsBinding;
import com.app.cryptok.LiveShopping.Model.ProductModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.CustomDialogBuilder;
import com.app.cryptok.utils.FastClickUtil;
import com.app.cryptok.utils.Permissions;
import com.app.cryptok.utils.SessionManager;
import com.vlk.multimager.activities.GalleryActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class AddCatalogsActivity extends AppCompatActivity {
    private ActivityAddCatalogsBinding binding;
    private Context context;
    private AddCatalogsActivity activity;
    private String mProduct_name, mProduct_price, mProduct_desc;
    private boolean isFilled = true;

    private SelectedImagesAdapter selectedImagesAdapter = new SelectedImagesAdapter();
    private CompositeDisposable disposable = new CompositeDisposable();
    private String type = "", update_image_id = "";
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private ProductModel.ProductsDatum productModel;
    private ProductImageAdapter productImageAdapter = new ProductImageAdapter();
    private boolean add_image_type = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_catalogs);
        context = activity = this;
        iniView();
        iniIntent();
        iniObserver();
        handleClick();
    }

    private void iniObserver() {
        productImageAdapter.onImageClick = (type, position, model, holder_binding) -> {
            switch (type) {
                case 1:
                    Intent intent = new Intent(context, ViewImageActivity.class);
                    intent.putExtra(DBConstants.user_image, model.getImages());
                    startActivity(intent);
                    break;

                case 2:
                    add_image_type = false;
                    update_image_id = model.getTblUserProductsImageId();
                    updateImagePicker();
                    break;
            }
        };
        selectedImagesListner();
    }

    private void updateImagePicker() {
        Intent intent = new Intent(this, GalleryActivity.class);
        Params params = new Params();
        params.setPickerLimit(1);
        params.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
        params.setActionButtonColor(context.getResources().getColor(R.color.colorPrimary));
        params.setButtonTextColor(context.getResources().getColor(R.color.colorPrimary));
        intent.putExtra(Constants.KEY_PARAMS, params);
        startActivityForResult(intent, Constants.TYPE_MULTI_PICKER);
    }

    private void iniView() {
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }

    private void iniIntent() {
        if (getIntent().hasExtra(Commn.TYPE)) {
            type = getIntent().getStringExtra(Commn.TYPE);
            if (Commn.EDIT_TYPE.equalsIgnoreCase(type)) {
                showEditUi();
                productModel = new Gson().fromJson(getIntent().getStringExtra(Commn.MODEL), ProductModel.ProductsDatum.class);
                if (productModel != null) {
                    setProductInfo();
                }


            } else {
                showAddUi();
            }
        }
    }

    private void showAddUi() {
        binding.rvProductImages.setVisibility(View.GONE);
        binding.btnAddImage.setVisibility(View.VISIBLE);
        binding.btnAddProduct.setText("Add Product");
    }

    private void showEditUi() {
        binding.rvProductImages.setVisibility(View.VISIBLE);
        binding.btnAddImage.setVisibility(View.GONE);
        binding.btnAddProduct.setText("Update Product");
    }

    private void setProductInfo() {

        binding.etProductName.setText(productModel.getProName());
        binding.etProductDesc.setText(productModel.getProDescription());
        binding.etProductPrice.setText(productModel.getProPrice());
        productImageAdapter.updateData(productModel.getProimages());


        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.rvProductImages);
        binding.rvProductImages.setAdapter(productImageAdapter);
    }

    private void handleClick() {

        binding.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (Commn.EDIT_TYPE.equalsIgnoreCase(type)) {
                    updateProduct();
                } else {
                    add_image_type = true;
                    validateProduct();
                }

            }
        });


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedImagesAdapter.mList.size() > 6) {
                    Commn.myToast(context, "you cannot upload more than 6 images...");
                } else {
                    checkStoragePermission();
                }

            }
        });

    }

    private void updateProduct() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_id, profilePOJO.getUserId() + "");
        map.put("tbl_user_products_id", productModel.getTblUserProductsId() + "");
        map.put("pro_name", binding.etProductName.getText().toString() + "");
        map.put("pro_price", binding.etProductPrice.getText().toString() + "");
        map.put("pro_description", binding.etProductDesc.getText().toString() + "");
        map.put("pro_status", "1");
        Commn.showDebugLog("getProductList_params:" + new Gson().toJson(map));
        disposable.add(MyApi.initRetrofit().updateProductApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.showDebugLog("getProductList_response:" + new Gson().toJson(dates));
                        Commn.myToast(context, dates.getMessage());
                        onBackPressed();
                    }
                }));

    }

    private void validateProduct() {
        mProduct_name = binding.etProductName.getText().toString();
        mProduct_price = binding.etProductPrice.getText().toString();
        mProduct_desc = binding.etProductDesc.getText().toString();
        isFilled = true;

        if (TextUtils.isEmpty(mProduct_name)) {
            isFilled = false;
            binding.etProductName.setError("Enter Product name...");
            binding.etProductName.requestFocus();
        }
        if (TextUtils.isEmpty(mProduct_price)) {
            isFilled = false;
            binding.etProductPrice.setError("Enter Product price...");
            binding.etProductDesc.requestFocus();

        }
        if (TextUtils.isEmpty(mProduct_desc)) {
            isFilled = false;
            binding.etProductDesc.setError("Enter Product Description...");
            binding.etProductDesc.requestFocus();
        }
        if (selectedImagesAdapter.mList != null && selectedImagesAdapter.mList.size() > 6) {
            Commn.myToast(context, "you cannot upload more than 6 images...");
            isFilled = false;
        }

        if (isFilled) {

            addProductApi();
        }
        if (selectedImagesAdapter.mList.size() < 1) {
            isFilled = false;
            Commn.myToast(context, "choose product image");
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
        Intent intent = new Intent(this, GalleryActivity.class);
        Params params = new Params();
        params.setPickerLimit(6);
        params.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
        params.setActionButtonColor(context.getResources().getColor(R.color.colorPrimary));
        params.setButtonTextColor(context.getResources().getColor(R.color.colorPrimary));
        intent.putExtra(Constants.KEY_PARAMS, params);
        startActivityForResult(intent, Constants.TYPE_MULTI_PICKER);
    }

    private void addProductApi() {

        addProducts();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.TYPE_MULTI_PICKER:
                ArrayList<Image> imagesList = data.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                Commn.showDebugLog("imagesList:" + new Gson().toJson(imagesList));
                compreessImage(imagesList);


                break;
        }
    }

    private void compreessImage(ArrayList<Image> imagesList) {

        for (int i = 0; i < imagesList.size(); i++) {
            ImageCompression imageCompression = new ImageCompression(context);
            imageCompression.execute(imagesList.get(i).imagePath);
        }

    }

    @SuppressLint("StaticFieldLeak")
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
            selectedImagesAdapter.mList.add(imagePath);
            setSelectedImages();
            if (imagePath != null) {
                Commn.showDebugLog("compressed_path:" + new Gson().toJson(selectedImagesAdapter.mList));

                if (!add_image_type) {
                    updateImageApi();
                }
            }
        }
    }

    private void selectedImagesListner() {
        selectedImagesAdapter.onHolderClick = (type, position, model, holder_binding) -> {
            switch (type) {
                case 1:
                    removeImage(position);
                    break;
            }
        };
    }

    private void removeImage(int position) {
        if (selectedImagesAdapter.mList.size() > 0) {
            selectedImagesAdapter.mList.remove(position);
            selectedImagesAdapter.notifyItemRemoved(position);
            selectedImagesAdapter.notifyItemRangeRemoved(position, selectedImagesAdapter.mList.size());
        }

    }

    private void setSelectedImages() {


        binding.rvSelectedImages.setAdapter(selectedImagesAdapter);
        selectedImagesAdapter.updateData(selectedImagesAdapter.mList);
    }

    MultipartBody.Part product_image_part = null;

    private void updateImageApi() {
        File file = new File(selectedImagesAdapter.mList.get(0));
        product_image_part = prepareFilePart("pro_images", file);
        if (product_image_part == null) {
            return;
        }
        HashMap<String, RequestBody> map = new HashMap<>();
        //  map.put("user_id", toRequestBody(sessionManager.getUserDeatail().getUser_id()));

        map.put("user_id", toRequestBody(profilePOJO.getUserId()));
        map.put("tbl_user_products_image_id", toRequestBody(update_image_id));
        showProgress();
        disposable.add(MyApi.initRetrofit().update_product_image(map, product_image_part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    hideProress();
                    if (dates != null && dates.getStatus() != null && !dates.getStatus().isEmpty()) {
                        Commn.showDebugLog("updateImageApi_response:" + new Gson().toJson(dates));
                        hideProress();
                        onBackPressed();
                        Commn.myToast(context, dates.getMessage());
                    }
                }));
    }

    private void addProducts() {
        List<MultipartBody.Part> list = new ArrayList<>();

        for (int i = 0; i < selectedImagesAdapter.mList.size(); i++) {
            File file = new File(selectedImagesAdapter.mList.get(i));

            MultipartBody.Part imageRequest = prepareFilePart("pro_images[]", file);
            list.add(imageRequest);

        }
        if (list.size() == 0) {
            return;
        }
        HashMap<String, RequestBody> map = new HashMap<>();
        //  map.put("user_id", toRequestBody(sessionManager.getUserDeatail().getUser_id()));
        Commn.showDebugLog("uploadImages_params:" + map.toString() + ",list_part:" + new Gson().toJson(list));

        map.put("user_id", toRequestBody(profilePOJO.getUserId()));
        map.put("pro_name", toRequestBody(mProduct_name));
        map.put("pro_price", toRequestBody(mProduct_price));
        map.put("pro_description", toRequestBody(mProduct_desc));

        showProgress();
        disposable.add(MyApi.initRetrofit().add_products_api(map, list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    hideProress();
                    if (dates != null && dates.getStatus() != null && !dates.getStatus().isEmpty()) {
                        Commn.showDebugLog("add_products_api_response:" + new Gson().toJson(dates));

                        onBackPressed();
                    }
                }));

    }

    private void hideProress() {
        customDialogBuilder.hideLoadingDialog();
    }

    private CustomDialogBuilder customDialogBuilder;

    private void showProgress() {
        if (customDialogBuilder == null) {

            customDialogBuilder = new CustomDialogBuilder(activity);

        }
        customDialogBuilder.showLoadingDialog();
        customDialogBuilder.showPercentageLoading();
        customDialogBuilder.tv_percentage.setVisibility(View.VISIBLE);
    }

    public RequestBody toRequestBody(String value) {
        Commn.showDebugLog("toRequestBody:update_user:" + value);
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, File fileUri) {

        Commn.showDebugLog("prepareFilePart:" + fileUri.getAbsolutePath());
        ProgressRequestBody requestBody = new ProgressRequestBody(fileUri, percentage -> {
            try {
                Commn.showDebugLog("onProgress:" + percentage + "");
                customDialogBuilder.tv_percentage.setText(String.valueOf("Uploading Product..." + (int) percentage) + "%");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, fileUri.getName(), requestBody);
    }
}