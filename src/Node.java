import java.util.UUID;

public class Node {
    private final String ID;
    private String status;

    public Node(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setStatus(String status) {
        if (status == "CONNECTED" || status == "DISCONNECTED") {
            this.status = status;
        }
    }

    public String getStatus(String status){
        return status;
    }


}
