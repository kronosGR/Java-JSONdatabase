package server;

import client.Request;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Handler extends Thread {

    ServerSocket server;
    Socket socket;

    DataInputStream inputStream;
    DataOutputStream outputStream;

    Request request;
    Response response;

    boolean locked = false;

    public Handler(ServerSocket server,
                   Socket socket,
                   DataInputStream input,
                   DataOutputStream output) {
        this.server = server;
        this.socket = socket;
        this.inputStream = input;
        this.outputStream = output;
    }

    @Override
    public void run() {

        while (!locked) {
            try {
                request = new Gson().fromJson(inputStream.readUTF(), Request.class);
                response = new Response();

                try {
                    switch (request.getType()) {
                        case "get":
                            response.setValue(get(request.getKey()).toString()/*.replaceAll("\"", "")*/);
                            break;
                        case "set":
                            set(request.getKey(), request.getValue());
                            break;
                        case "delete":
                            delete(request.getKey());
                            break;
                        case "exit":
                            response.setRes(Response.RES_OK);
                            outputStream.writeUTF(response.toJSON());
                            server.close();
                            return;
                        default:
                            System.out.println("Bad Request");
                    }
                    response.setRes(Response.RES_OK);
                } catch (Exception e) {
                    response.setRes(Response.RES_ERROR);
                    response.setReas(e.getMessage());
                } finally {
                    try {
                        outputStream.writeUTF(response.toJSON());
                       locked = true;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (Exception e) {
                try {
                    inputStream.close();
                    outputStream.close();
                    socket.close();
                    return;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    private JsonElement get(JsonElement key) {
        return Core.getInstance().get(key);
    }

    private void set(JsonElement key, JsonElement value) {
        Core.getInstance().set(key, value);
    }

    private void delete(JsonElement key) {
        Core.getInstance().delete(key);
    }
}
