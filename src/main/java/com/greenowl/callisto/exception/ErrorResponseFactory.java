package com.greenowl.callisto.exception;

import org.apache.http.HttpStatus;

/**
 * Created by Ahmed on 2015-05-28.
 */
public class ErrorResponseFactory {

    private static final String UNAUTHORIZED = "Unauthorized";
    private static final String UNAUTHORIZED_MSG = "Access Denied";
    private static final String BAD_REQUEST_MSG = "Bad Request";

    private static final String NOT_FOUND_MSG = "Resource not found";
    private static final String NOT_MODIFIED_MSG = "Resource not changed";

    /**
     * Unable to grant access to :api
     * @param message
     * @param api
     * @return
     */
    public static ErrorResponse unAuthException(String message, String api) {
        return fullMessage(message, HttpStatus.SC_UNAUTHORIZED, UNAUTHORIZED, api);
    }

    /**
     * Generic access denied message
     * @param api
     * @return
     */
    public static ErrorResponse genericUnAuth(String api){
        return unAuthException(UNAUTHORIZED_MSG, api);
    }

    public static ErrorResponse fullMessage(String message, Integer status, String error, String api){
        return ErrorResponse.getBuidler()
                    .message(message)
                    .error(error)
                    .path(api)
                    .status(status)
                    .build();
    }

    public static ErrorResponse genericBadReq(String message, String api){
        return fullMessage(message, HttpStatus.SC_BAD_REQUEST, BAD_REQUEST_MSG, api);
    }

    public static ErrorResponse notFound(String message, String api){
        return fullMessage(message, HttpStatus.SC_NOT_FOUND, NOT_FOUND_MSG, api);
    }

    public static ErrorResponse conflict(String message, String api){
        return fullMessage(message, 409, message, api);
    }

    public static ErrorResponse genericNotFound(String api){
        return notFound(NOT_FOUND_MSG, api);
    }

    public static ErrorResponse genericNotModified(String message, String api){
        return fullMessage(message, HttpStatus.SC_NOT_MODIFIED, NOT_MODIFIED_MSG, api);
    }

    public static ErrorResponse serverError(String message, String exception, String api){
        return ErrorResponse.getBuidler()
                .message(message)
                .path(api)
                .exception(exception)
                .status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .build();
    }



}
