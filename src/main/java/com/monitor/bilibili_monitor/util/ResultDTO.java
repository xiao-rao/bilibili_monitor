package com.monitor.bilibili_monitor.util;


import java.io.Serializable;

public class ResultDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean success = true;
    private T data;
    private Integer code = 200;
    private String message;

    public ResultDTO() {
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFailed(Integer code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
    }

    public void setFailed(MessageCodeEnum msgCodeEnum, String message) {
        this.success = false;
        this.code = msgCodeEnum.getCode();
        this.message = message;
    }

    public void setFailed(MessageCodeEnum msgCodeEnum) {
        this.success = false;
        this.code = msgCodeEnum.getCode();
        this.message = msgCodeEnum.getDesc();
    }

    public static <T> ResultDTO<T> success(T value) {
        ResultDTO<T> r = new ResultDTO();
        r.setData(value);
        return r;
    }

    public static <T> ResultDTO<T> fail(String msg) {
        ResultDTO<T> r = new ResultDTO();
        r.setCode(MessageCodeEnum.parameter_fail.getCode());
        r.setSuccess(Boolean.FALSE);
        r.setMessage(msg);
        return r;
    }

    @Override
    public String toString() {
        return "ResultDTO{success=" + this.success + ", data=" + this.data + ", errorCode=" + this.code + ", errorMessage='" + this.message + '\'' + '}';
    }
}