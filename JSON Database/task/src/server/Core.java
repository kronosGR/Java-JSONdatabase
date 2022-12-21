package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import server.Exceptions.NoKeyException;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Core {

    private static Core instance = new Core();
    private JsonObject db;
    private Lock rLock;
    private Lock wLock;
    private ReadWriteLock rwLock;
    private String filename = "C:\\Users\\geoel\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json";

    private Core() {
        rwLock = new ReentrantReadWriteLock();
        wLock = rwLock.writeLock();
        rLock = rwLock.readLock();

        try {
            String jsonFile = new String(Files.readAllBytes(Paths.get(filename)));
            db =new Gson().fromJson(jsonFile, JsonObject.class);
        } catch (FileNotFoundException e) {
            throw new NoKeyException();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new NoKeyException();
        }
    }

    public static Core getInstance(){
        return instance;
    }

    private void save() {
        try {
            FileWriter fw = new FileWriter("C:\\Users\\geoel\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json");
            fw.write(db.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonElement find(JsonArray array, boolean create) {
        JsonElement tmp = db;
        if (create) {
            for (JsonElement item : array) {
                if (!tmp.getAsJsonObject().has(item.getAsString())) {
                    tmp.getAsJsonObject().add(item.getAsString(), new JsonObject());
                }

                tmp = tmp.getAsJsonObject().get(item.getAsString());
            }
        } else {
            for (JsonElement item : array) {
                if (!item.isJsonPrimitive() || !tmp.getAsJsonObject().has(item.getAsString())) {
                    throw new NoKeyException();
                }
                tmp = tmp.getAsJsonObject().get(item.getAsString().replaceAll("\"", ""));
            }
        }
        return tmp;
    }

    public void set(JsonElement key, JsonElement value) {
        try {
            wLock.lock();

            if (db == null) {
                db = new JsonObject();
                db.add(key.getAsString(), value);
            } else {
                if (key.isJsonPrimitive()) {
                    db.add(key.getAsString(), value);
                } else if (key.isJsonArray()) {
                    JsonArray array = key.getAsJsonArray();
                    String tmp = array.remove(array.size() - 1).getAsString();
                    find(array, true).getAsJsonObject().add(tmp, value);
                } else {
                    throw new NoKeyException();
                }
            }
            save();
        } finally {
            wLock.unlock();
        }
    }

    public JsonElement get(JsonElement key){
        try {
            rLock.lock();
            if (key.isJsonPrimitive() && db.has(key.getAsString())){
                return (JsonElement) db.get(key.getAsString());
            }
            else if(key.isJsonArray()){
                return (JsonElement) find(key.getAsJsonArray(), false);
            }
            throw new NoKeyException();
        } finally {
            rLock.unlock();
        }
    }

    public void delete(JsonElement key){
        try {
            wLock.lock();
            if (key.isJsonPrimitive() && db.has(key.getAsString())){
                db.remove(key.getAsString());
            }
            else if(key.isJsonArray()){
                JsonArray keys = key.getAsJsonArray();
                String tmp = keys.remove(keys.size()-1).getAsString();
                find(keys, true).getAsJsonObject().remove(tmp);
            }
            save();
        } finally {
            wLock.unlock();
        }
    }

}
