package com.zhpan.idea;

import android.content.Context;

import com.zhpan.idea.utils.PreferencesUtil;

import java.util.Map;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 16/12/08
 *     desc  : Utils初始化相关
 * </pre>
 */
public class RealToken {

    private static Context context;

    private RealToken() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    public static void init(Context context) {
        RealToken.context = context.getApplicationContext();
    }

    public static void init(Context context, Map<String, Object> strMap) {
        RealToken.context = context.getApplicationContext();
        if (null == strMap || strMap.isEmpty()) {
            throw new RuntimeException("Map<String,String> 不能为空。否则请调用init的不带初始值的重载方法");
        }
        for (Map.Entry<String, Object> entry : strMap.entrySet()) {
            PreferencesUtil.put(context, entry.getKey(), entry.getValue());
        }
    }

    public static void setMsg(Map<String, Object> strMap) {
        for (Map.Entry<String, Object> entry : strMap.entrySet()) {
            PreferencesUtil.put(context, entry.getKey(), entry.getValue());
        }
    }

    public static void clearMsg() {
        PreferencesUtil.clearValues(context);
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