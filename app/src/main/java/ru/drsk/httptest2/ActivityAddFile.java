package ru.drsk.httptest2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by sergei on 04.02.2016.
 */
public class ActivityAddFile extends AppCompatActivity {
    public static final String FLAG_ID_ZAYAV = "flag_id_zayav";
    public static final String FLAG_USER_FILE = "flag_user_file";
    private static final String URL_FROM = "https://lk.drsk.ru/tp/user.php";
    private static final String URL_TO_FILE = "https://lk.drsk.ru/tp/user_files.php";

    private Session session = Session.getInstance();
    private String flagUserFile, idZayav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_files);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Добавьте файлы");
        }
        flagUserFile = getIntent().getStringExtra(FLAG_USER_FILE);
        idZayav = getIntent().getStringExtra(FLAG_ID_ZAYAV);
        new LoadHtmlAddFiles().execute();
    }

    public class LoadHtmlAddFiles extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document d = loadHtml();
                Log.d("Files", d.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
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
}
