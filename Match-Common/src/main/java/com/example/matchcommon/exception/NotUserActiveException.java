package com.example.matchcommon.exception;

public class NotUserActiveException extends RuntimeException {
    private static final long serialVersionUID = 6769829250639411880L;

    /**
     * Constructs a {@code NoSuchElementException} with {@code null}
     * as its error message string.
     */
    public NotUserActiveException() {
        super();
    }

    /**
     * Constructs a {@code NoSuchElementException}, saving a reference
     * to the error message string {@code s} for later retrieval by the
     * {@code getMessage} method.
     *
     * @param   s   the detail message.
     */
    public NotUserActiveException(String s) {
        super(s);
    }
}
