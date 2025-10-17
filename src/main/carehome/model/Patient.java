package au.edu.rmit.carehome.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a patient/resident in the care home.
 * 
 * Design Decision: Patient class contains basic demographic and medical information.
 * Gender is tracked for bed assignment compliance (gender-separated rooms).
 */
public class Patient implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum Gender {
        MALE, FEMALE
    }
    
    private String patientId;
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String medicalCondition;
    private boolean requiresIsolation;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
    
    public Patient(String patientId, String name, LocalDate dateOfBirth, Gender gender, 
                   String medicalCondition, boolean requiresIsolation) {
        this.patientId = patientId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.medicalCondition = medicalCondition;
        this.requiresIsolation = requiresIsolation;
        this.admissionDate = LocalDate.now();
    }
    
    // Getters and setters
    public String getPatientId() {
        return patientId;
    }
    
    public String getName() {
        return name;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public Gender getGender() {
        return gender;
    }
    
    public String getMedicalCondition() {
        return medicalCondition;
    }
    
    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = medicalCondition;
    }
    
    public boolean requiresIsolation() {
        return requiresIsolation;
    }
    
    public void setRequiresIsolation(boolean requiresIsolation) {
        this.requiresIsolation = requiresIsolation;
    }
    
    public LocalDate getAdmissionDate() {
        return admissionDate;
    }
    
    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }
    
    public LocalDate getDischargeDate() {
        return dischargeDate;
    }
    
    public void setDischargeDate(LocalDate dischargeDate) {
        this.dischargeDate = dischargeDate;
    }
    
    public int getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }
    
    @Override
    public String toString() {
        return name + " (ID: " + patientId + ", " + gender + ", Age: " + getAge() + ")";
    }
}
