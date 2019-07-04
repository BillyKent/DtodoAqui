package com.miedo.dtodoaqui.presentation.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseFragment;
import com.miedo.dtodoaqui.viewmodels.RegisterEstablishmentViewModel;

public class StepLoading extends BaseFragment {

    RegisterEstablishmentViewModel viewModel;

    public StepLoading() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_loading, container, false);
        setUpStateView(view.findViewById(R.id.container));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(requireActivity()).get(RegisterEstablishmentViewModel.class);

        final NavController navController = Navigation.findNavController(view);

        viewModel.getRegisterState().observe(getViewLifecycleOwner(), registerState -> {

            switch (registerState) {
                case FETCHING_FORM_DATA:
                    getStateView().forceLoadingTitle("Preparando el registro");
                    break;
                case ERROR_FETCHING:
                    getStateView().forceTitleMessageIcon("Error", "Algo salió mal", R.drawable.perrito);
                    break;
                case READY_TO_REGISTER:
                    navController.navigate(R.id.ready_action);
                    break;

                case TO_REGISTER:
                    viewModel.registerEstablishment();
                    viewModel.getRegisterState().setValue(RegisterEstablishmentViewModel.RegisterState.REGISTERING);
                    break;
                case REGISTERING:
                    getStateView().forceLoadingTitle("Registrando establecimiento");
                    break;
                case REGISTER_SUCCESS:
                    showToastMessage("Establecimiento registrado correctamente");
                    requireActivity().finish();
                    break;
                case ERROR_REGISTERING:
                    getStateView().forceTitleMessageIcon("Error al registrar establecimiento", "Algo salió mal", R.drawable.perrito);
                    break;

            }

        });

    }
}
