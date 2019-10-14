package com.biedin.core.exceptions;

public class CompositeObjectNotExist extends Exception {
    public CompositeObjectNotExist() {
    }

    public CompositeObjectNotExist(String s) {
        super(s);
    }

    public CompositeObjectNotExist(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CompositeObjectNotExist(Throwable throwable) {
        super(throwable);
    }

    public CompositeObjectNotExist(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
