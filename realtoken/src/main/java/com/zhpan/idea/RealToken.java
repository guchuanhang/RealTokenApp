package com.zhpan.idea;

import android.content.Context;

import com.zhpan.idea.utils.FileUtils;
import com.zhpan.idea.utils.Utils;

import java.io.File;


public class RealToken {

    private static Context context;

    private RealToken() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    public static void init(Context context) {
        RealToken.context = context.getApplicationContext();
        Utils.init(context);
    }

    //用户登录成功后，刷新token
    public static void updateToken(TokenBean tokenBean) {
        ServerConfig.instance.tokenBean = tokenBean;
        FileUtils.saveObject(tokenBean, ServerConfig.instance.savePath);
    }

    //登出操作,清空token
    public static void clearToken() {
        File tobeDelete = new File(ServerConfig.instance.savePath);
        FileUtils.deleteFile(tobeDelete);
        ServerConfig.instance.tokenBean = null;
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

    // 判断用户是否已经登录
    public static boolean isLogin() {
        Object rawObject = FileUtils.readObject(ServerConfig.instance.savePath);
        if (rawObject instanceof TokenBean) {
            return true;
        }
        return false;
    }


}