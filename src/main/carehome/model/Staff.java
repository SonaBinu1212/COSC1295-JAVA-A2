package au.edu.rmit.carehome.model;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.*;

/**
 * Abstract base class for all staff members in the care home.
 * Demonstrates inheritance and encapsulation principles.
 * 
 * Design Decision: Using abstract class to share common attributes (id, name, password)
 * while allowing specific staff types to implement their own authorization logic.
 */
public abstract class Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String staffId;
    protected String name;
    protected String username;
    protected String password;
    
    public Staff(String staffId, String name, String username, String password) {
        this.staffId = staffId;
        this.name = name;
        this.username = username;
        this.password = password;
    }
    
    // Getters and setters
    public String getStaffId() {
        return staffId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getUsername() {
        return username;
    }
    
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
    
    public abstract String getRole();
    
    public abstract boolean canPrescribeMedication();
    
    public abstract boolean canAdministerMedication();
    
    public abstract boolean canManageStaff();
    
    @Override
    public String toString() {
        return getRole() + " - " + name + " (ID: " + staffId + ")";
    }
}
