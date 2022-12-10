package client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class Request {

    String type;
    String key;
    String value;
    String filename;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Request(String[] args) {

        Args arg = new Args();
        JCommander.newBuilder().addObject(arg).build().parse(args);

        type = arg.getType();
        key = arg.getKey();
        value = arg.getValue();
        filename = arg.getFilename();
    }

    public String toJSON(){
        if (filename != null){
            try {
                return readFile();
            } catch (IOException e){
                System.out.println("Cannot read file: " + e.getMessage());
                System.exit(1);
            }
        }

        Map<String, String> reqMap = new LinkedHashMap<>();
        reqMap.put("type", type);
        reqMap.put("key", key);
        reqMap.put("value", value);
        reqMap.put("filename", filename);
        return new Gson().toJson(reqMap);
    }

    private String readFile() throws IOException{
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}
