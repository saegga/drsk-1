package ru.drsk.httptest2.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import ru.drsk.httptest2.R;
import ru.drsk.httptest2.activity.MainActivity;
import ru.drsk.httptest2.util.ConstantRequest;
import ru.drsk.httptest2.util.Session;

/**
 * Created by sergei on 10.02.2016.
 */
public class RegCorporateFragment extends Fragment {

    private EditText regInn, regOgrn, regPass,
            regPassRepl, regNameOrg, regEmail, regPost,
            regionCorp, regCityOrg, regStreetOrg,
            regHouseOrg, regKorpusOrg, regAppartmentOrg,
            regPhoneOrg, regArea;

    private Button btnReg;
    private String saveInn, saveOgrn; // для передачи в окно авторизации после регистр

    private static boolean successInput = false;

    private static final String USER_LOGIN_UR = "user_login_ur";
    private static final String USER_LOGIN_PLUS_UR = "user_login_plus_ur";
    private static final String STAT = "stat";
    private static final String PASSWORD_UR = "password_ur";
    private static final String PASSWORD_DUPLICATION = "password_duplication_ur";
    private static final String USER_NAME_UR = "user_name_ur";
    private static final String EMAIL_UR = "e-mail_ur";
    private static final String INDEX_UR = "index_ur";
    private static final String REGION_UR = "region_ur";
    private static final String AREA_UR = "area_ur";
    private static final String CITY_UR = "city_ur";
    private static final String STREET_UR = "street_ur";
    private static final String HOUSE_UR = "house_ur";
    private static final String PAVILION_UR = "pavilion_ur";
    private static final String APARTMENT_UR = "apartment_ur";
    private static final String TEL_UR = "tel_ur";
    private static final String FLAG = "flag";

    private static final String STAT_DATA = "3";
    private static final String FLAG_DATA = "1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.corporate_fragment, container, false);
        regInn = (EditText) view.findViewById(R.id.reg_inn_corp);
        regOgrn = (EditText) view.findViewById(R.id.reg_ogrn);
        btnReg = (Button) view.findViewById(R.id.btn_reg_corp);

        regPass = (EditText) view.findViewById(R.id.reg_pass_corp);
        regPassRepl = (EditText) view.findViewById(R.id.reg_pass_repl_corp);

        regNameOrg = (EditText) view.findViewById(R.id.reg_name_org_corp);
        regEmail = (EditText) view.findViewById(R.id.reg_email_corp);
        regPost = (EditText) view.findViewById(R.id.reg_pocht_corp);
        regionCorp = (EditText) view.findViewById(R.id.reg_region_org_corp);
        regCityOrg = (EditText) view.findViewById(R.id.reg_city_org);
        regStreetOrg = (EditText) view.findViewById(R.id.reg_street_org);
        regHouseOrg = (EditText) view.findViewById(R.id.reg_house_org);
        regKorpusOrg = (EditText) view.findViewById(R.id.reg_korpus_org);
        regAppartmentOrg = (EditText) view.findViewById(R.id.reg_appartment_org);
        regPhoneOrg = (EditText) view.findViewById(R.id.reg_phone_org);
        regArea = (EditText) view.findViewById(R.id.area_org);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatusEdit();
                checkPassword();
                if (successInput) {
//                    saveInn = regInn.getText().toString();
//                    saveOgrn = regOgrn.getText().toString();
                    new RequestRegistration().execute();
                    //делаем запрос
                    Log.d("Success: ", "Request success");
                } else {
                    Log.d("Success: ", "Request failer");
                }

            }
        });
        regOgrn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && regOgrn.getText().length() != 0) {
                    if (!checkOGRN(regOgrn.getText().toString())) {
                        successInput = false;
                        regOgrn.setText("");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            regOgrn.setHintTextColor(getResources().getColor(R.color.snils_error, getActivity().getTheme()));
                        } else {
                            regOgrn.setHintTextColor(getResources().getColor(R.color.snils_error));
                        }
                        regOgrn.setHint("Неверный ОГРН");
                    }
                }
            }
        });
        regInn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && regInn.getText().length() != 0) {
                    if (!checkInn(regInn.getText().toString())) {
                        successInput = false;
                        regInn.setText("");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            regInn.setHintTextColor(getResources().getColor(R.color.snils_error, getActivity().getTheme()));
                        } else {
                            regInn.setHintTextColor(getResources().getColor(R.color.snils_error));
                        }
                        regInn.setHint("Неверный ИНН");

                    }
                }
            }
        });
        return view;
    }
    // todo протестить
    public boolean requestRegistration() throws IOException {
        Connection connection = Jsoup.connect(ConstantRequest.URL_REGISTRATION);
        boolean successReg;
        Connection.Response res =
                connection.cookie(ConstantRequest.PHP_SEISSION_ID, Session.getInstance().getSessionId())
                  .method(Connection.Method.POST)
                  .timeout(0)
                  .data(USER_LOGIN_UR, regInn.getText().toString())
                  .data(USER_LOGIN_PLUS_UR, regOgrn.getText().toString())
                  .data(STAT, STAT_DATA)
                  .data(PASSWORD_UR, regPass.getText().toString())
                  .data(PASSWORD_DUPLICATION, regPassRepl.getText().toString())
                  .data(USER_NAME_UR, regNameOrg.getText().toString())
                  .data(EMAIL_UR, regEmail.getText().toString())
                  .data(INDEX_UR, regPost.getText().toString())
                  .data(REGION_UR, regionCorp.getText().toString())
                  .data(AREA_UR, regArea.getText().toString())
                  .data(CITY_UR, regCityOrg.getText().toString())
                  .data(STREET_UR, regStreetOrg.getText().toString())
                  .data(HOUSE_UR, regHouseOrg.getText().toString())
                  .data(PAVILION_UR, regKorpusOrg.getText().toString())
                  .data(APARTMENT_UR, regAppartmentOrg.getText().toString())
                  .data(TEL_UR, regPhoneOrg.getText().toString())
                  .data(FLAG, FLAG_DATA).execute();
        successReg = !ConstantRequest.URL_REGISTRATION.equals(res.url().toString());
        return successReg;
    }

    public class RequestRegistration extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean result = false;
            try {
                result = requestRegistration();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(!aBoolean){
                Toast.makeText(getActivity(), "Ошибка регистрации", Toast.LENGTH_LONG).show();
            }else{
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                getActivity().finish();
            }
        }
    }

    public void checkStatusEdit() {
        successInput = true;
        int[] edits = new int[]{
                R.id.reg_inn_corp, R.id.reg_ogrn, R.id.reg_name_org_corp,
                R.id.reg_email_corp, R.id.reg_pocht_corp,
                R.id.reg_region_org_corp, R.id.reg_city_org,
                R.id.reg_street_org, R.id.reg_house_org,

        };
        for (int i = 0; i < edits.length; i++) {
            EditText e = (EditText) getActivity().findViewById(edits[i]);
            if (e.getText().toString().isEmpty()) {
                e.setHint("Заполни поле!!!");
                successInput = false;
            }
        }
    }

    public void checkPassword() {
        if (regPass.getText().length() == 0) {
            regPass.setHint("Заполни поле!");
            successInput = false;
        }
        if (regPassRepl.getText().length() == 0) {
            regPassRepl.setHint("Заполни поле!");
            successInput = false;
        }
        if (!regPass.getText().toString().equals(regPassRepl.getText().toString())) {
            successInput = false;
            regPassRepl.setHint("Пароль не совпадает!");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                regPassRepl.setHintTextColor(getResources().getColor(R.color.snils_error, getActivity().getTheme()));
            } else {
                regPassRepl.setHintTextColor(getResources().getColor(R.color.snils_error));
            }
        }
    }

    public boolean checkInn(String inn) {
        int lengthInn = inn.length();
        if (lengthInn < 10) {
            return false;
        }
        int[] arrInn = new int[lengthInn];
        for (int i = 0; i < lengthInn; i++) {
            arrInn[i] = Integer.valueOf(inn.charAt(i) + "");
        }


        if (lengthInn == 10) {
            return (arrInn[9] == ((2 * arrInn[0] + 4 * arrInn[1] + 10 * arrInn[2] +
                    3 * arrInn[3] + 5 * arrInn[4] + 9 * arrInn[5] + 4 * arrInn[6] +
                    6 * arrInn[7] + 8 * arrInn[8]) % 11) % 10);
        }

        if (lengthInn == 12) {//для ип
//             return ((arrInn[10] == ((7 * arrInn[0] + 2 * arrInn[1] + 4 * arrInn[2] + 10 * arrInn[3] +
//                      3 * arrInn[4] + 5 * arrInn[5] + 9 * arrInn[6] + 4 * arrInn[7] +
//                      6 * arrInn[8] + 8 * arrInn[9]) % 11) % 10) &&
//                      (arrInn[11] == ((3 * arrInn[0] + 7 * arrInn[1] +
//                              2 * arrInn[2] + 4 * arrInn[3] + 10 * arrInn[4] + 3 * arrInn[5] + 5 * arrInn[6] +
//                              9 * arrInn[7] + 4 * arrInn[8] + 6 * arrInn[9] + 8 * arrInn[10]) % 11) % 10));
        }
        return false;
    }

    public boolean checkOGRN(String ogrn) {
        int lengthOgrn = ogrn.length();
        long value;
        if (lengthOgrn == 13) {
            value = Long.valueOf(ogrn.substring(0, 12)) % 11;
            if (ogrn.subSequence(12, 13).equals((value + "").substring(String.valueOf(value).length() - 1))) {
                return true;
            }
        } else if (lengthOgrn == 15) {
            value = Long.valueOf(ogrn.substring(0, 14)) % 13;
            if (ogrn.substring(14, 15).equals((value + "").substring(String.valueOf(value).length() - 1))) {
                return true;
            }
        }
        return false;
    }
}
//// TODO: 02.03.2016 сделать регистрацию юриков