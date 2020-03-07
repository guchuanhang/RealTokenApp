package com.zhpan.idea.net.exception;

/**
 * 服务器返回的异常
 */
public class ServerResponseException extends RuntimeException {

    private int errorCode;

    public ServerResponseException(int errorCode, String cause) {
        super(cause);
        this.errorCode = errorCode;
    }
}
