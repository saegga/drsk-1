package ru.drsk.httptest2.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import ru.drsk.httptest2.R;

/**
 * Created by sergei on 09.02.2016.
 */
public class AgreementRegistrationFragment extends Fragment {

    private CheckBox checkAgree;
    private Button btnResume;
    private Button btnBack;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.agreement_fragment, container, false);
        checkAgree = (CheckBox) view.findViewById(R.id.check_agree);
        btnResume = (Button) view.findViewById(R.id.btn_resume_reg);
        btnBack = (Button) view.findViewById(R.id.btn_back);
        checkAgree.setChecked(false);
        btnResume.setEnabled(false);
        checkAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnResume.setEnabled(true);
                    btnResume.setActivated(true);
                } else {
                    btnResume.setEnabled(false);
                    btnResume.setActivated(false);
                }
            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNextFragment();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }
    public void initNextFragment(){
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.registr_container_fragment, new RegistrationFragment())
                .commit();
    }
}
