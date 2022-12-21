package client;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class Args {

    @Expose
    @Parameter(names = {"--type", "-t"}, description = "Type or request", order = 0)
    String type;

    @Expose
    @Parameter(names = {"--key", "-k"}, description = "Key", order = 1)
    String key;

    @Expose
    @Parameter(names = {"--value", "-v"}, description = "Value", order = 2)
    String value;

    @Parameter(names = {"--input", "-in"}, description = "Input file", order = 3)
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

    private String readFile(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }

    public String toJSON() {
        if (filename != null) {
            try {
                return readFile("C:\\Users\\geoel\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\client\\data\\"+ filename);
            } catch (IOException e) {
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
}