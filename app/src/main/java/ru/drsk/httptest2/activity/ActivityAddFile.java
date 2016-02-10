package ru.drsk.httptest2.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import ru.drsk.httptest2.R;
import ru.drsk.httptest2.util.Session;

/**
 * Created by sergei on 04.02.2016.
 */
public class ActivityAddFile extends AppCompatActivity {

    private WebView webView;
    public static final String FLAG_ID_ZAYAV = "id_zayav";
    public static final String FLAG_USER_FILE = "flag_userfile";
    private static final String URL_FROM = "https://lk.drsk.ru/tp/user.php";
    private static final String URL_TO_FILE = "https://lk.drsk.ru/tp/user_files.php";
    private Button addFile;
    private Session session = Session.getInstance();
    private String flagUserFile, idZayav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_files);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Добавьте файлы");
        }
        addFile = (Button) findViewById(R.id.add);

        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddFile().execute();
            }
        });
//        webView = (WebView) findViewById(R.id.web);
//        webView.getSettings().setJavaScriptEnabled(true);
        flagUserFile = getIntent().getStringExtra(FLAG_USER_FILE);
        idZayav = getIntent().getStringExtra(FLAG_ID_ZAYAV);
        new LoadHtmlAddFiles().execute();
    }

    public class LoadHtmlAddFiles extends AsyncTask<Void, Void, Document>{

        @Override
        protected Document doInBackground(Void... params) {
            Document d = null;
            try {
                d = loadHtml();
                Log.d("Files", d.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return d;
        }

        @Override
        protected void onPostExecute(Document doc) {
           // webView.loadDataWithBaseURL(null,doc.toString(), "text/html", "utf-8", null);
        }
    }
    public class AddFile extends AsyncTask<Void, Void, Document>{

        @Override
        protected Document doInBackground(Void... params) {
            Document d = null;
            try {
                d = addFile();
                Log.d("Response: ", d.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return d;
        }

        @Override
        protected void onPostExecute(Document doc) {
            // webView.loadDataWithBaseURL(null,doc.toString(), "text/html", "utf-8", null);
        }
    }
    public Document loadHtml() throws IOException {
        Document document;
        Connection c = Jsoup.connect(URL_FROM);
        c.data(FLAG_USER_FILE, flagUserFile)
                .data(FLAG_ID_ZAYAV, idZayav)
                .cookie(TableStatusActivity.PHP_SEISSION_ID, session.getSessionId())
                .method(Connection.Method.POST)
                .timeout(0)
                .execute();
        document = c
                .url(URL_TO_FILE)
                .timeout(0)
                .get();

        return document;
    }
//    public File loadAddingFile(){
//        AssetManager assetManager = getAssets();
//        InputStream
//    }
    public Document addFile() throws IOException {
        Connection c = Jsoup.connect(URL_TO_FILE);
        c.cookie(TableStatusActivity.PHP_SEISSION_ID, session.getSessionId())
                .method(Connection.Method.POST)
                .header("Content-Disposition", "form-data")
                .header("PHP_SESSION_UPLOAD_PROGRESS", "test")
                .header("Content-Type","application/octet-stream")
                .header("name","f5")
                .header("filename", "")
                .timeout(0);
        Connection.Response r =  c.execute();
        Log.d("stat: ", r.statusMessage());
        Log.d("header: ", r.contentType());
        Log.d("header: ", r.header("Content-Disposition:") + " ?");
       // Log.d("header: ", r.);
        return c.get();
    }
}
