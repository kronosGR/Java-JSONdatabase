package client;
import com.beust.jcommander.JCommander;

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

        Args arg = new Args();
        JCommander jc = new JCommander(arg);
        jc.setProgramName("JSON Database");
        JCommander.newBuilder().addObject(arg).build().parse(args);

        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        String request = arg.toJSON();
        output.writeUTF(request);
        System.out.println("Sent: " + request);

        // System.out.println(req.filename);
        System.out.println();
        System.out.println("Received: " + input.readUTF());
    }
}
