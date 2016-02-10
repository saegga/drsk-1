package ru.drsk.httptest2.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.drsk.httptest2.R;

/**
 * Created by sergei on 09.02.2016.
 */
public class AgreementRegistrationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.agreement_fragment, container, false);
        return view;
    }
}
