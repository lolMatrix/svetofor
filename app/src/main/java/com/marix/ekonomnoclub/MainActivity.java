package com.marix.ekonomnoclub;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.onesignal.OneSignal;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private final String TAG = "MyApp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        createNotificationChannel();

        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        /*webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(false);*/
        /*WebInterface w = new WebInterface(this);
        webView.addJavascriptInterface(w, "Android");*/
        SimpleWebViewClient webViewClient = new SimpleWebViewClient(this);
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(new WebChromeClient() {
            // Grant permissions for cam
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d(TAG, "onPermissionRequest");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void run() {


                        if (Build.VERSION.SDK_INT >= 23) {
                            //динамическое получение прав на INTERNET
                            if (checkSelfPermission(Manifest.permission.CAMERA)
                                    == PackageManager.PERMISSION_GRANTED) {
                                Log.d(TAG, "Permission is granted");
                                Log.d(TAG, request.getOrigin().toString());
                                Log.d(TAG, "GRANTED");
                                request.grant(request.getResources());

                                //делаете что-то с интернетом

                            } else {
                                Log.d(TAG, "Permission is revoked");
                                //запрашиваем разрешение
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                                if (checkSelfPermission(Manifest.permission.CAMERA)
                                        == PackageManager.PERMISSION_GRANTED) {
                                    Log.d(TAG, "Permission is granted");
                                    Log.d(TAG, request.getOrigin().toString());
                                    Log.d(TAG, "GRANTED");
                                    request.grant(request.getResources());

                                    //делаете что-то с интернетом

                                }
                            }
                        }else {
                            Log.d(TAG, request.getOrigin().toString());
                            Log.d(TAG, "GRANTED");
                            request.grant(request.getResources());
                        }
                    }
                });
            }


        });
        /*if(Build.VERSION.SDK_INT >= 21){
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, false);
        }else {
            CookieManager.getInstance().setAcceptCookie(false);
        }*/
        Map<String, String> noCacheHeaders = new HashMap<String, String>(2);
        noCacheHeaders.put("Pragma", "no-cache");
        noCacheHeaders.put("Cache-Control", "no-cache");
        webView.loadUrl("https://ekonomno.club/", noCacheHeaders);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /*private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MyAppChennel";
            String description = "Lol";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("MyApp", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }*/

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        webView.loadUrl("javascript: document.location.href = 'https://ekonomno.club/logout';");
//        finish();
//    }
}
