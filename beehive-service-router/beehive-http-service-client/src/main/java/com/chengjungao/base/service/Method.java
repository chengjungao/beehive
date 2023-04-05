package com.chengjungao.base.service;

public enum Method {
    /**
     * GET
     */
    GET,
    /**
     * POST
     */
    POST,
    /**
     * PUT
     */
    PUT,
    /**
     * DELETE
     */
    DELETE;

    public static Method getMethod(String method){
        if(method==null){
            return null;
        }
        if(method.equalsIgnoreCase("GET")){
            return GET;
        }else if(method.equalsIgnoreCase("POST")){
            return POST;
        }else if(method.equalsIgnoreCase("PUT")){
            return PUT;
        }else if(method.equalsIgnoreCase("DELETE")){
            return DELETE;
        }
        return null;
    }

}
