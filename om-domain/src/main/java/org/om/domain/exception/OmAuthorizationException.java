package org.om.domain.exception;

public class OmAuthorizationException extends RuntimeException {
    private OmAuthorizationException(String s) {
        super(s);
    }

    public static OmAuthorizationException newNotAuthorizedException() {
        return new OmAuthorizationException("Not Authorized!");
    }
}
