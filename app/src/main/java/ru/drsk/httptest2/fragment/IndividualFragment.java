package ru.drsk.httptest2.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.drsk.httptest2.R;

/**
 * Created by sergei on 09.02.2016.
 */
public class IndividualFragment extends Fragment {

    private EditText inputSnils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.individual_fragment, container, false);
        inputSnils = (EditText) view.findViewById(R.id.reg_snils);
        inputSnils.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
            }
//// TODO: 15.02.2016 сделать форматный ввод изменить остальные поля числа, дата
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before == 0) {
                    if (start == 3 || start == 7) {
                        char c = s.charAt(start);
                        s = s.subSequence(0, start);
                        s = s + "-" + c;
                        inputSnils.setText(s);
                        inputSnils.setSelection(start + 2);

                    }
                    else if (start == 11) {
                        char c = s.charAt(start);
                        s = s.subSequence(0, start);
                        s = s + " " + c;
                        inputSnils.setText(s);
                        inputSnils.setSelection(start + 2);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inputSnils.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && inputSnils.getText().length() != 0){
                    if(!checkSnils(inputSnils.getText().toString())){
                        inputSnils.setText("");
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            inputSnils.setHintTextColor(getResources().getColor(R.color.snils_error, getActivity().getTheme()));
                        }else{
                            inputSnils.setHintTextColor(getResources().getColor(R.color.snils_error));
                        }
                        inputSnils.setHint("Неверный снилс");
                    }
                }
            }
        });

        return view;
    }

    public boolean checkSnils(String snils) {
        snils = snils.replaceAll("\\D+", "");
        if(snils.length() < 11 ){
            return false;
        }
        int checkSum = Integer.parseInt(snils.substring(9));
        int[] a = new int[snils.length()];
        int sum = 0;
        for (int i = 0; i < snils.length() - 2; i++) {
            a[i] = Integer.parseInt(snils.charAt(i) + "");
            sum += a[i] * (9 - i);
        }
        if (sum < 100 && sum == checkSum) {
            return true;
        } else if ((sum == 100 || sum == 101) && checkSum == 0) {
            return true;
        } else if (sum > 101 && (sum % 101 == checkSum || (sum % 101 == 100 && checkSum == 0))) {
            return true;
        } else {
            return false;
        }
    }

}
