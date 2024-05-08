import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameServer {
    private int port;
    private ServerSocket serverSocket;
    private CredentialManager credentialManager;
    private Map<UUID, ClientHandler> connectedClients;
    private GameQueue gameQueue;

    public GameServer(int port, String credentialFilePath) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.credentialManager = new CredentialManager(credentialFilePath);
        this.connectedClients = new HashMap<>();
        this.gameQueue = new GameQueue();
    }

    public void acceptClients() {
        System.out.println("Server started on port " + port);
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                UUID token = UUID.randomUUID();
                ClientHandler clientHandler = new ClientHandler(clientSocket, credentialManager, token, this,gameQueue);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                System.out.println("Error accepting client connection.");
                e.printStackTrace();
            }
        }
    }

    public void addConnectedClient(UUID token, ClientHandler clientHandler) {
        connectedClients.put(token, clientHandler);
        System.out.println("Added new client with UUID: " + token + " after successful login.");
        printConnectedClients();
    }

    public void handleDisconnectedClient(UUID token) {
        connectedClients.remove(token);
        System.out.println("Client disconnected: " + token);
        printConnectedClients();
    }

    private void printConnectedClients() {
        System.out.println("Connected Clients:");
        connectedClients.forEach((uuid, handler) -> {
            System.out.println("Client UUID: " + uuid + ", Address: " + handler.getClientSocket().getRemoteSocketAddress());
        });
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java GameServer <port> <credentialFilePath>");
            return;
        }
        try {
            int port = Integer.parseInt(args[0]);
            String credentialFilePath = args[1];
            GameServer server = new GameServer(port, credentialFilePath);
            server.acceptClients();
        } catch (IOException e) {
            System.out.println("Cannot start server.");
            e.printStackTrace();
        }
    }
}
