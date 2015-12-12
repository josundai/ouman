package com.qiaosheng.common.Exception;

public class InvalidCollectionException extends Exception {

    public InvalidCollectionException() {
        super();
    }

    public InvalidCollectionException(String s) {
        super(s);
    }

    public InvalidCollectionException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidCollectionException(Throwable throwable) {
        super(throwable);
    }
}
