import java.net.Socket;
import java.util.Random;

public class Player {
    private String username;
    private String token;
    private int rank;
    private Socket socket;

    public Player(String username, String token, Socket socket) {
        this.username = username;
        this.token = token;
        this.rank = 1; //new Random().nextInt(11); // Assigns a random rank from 0 to 10
        this.socket = socket;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public int getRank() {
        return rank;
    }

    public Socket getSocket() {
        return socket;
    }
}
