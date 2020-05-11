package com.wangk.core;

/**
 * @ClassName :ResultGenerator
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/7 10:33
 * @Version :1.0
 **/
public class ResultGenerator {

    public static Result getSuccessInfo(){
        return new Result().setCode(ResultCode.SUCCESS)
                .setMessage("SUCCESS");
    }

    public static<T> Result<T> getSuccessInfo(T data){
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage("SUCCESS")
                .setData(data);
    }

    public static Result getFailureInfo(String message){
        return new Result().setCode(ResultCode.FAIL)
                .setMessage(message);
    }


}
