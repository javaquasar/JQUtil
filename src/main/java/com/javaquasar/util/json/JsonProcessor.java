package com.javaquasar.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author Java Quasar
 */
public class JsonProcessor {

    /**
     * Convert a JSON string to pretty print version
     *
     * @param jsonString
     * @return
     */
    public static String toPrettyFormat(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }

    public static void writeJsonToFile(String path, String json) {
        try (FileOutputStream fos = new FileOutputStream(path);
                Writer writer = new OutputStreamWriter(fos, "UTF-8")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(json, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writePrettyJsonToFile(String path, String json) {
        writeJsonToFile(path, toPrettyFormat(json));
    }

}
