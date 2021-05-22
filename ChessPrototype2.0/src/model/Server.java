package model;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Class with multiple static Methods to handle Hosting a game
 */
public class Server extends Thread{

    private static boolean isAlreadyHosting = false;
    private static Socket server;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;

    private ServerSocket serverSocket;

    private Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            System.out.println("Waiting for client on port " +
                    serverSocket.getLocalPort() + "...");
            server = serverSocket.accept();
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            inputStream = new DataInputStream(server.getInputStream());
            //System.out.println(in.readUTF());
            outputStream = new DataOutputStream(server.getOutputStream());
            //out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()+ "\nGoodbye!");
            //server.close();
            Chessboard.getInstance().setPlayerConnected(true);

        } catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Creates a Server Thread if no other ServerThread is already running
     */
    public static void startHosting() {
        if (isAlreadyHosting) return;
        isAlreadyHosting = true;
        try {
            Thread t = new Server(50000);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connects to a IP on port 50000
     * @return true if success
     */
    public static boolean startConnection(String ip) {
        int port = 50000;
        try {
            System.out.println("Connecting to " + ip + " on port " + port);
            server = new Socket(ip, port);

            System.out.println("Just connected to " + server.getRemoteSocketAddress());

            outputStream = new DataOutputStream(server.getOutputStream());
            inputStream = new DataInputStream(server.getInputStream());

            Chessboard.getInstance().setPlayerConnected(true);

            //out.writeUTF("Hello from " + server.getLocalSocketAddress());

            //System.out.println("Server says " + in.readUTF());
            //client.close();
        } catch (IOException e) {
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Socket getServer() {
        return server;
    }

    public static DataInputStream getInputStream() {
        return inputStream;
    }

    public static DataOutputStream getOutputStream() {
        return outputStream;
    }

}
