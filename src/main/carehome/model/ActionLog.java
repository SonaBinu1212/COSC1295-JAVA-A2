package au.edu.rmit.carehome.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a log entry for an action performed in the system.
 * All actions must be logged with timestamp and performing staff member.
 * 
 * Design Decision: Centralized logging for audit trail and compliance.
 * Implements Serializable for persistence.
 */
public class ActionLog implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum ActionType {
        PATIENT_ADMITTED,
        PATIENT_DISCHARGED,
        PATIENT_MOVED,
        PRESCRIPTION_ADDED,
        MEDICATION_ADMINISTERED,
        STAFF_ADDED,
        STAFF_MODIFIED,
        SHIFT_ASSIGNED,
        SYSTEM_START,
        SYSTEM_SHUTDOWN
    }
    
    private String logId;
    private LocalDateTime timestamp;
    private Staff performedBy;
    private ActionType actionType;
    private String description;
    private String targetId; // Patient ID, Bed ID, etc.
    
    public ActionLog(String logId, Staff performedBy, ActionType actionType, 
                    String description, String targetId) {
        this.logId = logId;
        this.timestamp = LocalDateTime.now();
        this.performedBy = performedBy;
        this.actionType = actionType;
        this.description = description;
        this.targetId = targetId;
    }
    
    public String getLogId() {
        return logId;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public Staff getPerformedBy() {
        return performedBy;
    }
    
    public ActionType getActionType() {
        return actionType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getTargetId() {
        return targetId;
    }
    
    @Override
    public String toString() {
        String staffInfo = performedBy != null ? performedBy.getName() : "System";
        return "[" + timestamp + "] " + actionType + " by " + staffInfo + ": " + description;
    }
}
