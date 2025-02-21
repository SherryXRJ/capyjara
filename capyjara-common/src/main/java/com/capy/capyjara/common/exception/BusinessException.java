package com.capy.capyjara.common.exception;

import com.capy.capyjara.common.response.ResultStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {

    private Integer code;

    private String message;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ResultStatus resultStatus){
        this(resultStatus.getCode(), resultStatus.getMsg());
    }


}
