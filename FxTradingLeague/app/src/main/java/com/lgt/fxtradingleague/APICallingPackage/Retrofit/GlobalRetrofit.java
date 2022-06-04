package com.lgt.fxtradingleague.APICallingPackage.Retrofit;

import com.lgt.fxtradingleague.Config;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GlobalRetrofit {
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
                .baseUrl(Config.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(ApiService.class);
    }

}
