package com.opensesame.core.utils;

/**
 * 响应结果生成工具
 *
 * @author 13
 * @qq交流群 796794009
 * @email 2449207463@qq.com
 * @link https://github.com/newbee-ltd
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";
    private static final int RESULT_CODE_SUCCESS = 200;
    private static final int RESULT_CODE_SERVER_ERROR = 500;

    public static Result genSuccessResult() {
        Result result = new Result();
        result.setError_code(RESULT_CODE_SUCCESS);
        result.setError_info(DEFAULT_SUCCESS_MESSAGE);

        return result;
    }

    public static Result genSuccessResult(String message) {
        Result result = new Result();
        result.setError_code(RESULT_CODE_SUCCESS);
        result.setError_info(message);
        return result;
    }

    public static Result genSuccessResult(Object data) {
        Result result = new Result();
        result.setError_code(RESULT_CODE_SUCCESS);
        result.setError_info(DEFAULT_SUCCESS_MESSAGE);
        result.setData(data);
        return result;
    }

    public static Result genFailResult(String message) {
        Result result = new Result();
        result.setError_code(RESULT_CODE_SERVER_ERROR);
//        if (StringUtils.isEmpty(message)) {
//            result.setError_info(DEFAULT_FAIL_MESSAGE);
//        } else {
        result.setError_info(message);
//        }
        return result;
    }

    public static Result genErrorResult(int code, String message) {
        Result result = new Result();
        result.setError_code(code);
        result.setError_info(message);
        return result;
    }
}