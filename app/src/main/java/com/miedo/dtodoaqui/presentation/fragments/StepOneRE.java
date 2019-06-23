package com.miedo.dtodoaqui.presentation.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.MultiSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepOneRE extends Fragment {

    private static final String TAG = StepOneRE.class.getSimpleName();

    // variables para identificar el tipo de error
    private static final String NOMBRE_VACIO_ERROR = "1";
    private static final String CATEGORIAS_ERROR = "2";

    @BindView(R.id.et_nombre)
    public TextInputEditText et_nombre;

    @BindView(R.id.spinner_category)
    public AppCompatSpinner spinner;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_step_one_re, container, false);
        ButterKnife.bind(this, view);

        addItemsToSpinner();

        return view;
    }

    private void addItemsToSpinner() {
        List<String> list = new ArrayList<String>();
        list.add("list 1");
        list.add("list 2");
        list.add("list 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }


    // metodo del Step para verificar si hay error
    /*@Nullable
    @Override
    public VerificationError verifyStep() {
        et_nombre.setError(null);

        String nombre = et_nombre.getText().toString();
        VerificationError ve = null;
        if (nombre.isEmpty()) {
            ve = new VerificationError(NOMBRE_VACIO_ERROR);
        } else {
            boolean selected = false;
            for (boolean b : seleccionados) {
                if (b) selected = true;
            }
            if (!selected) {
                ve = new VerificationError(CATEGORIAS_ERROR);
            }
        }

        if (ve == null) {
            updateData();
        }

        return ve;
    }

    @Override
    public void onSelected() {
        Log.i(TAG, "onSelected");
        updateItemsSelected();
    }


    // metodo para actuar despues de hayada la interfaz
    @Override
    public void onError(@NonNull VerificationError error) {
        if (error.getErrorMessage().equals(NOMBRE_VACIO_ERROR)) {
            et_nombre.setError("El nombre no puede estar vacío");
            et_nombre.requestFocus();
        } else if (error.getErrorMessage().equals(CATEGORIAS_ERROR)) {
            Toast.makeText(getContext(), "Selecciona al menos una categoría", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateData() {

        Bundle bundle = new Bundle();

        bundle.putString("nombre", et_nombre.getText().toString().trim());
        //bundle.putBooleanArray("categorias", seleccionados);

        // FAKE
        ((RegisterEstablishmentActivity) getActivity()).loadData(bundle, RegisterEstablishmentActivity.STEP_ONE);


    }*/
}
