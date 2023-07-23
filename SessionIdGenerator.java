import java.util.BitSet;
import java.util.LinkedList;
import java.util.Queue;

public class SessionIdGenerator {
    private BitSet sessionIds; // To keep track of available session IDs
    private Queue<Integer> releasedIds; // To store released session IDs for reuse
    private int nextId; // To keep track of the next available session ID

    public SessionIdGenerator(int maxUsers) {
        // The maximum number of users determines the number of possible session IDs
        sessionIds = new BitSet(maxUsers);
        releasedIds = new LinkedList<>();
        nextId = 0;
    }

    // Method to provide a unique session ID
    public int getUniqueId() {
        if (!releasedIds.isEmpty()) {
            // If there are released IDs, use one of them for reuse
            int releasedId = releasedIds.poll();
            sessionIds.set(releasedId); // Mark the ID as active
            return releasedId;
        } else {
            // Otherwise, find the next available session ID
            int id = nextId;
            sessionIds.set(id); // Mark the ID as active
            nextId++;
            return id;
        }
    }

    // Method to release an active ID and make it available for reuse
    public void releaseId(int sessionId) {
        if (sessionId < nextId) {
            // If the ID is within the range of possible IDs, release it
            sessionIds.clear(sessionId);
            releasedIds.offer(sessionId);
        } else {
            // If the ID is outside the range of possible IDs, ignore it or handle the error accordingly
            // (e.g., log an error or throw an exception)
        }
    }

    public static void main(String[] args) {
        int maxUsers = 1000000; // Set the maximum number of users (adjust this based on your requirements)

        SessionIdGenerator sessionIdGenerator = new SessionIdGenerator(maxUsers);

        // Example usage:
        int sessionId1 = sessionIdGenerator.getUniqueId();
        int sessionId2 = sessionIdGenerator.getUniqueId();

        System.out.println("Session ID 1: " + sessionId1);
        System.out.println("Session ID 2: " + sessionId2);

        // Example of releasing a session ID for reuse
        sessionIdGenerator.releaseId(sessionId1);
        int sessionId3 = sessionIdGenerator.getUniqueId();

        System.out.println("Session ID 3 (reused): " + sessionId3);
    }
}
