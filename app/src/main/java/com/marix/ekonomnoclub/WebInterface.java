package com.marix.ekonomnoclub;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.Context.NOTIFICATION_SERVICE;


public class WebInterface {

    private Thread thread;
    public String password;
    public String login;
    private Context context;
    private final String TAG = "MyApp";

    public WebInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void getUserInfo(String p, String l){
        login = l;
        password = p;
        AsyncQuery asyncQuery = new AsyncQuery(login, password, "https://ekonomno.club/api/auth");
        asyncQuery.execute();
    }

    private class AsyncQuery extends AsyncTask<Void, Void, Void>{

        private String username;
        private String password;
        private String url;
        private int count = 0;

        AsyncQuery(@NonNull String username, @NonNull String password, @NonNull String url){

            this.username = username;
            this.password = password;
            this.url = url;
        }
        @Override
        protected Void doInBackground(Void... voids) {

            //TODO: сделать класс запросов на сервер

            Log.d(TAG, "doInBackground: lol");
            ServerQuery serverQuery = new ServerQuery(url, username, password);
            serverQuery.query(context.getResources().getString(R.string.serverKey), new CallBack() {
                @Override
                public void callBack(JSONObject jsonObject) {
                    if(!jsonObject.has("errors")){
                        try {
                            Log.d(TAG, "doInBackground:" + jsonObject.toString());
                            final String accessToken = "https://ekonomno.club/api/get-general-data?access_token=" + jsonObject.getString("access_token");
                            final Map<String, String> request = new HashMap<>();
                            request.put("fetch", "notifications");
                            thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (;;) {

                                        ServerQuery q = new ServerQuery(accessToken, login, password);
                                        q.query(context.getResources().getString(R.string.serverKey), request, new CallBack() {
                                            @Override
                                            public void callBack(JSONObject jsonObject) {

                                                try {
                                                    JSONArray a = jsonObject.getJSONArray("notifications");
                                                    int c;
                                                    if (a.length() > count && (c = jsonObject.getInt("new_notifications_count")) > 0) {
                                                        c = a.length() - c;
                                                        for (int i = 0; i < c; i++) {
                                                            NotificationCompat.Builder builder =
                                                                    new NotificationCompat.Builder(context, "MyApp")
                                                                            .setSmallIcon(R.drawable.ic_launcher_background)
                                                                            .setContentTitle("Уведомление")
                                                                            .setContentText(a.getJSONObject(a.length() - 1).getJSONObject("notifier").getString("first_name") + " " + a.getJSONObject(a.length() - 1).getString("type_text"))
                                                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                                            NotificationManagerCompat notificationManager =
                                                                    NotificationManagerCompat.from(context);
                                                            notificationManager.notify("lol", i, builder.build());
                                                        }
                                                        Log.d(TAG, "callBack: count = " + count + " lenght = " + a.length());
                                                        count = a.length();
                                                    }
                                                    TimeUnit.SECONDS.sleep(10);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                    }
                                }
                            });
                            thread.start();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            return null;
        }
    }

}
