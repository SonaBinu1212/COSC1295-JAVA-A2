package au.edu.rmit.carehome.exception;

/**
 * Exception thrown when a staff member attempts to perform an action
 * during a time they are not rostered to work.
 */
public class StaffNotRosteredException extends Exception {
    public StaffNotRosteredException(String message) {
        super(message);
    }
}
