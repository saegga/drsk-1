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
 * Created by 29979 on 03.01.2016.
 */
public class TestJsoupExample extends AppCompatActivity {

    public static final String LOGIN = "000-000-000 00";
    public static final String PASSWORD = "111111";
    public static final String URl = "https://lk.drsk.ru/tp/userlog.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_jsoup);
        new AsynTaskNetwokrRequest().execute();

    }

    class AsynTaskNetwokrRequest extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                authSite();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void authSite() throws IOException {
        Document doc = null;
        Connection.Response res = Jsoup.connect(URl)
                .data("user_login", LOGIN, "user_password", PASSWORD)
                .data("stat", "1")
                .data("flag", "1")
                .method(Connection.Method.POST)
                .execute();

        doc = res.parse();
        String sessionId = res.cookie("PHPSESSID");
        Document doc2 = Jsoup.connect("https://lk.drsk.ru/tp/user.php")
                .cookie("PHPSESSID", sessionId)
                .get();
        Log.d("Test3 ", doc2.toString());

    }
}
