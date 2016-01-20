package ru.drsk.httptest2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 29979 on 03.01.2016.
 */
public class TestJsoupExample extends AppCompatActivity {

    public static final String LOGIN = "000-000-000 00";
    public static final String PASSWORD = "111111";
    public static final String URl = "https://lk.drsk.ru/tp/userlog.php";
    private List<String> tableData;
    private GridView table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_jsoup);
        table = (GridView) findViewById(R.id.gridview);
        new AsynTaskNetwokrRequest().execute();

    }

    class AsynTaskNetwokrRequest extends AsyncTask<Void, Void, Document> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(TestJsoupExample.this,  "В процессе", null);
        }

        @Override
        protected Document doInBackground(Void... params) {
            Document doc = null;
            try {
                doc = authSite();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document document) {
            if(dialog != null){
                dialog.dismiss();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, parse(document));
            table.setAdapter(adapter);

        }
    }

    public Document authSite() throws IOException {
        Document doc;
        Connection.Response res = Jsoup.connect(URl)
                .data("user_login", LOGIN, "user_password", PASSWORD)
                .data("stat", "1")
                .data("flag", "1")
                .timeout(10 * 1000)
                .method(Connection.Method.POST)
                .execute();

        String sessionId = res.cookie("PHPSESSID");
        doc = Jsoup.connect("https://lk.drsk.ru/tp/user.php")
                .cookie("PHPSESSID", sessionId)
                .get();
        return doc;
    }

    private List<String> parse(Document doc) {
        tableData = new ArrayList<>();
        Elements el = doc.select("table.agree_color_no_bottom tr");
        //Log.d("TEST: ", el.toString());
        for (int i = 0; i < el.size(); i++) {
            for (int j = 0; j < el.size(); j++) {
                if (j == 8) {
                    Log.d("New : ", el.get(i).child(j).text());
                    tableData.add(el.get(i).child(j).text());
                }
            }
        }
        return tableData;
    }

}

