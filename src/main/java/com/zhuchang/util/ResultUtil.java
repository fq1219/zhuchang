package com.zhuchang.util;

import com.zhuchang.vo.Result;
import com.zhuchang.vo.ResultCodeEnum;

public class ResultUtil {

    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    //成功
    public static Result getSuccessResult() {
        return new Result()
                .setCode(ResultCodeEnum.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> getSuccessResult(T data) {
        return new Result()
                .setCode(ResultCodeEnum.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static <T> Result<T> getSuccessResult(T data, String message) {
        return new Result()
                .setCode(ResultCodeEnum.SUCCESS)
                .setMessage(message)
                .setData(data);
    }

    public static Result getFailResult(String message) {
        return new Result()
                .setCode(ResultCodeEnum.FAIL)
                .setMessage(message);
    }

    public static <T> Result<T> getFailResult(T data, String message) {
        return new Result()
                .setCode(ResultCodeEnum.FAIL)
                .setMessage(message)
                .setData(data);
    }

    public static Result getUnauthorizedResult() {
        return new Result()
                .setCode(ResultCodeEnum.UNAUTHORIZED)
                .setMessage("权限不足！");
    }
}
