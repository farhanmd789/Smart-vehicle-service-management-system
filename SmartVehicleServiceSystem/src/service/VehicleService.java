package service;

import exceptions.DuplicateRecordException;
import exceptions.RecordNotFoundException;
import interfaces.Manageable;
import model.Vehicle;
import util.FileHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Manages vehicle records — add, update, delete, search, report.
 * Implements Manageable interface — demonstrates Abstraction, Polymorphism.
 * Uses ArrayList + HashMap for dual in-memory storage.
 * Persists data via FileHandler (File Handling).
 */
public class VehicleService implements Manageable {

    private static final String DATA_FILE = "data/vehicles.dat";

    private ArrayList<Vehicle>         vehicleList; // ordered storage
    private HashMap<String, Vehicle>   vehicleMap;  // fast lookup by ID

    private final ReportService        reportService;
    private final Scanner              sc;
    private String                     currentUser;

    @SuppressWarnings("unchecked")
    public VehicleService(ReportService reportService, Scanner sc) {
        this.reportService = reportService;
        this.sc = sc;
        Object loaded = FileHandler.load(DATA_FILE);
        if (loaded instanceof HashMap) {
            vehicleMap  = (HashMap<String, Vehicle>) loaded;
            vehicleList = new ArrayList<>(vehicleMap.values());
        } else {
            vehicleList = new ArrayList<>();
            vehicleMap  = new HashMap<>();
        }
    }

    public void setCurrentUser(String username) {
        this.currentUser = username;
    }

    // ─── Manageable Implementation ────────────────────────────────────────────

    @Override
    public void addRecord() {
        System.out.println("\n  ┌─────────────────────────────────┐");
        System.out.println("  │         ADD VEHICLE RECORD       │");
        System.out.println("  └─────────────────────────────────┘");
        try {
            System.out.print("  Enter Vehicle ID     : ");
            String id = sc.nextLine().trim().toUpperCase();
            if (id.isEmpty()) throw new IllegalArgumentException("Vehicle ID cannot be empty.");

            if (vehicleMap.containsKey(id)) {
                throw new DuplicateRecordException(
                        "Vehicle ID '" + id + "' already exists. Use a unique ID.");
            }

            System.out.print("  Enter Owner Name     : ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) throw new IllegalArgumentException("Owner name cannot be empty.");

            System.out.print("  Enter Contact Number : ");
            String contact = sc.nextLine().trim();
            if (!contact.matches("\\d{10}"))
                throw new IllegalArgumentException("Contact number must be exactly 10 digits.");

            System.out.print("  Enter Category (Car/Bike/Truck/Other): ");
            String category = sc.nextLine().trim();
            if (category.isEmpty()) throw new IllegalArgumentException("Category cannot be empty.");

            System.out.print("  Enter Status (Pending/In Progress/Completed): ");
            String status = sc.nextLine().trim();
            if (status.isEmpty()) status = "Pending";

            Vehicle v = new Vehicle(id, name, contact, category, status);
            vehicleList.add(v);
            vehicleMap.put(id, v);
            persist();

            reportService.logOperation(id, "ADD",
                    "Added vehicle: " + name + " [" + category + "]", currentUser);
            System.out.println("\n  [SUCCESS] Vehicle record added successfully!");

        } catch (DuplicateRecordException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("\n  [VALIDATION ERROR] " + e.getMessage());
        }
    }

    @Override
    public void updateRecord() {
        System.out.println("\n  ┌─────────────────────────────────┐");
        System.out.println("  │       UPDATE VEHICLE RECORD      │");
        System.out.println("  └─────────────────────────────────┘");
        try {
            System.out.print("  Enter Vehicle ID to update: ");
            String id = sc.nextLine().trim().toUpperCase();

            if (!vehicleMap.containsKey(id)) {
                throw new RecordNotFoundException("No vehicle found with ID: '" + id + "'.");
            }

            Vehicle v = vehicleMap.get(id);
            System.out.println("  Current Record: ");
            printHeader();
            System.out.println("  " + v);
            printFooter();

            System.out.println("\n  (Press ENTER to keep current value)");
            System.out.print("  New Owner Name [" + v.getOwnerName() + "]: ");
            String name = sc.nextLine().trim();
            if (!name.isEmpty()) v.setOwnerName(name);

            System.out.print("  New Contact Number [" + v.getContactNumber() + "]: ");
            String contact = sc.nextLine().trim();
            if (!contact.isEmpty()) {
                if (!contact.matches("\\d{10}"))
                    throw new IllegalArgumentException("Contact number must be exactly 10 digits.");
                v.setContactNumber(contact);
            }

            System.out.print("  New Category [" + v.getCategory() + "]: ");
            String category = sc.nextLine().trim();
            if (!category.isEmpty()) v.setCategory(category);

            System.out.print("  New Status [" + v.getStatus() + "]: ");
            String status = sc.nextLine().trim();
            if (!status.isEmpty()) v.setStatus(status);

            persist();
            reportService.logOperation(id, "UPDATE",
                    "Updated record for vehicle: " + v.getOwnerName(), currentUser);
            System.out.println("\n  [SUCCESS] Vehicle record updated successfully!");

        } catch (RecordNotFoundException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("\n  [VALIDATION ERROR] " + e.getMessage());
        }
    }

    @Override
    public void deleteRecord() {
        System.out.println("\n  ┌─────────────────────────────────┐");
        System.out.println("  │       DELETE VEHICLE RECORD      │");
        System.out.println("  └─────────────────────────────────┘");
        try {
            System.out.print("  Enter Vehicle ID to delete: ");
            String id = sc.nextLine().trim().toUpperCase();

            if (!vehicleMap.containsKey(id)) {
                throw new RecordNotFoundException("No vehicle found with ID: '" + id + "'.");
            }

            Vehicle v = vehicleMap.get(id);
            System.out.print("  Confirm deletion of [" + id + " - " + v.getOwnerName() + "]? (yes/no): ");
            String confirm = sc.nextLine().trim().toLowerCase();
            if (!confirm.equals("yes")) {
                System.out.println("  [INFO] Deletion cancelled.");
                return;
            }

            vehicleList.remove(v);
            vehicleMap.remove(id);
            persist();
            reportService.logOperation(id, "DELETE",
                    "Deleted vehicle record: " + v.getOwnerName(), currentUser);
            System.out.println("\n  [SUCCESS] Vehicle record deleted successfully!");

        } catch (RecordNotFoundException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        }
    }

    @Override
    public void searchRecord() {
        System.out.println("\n  ┌─────────────────────────────────┐");
        System.out.println("  │       SEARCH VEHICLE RECORD      │");
        System.out.println("  └─────────────────────────────────┘");
        try {
            System.out.print("  Enter Vehicle ID to search: ");
            String id = sc.nextLine().trim().toUpperCase();

            if (!vehicleMap.containsKey(id)) {
                throw new RecordNotFoundException("No vehicle found with ID: '" + id + "'.");
            }

            Vehicle v = vehicleMap.get(id);
            System.out.println("\n  Vehicle Record Found:");
            printHeader();
            System.out.println("  " + v);
            printFooter();
            reportService.logOperation(id, "SEARCH",
                    "Searched for vehicle: " + id, currentUser);

        } catch (RecordNotFoundException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        }
    }

    @Override
    public void viewReport() {
        System.out.println("\n  ┌──────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("  │                        ALL VEHICLE RECORDS                                   │");
        System.out.println("  └──────────────────────────────────────────────────────────────────────────────┘");
        if (vehicleList.isEmpty()) {
            System.out.println("  [INFO] No vehicle records found.");
            return;
        }
        printHeader();
        for (Vehicle v : vehicleList) {
            System.out.println("  " + v);
        }
        printFooter();
        System.out.println("  Total Records: " + vehicleList.size());
    }

    // ─── Admin Utility ────────────────────────────────────────────────────────

    public ArrayList<Vehicle> getAllVehicles() { return vehicleList; }

    public boolean adminDeleteVehicle(String id) {
        if (!vehicleMap.containsKey(id)) return false;
        Vehicle v = vehicleMap.remove(id);
        vehicleList.remove(v);
        persist();
        return true;
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private void persist() {
        FileHandler.save(vehicleMap, DATA_FILE);
    }

    private void printHeader() {
        System.out.println("  +------------+--------------------+----------------+----------+--------------+");
        System.out.println("  | Vehicle ID |   Owner Name       | Contact Number | Category |    Status    |");
        System.out.println("  +------------+--------------------+----------------+----------+--------------+");
    }

    private void printFooter() {
        System.out.println("  +------------+--------------------+----------------+----------+--------------+");
    }
}
