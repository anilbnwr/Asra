
package com.asra.mobileapp.model;

import com.google.gson.annotations.Expose;

public class EventHost {

    @Expose
    private Long id;
    @Expose
    private Boolean status;
    @Expose
    private String type;
    @Expose
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
