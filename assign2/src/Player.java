import java.util.UUID;

public class Player {
    private String username;
    private String token;

    public Player(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
