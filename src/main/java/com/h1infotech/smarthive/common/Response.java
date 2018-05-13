package com.h1infotech.smarthive.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private final T data;
    private final String responseCode;
    private final String responseMessage;

    private Response(BizCodeEnum bizCodeEnum) {
    	this.data = null;
    	this.responseCode = bizCodeEnum.getCode();
    	this.responseMessage = bizCodeEnum.getMessage();
    }

    private Response(BizCodeEnum bizCodeEnum, T data) {
        this.data = data;
    	this.responseCode = bizCodeEnum.getCode();
    	this.responseMessage = bizCodeEnum.getMessage();
    }
    
    private Response(String responseCode, String responseMessage, T data) {
        this.data = data;
    	this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

	public String getResponseCode() {
		return responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public Object getData() {
        return data;
    }

    public static <T> Response<T> success(final T data) {
        return new Response<T>(BizCodeEnum.SERVICE_SUCCESS, data);
    }

    public static <T> Response<T> fail(BizCodeEnum bizCodeEnum) {
        return new Response<T>(bizCodeEnum);
    }
    
    public static <T> Response<T> fail(String code, String response) {
    	return new Response<T>(code, response, null);
    }
}
