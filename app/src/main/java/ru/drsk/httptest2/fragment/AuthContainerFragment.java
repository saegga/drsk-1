package ru.drsk.httptest2.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import ru.drsk.httptest2.R;
import ru.drsk.httptest2.activity.RegistrationActivity;

/**
 * Created by sergei on 01.03.2016.
 */
public class AuthContainerFragment extends Fragment {

    private Spinner spinner;
    private Button btnRegistration;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_fragment_container, container, false);
        btnRegistration = (Button) view.findViewById(R.id.registration_btn);
        btnRegistration.setOnClickListener(new Registration());
        spinner = (Spinner) view.findViewById(R.id.choose_stat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (getActivity(), R.array.type_subject, R.layout.spinner_subject_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadFragment(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void loadFragment(int position) {
        FragmentManager manager = getFragmentManager();
        Fragment fragment = null;
        switch (position){
            case 0 : fragment = new AuthIndividual(); break;
//            case 1 : fragment = new BusinessFragment(); break;
            case 2 : fragment = new AuthCorporate(); break;
        }
        manager
                .beginTransaction()
                .replace(R.id.container_form_auth, fragment)
                .commit();
    }
    private class Registration implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), RegistrationActivity.class);
            startActivity(i);
        }
    }
}
