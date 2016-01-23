package ru.drsk.httptest2;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by 29979 on 03.01.2016.
 */
public class TestJsoupExample extends AppCompatActivity {

    public static final String LOGIN = "000-000-000 00";
    public static final String PASSWORD = "111111";
    public static final String URl = "https://lk.drsk.ru/tp/userlog.php";
    private List<String> tableData;
    private RecyclerView table;
    private ProgressDialog dialog;
    private TableAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_jsoup);
        tableData = new ArrayList<>();
        table = (RecyclerView) findViewById(R.id.grid);
        table.setLayoutManager(new GridLayoutManager(this, 3));
        new AsynTaskNetwokrRequest().execute();

    }

    class AsynTaskNetwokrRequest extends AsyncTask<Void, Void, Document> {

        @Override
        protected void onPreExecute() {
            showDialog();
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
            if(!TestJsoupExample.this.isDestroyed()){
               dismissDialog();
           }
                tableData = parse(document);
                adapter = new TableAdapter(tableData);
                table.setAdapter(adapter);
        }
    }
    private void showDialog(){
        dialog = new ProgressDialog(TestJsoupExample.this);
        dialog.show();
    }
    private void dismissDialog(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }


    public Document authSite() throws IOException {
        Document doc;
        Connection.Response res = Jsoup.connect(URl)
                .data("user_login", LOGIN, "user_password", PASSWORD)
                .data("stat", "1")
                .data("flag", "1")
                .timeout(0)
                .method(Connection.Method.POST)
                .execute();

        String sessionId = res.cookie("PHPSESSID");
        doc = Jsoup.connect("https://lk.drsk.ru/tp/user.php")
                .cookie("PHPSESSID", sessionId)
                .timeout(0)
                .get();
        return doc;
    }

    private List<String> parse(Document doc) {
        tableData = new ArrayList<>();
        Elements el = doc.select("table.agree_color_no_bottom tr");
        //Log.d("TEST: ", el.toString());
        for (int i = 0; i < el.size(); i++) {
            for (int j = 0; j < el.size(); j++) {
                if (j==1 ||j == 3 || j == 8) {
                    Log.d("New : ", el.get(i).child(j).text());
                    tableData.add(el.get(i).child(j).text());
                }
            }
        }
        return tableData;
    }
    public static class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder>{

        List<String> items = new ArrayList<>();

        public TableAdapter(List<String> items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.text, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cellText.setText(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView cellText;

            public ViewHolder(View itemView) {
                super(itemView);
                cellText = (TextView) itemView.findViewById(R.id.cell);
            }
        }
    }
}



