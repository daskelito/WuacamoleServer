import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WackServer extends Thread {
    private static StringBuilder sb;
    private final ServerSocket serverSocket;
    private int nodeIDindex = 10;

    //Main method for starting the server on the given port.
    public static void main(String[] args) throws IOException {
        WackServer ws = new WackServer(5008);
    }

    //Constructor for the server. Establishes a socket and prints what port it's started on.
    public WackServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.start();
        System.out.println("Server started on port " + port);
        sb = new StringBuilder();
    }

    //Converts the incoming string from a node into a sensible string that can be printed and read.
    public static String convertResult(String text) {
        sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (text.charAt(i) != 'x') {
                sb.append(text.charAt(i));
            }
        }
        return sb.toString();
    }

    //Thread to listen for new clients connecting, creates an instance of ClientHandler if found.
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
                while (!interrupted()) {
                    sleep(50);
                    String s = br.readLine();
                    //Two cases: either a node requesting an index or a node sending result from the game round.
                    if (s != null) {
                        if (s.contains("index")) {
                            bw.write(nodeIDindex);
                            bw.flush();
                            System.out.println("Index: " + nodeIDindex + " sent.");
                            System.out.println("-------------------------");
                            nodeIDindex++;
                        } else if (s.contains("result")) {
                            sb.append(s);
                            System.out.println("-------------------------");
                            //System.out.println(s);
                            s = sb.substring(6, 9);
                            String result = convertResult(s);
                            System.out.println("Game over.");
                            System.out.println("Score from round: " + result);
                            System.out.println("-------------------------");
                            sb.setLength(0);
                            nodeIDindex = 10; //Resets index to avoid going over 99
                            this.sleep(5000); //Sleeps in order to ignore more than one result message
                        }
                    }
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
