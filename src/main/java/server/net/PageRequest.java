package server.net;

import javax.json.JsonObject;

public class PageRequest {
    
    private JsonObject json;

    public PageRequest(JsonObject json) {
        this.json = json;
    }

    public JsonObject getJson() {
        return json;
    }

    public void setJson(JsonObject json) {
        this.json = json;
    }
    
    
    
}
