package com.afcooney.rsvpclientsim.model;

import java.util.HashMap;

/**
 * A URL Builder class that takes in values and builds them into a url
 */

public class UrlBuilder {
    private String root;
    private String path;
    private HashMap<String, String> queryParams = new HashMap<>();

    /**
     * Adds a root server url value to the URL Builder
     * @param root the server url vale to be added to the url
     * @return UrlBuilder
     */
    public UrlBuilder root(String root){
        this.root = root;
        return this;
    }

    /**
     * Adds a url path to the URL Builder
     * @param path the path to add to the url
     * @return UrlBuilder
     */
    public UrlBuilder path(String path){
        this.path = path;
        return this;
    }

    /**
     * Adds an individual query parameter to the URL Builder
     * @param key the key of the query parameter
     * @param value the value of the query parameter
     * @return UrlBuilder
     */
    public UrlBuilder queryParam(String key, String value){
        this.queryParams.put(key, value);
        return this;
    }

    /**
     * Adds all the given query parameters to the URL Builder
     * @param queryParams a hash map of query parameters that are to be added to the URL
     * @return UrlBuilder
     */
    public UrlBuilder queryParams(HashMap<String, String> queryParams){
        queryParams.forEach((key, value) -> {
            this.queryParams.put(key, value);
        });
        return this;
    }

    /**
     * Builds a URL with provided builder function calls
     * @return the string url
     */
    public String build() {
        String url = root + path;
        url = this.queryParams.isEmpty() ? url : url + "?";
        int index = 0;
        for (String key : this.queryParams.keySet()) {
            url = url + key + "=" + this.queryParams.get(key);
            if (index < (this.queryParams.size() - 1)) {
                url = url + "&";
            }
            index++;
        }
        return url;
    }
}
