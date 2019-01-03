package com.knyaz.testtask.api.utils;

import com.knyaz.testtask.api.model.Meta;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Response;

public class ErrorResponse extends Exception {

    private ErrorResponse(Meta meta, Kind kind) {
        this.mMeta = meta;
        this.mKind = kind;
    }

    public static String getServerMessage(Object throwable) {
        if (throwable instanceof ErrorResponse) {
            return ((ErrorResponse) throwable).getServerMessage();
        } else if (throwable instanceof Throwable) {
            return ((Throwable) throwable).getMessage();
        }
        return "";
    }

    public static ErrorResponse httpError(String url, Response response) {
        String message = response.code() + " " + response.message();
        return new ErrorResponse(message, url, response, ErrorResponse.Kind.HTTP, null);
    }

    public static ErrorResponse networkError(IOException exception) {
        return new ErrorResponse(exception.getMessage(), null, null, ErrorResponse.Kind.NETWORK, exception);
    }

    public static ErrorResponse unexpectedError(Throwable exception) {
        return new ErrorResponse(exception.getMessage(), null, null, ErrorResponse.Kind.UNEXPECTED, exception);
    }

    public static ErrorResponse parseError(Exception exception) {
        return new ErrorResponse(exception.getMessage(), null, null, Kind.SERVER_BULLSHIT, exception);
    }

    public static ErrorResponse serverError(String text) {
        return new ErrorResponse(text, null, null, Kind.SERVER_ERROR, null);
    }

    /**
     * Identifies the event kind which triggered a {@link ErrorResponse}.
     */
    public enum Kind {
        /**
         * An {@link IOException} occurred while communicating to the server.
         */
        NETWORK,
        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED,
        SERVER_BULLSHIT,
        SERVER_ERROR
    }

    private String mUrl;
    private Response mResponse;
    private ErrorResponse.Kind mKind;
    private Meta mMeta;

    private ErrorResponse(String message, String url, Response response, ErrorResponse.Kind kind, Throwable exception) {
        super(message, exception);
        this.mUrl = url;
        this.mResponse = response;
        this.mKind = kind;
        initMeta();
    }

    private void initMeta() {
        HashMap<String, String[]> hashMap = new HashMap<>();
        switch (mKind) {
            case HTTP:
                hashMap.put("message", new String[]{"Server error"});
                break;
            case NETWORK:
                hashMap.put("message", new String[]{"NETWORK doesn't work"});
                break;
            case UNEXPECTED:
                hashMap.put("message", new String[]{"Unexpected error "});
                break;
            case SERVER_BULLSHIT:
                hashMap.put("message", new String[]{"Sorry, but server is broken"});
                break;
        }
        mMeta = new Meta(-1, "", hashMap);
    }

    /**
     * The request URL which produced the error.
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Response object containing status code, headers, body, etc.
     */
    public Response getResponse() {
        return mResponse;
    }

    /**
     * The event kind which triggered this error.
     */
    public ErrorResponse.Kind getKind() {
        return mKind;
    }

    public Meta getMeta() {
        return mMeta;
    }

    public String getServerMessage() {
        return mMeta.getFirstMessage();
    }
}