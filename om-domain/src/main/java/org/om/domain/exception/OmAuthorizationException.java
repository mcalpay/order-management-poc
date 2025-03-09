package org.om.domain.exception;

public class OmAuthorizationException extends RuntimeException {

    public OmAuthorizationException() {
        super("Not Authorized!");
    }

}
