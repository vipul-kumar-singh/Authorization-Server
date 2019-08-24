package com.vkstech.authorizationserver.utils;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseObject implements Serializable {

    private Date timestamp;
    private Object data;
    private String message;

    public ResponseObject() {
        this.timestamp = new Date();
    }

    public ResponseObject(String message) {
        this.message = message;
        this.timestamp = new Date();
    }

    public ResponseObject(String message, Object data) {
        this.message = message;
        this.data = data;
        this.timestamp = new Date();
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}