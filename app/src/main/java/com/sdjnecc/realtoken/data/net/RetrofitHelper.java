package com.sdjnecc.realtoken.data.net;


import com.zhpan.idea.net.common.RetrofitService;


/**
 * Created by zhpan on 2018/3/22.
 */

public class RetrofitHelper {
    public static String BASE_URL = "https://www.fastmock.site/mock/34c3ffadc614310cc15d0b7d542a0d1d/realtoken/";

    private static IdeaApiService mIdeaApiService;

    public static IdeaApiService getApiService() {
        if (mIdeaApiService == null) {
            mIdeaApiService = RetrofitService.getRetrofitBuilder(BASE_URL)
                    .build().create(IdeaApiService.class);
        }

        return mIdeaApiService;
    }
}
