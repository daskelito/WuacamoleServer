import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class WackServer extends Thread {
    private ServerSocket serverSocket;
    private Database database = new Database();
    private int chIndex = 0;

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



    public void interpretMessage(byte[] bytedata) {
        String data = new String(bytedata, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder(data);
        String to = "" + sb.substring(1, 2);
        String from = "" + sb.substring(4, 5);
        String mtype = "" + sb.substring(7, 7);
        String message = "" + sb.substring(9, 14);

        //case 1 exists only for logging, its handled during initial connection via the socket
        switch (mtype) {
            case "2": //keep alive
            case "3": //mole active
            case "4": //mole hit
            case "5": //mole miss
            case "6": //start game
            case "7": //stop game
                sendMessageToAll(from, mtype, message);
                break;
            case "8": //node disconnected
                String index = "" + data.charAt(8) + data.charAt(9);
                Node m = database.getNode(index);
                m.setStatus("DISCONNECTED");
                break;
            case "9": //mole diff status

                break;
        }
        //log data
    }

    // Format: +[to(00-15]x[from(00-15)]x[message type(1-7)]x[data]-
    public void sendMessage(String to, String from, String messageType, String data) {
        String message = "+" + "to" + 'x' + "from" + 'x' + messageType + 'x' + data + "-";
        //TODO
        //Send message to the right receiver
    }

    // Format: [to(00-15]x[from(00-15)]x[message type(1-7)]x[data]
    // first 2 chars are "to" and modified in the function
    public void sendMessageToAll(String from, String messageType, String data)  {
        String message = "+" + "xxx" + "from" + 'x' + messageType + 'x' + data + "-";
        StringBuilder sb = new StringBuilder(message);
        ArrayList<Node> list = database.getAllOtherNodes(from);
        for (Node n : list) {
            sb.setCharAt(1, n.getID().charAt(1));
            sb.setCharAt(2, n.getID().charAt(2));
            String messageToSend = sb.toString();
            ClientHandler ch = n.getClientHandler();
            try {
                ch.bos.write(messageToSend.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //private class to start a thread for each client
    public class ClientHandler extends Thread {
        private Socket socket;
        private BufferedOutputStream bos;
        private BufferedInputStream bis;


        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            System.out.println("BASS");
            bos = new BufferedOutputStream(new DataOutputStream(socket.getOutputStream()));
            bis = new BufferedInputStream(new DataInputStream(socket.getInputStream()));

            int index = chIndex;
            String i = "";
            if (index < 10) {
                i = "0";
            }
            i += index;
            Node n = new Node(i);
            n.setClientHandler(this);
            n.setStatus("CONNECTED");
            database.addNode(n);

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
