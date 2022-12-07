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

        Map<String, String> resMap = new LinkedHashMap<>();
        resMap.put("type", type);
        resMap.put("key", key);
        resMap.put("value", value);
        return new Gson().toJson(resMap);
    }

    private String readFile() throws IOException{
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}
