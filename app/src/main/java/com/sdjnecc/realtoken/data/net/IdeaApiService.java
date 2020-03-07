package com.sdjnecc.realtoken.data.net;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by dell on 2017/4/1.
 */

public interface IdeaApiService {


    /**
     * 设备心跳
     *
     * @return
     */
    @FormUrlEncoded
    @POST("device/ping")
    Observable<List<Void>> heartBeat(@Field("device_name") String deviceName);


}
