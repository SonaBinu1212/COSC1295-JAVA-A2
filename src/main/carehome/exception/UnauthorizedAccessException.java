package au.edu.rmit.carehome.exception;

/**
 * Exception thrown when a staff member attempts to perform an action
 * they are not authorized to perform.
 */
public class UnauthorizedAccessException extends Exception {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
