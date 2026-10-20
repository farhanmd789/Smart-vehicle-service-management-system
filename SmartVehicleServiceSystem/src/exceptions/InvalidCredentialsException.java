package exceptions;

/**
 * Custom exception thrown when login credentials are invalid.
 * Demonstrates custom Exception Handling in Core Java.
 */
public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
