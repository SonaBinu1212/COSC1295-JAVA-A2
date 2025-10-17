package au.edu.rmit.carehome.model;

import java.io.Serializable;

/**
 * Represents a medicine with name and standard dosage information.
 * 
 * Design Decision: Separate Medicine class to maintain a catalog of available medications
 * that can be referenced in prescriptions.
 */
public class Medicine implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String medicineId;
    private String name;
    private String description;
    private String standardDosage;
    private String unit; // e.g., "mg", "ml", "tablets"
    
    public Medicine(String medicineId, String name, String description, 
                    String standardDosage, String unit) {
        this.medicineId = medicineId;
        this.name = name;
        this.description = description;
        this.standardDosage = standardDosage;
        this.unit = unit;
    }
    
    public String getMedicineId() {
        return medicineId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getStandardDosage() {
        return standardDosage;
    }
    
    public String getUnit() {
        return unit;
    }
    
    @Override
    public String toString() {
        return name + " (" + standardDosage + " " + unit + ")";
    }
}
