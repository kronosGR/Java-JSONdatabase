package client;

import com.beust.jcommander.Parameter;

public class Args {

    @Parameter(names = {"--type", "-t"}, description = "Type or request")
    String type;

    @Parameter(names = {"--key", "-k"}, description = "Key")
    String key;

    @Parameter(names = {"--value", "-v"}, description = "Value")
    String value;

    @Parameter(names = {"--input", "-in"}, description = "Input file")
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
}
