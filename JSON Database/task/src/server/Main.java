package server;

import org.json.simple.JSONArray;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final int PORT = 23456;
    private static Map<String, String> db = new HashMap<>();

    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(PORT);
        JSONArray jsonArray = new JSONArray();
        System.out.println("Server started!");

        while (!server.isClosed()) {
            Socket socket = null;

            try {
                socket = server.accept();
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                Thread thread = new Handler(server, socket, jsonArray, input, output );
                thread.start();

            } catch (Exception e) {
                server.close();
            }
        }
    }
}

