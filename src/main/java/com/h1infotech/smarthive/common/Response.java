package com.h1infotech.smarthive.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private static final int SUCCESS_CODE = 0;
    private static final int FAIL_CODE = 1;
    private final int ret;
    private final Object data;
    private final String message;

    private Response(int ret, Object data, String message) {
        this.ret = ret;
        this.data = data;
        this.message = message;
    }

    public int getRet() {
        return ret;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static Response success(final Object data) {
        return new Response(SUCCESS_CODE, data, null);
    }

    public static Response fail(final String message) {
        return new Response(FAIL_CODE, null, message);
    }
}
