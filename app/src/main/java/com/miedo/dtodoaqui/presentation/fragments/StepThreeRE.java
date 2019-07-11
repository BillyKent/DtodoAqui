package com.miedo.dtodoaqui.presentation.fragments;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseFragment;
import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.viewmodels.RegisterEstablishmentViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepThreeRE extends BaseFragment implements View.OnClickListener {
    private static final String TAG = StepThreeRE.class.getSimpleName();
    RegisterEstablishmentViewModel viewModel;

    @BindView(R.id.et_horario)
    public TextInputEditText et_horario;

    @BindView(R.id.register_button)
    public Button registerButton;

    @BindView(R.id.spinner_location)
    public TextView spinner_location;

    @BindView(R.id.bt_cargar_imagen)
    public Button uploadButton;

    // Cosntructor necesario
    public StepThreeRE() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_step_three_re, container, false);

        ButterKnife.bind(this, view);
        viewModel = ViewModelProviders.of(requireActivity()).get(RegisterEstablishmentViewModel.class);

        spinner_location.setOnClickListener(this);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        viewModel.getRegisterState().setValue(RegisterEstablishmentViewModel.RegisterState.NORMAL);
        final NavController navController = NavHostFragment.findNavController(this);

        viewModel.getRegisterState().observe(getViewLifecycleOwner(),
                registerState -> {
                    switch (registerState) {
                        case TO_REGISTER:
                            viewModel.getEstablishment().setUserId(SessionManager.getInstance(requireContext()).getCurrentSession()
                                    .getId());
                            navController.navigate(R.id.register_action);
                            break;

                    }
                });

        registerButton.setOnClickListener(v -> {
            viewModel.getEstablishment().setOpeningHours(et_horario.getText().toString().trim());
            if (viewModel.validarTercerPaso()) {
                viewModel.getRegisterState().setValue(RegisterEstablishmentViewModel.RegisterState.TO_REGISTER);
            } else {
                showMessage("Campos inválidos.");
            }
        });




    }


    @Override
    public void onClick(View v) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(requireContext());
        builderSingle.setIcon(R.drawable.ic_location_on_black_24dp);
        builderSingle.setTitle("Seleccionar Ubicación");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(requireContext(), R.layout.checked_text_item,
                viewModel.getLocationsNombres()
        );


        builderSingle.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                viewModel.getEstablishment().setLocationId(viewModel.getIndicesLocations().get(which));

                spinner_location.setText(viewModel.getLocationsNombres().get(which));
            }
        });
        builderSingle.show();
    }
}