package server;

import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

public class Response {
    String res;
    String reas;
    String value;

    public final static String RES_OK = "OK";
    public final static String RES_ERROR = "ERROR";

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getReas() {
        return reas;
    }

    public void setReas(String reas) {
        this.reas = reas;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toJSON(){
        Map<String, String> resMap = new LinkedHashMap<>();
        resMap.put("response", res);
        if (value != null){
            resMap.put("value", value);
        }
        if (reas != null){
            resMap.put("reason", reas);
        }
        return new Gson().toJson(resMap);
    }
}
