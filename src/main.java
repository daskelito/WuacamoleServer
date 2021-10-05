import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {
        WackServer ws = new  WackServer(888);
        Client client = new Client(888);
        client.send("afsafsfasfas");
    }
}
