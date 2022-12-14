package server;

import client.Request;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
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

                try {
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
                            delete(request.getKey(), request.getFilename());
                            wLock.unlock();
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

    private String get(String key) {
        String res = null;
        JSONParser jsonParser = new JSONParser();
        Object object = new Object();
        try (FileReader reader = new FileReader("C:\\Users\\geoel\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json")) {
            object = jsonParser.parse(reader);
            JSONArray list = (JSONArray) object;

            for (int i = 0; i < list.size(); i++) {
                JSONObject tmp = (JSONObject) list.get(i);
                if (tmp.get("key").equals(key)) {
                    return res = tmp.get("value").toString();
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (res == null) {
                throw new RuntimeException("No such key");
            }
        }

        return null;
    }

    private void set(String key, String value, JSONArray list, String filename) {
        boolean exists = false;
        JSONArray values = new JSONArray();
        Object obj = new Object();
        try (FileReader reader = new FileReader("C:\\Users\\geoel\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json")) {
            JSONParser parser = new JSONParser();
            obj = parser.parse(reader);
            if (obj.toString().length() == 0) {
                values = (JSONArray) obj;

                for (int i = 0; i < values.size(); i++) {
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
                values.add(obj2);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        save(values, "C:\\Users\\geoel\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json");
    }

    private void delete(String key, String filename) {
        JSONArray values = new JSONArray();
        Integer pos = null;
        boolean exists = false;

        try (FileReader fr = new FileReader("C:\\Users\\geoel\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json")) {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(fr);
            values = (JSONArray) obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < values.size(); i++) {
            JSONObject tmp = (JSONObject) values.get(i);
            if (tmp.get("key").toString().equals(key)) {
                pos = i;
            }
        }

        JSONArray newValues = new JSONArray();
        int size = values.size();
        if (pos != null) {
            for (int i = 0; i < size; i++) {
                if (i != pos) {
                    newValues.add(values.get(i));
                }
            }
        } else {
            throw new RuntimeException("No such key");
        }
        save(newValues, "C:\\Users\\geoel\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json");
    }

    private void save(JSONArray values, String filename) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(filename);
            values.writeJSONString(values, writer);
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
