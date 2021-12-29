package org.dssc.demo.common.api;

import org.springframework.http.HttpStatus;

public class CommonResult<T> {
    private String code;
    private String message;
    private T data;

    protected CommonResult(){

    }

    protected CommonResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(HttpStatus.OK.toString(), null, data);
    }

    public static <T> CommonResult<T> success() {
        return new CommonResult<>(HttpStatus.OK.toString(), null, null);
    }

    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<>(HttpStatus.INTERNAL_SERVER_ERROR.toString(), message, null);
    }

    public static <T> CommonResult<T> failed(IErrorCode errorCode) {
        return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

