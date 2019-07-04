package com.miedo.dtodoaqui.presentation.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.viewmodels.RegisterEstablishmentViewModel;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepTwoRE extends Fragment {


    private static final String TAG = StepTwoRE.class.getSimpleName();
    RegisterEstablishmentViewModel viewModel;


    // Cosntructor necesario
    public StepTwoRE() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_step_two_re, container, false);
        viewModel = ViewModelProviders.of(requireActivity()).get(RegisterEstablishmentViewModel.class);

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
                viewModel.setSelectedPlace(place);

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
                        viewModel.setSelectedPlace(null);
                        view.setVisibility(View.GONE);

                    }
                });


        return view;

    }

/*
    @Nullable
    @Override
    public VerificationError verifyStep() {
        VerificationError ve = null;

        if (selectedPlace == null) {
            ve = new VerificationError("Debes elegir una dirección válida.");
        }

        if (ve == null) {
            updateData();
        }

        return ve;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

        Toast.makeText(getContext(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();

    }

    public void updateData() {

        Bundle bundle = new Bundle();

        for (AddressComponent a : selectedPlace.getAddressComponents().asList()) {
            if (a.getTypes().equals(Arrays.asList("locality", "political"))) {
                bundle.putString("district", a.getShortName());
                Log.i(TAG, "distrito " + a.getName());
            }
            //Log.i(TAG, a.getName() + " : " + a.getTypes());
        }

        Log.i(TAG, "direccion" + selectedPlace.getAddress());
        Log.i(TAG, "latitud : " + selectedPlace.getLatLng().latitude);
        Log.i(TAG, "longitud : " + selectedPlace.getLatLng().longitude);

        bundle.putString("address", selectedPlace.getAddress());
        bundle.putDouble("latitude", selectedPlace.getLatLng().latitude);
        bundle.putDouble("longitude", selectedPlace.getLatLng().longitude);

        ((RegisterEstablishmentActivity) getActivity()).loadData(bundle, RegisterEstablishmentActivity.STEP_TWO);

    }*/
}