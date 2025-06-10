package com.sample.portal.core.pojo;

public class HttpConnectionResponse {
    private int status;
    private String response;

    public HttpConnectionResponse(int status, String response) {
        this.status = status;
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public int getStatus() {
        return status;
    }

}
