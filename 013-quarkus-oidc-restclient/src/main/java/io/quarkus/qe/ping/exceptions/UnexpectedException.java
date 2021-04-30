package io.quarkus.qe.ping.exceptions;

public class UnexpectedException extends CustomException {
    public static final int UNIQUE_SERVICE_ERROR_ID = 000;

    private static final long serialVersionUID = 4442033229110468176L;

    public UnexpectedException(String msg) {
        super(msg, UNIQUE_SERVICE_ERROR_ID);
    }

}
