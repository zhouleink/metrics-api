package com.flexreceipts.metricsapi.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
    private Object[] params;

    public ResourceAlreadyExistsException() {
    }

    public ResourceAlreadyExistsException(Object[] params) {
        this.params = params;
    }

    public ResourceAlreadyExistsException(String message, Object[] params) {
        super(message);
        this.params = params;
    }

    public Object[] getParams() {
        return params;
    }

}
