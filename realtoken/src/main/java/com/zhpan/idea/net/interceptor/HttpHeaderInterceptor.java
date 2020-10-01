package com.zhpan.idea.net.interceptor;

import com.zhpan.idea.ServerConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = "";
        if (null != ServerConfig.instance.tokenBean) {
            token = ServerConfig.instance.tokenBean.refreshTokenStr;
        }
        Request request = chain.request().newBuilder()
                .header("token", token)
                .header("Content-Type", "application/json")
                .build();
        return chain.proceed(request);
    }
}
