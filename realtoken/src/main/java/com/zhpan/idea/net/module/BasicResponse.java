package com.zhpan.idea.net.module;

/**
 * "status": 100,
 * "msg": "",
 * "data": {}
 */
public class BasicResponse<T> {

    private int status;
    private String msg;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T results) {
        this.data = results;
    }


    public int getStatus() {
        return status;
    }

    public void setCode(int code) {
        this.status = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String message) {
        this.msg = message;
    }
}
