package ru.drsk.httptest2.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import ru.drsk.httptest2.activity.TableStatusActivity;
import ru.drsk.httptest2.util.ConstantRequest;

/**
 * Created by sergei on 01.03.2016.
 */
public class AuthCorporate extends Fragment {
    /*
    * инн 7714698320
    * огрн 5077746887312
    * пароль 123456
    * */
    private Button authCorp;
    private EditText innCorp;
    private EditText ogrnCorp;
    private EditText password;

    private String inn, ogrn, pass;

    private static final String USER_LOGIN_UR = "user_login_ur";
    private static final String USER_LOGIN_UR_PLUS = "user_login_ur_plus";
    private static final String STAT = "stat";
    private static final String USER_UR_PASSWORD = "user_ur_password";
    private static final String FLAG = "flag";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_corporate, container, false);
        authCorp = (Button) view.findViewById(R.id.auth_corp_btn);
        innCorp = (EditText) view.findViewById(R.id.inn_corp);
        ogrnCorp = (EditText) view.findViewById(R.id.ogrn_corp);
        password = (EditText) view.findViewById(R.id.password_corp);
        authCorp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.checkNetwork(getActivity())){
                    inn = innCorp.getText().toString();
                    ogrn = ogrnCorp.getText().toString();
                    pass = password.getText().toString();
                    new AsyncCheckCorpLogin().execute();
                    v.setClickable(false);
                }else {
                    Toast.makeText(getActivity(), "Нет соединения с интернетом", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        return view;
    }
    private String checkLoginIn() throws IOException {
        Connection.Response res = Jsoup.connect(ConstantRequest.URl_USER_LOG)
                .data(USER_LOGIN_UR, inn, USER_LOGIN_UR_PLUS, ogrn)
                .data(STAT, "3")
                .data(USER_UR_PASSWORD, pass)
                .data(FLAG, "1")
                .timeout(0)
                .method(Connection.Method.POST)
                .execute();
        if (!ConstantRequest.URl_USER.equals(res.url().toString())) {
            return null;
        }
        return res.cookie(ConstantRequest.PHP_SEISSION_ID);
    }

    private class AsyncCheckCorpLogin extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String sessionId = null;
            try {
                sessionId = checkLoginIn();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sessionId;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                Intent intent = new Intent(getActivity(), TableStatusActivity.class);
                intent.putExtra(ConstantRequest.EXTRA_PHP_SESSION, s);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "Неверные данные", Toast.LENGTH_SHORT)
                        .show();
                authCorp.setClickable(true);
            }

        }
    }
}
//// TODO: 02.03.2016 сделать доработки полей пароль и спинер