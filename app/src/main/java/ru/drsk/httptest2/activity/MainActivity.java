package ru.drsk.httptest2.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

import ru.drsk.httptest2.R;
import ru.drsk.httptest2.fragment.AuthContainerFragment;
import ru.drsk.httptest2.util.ConstantRequest;
import ru.drsk.httptest2.util.Session;


/**
 * Created by sergei on 28.01.2016.
 */
public class MainActivity extends AppCompatActivity {

//    private static final String BUNDLE_INN_CORP = "bundle_inn_corp";
//    private static final String BUNDLE_OGRN_CORP = "bundle_ogrn_corp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.drsk));
        }
        if (checkNetwork(this)){
            new AsyncRequestMain().execute();
        }else{
            Toast.makeText(this, "Нет соединения с интрнетом", Toast.LENGTH_LONG).show();
        }
        initContainerFragment();
    }
    public void initContainerFragment(){
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.container_fragment_auth);
        if(fragment == null){
            fragment = new AuthContainerFragment();
            manager.beginTransaction()
                    .add(R.id.container_fragment_auth, fragment)
                    .commit();
        }
    }

    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo active = connectivityManager.getActiveNetworkInfo();
        if (active != null) {
            if (active.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (active.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }
    private class AsyncRequestMain extends AsyncTask<Void, Void, String>{
        private String cookie;
        @Override
        protected String doInBackground(Void... params) {
            try {
                cookie = request();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return cookie;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("Cookie: ", s);
            Session.getInstance().setSessionId(cookie);
        }
    }
    private String request() throws IOException {
        Connection.Response res = Jsoup.connect(ConstantRequest.URl_USER_LOG)
                .timeout(0)
                .execute();
        return res.cookie(ConstantRequest.PHP_SEISSION_ID);
    }
}
