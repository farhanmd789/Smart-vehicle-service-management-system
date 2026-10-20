package exceptions;

/**
 * Custom exception thrown when a requested record is not found.
 * Demonstrates custom Exception Handling in Core Java.
 */
public class RecordNotFoundException extends Exception {
    public RecordNotFoundException(String message) {
        super(message);
    }
}
