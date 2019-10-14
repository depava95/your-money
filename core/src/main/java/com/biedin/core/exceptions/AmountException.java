package com.biedin.core.exceptions;

public class AmountException extends Exception {
    public AmountException() {
        super();
    }

    public AmountException(String s) {
        super(s);
    }

    public AmountException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AmountException(Throwable throwable) {
        super(throwable);
    }

    protected AmountException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
