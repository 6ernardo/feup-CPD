import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameQueue {
    private Queue<Player> players;
    private Lock lock;
    private static final int NUMBER_OF_PLAYERS_PER_TEAM = 2; // Example value, adjust as needed

    public GameQueue() {
        this.players = new LinkedList<>();
        this.lock = new ReentrantLock();
    }

    // Method to check if a player is already in the queue
    public boolean isPlayerInQueue(String username) {
        lock.lock();
        try {
            return players.stream().anyMatch(p -> p.getUsername().equals(username));
        } finally {
            lock.unlock();
        }
    }

    public void enqueue(Player player) {
        lock.lock();
        try {
            // Use the new method to check if the player is already in the queue
            if (isPlayerInQueue(player.getUsername())) {
                System.out.println("Player " + player.getUsername() + " is already in the queue.\n");
            } else {
                players.add(player);
                System.out.println("Player " + player.getUsername() + " has joined the queue.\n");
    
                // Check if enough players are in the queue to form a team
                if (players.size() >= NUMBER_OF_PLAYERS_PER_TEAM) {
                    System.out.println("Enough players to form a team. Starting the game...\n");
                    // Form a team and start a new game instance
                    Player[] team = new Player[NUMBER_OF_PLAYERS_PER_TEAM];
                    for (int i = 0; i < NUMBER_OF_PLAYERS_PER_TEAM; i++) {
                        team[i] = players.poll();
                    }
                    // Start a new game instance with the formed team
                    new GameInstance(team).start();
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
