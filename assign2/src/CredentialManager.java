import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CredentialManager {
    private Map<String, String> credentials = new HashMap<>();

    public CredentialManager(String filePath) {
        loadCredentials(filePath);
    }

    private void loadCredentials(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String hashedPassword = parts[1].trim(); // Already hashed in Base64
                    credentials.put(username, hashedPassword);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load credentials from file: " + filePath);
            e.printStackTrace();
        }
    }

    public boolean authenticate(String username, String password) {
        String hashedPassword = credentials.get(username);
        if (hashedPassword != null) {
            String userHashedPassword = hashPassword(password);
         //   System.out.println("Hashed password from file: " + hashedPassword);
         //   System.out.println("Hashed user input password: " + userHashedPassword);
            return userHashedPassword.equals(hashedPassword);
        }
        return false;
    }

    private String hashPassword(String password) {
        // Convert the password to a Base64-encoded hash
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
}
