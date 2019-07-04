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
import android.widget.Button;
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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseFragment;
import com.miedo.dtodoaqui.viewmodels.RegisterEstablishmentViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepOneRE extends BaseFragment implements View.OnClickListener {

    private static final String TAG = StepOneRE.class.getSimpleName();

    RegisterEstablishmentViewModel viewModel;

    ArrayAdapter<String> dataAdapter;

    @BindView(R.id.et_nombre)
    public TextInputEditText et_nombre;

    @BindView(R.id.spinner_category)
    public TextView spinner;

    @BindView(R.id.nextButton)
    public Button nextButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_step_one_re, container, false);
        ButterKnife.bind(this, view);
        viewModel = ViewModelProviders.of(this).get(RegisterEstablishmentViewModel.class);

        spinner.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        viewModel.getRegisterState().setValue(RegisterEstablishmentViewModel.RegisterState.NORMAL);
        final NavController navController = NavHostFragment.findNavController(this);

        viewModel.getRegisterState().observe(getViewLifecycleOwner(),
                registerState -> {
                    switch (registerState) {
                        case NEXT_STEP:
                            navController.navigate(R.id.next_action);
                            break;
                    }
                });
        nextButton.setOnClickListener(v -> {
            viewModel.getRegisterState().setValue(RegisterEstablishmentViewModel.RegisterState.NEXT_STEP);
        });
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(requireContext());
        builderSingle.setIcon(R.drawable.ic_store_black_24dp);
        builderSingle.setTitle("Seleccionar categoría");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(requireContext(), R.layout.checked_text_item,
                viewModel.getCategories()
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
                viewModel.setCategoryId(viewModel.getIndices().get(which));
                spinner.setText(viewModel.getCategories().get(which));
            }
        });
        builderSingle.show();
    }


}
