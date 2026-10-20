package model;

/**
 * Admin extends User — demonstrates Inheritance.
 * Admin always has the role "ADMIN".
 */
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin(String userId, String username, String password) {
        super(userId, username, password, "ADMIN");
    }

    /**
     * Polymorphic override — admin-specific descriptor.
     */
    @Override
    public String toString() {
        return String.format("| %-10s | %-15s | %-8s | [ADMINISTRATOR] |",
                getUserId(), getUsername(), getRole());
    }
}
