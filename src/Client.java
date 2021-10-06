import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;


public class Client implements Serializable {
    private DataInputStream dis;
    private DataOutputStream dos;
    private final String ip;
    private final int port;


    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        new Connection().start();
    }


    public void send(String message) {
        try {
            dos.write(message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * disconnects the client from the server and closing the streams.
     */
    public void disconnect() {
        try {
            dos.flush();
            dos.close();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * the Connection class is establishing and maintaining the connection to the server.
     * Starting a listener for incoming data from the server.
     */
    private class Connection extends Thread {
        @Override
        public void run() {
            try {
                Socket socket = new Socket(ip, port);
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());

                new listener().start();

                while (!Thread.interrupted()) {
                    send("kdsjfhgö");
                    sleep(300);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * listener listens to all incoming data from the server.
     */
    private class listener extends Thread {
        @Override
        public void run() {
            while (!Thread.interrupted()) {


            }
        }
    }
}