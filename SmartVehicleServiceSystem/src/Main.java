import menu.MainMenu;
import service.AuthService;
import service.ReportService;
import service.VehicleService;

/**
 * Application Entry Point — Smart Vehicle Service Management System.
 *
 * OOP Concepts Demonstrated:
 *   ✔ Classes & Objects       — User, Admin, Vehicle, ServiceRecord
 *   ✔ Encapsulation           — Private fields + public getters/setters
 *   ✔ Inheritance             — Admin extends User
 *   ✔ Polymorphism            — toString() overrides, interface implementations
 *   ✔ Abstraction             — Manageable & Authenticatable interfaces
 *   ✔ Interfaces              — Manageable, Authenticatable
 *   ✔ Collections Framework   — ArrayList<Vehicle>, HashMap<String, User>
 *   ✔ Exception Handling      — Custom checked exceptions
 *   ✔ File Handling           — ObjectOutputStream / ObjectInputStream (serialization)
 */
public class Main {
    public static void main(String[] args) {
        // Single shared Scanner across the entire application
        java.util.Scanner sc = new java.util.Scanner(System.in);

        // Instantiate services (dependency order: report -> vehicle -> auth)
        ReportService  reportService  = new ReportService();
        VehicleService vehicleService = new VehicleService(reportService, sc);
        AuthService    authService    = new AuthService();

        // Start the menu-driven application
        MainMenu mainMenu = new MainMenu(authService, vehicleService, reportService, sc);
        mainMenu.start();
    }
}
