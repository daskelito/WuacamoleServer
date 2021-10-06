import java.util.HashMap;
import java.util.UUID;

public class Database {
    private HashMap<UUID, Node> nodes;

    public Database() {
        nodes = new HashMap<UUID, Node>();
    }

    public void addNode(Node node){
        nodes.put(node.getID(), node);
    }

    public Node getNode(UUID uuid){
        return nodes.get(uuid);
    }




}
