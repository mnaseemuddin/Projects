package com.app.cryptok.Api;

import com.app.cryptok.LiveShopping.Model.AddressListModel;
import com.app.cryptok.LiveShopping.Model.CartListModel;
import com.app.cryptok.LiveShopping.Model.ConfirmOrderModel;
import com.app.cryptok.LiveShopping.Model.DeliveryChargesModel;
import com.app.cryptok.LiveShopping.Model.OrderHistoryModel;
import com.app.cryptok.LiveShopping.Model.ProductModel;
import com.app.cryptok.LiveShopping.Model.ProductVideosModel;
import com.app.cryptok.Notifications.NotificationModel;
import com.app.cryptok.Notifications.Notification_Const;
import com.app.cryptok.Notifications.Sender;
import com.app.cryptok.model.CommonResponse;
import com.app.cryptok.model.streamBean;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;


public interface ApiService {
    @GET("rest-api/stream/find_all")
    Single<streamBean> getStreams();

    @Multipart
    @POST("add_user_video_api.php")
    Single<CommonResponse> uploadVideo(@PartMap HashMap<String, RequestBody> hasMap,
                                       @Part MultipartBody.Part postVideo,
                                       @Part MultipartBody.Part postthumb);

    @Multipart
    @POST("add_user_products_api.php")
    Single<CommonResponse> add_products_api(@PartMap HashMap<String, RequestBody> hasMap, @Part List<MultipartBody.Part> images);


    @FormUrlEncoded
    @POST("user_products_list_api.php")
    Single<ProductModel> product_list_Api(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("update_user_products_api.php")
    Single<CommonResponse> updateProductApi(@FieldMap HashMap<String, Object> hashMap);

    @Multipart
    @POST("update_products_image_api.php")
    Single<CommonResponse> update_product_image(@PartMap HashMap<String, RequestBody> hasMap,
                                                @Part MultipartBody.Part cover_image);

    @FormUrlEncoded
    @POST("user_video_list_api.php")
    Single<ProductVideosModel> productVideosApi(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("user_video_delete_api.php")
    Single<CommonResponse> delete_Video_api(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("user_cart_list_api.php")
    Single<CartListModel> cart_list_api(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("update_cart_qnt_api.php")
    Single<CommonResponse> update_quantity(@FieldMap HashMap<String, Object> hashMap);


    @FormUrlEncoded
    @POST("add_to_cart_api.php")
    Single<CommonResponse> addToCartApi(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("user_address_list.php")
    Single<AddressListModel> getAddressList(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("delet_user_address_api.php")
    Single<CommonResponse> deleteAddressApi(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("add_user_address.php")
    Single<CommonResponse> addAddressApi(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("user_address_update_api.php")
    Single<CommonResponse> updateAddressApi(@FieldMap HashMap<String, Object> hashMap);


    @FormUrlEncoded
    @POST("check_address_api.php")
    Single<CommonResponse> checkAddressAvailable(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("confirm_order_api.php")
    Single<ConfirmOrderModel> placeOrderApi(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("user_order_list_api.php")
    Single<OrderHistoryModel> orderHistoryApi(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("sell_order_list_api.php")
    Single<OrderHistoryModel> sellorderHistoryApi(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("add_delivery_charges_api.php")
    Single<CommonResponse> addDeliveryCharges(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("delivery_charges_list_api.php")
    Single<DeliveryChargesModel> getDeliveryChargesList(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("delete_delivery_charges_api.php")
    Single<CommonResponse> deleteDeliveryCharges(@FieldMap HashMap<String, Object> hashMap);

    @Headers({"Authorization:key=" + Notification_Const.serverKey,
            "Content-Type:application/json"})
    @POST("fcm/send")
    Single<NotificationModel> pushNotification(@Body Sender notificationRequest);
}
