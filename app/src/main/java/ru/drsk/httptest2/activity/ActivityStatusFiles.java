package ru.drsk.httptest2.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.drsk.httptest2.R;
import ru.drsk.httptest2.adapter.ListFileAdapter;
import ru.drsk.httptest2.pojo.TextAddFile;
import ru.drsk.httptest2.util.ConstantRequest;
import ru.drsk.httptest2.util.Session;

/**
 * Created by sergei on 04.02.2016.
 */
public class ActivityStatusFiles extends AppCompatActivity {

    public static final String FILE_ABS_PATH = "file_abs_path";
    public static final String FILE_CHOOSE_NAME = "file_choose_name";
    public static final String FLAG_ID_ZAYAV = "id_zayav";
    public static final String FLAG_USER_FILE = "flag_userfile";

    public static final int REQUEST_FILE_CHOOSE = 0;
    public static final String BUTTON_ADD_ID = "button_add_id";
    private Button addFile;
    private RecyclerView listAddFile;
    private Session session = Session.getInstance();
    private String flagUserFile, idZayav;
    private List<TextAddFile> listData;
    private ListFileAdapter adapter;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_files);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Добавьте файлы");
        }
        listData = new ArrayList<>();
        addFile = (Button) findViewById(R.id.add);
        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddFile().execute();
            }
        });
        listAddFile = (RecyclerView) findViewById(R.id.list_add_files);
        listAddFile.setLayoutManager(new GridLayoutManager(this, 1));
        flagUserFile = getIntent().getStringExtra(FLAG_USER_FILE);
        idZayav = getIntent().getStringExtra(FLAG_ID_ZAYAV);

        if(MainActivity.checkNetwork(this)){
            new LoadHtmlAddFiles().execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_FILE_CHOOSE && data != null){
            Log.d("Выбранный файл: ",//// TODO: 16.02.2016 сделать стурктуру где хранить пути к файлам
                    "путь: " + data.getStringExtra(FILE_ABS_PATH) +
                            " имя " + data.getStringExtra(FILE_CHOOSE_NAME) +
                                "кнопка " + data.getIntExtra(BUTTON_ADD_ID, -1));
            listData.get(data.getIntExtra(BUTTON_ADD_ID, -1)).setFileName(data.getStringExtra(FILE_CHOOSE_NAME));
            adapter.notifyDataSetChanged();
        }

    }

    public List<TextAddFile> parse(Document document){

        Elements el = document.select("table.agree_color_reg tr");
        for (int i = 2; i < el.size(); i++) {
            Elements e = el.get(i).getElementsByTag("td");
            if(el.get(i).text().length() > 1 && (el.get(i).select("div#progress").size() == 0)){
                if(e.size() == 2){
                    String textLoad =  e.get(0).text();
                    String statusLoad =  e.get(1).text();
                    listData.add(new TextAddFile(textLoad, statusLoad));
                }
            }
        }
        return listData;
    }
    public class LoadHtmlAddFiles extends AsyncTask<Void, Void, Document>{

        @Override
        protected void onPreExecute() {
            showDialog();
        }

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
            if(!ActivityStatusFiles.this.isDestroyed()){
                dismissDialog();
            }
            adapter = new ListFileAdapter(parse(doc), ActivityStatusFiles.this);
            listAddFile.setAdapter(adapter);
        }
    }
    private void showDialog(){
        dialog = new ProgressDialog(ActivityStatusFiles.this);
        dialog.show();
    }
    private void dismissDialog(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
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
        }
    }
    public Document loadHtml() throws IOException {
        Document document;
        Connection c = Jsoup.connect(ConstantRequest.URL_FROM);
        c.data(FLAG_USER_FILE, flagUserFile)
                .data(FLAG_ID_ZAYAV, idZayav)
                .cookie(ConstantRequest.PHP_SEISSION_ID, session.getSessionId())
                .method(Connection.Method.POST)
                .timeout(0)
                .execute();
        document = c
                .url(ConstantRequest.URL_TO_FILE)
                .timeout(0)
                .get();

        return document;
    }


    /* запррос
    header:
    ooockie: phpsessid=5487
    Content-Type: multipart/form-data; boundary=---WebKit1BEF0A57BE11
    Content-Length: 209
    body:
    */

    public Document addFile() throws IOException {
        Connection c = Jsoup.connect(ConstantRequest.URL_TO_FILE);
        c.cookie(ConstantRequest.PHP_SEISSION_ID, session.getSessionId())
                .method(Connection.Method.POST)
                .header("Content-Disposition", "form-data")
                .header("PHP_SESSION_UPLOAD_PROGRESS", "test")
                .header("Content-Type","application/octet-stream;"
                        + "boundary=----WebKitFormBoundaryTLw5SZnLKDsbbgUO")
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
