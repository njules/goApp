package edu.kit.pse.gruppe1.goApp.client.controler.serverConnection;


import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * If a service wants to communicate with the server, the service has to create a HTTPConnection object and must call the sendGet/PostRequest method.
 * For later requests the service can use the same HTTPConnection object.
 */
public class HTTPConnection {
    private static final String SERVER_URL = "https://i43pc164.ipd.kit.edu/PSEWS1617GoGruppe1/goApp/";
    private URL url;

    /**
     * Constructor which expects the name of the servlet.
     *
     * @param nameOfServlet The name of the servlet, the service wants to communicate with.
     */
    public HTTPConnection(String nameOfServlet) {
        try {
            url = new URL(SERVER_URL + nameOfServlet);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method that handles a get request.
     *
     * @param json The String which should be send to the server.
     * @return The JSONObject the server send back
     */
    public JSONObject sendGetRequest(String json) {
        return send("GET", json);
    }

    /**
     * Method that handles a get request.
     *
     * @param json The String which should be send to the server.
     * @return The JSONObject the server send back
     */
    public JSONObject sendPostRequest(String json) {
        return send("POST", json);
    }

    private JSONObject toJSONObject(String jsonString) {
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return json;
    }


    private JSONObject send(String requestMethod, String request) {
        BufferedReader reader = null;
        OutputStreamWriter out = null;
        String response = null;
        HttpsURLConnection conn = null;
        try {
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);

            conn.setDoOutput(true);
            out = new OutputStreamWriter(
                    conn.getOutputStream());
            out.write(request);
            out.close();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(out);
            close(reader);
        }
        if (response == null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(JSONParameter.ERROR_CODE.toString(), JSONParameter.ErrorCodes.CONNECTION_FAILED.getErrorCode());
            } catch (JSONException e) {
                return new JSONObject();
            }
            return jsonObject;
        }
        return toJSONObject(response);
    }

    private void close(Closeable inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}