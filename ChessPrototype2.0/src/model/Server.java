package model;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server extends Thread{

    private static boolean isAlreadyHosting = false;
    private static Socket server;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;

    private ServerSocket serverSocket;

    public Server(int port) throws IOException {
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
