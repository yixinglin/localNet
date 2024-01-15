package org.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.json.JSONObject;

import java.util.List;

public class JSON {
    DocumentContext context;
    public JSON(String rawJsonData) {
        context = JsonPath.parse(rawJsonData);
    }

    public JSON(Object json) {
        context = JsonPath.parse(json);
    }

    public JSON(JSONObject json) {
        context = JsonPath.parse(json.toString());
    }

    public JSON set(String jsonPath, Object value) {
        context.set(JsonPath.compile(jsonPath), value);
        return this;
    }

    public String read(String jsonPath) {
        try {
            return context.read(JsonPath.compile(jsonPath));
        } catch (PathNotFoundException e) {
            return null;
        }
    }

    public <T> T read(String jsonPath, Class<T> type) {
        try {
            return  context.read(JsonPath.compile(jsonPath), type);
        } catch (PathNotFoundException e) {
            return null;
        }

    }

    public List readArray(String jsonPath) {
        List<Object> field = context.read(JsonPath.compile(jsonPath));
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
