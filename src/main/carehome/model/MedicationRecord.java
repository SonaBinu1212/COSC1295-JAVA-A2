package au.edu.rmit.carehome.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a record of medication administration to a patient.
 * Tracks who administered the medication and when.
 * 
 * Design Decision: Separate MedicationRecord class to maintain audit trail
 * of all medication administrations for compliance and safety.
 */
public class MedicationRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String recordId;
    private Patient patient;
    private Prescription.PrescriptionItem prescriptionItem;
    private Nurse administeringNurse;
    private LocalDateTime administrationDateTime;
    private String notes;
    private boolean administered;
    
    public MedicationRecord(String recordId, Patient patient, 
                           Prescription.PrescriptionItem prescriptionItem,
                           Nurse administeringNurse, LocalDateTime administrationDateTime) {
        this.recordId = recordId;
        this.patient = patient;
        this.prescriptionItem = prescriptionItem;
        this.administeringNurse = administeringNurse;
        this.administrationDateTime = administrationDateTime;
        this.administered = true;
    }
    
    public String getRecordId() {
        return recordId;
    }
    
    public Patient getPatient() {
        return patient;
    }
    
    public Prescription.PrescriptionItem getPrescriptionItem() {
        return prescriptionItem;
    }
    
    public Nurse getAdministeringNurse() {
        return administeringNurse;
    }
    
    public LocalDateTime getAdministrationDateTime() {
        return administrationDateTime;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public boolean isAdministered() {
        return administered;
    }
    
    @Override
    public String toString() {
        return prescriptionItem.getMedicine().getName() + " administered by " + 
               administeringNurse.getName() + " on " + administrationDateTime;
    }
}
