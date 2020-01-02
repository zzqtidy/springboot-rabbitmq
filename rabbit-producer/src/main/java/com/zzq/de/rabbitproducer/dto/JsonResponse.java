package com.zzq.de.rabbitproducer.dto;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zzq
 * Date: 2018-04-11
 * Time: 21:09
 */
@Data
public class JsonResponse {
    private static final String OK = "ok";
    private static final String ERROR = "error";
    private Meta meta;
    private Object data;

    public JsonResponse success() {
        this.meta = new Meta(true, OK);
        return this;
    }

    public JsonResponse success(Object data) {
        this.meta = new Meta(true, OK);
        this.data = data;
        return this;
    }

    public JsonResponse failure() {
        this.meta = new Meta(false, ERROR);
        return this;
    }

    public JsonResponse failure(String message) {
        this.meta = new Meta(false, message);
        return this;
    }

}
