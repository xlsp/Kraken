package com.opensesame.core.utils;

import java.io.Serializable;

/**
 * @author 13
 * @qq交流群 796794009
 * @email 2449207463@qq.com
 * @link https://github.com/newbee-ltd
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int error_code;
    private String error_info;
    private T data;

    public Result() {
    }

    public Result(int error_code, String error_info) {
        this.error_code = error_code;
        this.error_info = error_info;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_info() {
        return error_info;
    }

    public void setError_info(String error_info) {
        this.error_info = error_info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "error_code=" + error_code +
                ", error_info='" + error_info + '\'' +
                ", data=" + data +
                '}';
    }
}
