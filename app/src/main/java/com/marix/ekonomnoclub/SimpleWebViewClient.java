package com.marix.ekonomnoclub;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.constraintlayout.widget.ConstraintLayout;

public class SimpleWebViewClient extends WebViewClient {

    private Activity activity = null;
    /*private WebInterface webInterface = null;*/

    public SimpleWebViewClient(Activity activity/*, WebInterface w*/) {
        this.activity = activity;
        /*webInterface = w;*/
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        // все ссылки, в которых содержится 'javadevblog.com'
        // будут открываться внутри приложения
        if (url.contains("ekonomno.club")) {
            return false;
        }
        // все остальные ссылки будут спрашивать какой браузер открывать
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        /*view.loadUrl("javascript:function myFunction() { var p = document.querySelector('#login input[name=password]').value;  var l = document.querySelector('#login input[name=username]').value; Android.getUserInfo(p, l); } document.querySelector('#login button').addEventListener('click', myFunction);");
        view.clearCache(true);*/
        view.setVisibility(View.VISIBLE);
        ConstraintLayout l = (ConstraintLayout) activity.findViewById(R.id.loader);
        l.setVisibility(View.GONE);
    }
}
