package hcmus.cnpm.team10.utils.api;


import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class HttpHandler {

    private static final String ROOT_URL     = "https://cnpm-t10-testserver-1.herokuapp.com";
//    private static final String ROOT_URL = "http://192.168.9.54:3000";
    private static final int    DEFAULT_PORT = 3000;

    private class HandleConnection {
        private final HttpURLConnection mHttpConnection;
        private final HttpsURLConnection mHttpsConnection;
        private final boolean isHttps;

        HandleConnection(URL url) throws IOException {
            if(url.getProtocol().equals("https")){
                isHttps = true;
                mHttpsConnection = (HttpsURLConnection) url.openConnection();
                mHttpConnection = null;

            }

            else{
                isHttps = false;
                mHttpsConnection = null;
                mHttpConnection = (HttpURLConnection) url.openConnection();
            }
        }

        InputStream getInputStream() throws IOException {
            if(isHttps)
                return mHttpsConnection.getInputStream();
            else
                return mHttpConnection.getInputStream();
        }
        void disconnect(){
            if(isHttps)
                mHttpsConnection.disconnect();
            else
                mHttpConnection.disconnect();
        }
    }

    public String getStringFromQuery(String query, HashMap<String, String> parameters) throws IOException {

        query = createQuery(query, parameters);

        URL url = new URL(query);
        // HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        // HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        HandleConnection urlConnection = new HandleConnection(url);

        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String output = readStream(in);

            return output;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }


        return null;
    }



    public String readStream(InputStream in) throws IOException {
        /*
            @brief Read input from InputStream and convert to String
         */

        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;

        try {
            while ((line = reader.readLine()) != null)
                builder.append(line);
            return builder.toString();
        }
        catch (IOException e){
            e.printStackTrace();
            throw e;
        }
        finally {
            try{
                reader.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String createQuery(String query, HashMap<String, String> parameters) {
        /*
            @brief Auto generate query with parameter

            Output:
                {ROOT_URL}{query}?{parameters...}


         */
        StringBuilder builder = new StringBuilder();

        //Not using port
        builder.append(ROOT_URL)//.append(":").append(DEFAULT_PORT)
                .append(query);

        //If no parameters, return result
        if(parameters == null || parameters.isEmpty())
            return builder.toString();


        builder.append("?");
        boolean isFirst = true;
        for (String key : parameters.keySet()) {
            String value = parameters.get(key);
            if (isFirst)
                isFirst = false;
            else
                builder.append("&");

            builder.append(key).append("=").append(value);

        }

        return builder.toString();
    }



}