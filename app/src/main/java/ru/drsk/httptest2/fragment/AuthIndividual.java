package ru.drsk.httptest2.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

import ru.drsk.httptest2.R;
import ru.drsk.httptest2.activity.MainActivity;
import ru.drsk.httptest2.activity.RegistrationActivity;
import ru.drsk.httptest2.activity.TableStatusActivity;
import ru.drsk.httptest2.util.ConstantRequest;

/**
 * Created by sergei on 01.03.2016.
 */
public class AuthIndividual extends Fragment {

    private EditText inpSnils;
    private EditText inpPassword;
    private Button btnAuth;
    private String snils;
    private String password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_individual, container, false);

        inpSnils = (EditText) view.findViewById(R.id.inp_snils);
        inpPassword = (EditText) view.findViewById(R.id.inp_password);
        btnAuth = (Button) view.findViewById(R.id.auth_btn);
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

        return view;
    }
    private class AuthButton implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (MainActivity.checkNetwork(getActivity())) {
                snils = inpSnils.getText().toString();
                password = inpPassword.getText().toString();
                new AsyncCheckLogin().execute();
                v.setClickable(false);
            } else {
                Toast.makeText(getActivity(), "Нет соединения с интернетом", Toast.LENGTH_SHORT)
                        .show();
            }
        }
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
        return res.cookie(ConstantRequest.PHP_SEISSION_ID);
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
                Intent intent = new Intent(getActivity(), TableStatusActivity.class);
                intent.putExtra(ConstantRequest.EXTRA_PHP_SESSION, session);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "Неверный логин или пароль", Toast.LENGTH_SHORT)
                        .show();
                btnAuth.setClickable(true);
            }
        }
    }
    private class Registration implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), RegistrationActivity.class);
            startActivity(i);
        }
    }
}
