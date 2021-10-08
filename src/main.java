import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {
//        WackServer ws = new  WackServer(888);
//        Client client = new Client("localhost", 888);
        Database db = new Database();
        db.addNode(new Node("04"));
        db.addNode(new Node("74"));
        db.addNode(new Node("84"));
        db.print();

    }
}
