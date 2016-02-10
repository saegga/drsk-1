package ru.drsk.httptest2.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import ru.drsk.httptest2.R;
import ru.drsk.httptest2.fragment.AgreementRegistrationFragment;

/**
 * Created by sergei on 09.02.2016.
 */
public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.btn_regist));
        }
        initFragment();
    }

    public void initFragment(){
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.registr_container_fragment);
        if(fragment == null){
            fragment = new AgreementRegistrationFragment();
            manager.beginTransaction()
                    .add(R.id.registr_container_fragment, fragment)
                    .commit();
        }


    }
}
