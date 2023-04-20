package com.asra.mobileapp.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteUserResponseModel {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("altype")
    @Expose
    private String altype;
    @SerializedName("msg")
    @Expose
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAltype() {
        return altype;
    }

    public void setAltype(String altype) {
        this.altype = altype;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
