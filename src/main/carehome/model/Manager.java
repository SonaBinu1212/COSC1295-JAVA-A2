package au.edu.rmit.carehome.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Represents a Manager who can add/modify staff and manage the system.
 * Managers have full administrative privileges.
 * 
 * Design Decision: Manager class for administrative functions,
 * separate from medical staff to maintain clear separation of concerns.
 */
public class Manager extends Staff {
    private static final long serialVersionUID = 1L;
    
    public Manager(String staffId, String name, String username, String password) {
        super(staffId, name, username, password);
    }
    
    public boolean isWorkingOn(DayOfWeek day, LocalTime time) {
        // Managers can access the system anytime
        return true;
    }
    
    @Override
    public String getRole() {
        return "Manager";
    }
    
    @Override
    public boolean canPrescribeMedication() {
        return false;
    }
    
    @Override
    public boolean canAdministerMedication() {
        return false;
    }
    
    @Override
    public boolean canManageStaff() {
        return true;
    }
}
