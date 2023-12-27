package org.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONObject;

public class JSON {
    DocumentContext context;
    public JSON(String rawJsonData) {
        context = JsonPath.parse(rawJsonData);
    }

    public JSON set(String jsonPath, Object value) {
        context.set(JsonPath.compile(jsonPath), value);
        return this;
    }

    public String read(String jsonPath) {
        String field = context.read(JsonPath.compile(jsonPath));
        return field;
    }

    public JSON addToArray(String pathToArray , Object elem) {
        context.add(JsonPath.compile(pathToArray), elem);
        return this;
    }

    public JSON delete(String jsonPath) {
        context.delete(JsonPath.compile(jsonPath) );
        return this;
    }

    public String toString() {
        return context.jsonString();
    }

    public JSONObject toJSONObject() {
        return new JSONObject(this.toString());
    }

}
