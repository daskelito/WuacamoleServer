import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class WackServer extends Thread {
    ServerSocket serverSocket;
    Database database = new Database();

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

    public void interpretMessage(byte[] data) {
        String s = new String(data, StandardCharsets.UTF_8);
        String to = "" + s.charAt(0) + s.charAt(1);
        String from = "" + s.charAt(3) + s.charAt(4);
        String mtype = "" + s.charAt(6);

        switch (mtype) {
            case "1": //new node
                Node n = new Node(from);
                n.setStatus("CONNECTED");
                database.addNode(n);
                sendMessage(from, "1", "doesnt matter");
                break;
            case "2": //keep alive

                break;
            case "3": //mole active

                break;
            case "4": //mole hit

                break;
            case "5": //mole miss

                break;
            case "6": //start game

                break;
            case "7": //stop game

                break;
        }
    }

    // Format: [to(00-15]x[from(00-15)]x[message type(1-7)]x[data]
    // first 2 chars are "to" and modified in the function
    public void sendMessage(String from, String messageType, String data) {
        String message = "" + "xxx" + "from" + 'x' + messageType + 'x' + data;
        StringBuilder sb = new StringBuilder(message);
        ArrayList<Node> list = database.getAllOtherNodes(from);
        for (Node n : list) {
            sb.setCharAt(0, n.getID().charAt(0));
            sb.setCharAt(1, n.getID().charAt(1));
            String messageToSend = sb.toString();
            //send messageToSend to all other nodes via this loop
        }
    }

    //private class to start a thread for each client
    private class ClientHandler extends Thread {
        private Socket socket;
        private BufferedOutputStream bos;
        private BufferedInputStream bis;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            System.out.println("BASS");
            bos = new BufferedOutputStream(new DataOutputStream(socket.getOutputStream()));
            bis = new BufferedInputStream(new DataInputStream(socket.getInputStream()));
            start();
        }

        public void run() {
            try {
                while (!interrupted()) {
                    byte[] data = new byte[1024];
                    if (bis.read(data) != -1) {
                        interpretMessage(data);
                    }
                }
            } catch (IOException e) {
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
