package com.app.cryptok.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApi {

    public static String base_url = "http://superlive.io/Admin/api/";
   // public static String base_url = "http://15.207.95.141/Admin/api/";
    public static String upload_base_url = "http://superlive.io/";
    public static String web_url = "http://superlive.io";
    public static String place_holder = upload_base_url + "icon/icon.png";
    public static String login_api = base_url + "login.php";
    public static String diamond_list_api = base_url + "diamond_list_api.php";
    public static String packages_list_api = base_url + "package_list_api.php";
    public static String exchange_diamond_list_api = base_url + "exchange_diamond_list_api.php";
    public static String GIF_Url = "https://api.tenor.com/v1/search?q=";
    public static String GIF_API_KEY = "UY7H8NPGG33K";


    public static ApiService initRetrofit() {
        // For logging request & response (Optional)
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor interceptor = chain -> {
            Request request = chain.request();
            Request newRequest = request.newBuilder()
                    //.addHeader("Authorization", Global.ACCESS_TOKEN)
                    .build();
            return chain.proceed(newRequest);
        };
        OkHttpClient.Builder builder =
                new OkHttpClient.Builder();
        builder.networkInterceptors().add(interceptor);
        OkHttpClient client = builder.addInterceptor(loggingInterceptor)
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(ApiService.class);
    }
    public static ApiService initPushNotification() {
        // For logging request & response (Optional)

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }
}
