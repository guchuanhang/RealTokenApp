package com.zhpan.idea.net.common;


import com.zhpan.idea.TokenBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by dell on 2017/4/1.
 */

public interface CommonService {
    @FormUrlEncoded
    @POST("refreshToken")
    Observable<TokenBean> refreshToken(@Field("refresh_token") String refreshToken);
}
