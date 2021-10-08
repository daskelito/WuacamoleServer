import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Database {
    private ArrayList<Node> nodes;

    public Database() {
        nodes = new ArrayList<Node>();
    }

    public void addNode(Node node){
        nodes.add(node);
    }

    public Node getNode(String ID){
        for(Node n : nodes){
            if(n.getID() == ID){
                return n;
            }
        }
        return null;
    }

    public ArrayList<Node> getAllOtherNodes(String ID){
        ArrayList<Node> templist = new ArrayList<Node>();
        for(Node n : nodes){
            if(n.getID() != ID){
                templist.add(n);
            }
        }
        return templist;
    }

    public void print(){
        for(Node n : nodes) {
            System.out.println("Node " + n.getID());
        }
    }

}
