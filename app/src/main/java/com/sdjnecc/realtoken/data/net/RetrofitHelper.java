package com.sdjnecc.realtoken.data.net;

import android.content.Intent;

import com.sdjnecc.realtoken.SplashActivity;
import com.zhpan.idea.RealToken;
import com.zhpan.idea.ServerConfig;
import com.zhpan.idea.TokenBean;
import com.zhpan.idea.net.common.IdeaApiProxy;
import com.zhpan.idea.net.token.IGlobalManager;
import com.zhpan.idea.utils.LogUtils;
import com.zhpan.idea.utils.Utils;


/**
 * Created by zhpan on 2018/3/22.
 */

public class RetrofitHelper {

    private static IdeaApiService mIdeaApiService;

    public static IdeaApiService getApiService() {
        if (mIdeaApiService == null)

            mIdeaApiService = new IdeaApiProxy().getApiService(IdeaApiService.class,
                    ServerConfig.BASE_URL, new IGlobalManager() {
                        @Override
                        public void logout() {
                            RealToken.clearToken();
                            Intent intent = new Intent(
                                    Utils.getContext(),
                                    SplashActivity.class
                            );
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Utils.getContext().startActivity(intent);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            LogUtils.d("logout");
                        }

                        @Override
                        public void tokenRefresh(TokenBean response) {
                            LogUtils.d("tokenRefresh");
                            RealToken.updateToken(response);
                        }
                    });
        return mIdeaApiService;
    }
}
