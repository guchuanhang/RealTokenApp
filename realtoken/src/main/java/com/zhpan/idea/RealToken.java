package com.zhpan.idea;

import android.content.Context;

import com.zhpan.idea.utils.FileUtils;
import com.zhpan.idea.utils.PreferencesUtil;

import java.io.File;
import java.util.Map;


public class RealToken {

    private static Context context;

    private RealToken() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    public static void init(Context context) {
        RealToken.context = context.getApplicationContext();
    }

    //用户登录成功后，刷新token
    public static void updateToken(TokenBean tokenBean) {
        ServerConfig.instance.tokenBean = tokenBean;
        FileUtils.saveObject(tokenBean, ServerConfig.instance.savePath);
    }

    //登出操作,清空token
    public static void clearMsg() {
        File tobeDelete = new File(ServerConfig.instance.savePath);
        FileUtils.deleteFile(tobeDelete);
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }


}