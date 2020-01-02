package com.zzq.de.rabbitproducer.dto;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zzq
 * Date: 2018-04-11
 * Time: 21:10
 */
@Data
public class Meta {
    private boolean success;
    private String message;
    private long timestamp;
    private String responseTime;

    public Meta(boolean success) {
        this.success = success;
        this.message = "成功";
        this.timestamp = System.currentTimeMillis();
        this.responseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public Meta(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        this.responseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
