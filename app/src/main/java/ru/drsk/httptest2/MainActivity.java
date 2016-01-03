package ru.drsk.httptest2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private WebView view;
    private WebSettings webSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (WebView) findViewById(R.id.webView);
        view.setWebViewClient(new MyWebView());
        view.loadUrl("https://lk.drsk.ru/tp/userlog.php");
        webSettings = view.getSettings();
        webSettings.setJavaScriptEnabled(true);


       // new AsynTaskNetwokrRequest().execute();
    }

    @Override
    public void onBackPressed() {
        if(view.canGoBack()) {
            view.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.setInitialScale(100);
    }

    class AsynTaskNetwokrRequest extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://lk.drsk.ru/tp/userlog.php");
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("snils", "000-000-000 00"));
            nameValuePairs.add(new BasicNameValuePair("user_password", "111111"));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpResponse response = null;
            try {
                response = httpclient.execute(httppost);

            } catch (IOException e) {
                e.printStackTrace();
            }

            String responseString = "";
            HttpEntity responseEntity = response.getEntity();
            if(responseEntity!=null) {
                try {
                    responseString = EntityUtils.toString(responseEntity);
                    Log.d("Main Activity ", responseString.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return null;
        }

    }

    private class MyWebView extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return false;

            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(intent);
//            return true;

        }
    }
}