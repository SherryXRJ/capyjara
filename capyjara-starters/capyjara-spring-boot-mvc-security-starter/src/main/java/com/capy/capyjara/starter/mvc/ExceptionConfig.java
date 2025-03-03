package com.capy.capyjara.starter.mvc;

import com.capy.capyjara.common.exception.BusinessException;
import com.capy.capyjara.common.response.Result;
import com.capy.capyjara.common.response.CommonResultStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@ControllerAdvice
public class ExceptionConfig {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result accessDeniedExceptionHandler(AccessDeniedException exception)  {
        log.error("Access Denied Exception", exception);
        return Result.fail(CommonResultStatus.FORBIDDEN.getCode(), exception.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result<String> businessExceptionHandler(BusinessException exception)  {
        log.error("Global Business Exception", exception);
        return Result.fail(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Result<String> illegalArgumentException(IllegalArgumentException exception)  {
        log.error("Global Illegal Argument Exception", exception);
        return Result.fail(CommonResultStatus.ILLEGAL_ARGUMENT.getCode(), exception.getMessage());
    }

    /**
     * 处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<String> paramsExceptionHandler(MethodArgumentNotValidException exception) {
        log.error("Global Parameter Exception", exception);
        List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
        String errorMsg = Optional.of(allErrors)
                .map(errorList -> errorList.stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(",")))
                .orElse(exception.getMessage());

        return Result.fail(CommonResultStatus.ILLEGAL_ARGUMENT.getCode(), errorMsg);
    }

    /**
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result<String> resolveConstraintViolationException(ConstraintViolationException exception) {
        log.error("Global Parameter Exception", exception);
        Result<String> session = new Result<>();
        session.setCode(CommonResultStatus.ILLEGAL_ARGUMENT.getCode());
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

        String errorMsg = Optional.of(constraintViolations)
                .map(violations -> violations.stream().map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(",")))
                .orElse(exception.getMessage());

        session.setMsg(exception.getMessage());
        return session;
    }


    /**
     * 处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result<String> bindExceptionHandler(BindException exception) {
        log.error("Global Parameter Exception", exception);
        return Result.fail(CommonResultStatus.ILLEGAL_ARGUMENT);
    }

    /**
     * 兜底
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> globalExceptionHandler(Exception exception) {
        log.error("Global Error", exception);
        return Result.fail(CommonResultStatus.INTERNAL_ERROR);
    }

}

