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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputEditText;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseFragment;
import com.miedo.dtodoaqui.viewmodels.RegisterEstablishmentViewModel;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepTwoRE extends BaseFragment implements View.OnClickListener {
    private static final String TAG = StepTwoRE.class.getSimpleName();
    RegisterEstablishmentViewModel viewModel;

    @BindView(R.id.et_horario)
    public TextInputEditText et_horario;

    @BindView(R.id.register_button)
    public Button registerButton;

    @BindView(R.id.spinner_location)
    public TextView spinner_location;

    // Cosntructor necesario
    public StepTwoRE() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_step_two_re, container, false);

        ButterKnife.bind(this, view);
        viewModel = ViewModelProviders.of(requireActivity()).get(RegisterEstablishmentViewModel.class);

        spinner_location.setOnClickListener(this);

        PlacesClient client = Places.createClient(getContext());

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS_COMPONENTS, Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));

        autocompleteFragment.setCountry("pe");
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                viewModel.getEstablishment().setAddress(place.getAddress());
                viewModel.getEstablishment().setLatitude("" + place.getLatLng().latitude);
                viewModel.getEstablishment().setLongitude("" + place.getLatLng().longitude);
                viewModel.getEstablishment().setUserId("1");
                //Log.i(TAG, "es nulo la lon lat ? : " + (place.getLatLng() == null));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        autocompleteFragment.setText("");
                        viewModel.getEstablishment().setAddress(null);
                        viewModel.getEstablishment().setLatitude(null);
                        viewModel.getEstablishment().setLongitude(null);
                        viewModel.getEstablishment().setUserId(null);
                        view.setVisibility(View.GONE);

                    }
                });


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
                            navController.navigate(R.id.register_action);
                            break;

                    }
                });

        registerButton.setOnClickListener(v -> {
            viewModel.getEstablishment().setOpeningHours(et_horario.getText().toString().trim());
            if (validateFields()) {
                viewModel.getRegisterState().setValue(RegisterEstablishmentViewModel.RegisterState.TO_REGISTER);
            }else{
                showMessage("Campos inválidos.");
            }
        });


    }

    private boolean validateFields() {
        return viewModel.validarSegundoPaso();
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