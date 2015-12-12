package com.qiaosheng.common.Exception;

/**

 */
public class TongjiException extends RuntimeException {

    public TongjiException() {
    }

    public TongjiException(String s) {
        super(s);
    }

    public TongjiException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public TongjiException(Throwable throwable) {
        super(throwable);
    }
}
