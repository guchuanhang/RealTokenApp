package com.zhpan.idea.net.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zhpan.idea.net.converter.GsonConverterFactory;
import com.zhpan.idea.net.interceptor.HttpCacheInterceptor;
import com.zhpan.idea.net.interceptor.HttpHeaderInterceptor;
import com.zhpan.idea.utils.LogUtils;
import com.zhpan.idea.RealToken;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by zhpan on 2018/3/21.
 */

public class RetrofitService {

    public static Retrofit.Builder getRetrofitBuilder(String baseUrl) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        OkHttpClient okHttpClient = getOkHttpClientBuilder().build();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl);
    }

    private static OkHttpClient.Builder getOkHttpClientBuilder() {
        File cacheFile = new File(RealToken.getContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        HttpLoggingInterceptor loggingInterceptor = getLogInterceptor(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new HttpHeaderInterceptor())
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(new HttpCacheInterceptor())
                .cache(cache);
    }

    private static HttpLoggingInterceptor getLogInterceptor(HttpLoggingInterceptor.Level level) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor((String message) ->
                LogUtils.e("OKHttp-----", message)
        );
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}
