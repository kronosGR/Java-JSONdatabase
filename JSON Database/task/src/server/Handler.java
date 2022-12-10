package server;

import client.Request;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Handler extends Thread {

    ServerSocket server;
    Socket socket;
    JSONArray jsonArray;
    DataInputStream inputStream;
    DataOutputStream outputStream;

    Request request;
    Response response;

    boolean locked = false;

    public Handler(ServerSocket server,
                   Socket socket,
                   JSONArray jsonArray,
                   DataInputStream input,
                   DataOutputStream output) {
        this.server = server;
        this.socket = socket;
        this.jsonArray = jsonArray;
        this.inputStream = input;
        this.outputStream = output;
    }

    @Override
    public void run() {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        Lock rLock = lock.readLock();
        Lock wLock = lock.writeLock();

        while (!locked) {
            try {
                request = new Gson().fromJson(inputStream.readUTF(), Request.class);
                response = new Response();

                switch (request.getType()) {
                    case "get":
                        rLock.lock();
                        response.setValue(get(request.getKey()));
                        rLock.unlock();
                        break;
                    case "set":
                        wLock.lock();
                        set(request.getKey(), request.getValue(), jsonArray, request.getFilename());
                        wLock.unlock();
                        break;
                    case "delete":
                        wLock.lock();

                        wLock.unlock();
                        break;


                }
            } catch (Exception e) {

            }
        }
    }

    private String get(String key) {
        String res;
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(request.getFilename())) {
            Object object = jsonParser.parse(reader);
            JSONArray list = (JSONArray) object;

            for (int i = 0; i < list.toList().size(); i++) {
                JSONObject tmp = (JSONObject) list.get(i);
                if (tmp.get("key").equals(key)) {
                    return res = tmp.getString("value");
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void set(String key, String value, JSONArray list, String filename) {
        boolean exists = false;
        JSONArray values = new JSONArray();
        try (FileReader reader = new FileReader(filename)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            if (obj.toString().length() == 0) {
                values = (JSONArray) obj;

                for (int i = 0; i < values.toList().size(); i++) {
                    JSONObject it = (JSONObject) values.get(i);
                    if (it.get("key").toString().equals(key)) {
                        it.put("value", value);
                        exists = true;
                    }
                }
            } else {
                JSONObject obj2 = new JSONObject();
                obj2.put("key", key);
                obj2.put("value", value);
                values.toList().add(obj2);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        save(request.getFilename(), values);
    }

    private void delete(String key, String filename) {
        JSONArray values = new JSONArray();
        Integer pos = null;
        boolean exists = false;

        try (FileReader fr = new FileReader(filename)) {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(fr);
            values = (JSONArray) obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < values.toList().size(); i++) {
            JSONObject tmp = (JSONObject) values.get(i);
            if (tmp.get("key").toString().equals(key)) {
                pos = i;
            }
        }

        JSONArray newValues = new JSONArray();
        int size = values.toList().size();
        if (pos != null) {
            for (int i = 0; i < size; i++) {
                if (i != pos) {
                    newValues.put(values.get(i));
                }
            }
        } else {
            System.out.println("No such key");
        }
        save(request.getFilename(), newValues);
    }

    private void save(String filename, JSONArray values) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(filename);
            values.write(writer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
