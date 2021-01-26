package io.quarkus.qe.ping.exceptions;

public class CustomException extends Exception {

    private static final long serialVersionUID = 7379574120935739461L;

    private final int errorCode;

    public CustomException(String msg, int errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
