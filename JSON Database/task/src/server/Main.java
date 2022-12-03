package server;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.sql.ClientInfoStatus;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final int PORT = 23456;
    private static Map<String, String> db = new HashMap<>();

    public static void main(String[] args) {

        Gson gson = new Gson();
        String inJson = "";
        String outJson = "";
        String text = "";
        String index = "";
        String type = "";

        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server started!");
            while (true) {

                Socket client = server.accept();
                DataInputStream input = new DataInputStream(client.getInputStream());
                DataOutputStream output = new DataOutputStream(client.getOutputStream());

                inJson = input.readUTF();
                JSONObject obj = new JSONObject(inJson);
                type = obj.getString("type");

                if (type.equals("exit")) {
                    Map<String, String> exit = new HashMap<>();
                    exit.put("response", "OK");
                    outJson = gson.toJson(exit);
                    output.writeUTF(outJson);
                    break;
                }

                switch (type) {
                    case "set":
                        index = obj.getString("key");
                        Map<String, String> set = new HashMap<>();
                        text = obj.getString("value");

                        db.put(index, text);
                        set.put("response", "OK");
                        outJson = gson.toJson(set);
                        output.writeUTF(outJson);
                        break;
                    case "get":
                        index = obj.getString("key");
                        Map<String, String> get = new HashMap<>();
                        if (db.containsKey(index)) {
                            get.put("response", "OK");
                            get.put("value", db.get(index));
                        } else {
                            get.put("response", "ERROR");
                            get.put("reason", "No such key");
                        }
                        outJson = gson.toJson(get);
                        output.writeUTF(outJson);
                        break;
                    case "delete":
                        index = obj.getString("key");
                        Map<String, String> delete = new HashMap<>();
                        if (db.containsKey(index) && !db.get(index).equals("")) {
                            db.remove(index);
                            delete.put("response", "OK");
                        } else {
                            delete.put("response", "ERROR");
                            delete.put("reason", "No such key");
                        }
                        outJson = gson.toJson(delete);
                        output.writeUTF(outJson);
                        break;
                }
            }
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
