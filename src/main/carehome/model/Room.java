package au.edu.rmit.carehome.model;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a room containing multiple beds.
 * Rooms should maintain gender separation for patient comfort and privacy.
 * 
 * Design Decision: Room enforces gender consistency (all patients in room same gender)
 * unless it's a single-bed room or patient requires isolation.
 */
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String roomId;
    private int capacity;
    private List<Bed> beds;
    
    public Room(String roomId, int capacity) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.beds = new ArrayList<>();
        
        // Initialize beds
        for (int i = 1; i <= capacity; i++) {
            beds.add(new Bed(roomId + "-B" + i));
        }
    }
    
    public String getRoomId() {
        return roomId;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public List<Bed> getBeds() {
        return new ArrayList<>(beds);
    }
    
    public Bed getBed(String bedId) {
        for (Bed bed : beds) {
            if (bed.getBedId().equals(bedId)) {
                return bed;
            }
        }
        return null;
    }
    
    public int getOccupiedBedCount() {
        int count = 0;
        for (Bed bed : beds) {
            if (bed.isOccupied()) count++;
        }
        return count;
    }
    
    public int getVacantBedCount() {
        return capacity - getOccupiedBedCount();
    }
    
    public boolean hasVacantBed() {
        return getVacantBedCount() > 0;
    }
    
    /**
     * Get the predominant gender in the room (for gender separation compliance).
     * Returns null if room is empty.
     */
    public Patient.Gender getRoomGender() {
        for (Bed bed : beds) {
            if (bed.isOccupied()) {
                return bed.getCurrentPatient().getGender();
            }
        }
        return null;
    }
    
    /**
     * Check if a patient can be assigned to this room based on gender and isolation requirements.
     */
    public boolean canAccommodatePatient(Patient patient) {
        if (!hasVacantBed()) {
            return false;
        }
        
        // If patient requires isolation, they need a private room
        if (patient.requiresIsolation() && capacity > 1) {
            return false;
        }
        
        // Check gender compatibility
        Patient.Gender roomGender = getRoomGender();
        if (roomGender != null && roomGender != patient.getGender()) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        return "Room " + roomId + " (" + getOccupiedBedCount() + "/" + capacity + " occupied)";
    }
}
