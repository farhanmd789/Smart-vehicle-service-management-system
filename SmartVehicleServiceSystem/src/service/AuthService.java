package service;

import exceptions.InvalidCredentialsException;
import interfaces.Authenticatable;
import model.Admin;
import model.User;
import util.FileHandler;

import java.util.HashMap;
import java.util.UUID;

/**
 * Handles user registration, login, and admin login.
 * Implements Authenticatable interface — demonstrates Abstraction & Polymorphism.
 * Uses HashMap for in-memory storage and FileHandler for persistence.
 */
public class AuthService implements Authenticatable {

    private static final String DATA_FILE  = "data/users.dat";
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin@123";

    private HashMap<String, User> userMap; // username -> User

    @SuppressWarnings("unchecked")
    public AuthService() {
        Object loaded = FileHandler.load(DATA_FILE);
        if (loaded instanceof HashMap) {
            userMap = (HashMap<String, User>) loaded;
        } else {
            userMap = new HashMap<>();
            // Seed default admin
            Admin defaultAdmin = new Admin("ADM001", ADMIN_USER, ADMIN_PASS);
            userMap.put(ADMIN_USER, defaultAdmin);
            FileHandler.save(userMap, DATA_FILE);
        }
    }

    // ─── Authenticatable Implementation ───────────────────────────────────────

    /**
     * Registers a new user if username is not already taken.
     */
    @Override
    public boolean register(String username, String password, String role) {
        if (userMap.containsKey(username.toLowerCase())) {
            System.out.println("\n  [ERROR] Username '" + username + "' already exists. Choose a different username.");
            return false;
        }
        String uid  = "USR" + String.format("%03d", userMap.size() + 1);
        User newUser = new User(uid, username, password, role);
        userMap.put(username.toLowerCase(), newUser);
        FileHandler.save(userMap, DATA_FILE);
        System.out.println("\n  [SUCCESS] User registered successfully! Your User ID: " + uid);
        return true;
    }

    /**
     * Authenticates a regular user.
     */
    @Override
    public User login(String username, String password) throws InvalidCredentialsException {
        User user = userMap.get(username.toLowerCase());
        if (user == null || user.getRole().equals("ADMIN")) {
            throw new InvalidCredentialsException("User not found: '" + username + "'.");
        }
        if (!user.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Invalid password for user: '" + username + "'.");
        }
        System.out.println("\n  [SUCCESS] Welcome back, " + user.getUsername() + "!");
        return user;
    }

    /**
     * Authenticates an admin user.
     */
    @Override
    public User adminLogin(String username, String password) throws InvalidCredentialsException {
        User user = userMap.get(username.toLowerCase());
        if (user == null || !user.getRole().equals("ADMIN")) {
            throw new InvalidCredentialsException("Admin account not found: '" + username + "'.");
        }
        if (!user.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Invalid admin password.");
        }
        System.out.println("\n  [SUCCESS] Admin access granted. Welcome, " + user.getUsername() + "!");
        return user;
    }

    // ─── Admin Utility Methods ────────────────────────────────────────────────

    public HashMap<String, User> getAllUsers() {
        return userMap;
    }

    public boolean deleteUser(String username) {
        if (!userMap.containsKey(username.toLowerCase())) return false;
        userMap.remove(username.toLowerCase());
        FileHandler.save(userMap, DATA_FILE);
        return true;
    }
}
