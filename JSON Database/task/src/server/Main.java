package server;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final int port = 23456;

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(port);

            System.out.println("Server started!");
            Socket client = server.accept();

            DataInputStream input = new DataInputStream(client.getInputStream());
            DataOutputStream output = new DataOutputStream(client.getOutputStream());

            String m = input.readUTF();
            String m2 = input.readUTF();

            System.out.println("Received: "+ m + m2);
            output.writeUTF("A record # " + m2 + " was sent!");
            System.out.println("Sent: " + "A record # " + m2 + " was sent!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
