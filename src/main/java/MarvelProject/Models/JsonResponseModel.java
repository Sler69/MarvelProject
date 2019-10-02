package MarvelProject.Models;

import com.google.gson.JsonObject;

public class JsonResponseModel {
    private String status;
    private int code;
    private JsonObject data;

    public JsonResponseModel(String status, int code) {
        this.status = status;
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString(){
        return "Code response: " + this.code + " . Status; " + this.status + ". Content: " + this.data.toString();
    }

    public void setCode(int code) {
        this.code = code;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }
}
