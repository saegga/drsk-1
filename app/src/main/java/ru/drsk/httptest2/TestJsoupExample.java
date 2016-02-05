package ru.drsk.httptest2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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


    public static final String URl = "https://lk.drsk.ru/tp/userlog.php";
    public static final String URl_USER = "https://lk.drsk.ru/tp/user.php";
    public static final String PHP_SEISSION_ID = "PHPSESSID";
    private List<TextElement> tableData;
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
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Личный кабинет");
        }
        String sessionId = getIntent().getStringExtra(MainActivity.EXTRA_PHP_SESSION);
        new AsyncTaskNetworkRequest().execute(sessionId);

    }

    class AsyncTaskNetworkRequest extends AsyncTask<String, Void, Document> {

        @Override
        protected void onPreExecute() {
            showDialog();
        }

        @Override
        protected Document doInBackground(String... params) {
            Document doc = null;
            try {
                String sessionId = params[0];
                doc = getHtml(sessionId);
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


    public Document getHtml(String sessId) throws IOException {
        Document doc;
        doc = Jsoup.connect("https://lk.drsk.ru/tp/user.php")
                .cookie(PHP_SEISSION_ID, sessId)
                .timeout(0)
                .get();
        return doc;
    }

    private List<TextElement> parse(Document doc) {
        //tableData = new ArrayList<>();
        Elements el = doc.select("table.agree_color_no_bottom tr");
        tableData = new ArrayList<>();
        for (int i = 0; i < el.size(); i++) {
            for (int j = 0; j < el.size(); j++) {
                if (j==1 || j == 3 || j == 8) {
                    Log.d("New : ", el.get(i).child(j).text());
                    String text = el.get(i).child(j).getElementsByTag("td").html();
                    if(j == 8 && el.get(i).child(j).getElementsByTag("td").html().contains("start_user_file")){
                        String flagUserFile = el.get(i).child(j).getElementsByTag("td").select("form").select("input").get(1).attr("value");
                        String idZayav = el.get(i).child(j).getElementsByTag("td").select("form").select("input").get(2).attr("value");
                        tableData.add(new TextElement(text, true, flagUserFile, idZayav));
                    }else{
                        tableData.add(new TextElement(text, false));
                    }
                }
            }
        }
        return tableData;
    }
    public static class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder>{

        List<TextElement> items = new ArrayList<>();

        public TableAdapter(List<TextElement> items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.table_cell, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.cellText.setText(Html.fromHtml(items.get(position).getText()));
            if(items.get(position).isHasButton()){
                holder.addFile.setVisibility(View.VISIBLE);
                holder.addFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }else{
                holder.addFile.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView cellText;
            Button addFile;
            public ViewHolder(View itemView) {
                super(itemView);
                cellText = (TextView) itemView.findViewById(R.id.cell);
                cellText.setMovementMethod(LinkMovementMethod.getInstance());
                addFile = (Button) itemView.findViewById(R.id.btn_add_files);
            }
        }
    }
}

