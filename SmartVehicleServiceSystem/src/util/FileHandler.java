package util;

import java.io.*;

/**
 * Generic file handler for saving and loading serialized objects.
 * Demonstrates File Handling using ObjectOutputStream / ObjectInputStream.
 */
public class FileHandler {

    /**
     * Saves any Serializable object to a file.
     *
     * @param data     the object to persist
     * @param filePath path of the output file
     */
    public static void save(Object data, String filePath) {
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // create directories if they don't exist
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.out.println("[ERROR] Could not save data: " + e.getMessage());
        }
    }

    /**
     * Loads an object from a serialized file.
     *
     * @param filePath path of the input file
     * @return the deserialized object, or null if file doesn't exist
     */
    @SuppressWarnings("unchecked")
    public static Object load(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[ERROR] Could not load data: " + e.getMessage());
            return null;
        }
    }
}
