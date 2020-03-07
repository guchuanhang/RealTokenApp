
package com.zhpan.idea.net.common;


/**
 * Created by zhpan on 2018/3/27.
 */
public class ErrorCode {
    /**
     * request success
     */
    public static final int SUCCESS = 100;
    /**
     * 发生异常，暂时没有进行细分
     */
    public static final int ERROR = 1001;
    /**
     * 返回数据不是json格式
     */
    public static final int ERR_JSON = -1;
    /**
     * 返回json没有data字段
     */
    public static final int ERR_NO_DATA = -1;

}
