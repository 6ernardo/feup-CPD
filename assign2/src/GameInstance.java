public class GameInstance extends Thread {
    private Player[] team;

    public GameInstance(Player[] team) {
        this.team = team;
    }

    public void run() {
        // Implement game logic here
    }
}