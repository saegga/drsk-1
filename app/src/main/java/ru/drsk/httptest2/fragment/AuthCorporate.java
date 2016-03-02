package ru.drsk.httptest2.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.drsk.httptest2.R;

/**
 * Created by sergei on 01.03.2016.
 */
public class AuthCorporate extends Fragment {

    //// TODO: 01.03.2016 сделать нажатие на кнопку вход
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_corporate, container, false);
        return view;
    }
}
