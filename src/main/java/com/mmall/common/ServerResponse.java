package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * @author 唐孟廷
 * @desc 通用数据返回处理, 序列化时不包含null数据
 * @date 2020/5/3 - 1:12
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status, String msg) {
        this(status, msg, null);
    }

    private ServerResponse(int status, T data) {
        this(status, null, data);
    }

    public ServerResponse(int status) {
        this(status, null, null);
    }


    @JsonIgnore
    /** json序列化时不包含这个属性 */
    public boolean isSuccess() {
        return this.status == 0;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    /**
     * 直接设置一个请求status为成功的信息
     * @param <T>
     * @return  返回一个状态为0的不包含描述和数据的信息
     */
    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse(ResponseCode.SUCCESS.getCode());
    }

    /**
     * 直接设置一个请求status为成功并且包含msg的信息
     * @param msg   具体成功描述
     * @param <T>
     * @return  返回一个状态为0的包含描述的信息
     */
    public static <T> ServerResponse<T> createBySuccess(String msg) {
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), msg);
    }

    /**
     * 设置一个请求status为成功,并且包含数据data的信息
     * @param data  具体返回数据
     * @param <T>
     * @return  返回一个状态为0的包含data的信息
     */
    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), data);
    }


    /**
     * 设置一个请求status为成功,并且包含描述和数据的信息
     * @param msg   具体返回结果描述
     * @param data  返回数据
     * @param <T>
     * @return  返回一个标准的成功数据
     */
    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), msg, data);
    }


    /**
     * 设置一个请求状态为失败的简单数据
     * @param <T>
     * @return  返回一个简单的错误数据
     */
    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse(ResponseCode.ERROR.getCode());
    }


    /**
     * 设置一个请求状态为失败并且包含错误描述简单数据
     * @param errorMessage  错误描述
     * @param <T>
     * @return  返回一个简单的包含描述的错误数据
     */
    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage) {
        return new ServerResponse(ResponseCode.ERROR.getCode(), errorMessage);
    }


    /**
     * 设置一个指定请求状态并且包含错误描述简单数据
     * @param errorCode 错误状态
     * @param errorMessage  错误描述
     * @param <T>
     * @return  返回标准的错误数据
     */
    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ServerResponse(errorCode, errorMessage);
    }
}
