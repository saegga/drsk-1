package ru.drsk.httptest2.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import ru.drsk.httptest2.R;
import ru.drsk.httptest2.fragment.AuthenticatorFragment;


/**
 * Created by sergei on 28.01.2016.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.drsk));
        }
        initContainerFragment();
    }
    public void initContainerFragment(){
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.container_fragment_auth);
        if(fragment == null){
            fragment = new AuthenticatorFragment();
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
}
