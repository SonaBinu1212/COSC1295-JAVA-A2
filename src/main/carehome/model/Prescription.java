package au.edu.rmit.carehome.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Represents a prescription written by a doctor for a patient.
 * Contains multiple medications with specific dosages and administration times.
 * 
 * Design Decision: Prescription aggregates multiple PrescriptionItems,
 * allowing a single prescription to contain multiple medications.
 * This follows the Composite pattern for medical prescriptions.
 */
public class Prescription implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String prescriptionId;
    private Patient patient;
    private Doctor prescribingDoctor;
    private LocalDateTime prescriptionDate;
    private List<PrescriptionItem> items;
    private boolean active;
    
    public Prescription(String prescriptionId, Patient patient, Doctor prescribingDoctor) {
        this.prescriptionId = prescriptionId;
        this.patient = patient;
        this.prescribingDoctor = prescribingDoctor;
        this.prescriptionDate = LocalDateTime.now();
        this.items = new ArrayList<>();
        this.active = true;
    }
    
    public void addItem(Medicine medicine, String dosage, List<LocalTime> administrationTimes, String instructions) {
        items.add(new PrescriptionItem(medicine, dosage, administrationTimes, instructions));
    }
    
    public String getPrescriptionId() {
        return prescriptionId;
    }
    
    public Patient getPatient() {
        return patient;
    }
    
    public Doctor getPrescribingDoctor() {
        return prescribingDoctor;
    }
    
    public LocalDateTime getPrescriptionDate() {
        return prescriptionDate;
    }
    
    public List<PrescriptionItem> getItems() {
        return new ArrayList<>(items);
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Inner class representing a single medication item in a prescription.
     */
    public static class PrescriptionItem implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private Medicine medicine;
        private String dosage;
        private List<LocalTime> administrationTimes; // Times of day to administer
        private String instructions;
        
        public PrescriptionItem(Medicine medicine, String dosage, 
                               List<LocalTime> administrationTimes, String instructions) {
            this.medicine = medicine;
            this.dosage = dosage;
            this.administrationTimes = new ArrayList<>(administrationTimes);
            this.instructions = instructions;
        }
        
        public Medicine getMedicine() {
            return medicine;
        }
        
        public String getDosage() {
            return dosage;
        }
        
        public List<LocalTime> getAdministrationTimes() {
            return new ArrayList<>(administrationTimes);
        }
        
        public String getInstructions() {
            return instructions;
        }
        
        @Override
        public String toString() {
            return medicine.getName() + " - " + dosage + " at " + administrationTimes;
        }
    }
    
    @Override
    public String toString() {
        return "Prescription " + prescriptionId + " by Dr. " + prescribingDoctor.getName() + 
               " on " + prescriptionDate + " (" + items.size() + " medications)";
    }
}
