package com.chat.im.im_common.entity.exception;

import com.chat.im.im_common.entity.enumeration.GlobalServiceMsgCode;
import lombok.Getter;

@Getter
public class RunErrorException extends RuntimeException{
    private GlobalServiceMsgCode code;
    private String errMsg;

    public RunErrorException() {
        super();
    }

    public RunErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public RunErrorException(GlobalServiceMsgCode code, String errMsg) {
        super(errMsg);
        this.code = code;
        this.errMsg = errMsg;
    }

    public RunErrorException(String message) {
        super(message);
    }

    public RunErrorException(Throwable cause) {
        super(cause);
    }
}
