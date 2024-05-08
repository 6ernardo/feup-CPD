import java.util.UUID;

public class Player {
    private String username;
    private UUID token;

    public Player(String username, UUID token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public UUID getToken() {
        return token;
    }
}
