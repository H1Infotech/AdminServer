package com.h1infotech.smarthive.common;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 191521098205748181L;
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BusinessException(BizCodeEnum bizCodeEnum) {
        super("Code: " + bizCodeEnum.getCode() + ", Message: " + bizCodeEnum.getMessage());
        this.code = bizCodeEnum.getCode();
        this.message = bizCodeEnum.getMessage();
    }

    public BusinessException(BizCodeEnum bizCodeEnum, Throwable cause) {
        super("Code: " + bizCodeEnum.getCode() + ", Message: " + bizCodeEnum.getMessage(), cause);
        this.code = bizCodeEnum.getCode();
        this.message = bizCodeEnum.getMessage();
    }
}
