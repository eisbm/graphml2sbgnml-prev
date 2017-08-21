package org.eisbm.graphmlsbfc;

public class CannotMapPropertyException extends Exception {
    public CannotMapPropertyException () {}

    public CannotMapPropertyException (String message) {
        super (message);
    }

    public CannotMapPropertyException (Throwable cause) {
        super (cause);
    }

    public CannotMapPropertyException (String message, Throwable cause) {
        super (message, cause);
    }
}
