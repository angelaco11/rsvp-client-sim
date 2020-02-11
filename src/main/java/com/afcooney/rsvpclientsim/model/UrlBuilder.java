package com.afcooney.rsvpclientsim.model;

import java.util.HashMap;

public class UrlBuilder {
    private boolean isHttps = false;
    private String root;
    private String path;
    private HashMap<String, String> queryParams = new HashMap<>();

    public UrlBuilder isHttps(){
        this.isHttps = true;
        return this;
    }

    public UrlBuilder root(String root){
        this.root = root;
        return this;
    }

    public UrlBuilder path(String path){
        this.path = path;
        return this;
    }

    public UrlBuilder queryParam(String key, String value){
        this.queryParams.put(key, value);
        return this;
    }

    public UrlBuilder queryParams(HashMap<String, String> queryParams){
        queryParams.forEach((key, value) -> {
            this.queryParams.put(key, value);
        });
        return this;
    }

    public String build() {
        String url = isHttps ? "https://" : "http://";
        url = url + root + path;
        url = this.queryParams.isEmpty() ? url : url + "?";
        int index = 0;
        for (String key : this.queryParams.keySet()) {
            url = url +key + "=" + this.queryParams.get(key);
            if (index < (this.queryParams.size() - 1)) {
                url = url + "&";
            }
            index++;
        }
        return url;
    }
}
