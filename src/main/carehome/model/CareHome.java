package au.edu.rmit.carehome.model;

import au.edu.rmit.carehome.exception.*;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main class representing the RMIT Care Home facility.
 * Implements Singleton pattern to ensure only one instance of the care home exists.
 * 
 * Design Decisions:
 * - Singleton pattern: Only one care home should exist in the system
 * - High cohesion: CareHome manages all wards, staff, and compliance rules
 * - Low coupling: Uses interfaces and dependency injection where appropriate
 * - Collections: Uses appropriate Java Collections (ArrayList, HashMap, EnumMap)
 * - Exception handling: All business rules enforced through custom exceptions
 * 
 * The CareHome manages:
 * - Two wards with 6 rooms each (varying bed capacity)
 * - Staff roster and compliance checking
 * - Patient admission, discharge, and transfers
 * - Prescription and medication tracking
 * - Action logging for audit trail
 */
public class CareHome implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Singleton instance
    private static CareHome instance;
    
    // Core data structures
    private List<Ward> wards;
    private List<Staff> allStaff;
    private List<Doctor> doctors;
    private List<Nurse> nurses;
    private List<Manager> managers;
    private List<Medicine> medicinesCatalog;
    private List<ActionLog> actionLogs;
    
    // ID generators
    private AtomicInteger nextPatientId;
    private AtomicInteger nextPrescriptionId;
    private AtomicInteger nextRecordId;
    private AtomicInteger nextLogId;
    
    /**
     * Private constructor for Singleton pattern.
     * Initializes the care home with default structure (2 wards, 6 rooms each).
     */
    private CareHome() {
        this.wards = new ArrayList<>();
        this.allStaff = new ArrayList<>();
        this.doctors = new ArrayList<>();
        this.nurses = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.medicinesCatalog = new ArrayList<>();
        this.actionLogs = new ArrayList<>();
        
        this.nextPatientId = new AtomicInteger(1);
        this.nextPrescriptionId = new AtomicInteger(1);
        this.nextRecordId = new AtomicInteger(1);
        this.nextLogId = new AtomicInteger(1);
        
        initializeWards();
        initializeMedicineCatalog();
        initializeDefaultStaff();
    }
    
    /**
     * Get the singleton instance of CareHome.
     */
    public static synchronized CareHome getInstance() {
        if (instance == null) {
            instance = new CareHome();
        }
        return instance;
    }
    
    /**
     * Set the singleton instance (used for deserialization).
     */
    public static synchronized void setInstance(CareHome careHome) {
        instance = careHome;
    }
    
    /**
     * Initialize the two wards with 6 rooms each as per assignment specifications.
     * Room capacities: varying from 1 to 4 beds.
     */
    private void initializeWards() {
        // Ward A
        Ward wardA = new Ward("WA", "Ward A");
        wardA.addRoom(new Room("WA-R1", 4)); // 4 beds
        wardA.addRoom(new Room("WA-R2", 4)); // 4 beds
        wardA.addRoom(new Room("WA-R3", 2)); // 2 beds
        wardA.addRoom(new Room("WA-R4", 2)); // 2 beds
        wardA.addRoom(new Room("WA-R5", 1)); // 1 bed (isolation)
        wardA.addRoom(new Room("WA-R6", 1)); // 1 bed (isolation)
        wards.add(wardA);
        
        // Ward B
        Ward wardB = new Ward("WB", "Ward B");
        wardB.addRoom(new Room("WB-R1", 4)); // 4 beds
        wardB.addRoom(new Room("WB-R2", 4)); // 4 beds
        wardB.addRoom(new Room("WB-R3", 2)); // 2 beds
        wardB.addRoom(new Room("WB-R4", 2)); // 2 beds
        wardB.addRoom(new Room("WB-R5", 1)); // 1 bed (isolation)
        wardB.addRoom(new Room("WB-R6", 1)); // 1 bed (isolation)
        wards.add(wardB);
    }
    
    /**
     * Initialize a catalog of common medicines.
     */
    private void initializeMedicineCatalog() {
        medicinesCatalog.add(new Medicine("MED001", "Paracetamol", "Pain relief and fever reducer", "500", "mg"));
        medicinesCatalog.add(new Medicine("MED002", "Ibuprofen", "Anti-inflammatory", "200", "mg"));
        medicinesCatalog.add(new Medicine("MED003", "Aspirin", "Blood thinner", "75", "mg"));
        medicinesCatalog.add(new Medicine("MED004", "Metformin", "Diabetes medication", "500", "mg"));
        medicinesCatalog.add(new Medicine("MED005", "Lisinopril", "Blood pressure medication", "10", "mg"));
        medicinesCatalog.add(new Medicine("MED006", "Amoxicillin", "Antibiotic", "250", "mg"));
    }
    
    /**
     * Initialize default staff (one manager for initial system access).
     */
    private void initializeDefaultStaff() {
        Manager defaultManager = new Manager("MGR001", "Admin", "admin", "admin123");
        try {
            addStaff(defaultManager, null); // No staff member performing this action (system initialization)
        } catch (UnauthorizedAccessException e) {
            // Should never happen during initialization with null performer
            System.err.println("Error initializing default staff: " + e.getMessage());
        }
    }
    
    // ==================== Staff Management ====================
    
    /**
     * Add a new staff member.
     * Only managers can add staff.
     */
    public void addStaff(Staff newStaff, Staff performedBy) throws UnauthorizedAccessException {
        if (performedBy != null && !performedBy.canManageStaff()) {
            throw new UnauthorizedAccessException(
                performedBy.getName() + " is not authorized to add staff members");
        }
        
        allStaff.add(newStaff);
        
        if (newStaff instanceof Doctor) {
            doctors.add((Doctor) newStaff);
        } else if (newStaff instanceof Nurse) {
            nurses.add((Nurse) newStaff);
        } else if (newStaff instanceof Manager) {
            managers.add((Manager) newStaff);
        }
        
        if (performedBy != null) {
            logAction(performedBy, ActionLog.ActionType.STAFF_ADDED,
                     "Added " + newStaff.getRole() + ": " + newStaff.getName(),
                     newStaff.getStaffId());
        }
    }
    
    /**
     * Modify staff details (e.g., password).
     */
    public void modifyStaffPassword(Staff staff, String newPassword, Staff performedBy) 
            throws UnauthorizedAccessException {
        if (!performedBy.canManageStaff() && !performedBy.getStaffId().equals(staff.getStaffId())) {
            throw new UnauthorizedAccessException(
                performedBy.getName() + " is not authorized to modify this staff member's password");
        }
        
        staff.setPassword(newPassword);
        
        logAction(performedBy, ActionLog.ActionType.STAFF_MODIFIED,
                 "Modified password for " + staff.getName(),
                 staff.getStaffId());
    }
    
    /**
     * Assign a shift to a nurse.
     */
    public void assignNurseShift(Nurse nurse, DayOfWeek day, Nurse.Shift shift, Staff performedBy) 
            throws UnauthorizedAccessException, ComplianceException {
        if (!performedBy.canManageStaff()) {
            throw new UnauthorizedAccessException(
                performedBy.getName() + " is not authorized to assign shifts");
        }
        
        // Check if nurse already has a shift on this day
        Nurse.Shift existingShift = nurse.getShift(day);
        if (existingShift != null) {
            throw new ComplianceException(
                "Nurse " + nurse.getName() + " already has a shift on " + day);
        }
        
        nurse.assignShift(day, shift);
        
        logAction(performedBy, ActionLog.ActionType.SHIFT_ASSIGNED,
                 "Assigned " + shift + " shift to " + nurse.getName() + " on " + day,
                 nurse.getStaffId());
    }
    
    /**
     * Check compliance with business rules:
     * - Nurses assigned to two shifts (8am-4pm, 2pm-10pm) every day
     * - Doctor assigned for 1 hour every day
     * - No more than 8 hours assigned to a nurse in a single day
     */
    public void checkCompliance() throws ComplianceException {
        // Check nurse shift coverage
        for (DayOfWeek day : DayOfWeek.values()) {
            int morningShiftCount = 0;
            int afternoonShiftCount = 0;
            
            for (Nurse nurse : nurses) {
                Nurse.Shift shift = nurse.getShift(day);
                if (shift == Nurse.Shift.MORNING) {
                    morningShiftCount++;
                } else if (shift == Nurse.Shift.AFTERNOON) {
                    afternoonShiftCount++;
                }
                
                // Check no nurse works more than 8 hours per day
                int hoursOnDay = 0;
                if (shift != null) {
                    hoursOnDay = shift.getDurationHours();
                }
                if (hoursOnDay > 8) {
                    throw new ComplianceException(
                        "Nurse " + nurse.getName() + " assigned more than 8 hours on " + day);
                }
            }
            
            if (morningShiftCount < 1) {
                throw new ComplianceException(
                    "No nurse assigned to morning shift on " + day);
            }
            if (afternoonShiftCount < 1) {
                throw new ComplianceException(
                    "No nurse assigned to afternoon shift on " + day);
            }
        }
        
        // Check doctor availability
        if (doctors.isEmpty()) {
            throw new ComplianceException("No doctor assigned to the care home");
        }
    }
    
    // ==================== Patient Management ====================
    
    /**
     * Admit a new patient to a vacant bed.
     */
    public void admitPatient(Patient patient, Bed bed, Staff performedBy) 
            throws BedOccupiedException, UnauthorizedAccessException, StaffNotRosteredException {
        
        validateStaffAuthorization(performedBy);
        
        bed.assignPatient(patient);
        
        logAction(performedBy, ActionLog.ActionType.PATIENT_ADMITTED,
                 "Admitted patient " + patient.getName() + " to bed " + bed.getBedId(),
                 patient.getPatientId());
    }
    
    /**
     * Move a patient from one bed to another.
     */
    public void movePatient(Bed fromBed, Bed toBed, Staff performedBy) 
            throws BedOccupiedException, UnauthorizedAccessException, StaffNotRosteredException {
        
        if (!(performedBy instanceof Nurse)) {
            throw new UnauthorizedAccessException("Only nurses can move patients");
        }
        
        validateStaffRoster(performedBy);
        
        if (!fromBed.isOccupied()) {
            throw new IllegalArgumentException("Source bed is not occupied");
        }
        
        Patient patient = fromBed.removePatient();
        toBed.assignPatient(patient);
        
        // Transfer medical records
        for (Prescription p : fromBed.getPrescriptions()) {
            toBed.addPrescription(p);
        }
        for (MedicationRecord mr : fromBed.getMedicationRecords()) {
            toBed.addMedicationRecord(mr);
        }
        fromBed.clearHistory();
        
        logAction(performedBy, ActionLog.ActionType.PATIENT_MOVED,
                 "Moved patient " + patient.getName() + " from " + fromBed.getBedId() + 
                 " to " + toBed.getBedId(),
                 patient.getPatientId());
    }
    
    /**
     * Discharge a patient from the care home.
     */
    public Patient dischargePatient(Bed bed, Staff performedBy) 
            throws UnauthorizedAccessException, StaffNotRosteredException {
        
        validateStaffAuthorization(performedBy);
        
        if (!bed.isOccupied()) {
            throw new IllegalArgumentException("Bed is not occupied");
        }
        
        Patient patient = bed.getCurrentPatient();
        patient.setDischargeDate(java.time.LocalDate.now());
        
        bed.removePatient();
        
        logAction(performedBy, ActionLog.ActionType.PATIENT_DISCHARGED,
                 "Discharged patient " + patient.getName() + " from bed " + bed.getBedId(),
                 patient.getPatientId());
        
        return patient;
    }
    
    // ==================== Prescription Management ====================
    
    /**
     * Add a prescription for a patient.
     * Only doctors can prescribe medication.
     */
    public Prescription addPrescription(Bed bed, Doctor doctor) 
            throws UnauthorizedAccessException, StaffNotRosteredException {
        
        if (!doctor.canPrescribeMedication()) {
            throw new UnauthorizedAccessException(
                doctor.getName() + " is not authorized to prescribe medication");
        }
        
        validateStaffRoster(doctor);
        
        if (!bed.isOccupied()) {
            throw new IllegalArgumentException("No patient in bed " + bed.getBedId());
        }
        
        String prescriptionId = "RX" + String.format("%05d", nextPrescriptionId.getAndIncrement());
        Prescription prescription = new Prescription(prescriptionId, bed.getCurrentPatient(), doctor);
        bed.addPrescription(prescription);
        
        logAction(doctor, ActionLog.ActionType.PRESCRIPTION_ADDED,
                 "Added prescription " + prescriptionId + " for patient " + 
                 bed.getCurrentPatient().getName(),
                 prescriptionId);
        
        return prescription;
    }
    
    // ==================== Medication Administration ====================
    
    /**
     * Record administration of medication.
     * Only nurses can administer medication.
     */
    public void administerMedication(Bed bed, Prescription.PrescriptionItem item, 
                                    Nurse nurse) 
            throws UnauthorizedAccessException, StaffNotRosteredException {
        
        if (!nurse.canAdministerMedication()) {
            throw new UnauthorizedAccessException(
                nurse.getName() + " is not authorized to administer medication");
        }
        
        validateStaffRoster(nurse);
        
        if (!bed.isOccupied()) {
            throw new IllegalArgumentException("No patient in bed " + bed.getBedId());
        }
        
        String recordId = "MR" + String.format("%05d", nextRecordId.getAndIncrement());
        MedicationRecord record = new MedicationRecord(
            recordId, bed.getCurrentPatient(), item, nurse, LocalDateTime.now());
        
        bed.addMedicationRecord(record);
        
        logAction(nurse, ActionLog.ActionType.MEDICATION_ADMINISTERED,
                 "Administered " + item.getMedicine().getName() + " to patient " + 
                 bed.getCurrentPatient().getName(),
                 recordId);
    }
    
    // ==================== Utility Methods ====================
    
    /**
     * Validate that staff member is authorized (rostered) at current time.
     */
    private void validateStaffRoster(Staff staff) throws StaffNotRosteredException {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek day = now.getDayOfWeek();
        LocalTime time = now.toLocalTime();
        
        boolean isRostered = false;
        
        if (staff instanceof Nurse) {
            isRostered = ((Nurse) staff).isWorkingOn(day, time);
        } else if (staff instanceof Doctor) {
            isRostered = ((Doctor) staff).isWorkingOn(day, time);
        } else if (staff instanceof Manager) {
            isRostered = true; // Managers always have access
        }
        
        if (!isRostered) {
            throw new StaffNotRosteredException(
                staff.getName() + " is not rostered for " + day + " at " + time);
        }
    }
    
    /**
     * Basic authorization check.
     */
    private void validateStaffAuthorization(Staff staff) throws UnauthorizedAccessException {
        if (staff == null) {
            throw new UnauthorizedAccessException("No staff member specified");
        }
    }
    
    /**
     * Log an action performed in the system.
     */
    private void logAction(Staff staff, ActionLog.ActionType actionType, 
                          String description, String targetId) {
        String logId = "LOG" + String.format("%06d", nextLogId.getAndIncrement());
        ActionLog log = new ActionLog(logId, staff, actionType, description, targetId);
        actionLogs.add(log);
    }
    
    /**
     * Generate a new patient ID.
     */
    public String generatePatientId() {
        return "P" + String.format("%05d", nextPatientId.getAndIncrement());
    }
    
    // ==================== Getters ====================
    
    public List<Ward> getWards() {
        return new ArrayList<>(wards);
    }
    
    public Ward getWard(String wardId) {
        for (Ward ward : wards) {
            if (ward.getWardId().equals(wardId)) {
                return ward;
            }
        }
        return null;
    }
    
    public Bed findBed(String bedId) {
        for (Ward ward : wards) {
            Bed bed = ward.getBed(bedId);
            if (bed != null) {
                return bed;
            }
        }
        return null;
    }
    
    public List<Staff> getAllStaff() {
        return new ArrayList<>(allStaff);
    }
    
    public List<Doctor> getDoctors() {
        return new ArrayList<>(doctors);
    }
    
    public List<Nurse> getNurses() {
        return new ArrayList<>(nurses);
    }
    
    public List<Manager> getManagers() {
        return new ArrayList<>(managers);
    }
    
    public List<Medicine> getMedicinesCatalog() {
        return new ArrayList<>(medicinesCatalog);
    }
    
    public void addMedicine(Medicine medicine) {
        medicinesCatalog.add(medicine);
    }
    
    public List<ActionLog> getActionLogs() {
        return new ArrayList<>(actionLogs);
    }
    
    public Staff findStaffByUsername(String username) {
        for (Staff staff : allStaff) {
            if (staff.getUsername().equals(username)) {
                return staff;
            }
        }
        return null;
    }
}
