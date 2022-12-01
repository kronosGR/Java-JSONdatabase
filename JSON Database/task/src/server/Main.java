package server;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ClientInfoStatus;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final int port = 23456;
    public  static String[] array = new String[1000];
    public static void main(String[] args) {
        // fill array with empty string
        Arrays.fill(array, "");

        String text = "";
        String type = "";
        int index = 0;

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server started!");
            while (true) {
                Socket client = server.accept();

                DataInputStream input = new DataInputStream(client.getInputStream());
                DataOutputStream output = new DataOutputStream(client.getOutputStream());
                type = input.readUTF();
                index = Integer.parseInt(input.readUTF()) -1;

                if (type.equals("exit")){
                    break;
                }

                switch (type){
                    case "set":
                        text = input.readUTF();
                        if (index >-1 && index <1000){
                            array[index] = text;
                            output.writeUTF("OK");
                        }
                        break;
                    case "get":
                        if (index >-1 && index < 100 && !array[index].isEmpty()){
                            output.writeUTF(array[index]);
                        } else {
                            output.writeUTF("ERROR");
                        }
                        break;
                    case "delete":
                        if (index > -1 && index <100){
                            array[index] = "";
                            output.writeUTF("OK");
                        } else {
                            output.writeUTF("ERROR");
                        }
                        break;
                }
            }
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
