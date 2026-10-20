package exceptions;

/**
 * Custom exception thrown when a duplicate record is detected.
 * Demonstrates custom Exception Handling in Core Java.
 */
public class DuplicateRecordException extends Exception {
    public DuplicateRecordException(String message) {
        super(message);
    }
}
