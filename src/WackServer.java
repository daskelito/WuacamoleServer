import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WackServer extends Thread {
    private ServerSocket serverSocket;
    private Database database = new Database();
    private int nodeIDindex = 1;
    private StringBuilder sb;

    public WackServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.start();
        System.out.println("Server started on port " + port);
        sb = new StringBuilder();
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
    public class ClientHandler extends Thread {
        private Socket socket;
        private InputStreamReader isr;
        private OutputStreamWriter osw;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            System.out.println("Handler and socket established.");

            isr = new InputStreamReader(socket.getInputStream());
            osw = new OutputStreamWriter(socket.getOutputStream());

            start();
        }

        public void run() {
            BufferedReader br = new BufferedReader(isr);
            BufferedWriter bw = new BufferedWriter(osw);

            try {
                while (true) {
                    String s = br.readLine();
                    if (s != null) {
                        if (s.contains("index")) {
                            bw.write(nodeIDindex);
                            bw.flush();
                            System.out.println("index " + nodeIDindex + " sent.");
                            nodeIDindex++;
                        } else if (s.contains("result")) {
                            sb.append(s);
                            s = sb.substring(6, 8);
                            System.out.println("Score from round: " + s);
                        }
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

//     Format: [to(00-15]x[from(00-15)]x[message type(1-7)]x[data]
//     first 2 chars are "to" and modified in the function
//    public void sendMessageToAll(String from, String messageType, String data) {
//        String message = "+" + "xxx" + "from" + 'x' + messageType + 'x' + data + "-";
//        StringBuilder sb = new StringBuilder(message);
//        ArrayList<Node> list = database.getAllOtherNodes(from);
//        for (Node n : list) {
//            sb.setCharAt(1, n.getID().charAt(1));
//            sb.setCharAt(2, n.getID().charAt(2));
//            String messageToSend = sb.toString();
//            ClientHandler ch = n.getClientHandler();
//            try {
//                ch.bos.write(messageToSend.getBytes(StandardCharsets.UTF_8));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//     public void interpretMessage(byte[] bytedata) {
//        String data = new String(bytedata, StandardCharsets.UTF_8);
//        StringBuilder sb = new StringBuilder(data);
//        String to = "" + sb.substring(1, 2);
//        String from = "" + sb.substring(4, 5);
//        String mtype = "" + sb.substring(7, 7);
//        String message = "" + sb.substring(9, 14);

//case 1 exists only for logging, it's handled during initial connection via the socket
//        switch (mtype) {
//            case "2": //keep alive
//            case "3": //mole active
//            case "4": //mole hit
//            case "5": //mole miss
//            case "6": //start game
//            case "7": //stop game
//                sendMessageToAll(from, mtype, message);
//                break;
//            case "8": //node disconnected
//                String index = "" + data.charAt(8) + data.charAt(9);
//                Node m = database.getNode(index);
//                m.setStatus("DISCONNECTED");
//                break;
//            case "9": //mole diff status
//
//                break;
//        }
//        System.out.println("Message sent from node " + from + " to all others with message type " + mtype);
//    }
//    // Format: +[to(00-15]x[from(00-15)]x[message type(1-7)]x[data]-
//    public void sendMessage(String to, String from, String messageType, String data) {
//        String message = "+" + "to" + 'x' + "from" + 'x' + messageType + 'x' + data + "-";
//        //TODO
//        //Send message to the right receiver
//    }
