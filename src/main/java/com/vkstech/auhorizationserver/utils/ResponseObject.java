package com.vkstech.auhorizationserver.utils;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseObject implements Serializable {

    public Date timestamp;
    public Object data;
    public String message;

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