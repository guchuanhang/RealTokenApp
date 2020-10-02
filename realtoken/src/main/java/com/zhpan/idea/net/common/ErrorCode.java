
package com.zhpan.idea.net.common;


import androidx.annotation.StringRes;

import com.zhpan.idea.utils.Utils;

/**
 * Created by zhpan on 2018/3/27.
 */
public class ErrorCode {
    /**
     * request success
     */
    public static final int SUCCESS = 100;   //请求成功

    public static final int TOKEN_EXPIRE = 1010; //token 过期
    public static final int REFRESH_TOKEN_EXPIRE = 1011; //refreshToken 过期

    /**
     * 异地登录
     */
    public static final int REMOTE_LOGIN = 91011;

    public static String getErrorMessage(int errorCode) {
        return getErrorMessage(errorCode, "");
    }

    /**
     * get error message with error code
     *
     * @param errorCode error code
     * @return error message
     */
    public static String getErrorMessage(int errorCode, String errorMsg) {
//        String message;
//        switch (errorCode) {
//            case REQUEST_FAILED:
//                message = getString(R.string.request_error) + errorCode + ",Error Message:" + errorMsg;
//                break;
//            case VERIFY_CODE_ERROR:
//                message = getString(R.string.verify_code_error);
//                break;
//            case INVALID_LOGIN_STATUS:
//                message = getString(R.string.invalid_status);
//                break;
//            case VERIFY_CODE_EXPIRED:
//                message = getString(R.string.verify_code_expired);
//                break;
//            case ACCOUNT_NOT_REGISTER:
//                message = getString(R.string.not_register);
//                break;
//            case PASSWORD_ERROR:
//                message = getString(R.string.wrong_pwd_username);
//                break;
//            case USER_REGISTERED:
//                message = getString(R.string.user_registered);
//                break;
//            case OLD_PASSWORD_ERROR:
//                message = getString(R.string.wrong_password);
//                break;
//            case PARAMS_ERROR:
//                message = getString(R.string.parameters_exception);
//                break;
//            case REMOTE_LOGIN:
//                message = getString(R.string.remote_login);
//                break;
//            default:
//                message = getString(R.string.request_error) + errorCode;
//                break;
//        }
        return errorMsg;
    }

    private static String getString(@StringRes int resId) {
        return Utils.getContext().getString(resId);
    }
}
