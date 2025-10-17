package au.edu.rmit.carehome.exception;

/**
 * Exception thrown when business rules or compliance requirements are violated.
 */
public class ComplianceException extends Exception {
    public ComplianceException(String message) {
        super(message);
    }
}
