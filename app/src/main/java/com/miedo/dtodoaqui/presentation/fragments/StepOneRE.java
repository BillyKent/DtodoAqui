package com.miedo.dtodoaqui.presentation.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseFragment;
import com.miedo.dtodoaqui.viewmodels.RegisterEstablishmentViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepOneRE extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = StepOneRE.class.getSimpleName();

    RegisterEstablishmentViewModel viewModel;

    ArrayAdapter<String> dataAdapter;

    @BindView(R.id.et_nombre)
    public TextInputEditText et_nombre;

    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_step_one_re, container, false);
        ButterKnife.bind(this, view);
        spinner = (Spinner) view.findViewById(R.id.spinner_category);
        viewModel = ViewModelProviders.of(this).get(RegisterEstablishmentViewModel.class);


        /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToastMessage(((TextView) view).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addItemsToSpinner();
    }

    private void addItemsToSpinner() {
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, viewModel.getCategories());
        // spinner.setAdapter(adapter);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, viewModel.getCategories());

        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.planets_array, android.R.layout.simple_spinner_item);*/
        // Specify the layout to use when the list of choices appears
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
