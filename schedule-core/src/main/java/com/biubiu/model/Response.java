package com.biubiu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 张海彪
 * @create 2018-02-11 上午2:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 7079394963654694302L;

    public static final long OK = 200L;

    public static final long BAD_REQUEST = 400L;

    public static final long UNAUTHORIZED = 403L;

    public static final long SERVER_UNAVAILABLE = 500L;

    private long code;

    private String message;

    private T payload;

    public Response(long code) {
        this.code = code;
    }

    public static Response success() {
        return new Response(Response.OK);
    }

    public static Response fail() {
        return new Response(Response.BAD_REQUEST);
    }

}
