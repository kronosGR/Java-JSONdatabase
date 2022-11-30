package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

        System.out.println("Client started!");

        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        output.writeUTF("Give me a record # ");
        output.writeUTF("12");

        System.out.println("Sent: " + "Give me a record # 12");
        String recMsg = input.readUTF();
        System.out.println("Received: "+ recMsg);
    }
}
