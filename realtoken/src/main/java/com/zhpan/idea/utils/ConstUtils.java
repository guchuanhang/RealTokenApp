package com.zhpan.idea.utils;

/**
 * <pre>
 *     author: zhpan
 *     time  : 2016/8/11
 *     desc  : 常量相关工具类
 * </pre>
 */
public class ConstUtils {

    private ConstUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /******************** 存储相关常量 ********************/
    /**
     * KB与Byte的倍数
     */
    public static final int KB = 1024;
    /**
     * MB与Byte的倍数
     */
    public static final int MB = 1048576;
    /**
     * GB与Byte的倍数
     */
    public static final int GB = 1073741824;

    /**
     * KB与Byte的倍数Double
     */
    public static final double DKB = 1024.0;
    /**
     * MB与Byte的倍数Double
     */
    public static final double DMB = 1048576.0;
    /**
     * GB与Byte的倍数Double
     */
    public static final double DGB = 1073741824.0;

}