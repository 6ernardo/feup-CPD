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
    private UUID token;
    private int loginAttempts = 0;
    private boolean isAuthenticated = false;
    private GameQueue gameQueue; 

    public ClientHandler(Socket clientSocket, CredentialManager credentialManager, UUID token, GameServer gameServer, GameQueue gameQueue) {
        this.clientSocket = clientSocket;
        this.credentialManager = credentialManager;
        this.token = token;
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

                if (credentialManager.authenticate(username, password)) {
                    out.println("Authentication successful.");
                    isAuthenticated = true;
                    gameServer.addConnectedClient(token, this); // This is the correct place to add the client
                    System.out.println("Authentication successful for " + username);
                    Player player = new Player(username, token);  // Ensure Player class is properly defined
                    gameQueue.enqueue(player);
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
                gameServer.handleDisconnectedClient(token);
                System.out.println("Client [" + token + "] connection closed.");
            }
        } catch (Exception e) {
            System.out.println("Error closing client socket for [" + token + "]: " + e.getMessage());
        }
    }
}
