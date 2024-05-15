import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private CredentialManager credentialManager;
    private GameServer gameServer;
    private String token;
    private int loginAttempts = 0;
    private boolean isAuthenticated = false;
    private GameQueue gameQueue;
    private int queuePos;
    private Player player;

    public ClientHandler(Socket clientSocket, CredentialManager credentialManager, GameServer gameServer, GameQueue gameQueue) {
        this.clientSocket = clientSocket;
        this.credentialManager = credentialManager;
        this.gameServer = gameServer;
        this.gameQueue = gameQueue;
    }

    public Socket getClientSocket() {
        return this.clientSocket;  
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            while (loginAttempts < 3 && !isAuthenticated) {
                out.println("Enter username:");
                String username = in.readLine();
                out.println("Enter password:");
                String password = in.readLine();
                token = credentialManager.authenticate(username, password);

                if (token != null) {
                    out.println("Authentication successful.");
                    isAuthenticated = true;
                    gameServer.addConnectedClient(token, this); // This is the correct place to add the client
                    System.out.println("Authentication successful for " + username);
                    player = new Player(username, token);  // Ensure Player class is properly defined
                    queuePos = gameQueue.enqueue(player, queuePos);
                    System.out.println("position: " + queuePos);
                    handleClientSession();
                    break;
                } else {
                    loginAttempts++;
                    out.println("Authentication failed. Attempts left: " + (3 - loginAttempts));
                    if (loginAttempts >= 3) {
                        out.println("Too many failed login attempts. Disconnecting...");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error handling client connection for [" + token + "]: " + e.getMessage());
        } finally {
            if (!isAuthenticated) {
                disconnectClient();
            }
        }
    }

    private void handleClientSession() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if ("quit".equalsIgnoreCase(message.trim())) {
                    out.println("Disconnecting...");
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error during client session for [" + token + "]: " + e.getMessage());
        } finally {
            disconnectClient();
        }
    }

    private void disconnectClient() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
                gameServer.handleDisconnectedClient(token, queuePos);
                gameQueue.handleDisconnect(player);
                System.out.println("Client [" + token + "] connection closed.");
            }
        } catch (Exception e) {
            System.out.println("Error closing client socket for [" + token + "]: " + e.getMessage());
        }
    }

    public void setQueuePos(int queuePos) {
        this.queuePos = queuePos;
    }

    public int getQueuePos() {
        return queuePos;
    }
}
