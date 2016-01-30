package ru.drsk.httptest2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;

import android.text.TextWatcher;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by sergei on 28.01.2016.
 */
public class MainActivity extends AppCompatActivity {

    private EditText inpSnils;
    private EditText inpPassword;
    private Button btnAuth;
    public static final String EXTRA_SNILS = "extra_snils";
    public static final String EXTRA_PASSWORD = "extra_password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.drsk));
        }
        inpSnils = (EditText) findViewById(R.id.inp_snils);
        inpPassword = (EditText) findViewById(R.id.inp_password);
        btnAuth = (Button) findViewById(R.id.auth_btn);
        btnAuth.setOnClickListener(new AuthButton());
        inpSnils.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (after == 1) {
                    if (start == 3 || start == 7) {
                        s = s + "-" + s.charAt(start - 1);
                        inpSnils.setText(s);
                        inpSnils.setSelection(start + 2);
                    }
                    if (start == 11) {
                        s = s + " " + s.charAt(start - 1);
                        inpSnils.setText(s);
                        inpSnils.setSelection(start + 2);
                    }
                }

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private class AuthButton implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(checkNetwork(getApplicationContext())){
                String snils = inpSnils.getText().toString();
                String password = inpPassword.getText().toString();
                Intent intent = new Intent(MainActivity.this, TestJsoupExample.class);
                intent.putExtra(EXTRA_SNILS, snils);
                intent.putExtra(EXTRA_PASSWORD, password);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "Нет соединения с интернетом", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    private boolean checkNetwork(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo active = connectivityManager.getActiveNetworkInfo();
        if(active != null){
            if(active.getType() == ConnectivityManager.TYPE_WIFI){
                return true;
            }
            else if(active.getType() == ConnectivityManager.TYPE_MOBILE){
                return true;
            }
        }
        return false;
    }
}
