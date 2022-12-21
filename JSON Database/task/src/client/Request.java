package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;


public class Request {

    @Parameter(names = {"-t", "--type"}, description = "The type of the request")
    private String type;

    @Parameter(names = {"-k", "--key"}, description = "The key")
    JsonElement key;

    @Parameter(names = {"-v", "--value"}, description = "The value to add")
    JsonElement value;

    @Parameter(names = {"-in", "--input"}, description = "The file")
    String filename;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonElement getKey() {
        return key;
    }

    public void setKey(JsonElement key) {
        this.key = key;
    }

    public JsonElement getValue() {
        return value;
    }

    public void setValue(JsonElement value) {
        this.value = value;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Request() {

    }


}
