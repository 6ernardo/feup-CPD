import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;

    public void startConnection(String ip, int port) throws Exception {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        stdIn = new BufferedReader(new InputStreamReader(System.in));

        // Thread to handle server messages
        new Thread(() -> {
            try {
                String fromServer;
                while ((fromServer = in.readLine()) != null) {
                    System.out.println("Server: " + fromServer);
                    if (fromServer.contains("Disconnecting")) {
                        break;  // Break if server sends a disconnect command
                    }
                }
            } catch (Exception e) {
                System.out.println("Server connection closed unexpectedly.");
            } finally {
                try {
                    stopConnection();  // Handle exceptions here
                } catch (Exception e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                }
            }
        }).start();

        // Handle user input
        try {
            String userInput;
            while (!(userInput = stdIn.readLine()).equalsIgnoreCase("quit")) {
                out.println(userInput);
            }
        } finally {
            try {
                stopConnection();  // Handle exceptions here too
            } catch (Exception e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public void stopConnection() throws Exception {
        if (in != null) in.close();
        if (out != null) out.close();
        if (stdIn != null) stdIn.close();
        if (clientSocket != null) clientSocket.close();
        System.out.println("Disconnected from server.");
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java GameClient <serverIP> <port>");
            return;
        }

        GameClient client = new GameClient();
        try {
            client.startConnection(args[0], Integer.parseInt(args[1]));
        } catch (Exception e) {
            System.out.println("Error connecting to server.");
            e.printStackTrace();
        }
    }
}
