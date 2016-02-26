package ru.drsk.httptest2.fragment;

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

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import ru.drsk.httptest2.R;
import ru.drsk.httptest2.util.ConstantRequest;
import ru.drsk.httptest2.util.Session;

/**
 * Created by sergei on 10.02.2016.
 */
public class CorporateFragment extends Fragment {

    private EditText regInn, regOgrn;
    private Button btnReg;
    private static final String USER_LOGIN_UR = "user_login_ur";
    private static final String USER_LOGIN_PLUS_UR = "user_login_plus_ur";
    private static final String STAT = "stat";
    private static final String PASSWORD_UR = "password_ur";
    private static final String PASSWORD_DUPLICATION = "password_duplication_ur";
    private static final String USER_NAME_UR = "user_name_ur";
    private static final String EMAIL_UR = "e-mail_ur";
    private static final String INDEX_UR = "index_ur";
    private static final String REGION_IR = "region_ur";
    private static final String AREA_UR = "area_ur";
    private static final String CITY_UR = "city_ur";
    private static final String STREET_UR = "street_ur";
    private static final String HOUSE_UR = "house_ur";
    private static final String PAVILION_UR = "pavilion_ur";
    private static final String APARTMENT_UR = "apartment_ur";
    private static final String TEL_UR = "tel_ur";
    private static final String FLAG = "flag";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.corporate_fragment, container, false);
        regInn = (EditText) view.findViewById(R.id.reg_inn_corp);
        regOgrn = (EditText) view.findViewById(R.id.reg_ogrn);
        btnReg = (Button) view.findViewById(R.id.btn_reg_corp);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatusEdit();
            }
        });
        regOgrn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && regOgrn.getText().length() != 0){
                    if(!checkOGRN(regOgrn.getText().toString())){
                        regOgrn.setText("");
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            regOgrn.setHintTextColor(getResources().getColor(R.color.snils_error, getActivity().getTheme()));
                        }else{
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
                if(!hasFocus && regInn.getText().length() != 0){
                    if(!checkInn(regInn.getText().toString())){
                        regInn.setText("");
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            regInn.setHintTextColor(getResources().getColor(R.color.snils_error, getActivity().getTheme()));
                        }else{
                            regInn.setHintTextColor(getResources().getColor(R.color.snils_error));
                        }
                        regInn.setHint("Неверный ИНН");

                    }
                }
            }
        });
        return view;
    }

    public void requestRegistration(){
        Connection connection = Jsoup.connect(ConstantRequest.URL_REGISTRATION)
                .cookie(ConstantRequest.PHP_SEISSION_ID, Session.getInstance().getSessionId());
                //.data()
        //// TODO: 21.02.2016 сделать добавление данных в запрос
    }

    public boolean checkStatusEdit(){
        boolean isEmpty = false;
        int[] edits = new int[]{
          R.id.reg_inn_corp, R.id.reg_ogrn, R.id.reg_pass_corp, R.id.reg_pass_repl_corp,
                R.id.reg_name_org_corp, R.id.reg_email_corp,R.id.reg_pocht_corp,
                R.id.reg_region_org_corp,R.id.reg_city_org,
                R.id.reg_street_org,R.id.reg_house_org,

        };
        for (int i = 0; i < edits.length; i++) {
            EditText e = (EditText) getActivity().findViewById(edits[i]);
            if(e.getText().toString().isEmpty()){
                e.setHint("Заполни поле!!!");
                isEmpty = true;
            }
        }
        return isEmpty;
    }
    public boolean checkInn(String inn){
        int lengthInn = inn.length();
        if(lengthInn < 10){
            return false;
        }
        int[] arrInn = new int[lengthInn];
        for (int i = 0; i < lengthInn; i++) {
            arrInn[i] = Integer.valueOf(inn.charAt(i) + "");
        }


        if(lengthInn == 10){
            return (arrInn[9] == ((2 * arrInn[0] + 4 * arrInn[1] + 10 * arrInn[2] +
                    3 * arrInn[3] + 5 * arrInn[4] + 9 * arrInn[5] + 4 * arrInn[6] +
                    6 * arrInn[7] + 8 * arrInn[8]) % 11) % 10);
        }

         if(lengthInn == 12){//для ип
//             return ((arrInn[10] == ((7 * arrInn[0] + 2 * arrInn[1] + 4 * arrInn[2] + 10 * arrInn[3] +
//                      3 * arrInn[4] + 5 * arrInn[5] + 9 * arrInn[6] + 4 * arrInn[7] +
//                      6 * arrInn[8] + 8 * arrInn[9]) % 11) % 10) &&
//                      (arrInn[11] == ((3 * arrInn[0] + 7 * arrInn[1] +
//                              2 * arrInn[2] + 4 * arrInn[3] + 10 * arrInn[4] + 3 * arrInn[5] + 5 * arrInn[6] +
//                              9 * arrInn[7] + 4 * arrInn[8] + 6 * arrInn[9] + 8 * arrInn[10]) % 11) % 10));
        }
        return false;
    }
    public boolean checkOGRN(String ogrn){
        int lengthOgrn = ogrn.length();
        long value;
        if(lengthOgrn == 13){
            value = Long.valueOf(ogrn.substring(0, 12)) % 11;
            if(ogrn.subSequence(12, 13).equals((value + "").substring(String.valueOf(value).length() - 1))){
                return true;
            }
        }else if(lengthOgrn == 15){
            value = Long.valueOf(ogrn.substring(0, 14)) % 13;
            if(ogrn.substring(14, 15).equals((value + "").substring(String.valueOf(value).length() - 1))){
                return true;
            }
        }
        return false;
    }
}

