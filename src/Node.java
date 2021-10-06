import java.util.UUID;

public class Node {
    private final UUID ID;
    private String status;

    public Node() {
        ID = UUID.randomUUID();
    }

    public UUID getID() {
        return ID;
    }

    public void setStatus(String status) {
        if (status == "CONNECTED" || status == "DISCONNECTED") {
            this.status = status;
        }
    }


}
