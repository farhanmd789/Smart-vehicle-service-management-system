package model;

import java.io.Serializable;

/**
 * Represents a vehicle's service record.
 * Demonstrates Encapsulation and File Handling via Serializable.
 */
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    private String vehicleId;
    private String ownerName;
    private String contactNumber;
    private String category;   // e.g. Car, Bike, Truck
    private String status;     // e.g. Pending, In Progress, Completed

    public Vehicle() {}

    public Vehicle(String vehicleId, String ownerName, String contactNumber,
                   String category, String status) {
        this.vehicleId     = vehicleId;
        this.ownerName     = ownerName;
        this.contactNumber = contactNumber;
        this.category      = category;
        this.status        = status;
    }

    // ─── Getters ──────────────────────────────────────────────────────────────
    public String getVehicleId()     { return vehicleId;     }
    public String getOwnerName()     { return ownerName;     }
    public String getContactNumber() { return contactNumber; }
    public String getCategory()      { return category;      }
    public String getStatus()        { return status;        }

    // ─── Setters ──────────────────────────────────────────────────────────────
    public void setVehicleId(String vehicleId)         { this.vehicleId     = vehicleId;     }
    public void setOwnerName(String ownerName)         { this.ownerName     = ownerName;     }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setCategory(String category)           { this.category      = category;      }
    public void setStatus(String status)               { this.status        = status;        }

    @Override
    public String toString() {
        return String.format("| %-10s | %-18s | %-14s | %-8s | %-12s |",
                vehicleId, ownerName, contactNumber, category, status);
    }
}
