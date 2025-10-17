package au.edu.rmit.carehome.model;

import au.edu.rmit.carehome.exception.BedOccupiedException;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a bed in a room that can be assigned to a patient.
 * Tracks patient occupancy and associated medical records.
 * 
 * Design Decision: Bed maintains references to patient, prescriptions, and medication records,
 * providing a central point for all patient-related information during their stay.
 */
public class Bed implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String bedId;
    private Patient currentPatient;
    private List<Prescription> prescriptions;
    private List<MedicationRecord> medicationRecords;
    
    public Bed(String bedId) {
        this.bedId = bedId;
        this.prescriptions = new ArrayList<>();
        this.medicationRecords = new ArrayList<>();
    }
    
    public String getBedId() {
        return bedId;
    }
    
    public boolean isOccupied() {
        return currentPatient != null;
    }
    
    public Patient getCurrentPatient() {
        return currentPatient;
    }
    
    public void assignPatient(Patient patient) throws BedOccupiedException {
        if (isOccupied()) {
            throw new BedOccupiedException("Bed " + bedId + " is already occupied by " + 
                                          currentPatient.getName());
        }
        this.currentPatient = patient;
    }
    
    public Patient removePatient() {
        Patient patient = this.currentPatient;
        this.currentPatient = null;
        return patient;
    }
    
    public void addPrescription(Prescription prescription) {
        prescriptions.add(prescription);
    }
    
    public List<Prescription> getPrescriptions() {
        return new ArrayList<>(prescriptions);
    }
    
    public List<Prescription> getActivePrescriptions() {
        List<Prescription> active = new ArrayList<>();
        for (Prescription p : prescriptions) {
            if (p.isActive()) {
                active.add(p);
            }
        }
        return active;
    }
    
    public void addMedicationRecord(MedicationRecord record) {
        medicationRecords.add(record);
    }
    
    public List<MedicationRecord> getMedicationRecords() {
        return new ArrayList<>(medicationRecords);
    }
    
    public void clearHistory() {
        prescriptions.clear();
        medicationRecords.clear();
    }
    
    @Override
    public String toString() {
        return "Bed " + bedId + (isOccupied() ? " (Occupied by " + currentPatient.getName() + ")" : " (Vacant)");
    }
}
