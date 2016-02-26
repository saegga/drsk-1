package ru.drsk.httptest2.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

import ru.drsk.httptest2.R;
import ru.drsk.httptest2.util.ConstantRequest;


/**
 * Created by sergei on 28.01.2016.
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_PHP_SESSION = "extra_php_session";
    private EditText inpSnils;
    private EditText inpPassword;
    private Button btnAuth;
    private Button btnRegistration;
    private String snils;
    private String password;

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
        btnRegistration = (Button) findViewById(R.id.registration_btn);
        btnRegistration.setOnClickListener(new Registration());
        btnAuth.setOnClickListener(new AuthButton());
        inpSnils.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before == 0) {
                    if (start == 3 || start == 7) {
                        char c = s.charAt(start);
                        s = s.subSequence(0, start);
                        s = s + "-" + c;
                        inpSnils.setText(s);
                        inpSnils.setSelection(start + 2);

                    } else if (start == 11) {
                        char c = s.charAt(start);
                        s = s.subSequence(0, start);
                        s = s + " " + c;
                        inpSnils.setText(s);
                        inpSnils.setSelection(start + 2);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private class AuthButton implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (checkNetwork(getApplicationContext())) {
                snils = inpSnils.getText().toString();
                password = inpPassword.getText().toString();
                new AsyncCheckLogin().execute();
                v.setClickable(false);
            } else {
                Toast.makeText(getApplicationContext(), "Нет соединения с интернетом", Toast.LENGTH_SHORT)
                        .show();
            }
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

    private String checkLogin() throws IOException {
        Connection.Response res = Jsoup.connect(ConstantRequest.URl)
                .data("user_login", snils, "user_password", password)
                .data("stat", "1")
                .data("flag", "1")
                .timeout(0)
                .method(Connection.Method.POST)
                .execute();
        if (!ConstantRequest.URl_USER.equals(res.url().toString())) {
            return null;
        }
        return res.cookie(TableStatusActivity.PHP_SEISSION_ID);
    }

    public class AsyncCheckLogin extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String sessionId = null;
            try {
                sessionId = checkLogin();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sessionId;
        }

        @Override
        protected void onPostExecute(String session) {
            if (session != null) {
                //btnAuth.setEnabled(false);
                Intent intent = new Intent(MainActivity.this, TableStatusActivity.class);
                intent.putExtra(EXTRA_PHP_SESSION, session);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Неверный логин или пароль", Toast.LENGTH_SHORT)
                        .show();
                btnAuth.setClickable(true);
            }
        }
    }

    private class Registration implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(i);
        }
    }
}
