package com.pes.spb.t1.exception;

public class TestServiceException extends IllegalStateException {
    private TestServiceExceptionType type;
    private TestServiceException(String msg, TestServiceExceptionType type) {
        super(msg);
        this.type = type;
    }

    public static TestServiceException getFromQuery(String msg) {
        return new TestServiceException(msg, TestServiceExceptionType.QUERY_ERROR);
    }
    public static TestServiceException getFromService(String msg) {
        return new TestServiceException(msg, TestServiceExceptionType.SERVICE_ERROR);
    }

    public TestServiceExceptionType getType() {
        return type;
    }
}
