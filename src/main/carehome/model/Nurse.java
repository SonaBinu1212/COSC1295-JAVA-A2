package au.edu.rmit.carehome.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

/**
 * Represents a Nurse who can administer medication.
 * Nurses work 8-hour shifts and must comply with rostering rules.
 * 
 * Design Decision: Nurses maintain their shift schedule internally,
 * allowing for validation of actions based on rostered times.
 */
public class Nurse extends Staff {
    private static final long serialVersionUID = 1L;
    
    // Map of DayOfWeek to assigned shift (null if not working that day)
    private Map<DayOfWeek, Shift> weeklySchedule;
    
    public enum Shift {
        MORNING(LocalTime.of(8, 0), LocalTime.of(16, 0)),  // 8am - 4pm
        AFTERNOON(LocalTime.of(14, 0), LocalTime.of(22, 0)); // 2pm - 10pm
        
        private final LocalTime startTime;
        private final LocalTime endTime;
        
        Shift(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
        
        public LocalTime getStartTime() {
            return startTime;
        }
        
        public LocalTime getEndTime() {
            return endTime;
        }
        
        public int getDurationHours() {
            return 8;
        }
    }
    
    public Nurse(String staffId, String name, String username, String password) {
        super(staffId, name, username, password);
        this.weeklySchedule = new EnumMap<>(DayOfWeek.class);
    }
    
    public void assignShift(DayOfWeek day, Shift shift) {
        weeklySchedule.put(day, shift);
    }
    
    public Shift getShift(DayOfWeek day) {
        return weeklySchedule.get(day);
    }
    
    public Map<DayOfWeek, Shift> getWeeklySchedule() {
        return new EnumMap<>(weeklySchedule);
    }
    
    public boolean isWorkingOn(DayOfWeek day, LocalTime time) {
        Shift shift = weeklySchedule.get(day);
        if (shift == null) return false;
        
        return !time.isBefore(shift.getStartTime()) && !time.isAfter(shift.getEndTime());
    }
    
    public int getTotalWeeklyHours() {
        return weeklySchedule.values().stream()
                .mapToInt(Shift::getDurationHours)
                .sum();
    }
    
    @Override
    public String getRole() {
        return "Nurse";
    }
    
    @Override
    public boolean canPrescribeMedication() {
        return false;
    }
    
    @Override
    public boolean canAdministerMedication() {
        return true;
    }
    
    @Override
    public boolean canManageStaff() {
        return false;
    }
}
