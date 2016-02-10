package ru.drsk.httptest2.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ru.drsk.httptest2.R;

/**
 * Created by sergei on 09.02.2016.
 */
public class RegistrationFragment extends Fragment {

    private Spinner spinner;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        spinner = (Spinner) view.findViewById(R.id.choice_subject);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getActivity(), R.array.type_subject, R.layout.spinner_subject_item);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addFragment(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
    public void addFragment(int position){
        FragmentManager manager = getFragmentManager();
        Fragment fragment = null;
        switch (position){
            case 0 : fragment = new IndividualFragment(); break;

        }
                manager
                .beginTransaction()
                .add(R.id.container_form_registration, fragment)
                .commit();

    }
}
