import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

public class WackServer extends Thread {
    ServerSocket serverSocket;

    public WackServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.start();
        System.out.println("Server started on port " + port);
    }

    //thread to listen for new clients connecting, creates an instance of ClientHandler if found
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connection detected, initiating handler...");
                new ClientHandler(socket);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    //private class to start a thread for each client
    private class ClientHandler extends Thread {
        private Socket socket;
        private BufferedOutputStream bos;
        private BufferedInputStream bis;


        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;

            bos = new BufferedOutputStream(new DataOutputStream(socket.getOutputStream()));
            bis = new BufferedInputStream(new DataInputStream(socket.getInputStream()));
            start();
        }

        public void run() {
            try {
                while (!interrupted()) {
                    byte[] data = new byte[1024];
                    bis.read(data);
                    System.out.println("server:" + new String (data, StandardCharsets.UTF_8 ));
                    sleep(500);
                }
            } catch (IOException | InterruptedException e) {
                System.err.println();
            }
            try {
                socket.close();
                System.out.println("Socket closed.");
            } catch (Exception e) {
                System.err.println();
            }
        }
    }
}
