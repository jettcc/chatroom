package com.chat.im.im_oauth2server.config.exception;


import com.chat.im.im_common.entity.enumeration.GlobalServiceMsgCode;
import com.chat.im.im_common.utils.SystemMsgJsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 这个用来处理系统抛出的异常,捕获到异常后抛出Json给前端
 * @author: AubreyChen in 2022/3/6
 */

@RestControllerAdvice
public class ResultExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultExceptionHandler.class);

    /**
     * 处理所有不可知的异常
     *
     * @param e 异常对象
     * @return {@link SystemMsgJsonResponse}
     */
    @ExceptionHandler(Exception.class)
    SystemMsgJsonResponse handleException(Exception e) {
        return SystemMsgJsonResponse.error(GlobalServiceMsgCode.ERROR_FAIL, "异常信息:" + e.getMessage());
    }
    /**
     * 处理注册方法异常
     *
     * @param e 异常对象
     * @return {@link SystemMsgJsonResponse}
     */
    @Deprecated
    @ExceptionHandler(IllegalArgumentException.class)
    SystemMsgJsonResponse illegalArgumentExceptionException(Exception e){
        return SystemMsgJsonResponse.error(GlobalServiceMsgCode.ERROR_FAIL, "错误:" + e.getMessage());
    }

    /**
     * 处理所有接口数据验证异常
     *
     * @param e 异常对象
     * @return {@link SystemMsgJsonResponse}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    SystemMsgJsonResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        LOGGER.error(e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder errorMessage = new StringBuilder("校验失败：");

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append(fieldError.getDefaultMessage()).append("; ");
        }
        return SystemMsgJsonResponse.fail(errorMessage.toString());

    }

}