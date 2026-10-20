package menu;

import model.User;
import service.AuthService;
import service.ReportService;
import service.VehicleService;

import java.util.Map;
import java.util.Scanner;

/**
 * Admin sub-menu — provides privileged management operations.
 * Admin can view all users, delete users, view all vehicles,
 * delete vehicles, and view full service history.
 */
public class AdminMenu {

    private final AuthService    authService;
    private final VehicleService vehicleService;
    private final ReportService  reportService;
    private final Scanner        sc;

    public AdminMenu(AuthService authService,
                     VehicleService vehicleService,
                     ReportService reportService,
                     Scanner sc) {
        this.authService    = authService;
        this.vehicleService = vehicleService;
        this.reportService  = reportService;
        this.sc             = sc;
    }

    public void show(String adminName) {
        boolean running = true;
        while (running) {
            printMenu(adminName);
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": viewAllUsers();        break;
                case "2": deleteUser();          break;
                case "3": viewAllVehicles();     break;
                case "4": deleteVehicle();       break;
                case "5": viewServiceHistory();  break;
                case "6": running = false;       break;
                default:
                    System.out.println("\n  [ERROR] Invalid option. Please choose 1–6.");
            }
            if (running) pause();
        }
    }

    // ─── Menu Options ─────────────────────────────────────────────────────────

    private void viewAllUsers() {
        System.out.println("\n  ┌─────────────────────────────────┐");
        System.out.println("  │         ALL REGISTERED USERS     │");
        System.out.println("  └─────────────────────────────────┘");
        System.out.println("  +------------+-----------------+----------+");
        System.out.println("  |  User ID   |    Username     |   Role   |");
        System.out.println("  +------------+-----------------+----------+");
        for (Map.Entry<String, User> entry : authService.getAllUsers().entrySet()) {
            System.out.println("  " + entry.getValue());
        }
        System.out.println("  +------------+-----------------+----------+");
        System.out.println("  Total Users: " + authService.getAllUsers().size());
    }

    private void deleteUser() {
        System.out.println("\n  ┌─────────────────────────────────┐");
        System.out.println("  │           DELETE USER            │");
        System.out.println("  └─────────────────────────────────┘");
        System.out.print("  Enter username to delete: ");
        String username = sc.nextLine().trim();
        if (username.equalsIgnoreCase("admin")) {
            System.out.println("  [ERROR] Cannot delete the default admin account.");
            return;
        }
        if (authService.deleteUser(username)) {
            System.out.println("  [SUCCESS] User '" + username + "' deleted.");
        } else {
            System.out.println("  [ERROR] User '" + username + "' not found.");
        }
    }

    private void viewAllVehicles() {
        vehicleService.viewReport();
    }

    private void deleteVehicle() {
        System.out.println("\n  ┌─────────────────────────────────┐");
        System.out.println("  │        DELETE VEHICLE RECORD     │");
        System.out.println("  └─────────────────────────────────┘");
        System.out.print("  Enter Vehicle ID to delete: ");
        String id = sc.nextLine().trim().toUpperCase();
        if (vehicleService.adminDeleteVehicle(id)) {
            System.out.println("  [SUCCESS] Vehicle '" + id + "' deleted.");
        } else {
            System.out.println("  [ERROR] Vehicle '" + id + "' not found.");
        }
    }

    private void viewServiceHistory() {
        reportService.generateReport();
    }

    // ─── UI Helpers ───────────────────────────────────────────────────────────

    private void printMenu(String adminName) {
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║        ADMIN OPERATIONS — " + padRight(adminName, 14) + "║");
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.println("  ║  1. View All Users                       ║");
        System.out.println("  ║  2. Delete User                          ║");
        System.out.println("  ║  3. View All Vehicles                    ║");
        System.out.println("  ║  4. Delete Vehicle Record                ║");
        System.out.println("  ║  5. View Full Service History            ║");
        System.out.println("  ║  6. Back to Main Menu                    ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.print("  Choose an option: ");
    }

    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    private void pause() {
        System.out.print("\n  Press ENTER to continue...");
        try { sc.nextLine(); } catch (java.util.NoSuchElementException ignored) {}
    }
}
