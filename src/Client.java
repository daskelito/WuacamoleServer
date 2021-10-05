import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    private int port;
    private Socket socket;
    DataInputStream dis;
    DataOutputStream dos;

//    public Client(int port) throws IOException {
//        this.port = port;
//        socket = new Socket("localhost", port);
//        dis = new DataInputStream(socket.getInputStream());
//        dos = new DataOutputStream(socket.getOutputStream());
//    }

    public Client(int port) {
        try (Socket socket = new Socket("localhost", port)) {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void send(String message) throws IOException {
        dos.write(message.getBytes(StandardCharsets.UTF_8));
        dos.flush();
    }

    private class Listener extends Thread {
        public void run() {
            String response;
            try {
                while (true) {
                    response = dis.readUTF();
                }
            } catch (IOException e) {
            }

        }
    }
}
