package org.eisbm.graphmlsbfc.configuration;

public class CannotFindPropertyException extends Exception{
    public CannotFindPropertyException () {}

    public CannotFindPropertyException (String message) {
        super (message);
    }

    public CannotFindPropertyException (Throwable cause) {
        super (cause);
    }

    public CannotFindPropertyException (String message, Throwable cause) {
        super (message, cause);
    }
}