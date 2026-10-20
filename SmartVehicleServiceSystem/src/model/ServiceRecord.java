package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Records the history of operations performed on vehicles.
 * Demonstrates Encapsulation and File Handling via Serializable.
 */
public class ServiceRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private String recordId;
    private String vehicleId;
    private String operationType; // ADD, UPDATE, DELETE, SEARCH
    private String description;
    private String performedBy;
    private String timestamp;

    public ServiceRecord(String recordId, String vehicleId, String operationType,
                         String description, String performedBy) {
        this.recordId      = recordId;
        this.vehicleId     = vehicleId;
        this.operationType = operationType;
        this.description   = description;
        this.performedBy   = performedBy;
        this.timestamp     = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // ─── Getters ──────────────────────────────────────────────────────────────
    public String getRecordId()       { return recordId;       }
    public String getVehicleId()      { return vehicleId;      }
    public String getOperationType()  { return operationType;  }
    public String getDescription()    { return description;    }
    public String getPerformedBy()    { return performedBy;    }
    public String getTimestamp()      { return timestamp;      }

    @Override
    public String toString() {
        return String.format("| %-8s | %-10s | %-10s | %-30s | %-15s | %-19s |",
                recordId, vehicleId, operationType, description, performedBy, timestamp);
    }
}
