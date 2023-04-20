package com.asra.mobileapp.retrofit;

public class MyErrorMessage {

    private boolean status;
    private int statusCode;
    private String msg;
    private String orignalError;

    public MyErrorMessage(boolean status, int statusCode, String msg, String orignalError) {
        this.status = status;
        this.statusCode = statusCode;
        this.msg = msg;
        this.orignalError = orignalError;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOrignalError() {
        return orignalError;
    }

    public void setOrignalError(String orignalError) {
        this.orignalError = orignalError;
    }

}
