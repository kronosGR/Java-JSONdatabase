package client;

import com.beust.jcommander.Parameter;

public class Args {

    @Parameter(names = {"--type", "-t"}, description = "Type or request")
    String type;
    @Parameter(names = {"--index", "-i"}, description = "Index of item in db")
    int index;

    @Parameter(names = {"--message", "-m"}, description = "Value to be saved")
    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
