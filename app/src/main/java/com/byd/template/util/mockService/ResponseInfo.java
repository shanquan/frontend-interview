package com.byd.template.util.mockService;

import org.json.JSONObject;
import java.util.Map;

/**
 * Created by liaohailiang on 2018/12/7.
 */
public class ResponseInfo {

    private int code;
    private String protocol;
    private String message;
    private String contentType;
    private Map<String, String> header;
    private JSONObject body;

    public ResponseInfo(){
        this.code=200;
        this.protocol = "http/1.1";
        this.message = "";
        this.contentType = "application/json;charset=UTF-8";
    }

    public ResponseInfo(JSONObject body){
        this.code=200;
        this.protocol = "http/1.1";
        this.message = "";
        this.contentType = "application/json;charset=UTF-8";
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBodyAsString() {
        if (body == null) {
            return "";
        }
        return body.toString();
    }
}
