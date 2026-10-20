package menu;

import exceptions.InvalidCredentialsException;
import model.User;
import service.AuthService;
import service.ReportService;
import service.VehicleService;

import java.util.Scanner;

/**
 * Main menu controller — the entry point for all user interactions.
 * Demonstrates the menu-driven console workflow.
 */
public class MainMenu {

    private final AuthService    authService;
    private final VehicleService vehicleService;
    private final ReportService  reportService;
    private final AdminMenu      adminMenu;
    private final Scanner        sc;

    private User  loggedInUser = null;

    public MainMenu(AuthService authService,
                    VehicleService vehicleService,
                    ReportService reportService,
                    Scanner sc) {
        this.authService    = authService;
        this.vehicleService = vehicleService;
        this.reportService  = reportService;
        this.adminMenu      = new AdminMenu(authService, vehicleService, reportService, sc);
        this.sc             = sc;
    }

    public void start() {
        printBanner();
        boolean running = true;
        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": handleRegister();        break;
                case "2": handleLogin();           break;
                case "3": handleAddRecord();       break;
                case "4": handleUpdateRecord();    break;
                case "5": handleViewReports();     break;
                case "6": handleAdminOperations(); break;
                case "7":
                    running = false;
                    printGoodbye();
                    break;
                default:
                    System.out.println("\n  [ERROR] Invalid option. Please choose 1–7.");
            }
            if (running) pause();
        }
    }

    // ─── Menu Action Handlers ─────────────────────────────────────────────────

    private void handleRegister() {
        System.out.println("\n  ┌─────────────────────────────────┐");
        System.out.println("  │         USER REGISTRATION        │");
        System.out.println("  └─────────────────────────────────┘");
        System.out.print("  Enter username : ");
        String username = sc.nextLine().trim();
        System.out.print("  Enter password : ");
        String password = sc.nextLine().trim();
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("  [ERROR] Username and password cannot be empty.");
            return;
        }
        authService.register(username, password, "USER");
    }

    private void handleLogin() {
        System.out.println("\n  ┌─────────────────────────────────┐");
        System.out.println("  │            USER LOGIN            │");
        System.out.println("  └─────────────────────────────────┘");
        System.out.print("  Enter username : ");
        String username = sc.nextLine().trim();
        System.out.print("  Enter password : ");
        String password = sc.nextLine().trim();
        try {
            loggedInUser = authService.login(username, password);
            vehicleService.setCurrentUser(loggedInUser.getUsername());
        } catch (InvalidCredentialsException e) {
            System.out.println("\n  [LOGIN FAILED] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        }
    }

    private void handleAddRecord() {
        if (!requireLogin()) return;
        vehicleService.addRecord();
    }

    private void handleUpdateRecord() {
        if (!requireLogin()) return;
        System.out.println("\n  Select update action:");
        System.out.println("  1. Update Vehicle Record");
        System.out.println("  2. Delete Vehicle Record");
        System.out.println("  3. Search Vehicle Record");
        System.out.print("  Choose: ");
        String sub = sc.nextLine().trim();
        switch (sub) {
            case "1": vehicleService.updateRecord(); break;
            case "2": vehicleService.deleteRecord(); break;
            case "3": vehicleService.searchRecord(); break;
            default: System.out.println("  [ERROR] Invalid option."); break;
        }
    }

    private void handleViewReports() {
        if (!requireLogin()) return;
        System.out.println("\n  Select report type:");
        System.out.println("  1. View All Vehicle Records");
        System.out.println("  2. View Service Operation History");
        System.out.print("  Choose: ");
        String sub = sc.nextLine().trim();
        switch (sub) {
            case "1": vehicleService.viewReport();    break;
            case "2": reportService.generateReport(); break;
            default:  System.out.println("  [ERROR] Invalid option."); break;
        }
    }

    private void handleAdminOperations() {
        System.out.println("\n  ┌─────────────────────────────────┐");
        System.out.println("  │           ADMIN LOGIN            │");
        System.out.println("  └─────────────────────────────────┘");
        System.out.print("  Admin Username : ");
        String username = sc.nextLine().trim();
        System.out.print("  Admin Password : ");
        String password = sc.nextLine().trim();
        try {
            User admin = authService.adminLogin(username, password);
            vehicleService.setCurrentUser(admin.getUsername());
            adminMenu.show(admin.getUsername());
        } catch (InvalidCredentialsException e) {
            System.out.println("\n  [ACCESS DENIED] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        }
    }

    // ─── UI Helpers ───────────────────────────────────────────────────────────

    private boolean requireLogin() {
        if (loggedInUser == null) {
            System.out.println("\n  [ERROR] You must log in first. Please choose option 2.");
            return false;
        }
        return true;
    }

    private void printBanner() {
        System.out.println();
        System.out.println("  ╔════════════════════════════════════════════════════════════╗");
        System.out.println("  ║                                                            ║");
        System.out.println("  ║      🚗  SMART VEHICLE SERVICE MANAGEMENT SYSTEM  🚗      ║");
        System.out.println("  ║                                                            ║");
        System.out.println("  ║           Powered by Core Java | OOP | Collections        ║");
        System.out.println("  ║                                                            ║");
        System.out.println("  ╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  Default Admin  →  Username: admin  |  Password: admin@123");
        System.out.println("  ─────────────────────────────────────────────────────────────");
    }

    private void printMenu() {
        String userStatus = (loggedInUser != null)
                ? "Logged in as: " + loggedInUser.getUsername()
                : "Not logged in";
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.printf ("  ║  %-42s║%n", " " + userStatus);
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.println("  ║  1. User Registration                    ║");
        System.out.println("  ║  2. User Login                           ║");
        System.out.println("  ║  3. Add Vehicle Record                   ║");
        System.out.println("  ║  4. Update / Delete / Search Records     ║");
        System.out.println("  ║  5. View Reports                         ║");
        System.out.println("  ║  6. Admin Operations                     ║");
        System.out.println("  ║  7. Exit                                 ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.print("  Choose an option: ");
    }

    private void printGoodbye() {
        System.out.println("\n  ════════════════════════════════════════════");
        System.out.println("   Thank you for using Smart Vehicle Service!  ");
        System.out.println("             Have a great day! 👋               ");
        System.out.println("  ════════════════════════════════════════════\n");
    }

    private void pause() {
        System.out.print("\n  Press ENTER to continue...");
        try { sc.nextLine(); } catch (java.util.NoSuchElementException ignored) {}
    }
}
