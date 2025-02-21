package com.capy.capyjara.common.response;

import com.capy.capyjara.common.constant.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {

    private Integer code;

    private T data;

    private String msg;


    public static Result ok(){
        return ok(true);
    }

    public static <T> Result<T> ok(T data){
        return new Result<>(CommonResultStatus.SUCCESS.getCode(), data, CommonResultStatus.SUCCESS.getMsg());
    }

    public static <T> Result<T> ok(T data, String msg){
        return new Result<>(CommonResultStatus.SUCCESS.getCode(), data, msg);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, null, message);
    }

    public static <T> Result<T> fail(@NonNull ResultStatus resultStatus) {
        return new Result<>(resultStatus.getCode(), null, resultStatus.getMsg());
    }


    public static <T> Result<T> build(Integer code, T data ,String msg) {
        return new Result<>(code, data, msg);
    }

}
