package ru.drsk.httptest2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sergei on 04.02.2016.
 */
public class ActivityAddFile extends AppCompatActivity {
    public static final String FLAG_ID_ZAYAV = "flag_id_zayav";
    public static final String FLAG_USER_FILE = "flag_user_file";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_files);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Добавьте файлы");
        }
    }
}
