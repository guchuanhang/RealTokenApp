package com.zhpan.idea.net.interceptor;

import com.zhpan.idea.ServerConfig;
import com.zhpan.idea.utils.KeyTools;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhpan on 2018/3/21.
 */

public class HttpHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Long timestamp = System.currentTimeMillis() / 1000;
        String token = "";
        if (null != ServerConfig.instance.tokenBean) {
            token = KeyTools.getMD5(String.format("%d&%s", timestamp, ServerConfig.instance.tokenBean.secretKey)).toLowerCase();
        }

        String deviceName = "";
        if (null != ServerConfig.instance.tokenBean) {
            deviceName = ServerConfig.instance.tokenBean.deviceName;
        }
        Request request = chain.request().newBuilder()
                .header("devicename", deviceName)
                .header("token", token)
                .addHeader("timestamp", "" + timestamp)
                .header("Content-Type", "application/json")
                .build();
        return chain.proceed(request);
    }
}
