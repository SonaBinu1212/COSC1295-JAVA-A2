package au.edu.rmit.carehome.model;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a ward containing multiple rooms.
 * The care home has two wards as per assignment specifications.
 * 
 * Design Decision: Ward organizes rooms and provides aggregate statistics
 * for capacity and occupancy management.
 */
public class Ward implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String wardId;
    private String wardName;
    private List<Room> rooms;
    
    public Ward(String wardId, String wardName) {
        this.wardId = wardId;
        this.wardName = wardName;
        this.rooms = new ArrayList<>();
    }
    
    public String getWardId() {
        return wardId;
    }
    
    public String getWardName() {
        return wardName;
    }
    
    public void addRoom(Room room) {
        rooms.add(room);
    }
    
    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }
    
    public Room getRoom(String roomId) {
        for (Room room : rooms) {
            if (room.getRoomId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }
    
    public Bed getBed(String bedId) {
        for (Room room : rooms) {
            Bed bed = room.getBed(bedId);
            if (bed != null) {
                return bed;
            }
        }
        return null;
    }
    
    public int getTotalCapacity() {
        return rooms.stream().mapToInt(Room::getCapacity).sum();
    }
    
    public int getTotalOccupied() {
        return rooms.stream().mapToInt(Room::getOccupiedBedCount).sum();
    }
    
    public int getTotalVacant() {
        return rooms.stream().mapToInt(Room::getVacantBedCount).sum();
    }
    
    /**
     * Find all vacant beds in the ward.
     */
    public List<Bed> getVacantBeds() {
        List<Bed> vacantBeds = new ArrayList<>();
        for (Room room : rooms) {
            for (Bed bed : room.getBeds()) {
                if (!bed.isOccupied()) {
                    vacantBeds.add(bed);
                }
            }
        }
        return vacantBeds;
    }
    
    /**
     * Find suitable rooms for a patient based on gender and isolation requirements.
     */
    public List<Room> getSuitableRooms(Patient patient) {
        List<Room> suitable = new ArrayList<>();
        for (Room room : rooms) {
            if (room.canAccommodatePatient(patient)) {
                suitable.add(room);
            }
        }
        return suitable;
    }
    
    @Override
    public String toString() {
        return wardName + " (" + getTotalOccupied() + "/" + getTotalCapacity() + " beds occupied)";
    }
}
