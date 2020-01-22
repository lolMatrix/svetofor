package com.marix.ekonomnoclub;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ServerQuery {

    private String url;
    private HttpsURLConnection connection;

    private InputStream inputStream = null;

    private String login;
    private String password;
    private final String TAG = "MyApp";

    ServerQuery(String url, String login, String password){

        this.url = url;
        this.login = login;
        this.password = password;
    }

    public void query(String serverKey, Map<String, String> params, CallBack c){
        try {
            URL url1 = new URL(url);
            connection =(HttpsURLConnection) url1.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            StringBuilder postData = new StringBuilder();
            for (Map.Entry < String, String > item: params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(item.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(item.getValue()), "UTF-8"));
            }
            postData.append('&');
            postData.append(URLEncoder.encode("server_key", "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(serverKey, "UTF-8"));
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            connection.getOutputStream().write(postDataBytes);

            inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }


            c.callBack(new JSONObject(stringBuilder.toString()));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void query(String serverKey, CallBack c){
        try {
            URL url1 = new URL(url);
            connection =(HttpsURLConnection) url1.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");

            StringBuilder postBuilder = new StringBuilder();
            postBuilder.append(URLEncoder.encode("username", "UTF-8"));
            postBuilder.append("=");
            postBuilder.append(URLEncoder.encode(login, "UTF-8"));
            postBuilder.append("&");
            postBuilder.append(URLEncoder.encode("password", "UTF-8"));
            postBuilder.append("=");
            postBuilder.append(URLEncoder.encode(password, "UTF-8"));
            postBuilder.append("&");
            postBuilder.append(URLEncoder.encode("server_key", "UTF-8"));
            postBuilder.append("=");
            postBuilder.append(URLEncoder.encode(serverKey, "UTF-8"));

            byte[] postData = postBuilder.toString().getBytes("UTF-8");

            connection.setRequestProperty("Content-Length", String.valueOf(postData.length));
            connection.getOutputStream().write(postData);

            inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }

            c.callBack(new JSONObject(stringBuilder.toString()));


        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "query: fail");
        }
    }

}
