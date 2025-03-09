package org.om.domain.exception;

public class OmException extends RuntimeException {
    private OmException(String s) {
        super(s);
    }

    public static OmException newNoPendingOrderException() {
        return new OmException("No pending order found!");
    }

    public static OmException newCustomerDoesNotHaveAssetException() {
        return new OmException("Customer does not have asset!");
    }

}
