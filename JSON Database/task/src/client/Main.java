package client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;
    static String type;
    static String key;
    static String value;


    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        System.out.println("Client started!");
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        Args arg = new Args();
        JCommander.newBuilder().addObject(arg).build().parse(args);
        type = arg.getType();
        key = arg.getKey();
        value = arg.getValue();

        start(socket, input, output);
        socket.close();
    }


    private static void start(Socket socket, DataInputStream input, DataOutputStream output) throws IOException {
        String msg = "";
        String json = "";

        Map<String, String> commands = new HashMap<>();
        Gson gson = new Gson();

        switch (type) {
            case "set":
                commands.put("type", "set");
                commands.put("key", key);
                commands.put("value", value);

                json = gson.toJson(commands);
                System.out.println("Sent: " + json);
                output.writeUTF(json);
                msg = input.readUTF();
                System.out.println("Received: " + msg);
                break;
            case "get":
                commands.put("type","get");
                commands.put("key",key);
                json = gson.toJson(commands);
                System.out.println("Sent: " + json);
                output.writeUTF(json);
                msg = input.readUTF();
                System.out.println("Received: " + msg);
                break;
            case "delete":
                commands.put("type","delete");
                commands.put("key", key);
                json = gson.toJson(commands);
                System.out.println("Sent: " + json);
                output.writeUTF(json);
                msg = input.readUTF();
                System.out.println("Received: " + msg);
                break;
            case "exit":
                commands.put("type","exit");
                json = gson.toJson(commands);
                System.out.println("Sent: " + json);
                output.writeUTF(json);
                msg = input.readUTF();
                System.out.println("Received: " + msg);
                break;
        }
    }
}
