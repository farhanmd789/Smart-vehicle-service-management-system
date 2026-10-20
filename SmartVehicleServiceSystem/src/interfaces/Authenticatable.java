package interfaces;

import model.User;

/**
 * Interface defining authentication operations.
 * Demonstrates Interface usage in Core Java OOP.
 */
public interface Authenticatable {
    boolean register(String username, String password, String role);
    User login(String username, String password) throws Exception;
    User adminLogin(String username, String password) throws Exception;
}
