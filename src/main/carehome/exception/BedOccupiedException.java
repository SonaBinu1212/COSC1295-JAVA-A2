package au.edu.rmit.carehome.exception;

/**
 * Exception thrown when attempting to assign a patient to an already occupied bed.
 */
public class BedOccupiedException extends Exception {
    public BedOccupiedException(String message) {
        super(message);
    }
}
