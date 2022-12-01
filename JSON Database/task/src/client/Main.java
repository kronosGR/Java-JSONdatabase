package client;

import com.beust.jcommander.JCommander;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;
    static String type;
    static int index;
    static String value;


    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        System.out.println("Client started!");
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        Args arg = new Args();
        JCommander.newBuilder().addObject(arg).build().parse(args);
        type = arg.getType();
        index = arg.getIndex();
        value = arg.getValue();

        start(socket, input, output);
        socket.close();
    }


    private static void start(Socket socket, DataInputStream input, DataOutputStream output) throws IOException {
        String msg = "";

        switch (type) {
            case "set":
                output.writeUTF("set");
                output.writeUTF(String.valueOf(index));
                output.writeUTF(value);
                System.out.println("Sent: " + type + " " + index + " " + value);
                msg = input.readUTF();
                System.out.println("Received: " + msg);
                break;
            case "get":
                output.writeUTF("get");
                output.writeUTF(String.valueOf(index));
                System.out.println("Sent: " + type + " " + index);
                msg = input.readUTF();
                System.out.println("Received: " + msg);
                break;
            case "delete":
                output.writeUTF("delete");
                output.writeUTF(String.valueOf(index));
                System.out.println("Sent: " + type + " " + index);
                msg = input.readUTF();
                System.out.println("Received: " + msg);
                break;
            case "exit":
                output.writeUTF("exit");
                System.out.println("Sent: " + type);
                msg = input.readUTF();
                System.out.println("Received: " + msg);
                break;
        }
    }
}
