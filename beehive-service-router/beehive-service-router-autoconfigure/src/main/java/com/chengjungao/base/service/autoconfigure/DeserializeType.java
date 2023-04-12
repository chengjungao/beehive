package com.chengjungao.base.service.autoconfigure;

public enum DeserializeType {
    /**
     * return java.lang.String
    */
    String,
    /**
     * String to JSON
     * return com.alibaba.fastjson2.JSONObject
     */
    Json,
    /**
     * String to XML
     * return org.jsoup.nodes.Document
     */
    Xml,
    /**
     * JSON String to Object
     * Response is String
     * return <T> T , T is the type of the parameter
     */
    Json_Object,

    /**
     * bytes to Object
     * Response is bytes, need to be deserialized, implement ResponseParser
     * return <T> T , T is the type of the parameter
     */
    Object,


}
