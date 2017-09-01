package org.eisbm.graphmlsbfc;

public class CannotGuessException extends Exception {
    public CannotGuessException () {}

    public CannotGuessException (String message) {
        super (message);
    }

    public CannotGuessException (Throwable cause) {
        super (cause);
    }

    public CannotGuessException (String message, Throwable cause) {
        super (message, cause);
    }
}
