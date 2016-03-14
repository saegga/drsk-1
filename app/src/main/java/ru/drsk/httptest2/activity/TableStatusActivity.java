package ru.drsk.httptest2.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.drsk.httptest2.R;
import ru.drsk.httptest2.adapter.CardViewAdapter;
import ru.drsk.httptest2.pojo.TableStatusCell;
import ru.drsk.httptest2.util.ConstantRequest;
import ru.drsk.httptest2.util.Session;


/**
 * Created by 29979 on 03.01.2016.
 */
public class TableStatusActivity extends AppCompatActivity {

    private List<TableStatusCell> dataList;
    private RecyclerView recyclerView;
    private ProgressDialog dialog;
    private CardViewAdapter cardViewAdapter;
    private Session session = Session.getInstance();
    private AsyncTaskNetworkRequest asyncTaskNetworkRequest;
    private static final String TAG = "TableStatusActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_card);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Личный кабинет");
        }
        session.setSessionId(getIntent().getStringExtra(ConstantRequest.EXTRA_PHP_SESSION));
        asyncTaskNetworkRequest = new AsyncTaskNetworkRequest();
        asyncTaskNetworkRequest.execute(session.getSessionId());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Session.getInstance().deleteSession();
        asyncTaskNetworkRequest.cancel(false);
    }
//  TODO: 13.03.2016 test cancel AsyncTask


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_app:
                Intent i = new Intent(TableStatusActivity.this, ActivityAddApp.class);
                startActivity(i);
                return true;
            default: return super.onOptionsItemSelected(item);
        }

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
                if(isCancelled()){
                    return null;
                }
                String sessionId = params[0];
                doc = getHtml(sessionId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document document) {
            if (!TableStatusActivity.this.isDestroyed()) {
                dismissDialog();
            }
            if(document == null){
                return;
            }
            dataList = parseData(document);
            cardViewAdapter = new CardViewAdapter(dataList, getApplicationContext());
            recyclerView.setAdapter(cardViewAdapter);
        }
    }

    private void showDialog() {
        dialog = new ProgressDialog(TableStatusActivity.this);
        dialog.show();
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    public Document getHtml(String sessId) throws IOException {
        Document doc;
        doc = Jsoup.connect(ConstantRequest.URl_USER)
                .cookie(ConstantRequest.PHP_SEISSION_ID, sessId)
                .timeout(0)
                .get();
        return doc;
    }

    private List<TableStatusCell> parseData(Document doc) {

        Elements el = doc.select("table.agree_color_no_bottom tr");
        List<TableStatusCell> list = new ArrayList<>(el.size());
        for (int i = 1; i < el.size(); i++) {
            String data = el.get(i).child(1).getElementsByTag("td").html();
            String nameObj = el.get(i).child(2).getElementsByTag("td").html();
            String address = el.get(i).child(3).getElementsByTag("td").html();
            String type = el.get(i).child(4).getElementsByTag("td").html();
            String power = el.get(i).child(5).getElementsByTag("td").html();
            String voltage = el.get(i).child(6).getElementsByTag("td").html();
            String comment = el.get(i).child(7).getElementsByTag("td").html();
            String status = el.get(i).child(8).getElementsByTag("td").html();
            if (el.get(i).child(8).getElementsByTag("td").html().contains("start_user_file")) {
                String flagUserFile = el.get(i).child(8).getElementsByTag("td").select("form").select("input").get(1).attr("value");
                String idZayav = el.get(i).child(8).getElementsByTag("td").select("form").select("input").get(2).attr("value");
                list.add(new TableStatusCell(data, nameObj, address, type, power, voltage, comment, status,
                        true, flagUserFile, idZayav));
            } else {
                list.add(new TableStatusCell(data, nameObj, address, type, power, voltage, comment, status, false));
            }
        }
        return list;
    }

}


