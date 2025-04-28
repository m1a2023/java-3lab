package org.example.models;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

/** * Custom JSON-reader class */
public class JsonReader {
    /**
     * Read JSON from request and write to StringBuffer
     * @param req StringBuffer
     * @return JSON
     * @throws IOException
     */
    public static JSONObject readFromRequest(HttpServletRequest req)
            throws IOException {
        /* Read JSON */
        StringBuffer sb = new StringBuffer();
        String line = null;
        try (BufferedReader r = req.getReader()) {
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
        }
        return new JSONObject(sb.toString());
    }
}
