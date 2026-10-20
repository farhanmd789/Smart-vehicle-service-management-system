package service;

import model.ServiceRecord;
import util.FileHandler;

import java.util.ArrayList;

/**
 * Tracks and reports all operations performed in the system.
 * Demonstrates ArrayList usage and File Handling for operation history.
 */
public class ReportService {

    private static final String DATA_FILE = "data/records.dat";

    private ArrayList<ServiceRecord> history;
    private int recordCounter;

    @SuppressWarnings("unchecked")
    public ReportService() {
        Object loaded = FileHandler.load(DATA_FILE);
        if (loaded instanceof ArrayList) {
            history = (ArrayList<ServiceRecord>) loaded;
            recordCounter = history.size();
        } else {
            history = new ArrayList<>();
            recordCounter = 0;
        }
    }

    /**
     * Logs a service operation to the history.
     */
    public void logOperation(String vehicleId, String operationType,
                             String description, String performedBy) {
        recordCounter++;
        String rid = "REC" + String.format("%04d", recordCounter);
        ServiceRecord sr = new ServiceRecord(rid, vehicleId, operationType, description, performedBy);
        history.add(sr);
        FileHandler.save(history, DATA_FILE);
    }

    /**
     * Prints all records in a formatted table.
     */
    public void generateReport() {
        System.out.println("\n  ┌────────────────────────────────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("  │                                      SERVICE OPERATION HISTORY                                          │");
        System.out.println("  └────────────────────────────────────────────────────────────────────────────────────────────────────────┘");
        if (history.isEmpty()) {
            System.out.println("  [INFO] No service records found.");
            return;
        }
        System.out.println("  +----------+------------+------------+--------------------------------+-----------------+---------------------+");
        System.out.println("  | RecordID | Vehicle ID | Operation  |          Description           |   Performed By  |      Timestamp      |");
        System.out.println("  +----------+------------+------------+--------------------------------+-----------------+---------------------+");
        for (ServiceRecord sr : history) {
            System.out.println("  " + sr);
        }
        System.out.println("  +----------+------------+------------+--------------------------------+-----------------+---------------------+");
        System.out.println("  Total Operations: " + history.size());
    }

    public ArrayList<ServiceRecord> getHistory() {
        return history;
    }
}
