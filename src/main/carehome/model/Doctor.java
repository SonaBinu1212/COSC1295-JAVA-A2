package au.edu.rmit.carehome.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Represents a Doctor who can prescribe medication.
 * Doctors work 1 hour per day (presumably for rounds and prescriptions).
 * 
 * Design Decision: Simplified doctor scheduling - they work 1 hour daily
 * as per assignment specifications.
 */
public class Doctor extends Staff {
    private static final long serialVersionUID = 1L;
    
    private LocalTime dailyWorkStartTime;
    private static final int WORK_DURATION_HOURS = 1;
    
    public Doctor(String staffId, String name, String username, String password) {
        super(staffId, name, username, password);
        this.dailyWorkStartTime = LocalTime.of(10, 0); // Default 10am
    }
    
    public void setDailyWorkStartTime(LocalTime startTime) {
        this.dailyWorkStartTime = startTime;
    }
    
    public LocalTime getDailyWorkStartTime() {
        return dailyWorkStartTime;
    }
    
    public LocalTime getDailyWorkEndTime() {
        return dailyWorkStartTime.plusHours(WORK_DURATION_HOURS);
    }
    
    public boolean isWorkingOn(DayOfWeek day, LocalTime time) {
        // Doctor works every day for 1 hour
        LocalTime endTime = dailyWorkStartTime.plusHours(WORK_DURATION_HOURS);
        return !time.isBefore(dailyWorkStartTime) && time.isBefore(endTime);
    }
    
    @Override
    public String getRole() {
        return "Doctor";
    }
    
    @Override
    public boolean canPrescribeMedication() {
        return true;
    }
    
    @Override
    public boolean canAdministerMedication() {
        return false;
    }
    
    @Override
    public boolean canManageStaff() {
        return false;
    }
}
