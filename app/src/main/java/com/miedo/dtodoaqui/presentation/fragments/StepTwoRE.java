package com.miedo.dtodoaqui.presentation.fragments;


import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseFragment;
import com.miedo.dtodoaqui.viewmodels.RegisterEstablishmentViewModel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepTwoRE extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener, PlaceSelectionListener, GoogleMap.OnCameraMoveStartedListener {

    private static final String TAG = StepTwoRE.class.getSimpleName();

    // Campos del registro
    private RegisterEstablishmentViewModel viewModel;

    // Campos del paso
    private GoogleMap mMap;
    private AutocompleteSupportFragment autocompleteFragment;
    private SupportMapFragment mapFragment;

    BuscarPosicion buscarTask = new BuscarPosicion();

    // Auxiliares
    private double currentZoom;

    // Binds
    @BindView(R.id.bt_continuar)
    public Button botonContinuar;

    public StepTwoRE() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_two_re, container, false);
        ButterKnife.bind(this, view);
        viewModel = ViewModelProviders.of(requireActivity()).get(RegisterEstablishmentViewModel.class);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        configureAutocomplete();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ((AppCompatActivity) requireActivity()).getSupportActionBar().setSubtitle("Paso 2 de 3");
        viewModel.getRegisterState().setValue(RegisterEstablishmentViewModel.RegisterState.NORMAL);
        final NavController navController = NavHostFragment.findNavController(this);

        botonContinuar.setOnClickListener(v -> {
            if (viewModel.validarSegundoPaso()) {
                viewModel.getRegisterState().setValue(RegisterEstablishmentViewModel.RegisterState.NEXT_STEP);
            } else {
                showMessage("Ocurrio un error");
            }

        });

        viewModel.getRegisterState().observe(getViewLifecycleOwner(),
                registerState -> {
                    switch (registerState) {
                        case NEXT_STEP:
                            navController.navigate(R.id.next_action);
                            break;
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng posInicial = new LatLng(-12.053456, -77.085624);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posInicial, 15));
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveStartedListener(this);
    }

    private void configureAutocomplete() {

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG));

        autocompleteFragment.setCountry("pe");
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment.setOnPlaceSelectedListener(this);

        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button).setOnClickListener(
                v -> {
                    autocompleteFragment.setText("");
                    viewModel.getEstablishment().setLatitude(null);
                    viewModel.getEstablishment().setLongitude(null);
                    viewModel.getEstablishment().setAddress(null);
                    botonContinuar.setEnabled(false);
                }
        );

    }

    // Autocomplete
    @Override
    public void onPlaceSelected(@NonNull Place place) {
        LatLng ubicacion = place.getLatLng();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, (float) 15));
        botonContinuar.setEnabled(true);
    }

    @Override
    public void onError(@NonNull Status status) {
        invalidar();
    }

    // Google Maps
    @Override
    public void onCameraIdle() {
        buscarTask.cancel(true);
        buscarTask = new BuscarPosicion();
        buscarTask.execute(mMap.getProjection().getVisibleRegion().latLngBounds.getCenter());

        /*Log.i(TAG, "determinando ubicacion");
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        List<Address> addresses = null;
        LatLng point = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
        try {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
            Log.i(TAG, addresses.toString());
            String cityName = addresses.get(0).getAddressLine(0);
            autocompleteFragment.setText(cityName);

            viewModel.getEstablishment().setAddress(cityName);
            viewModel.getEstablishment().setLatitude(point.latitude + "");
            viewModel.getEstablishment().setLongitude(point.longitude + "");
            botonContinuar.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onCameraMove() {
        if (mMap.getCameraPosition().zoom != currentZoom) {
            currentZoom = mMap.getCameraPosition().zoom;
        }
    }

    private void invalidar() {
        autocompleteFragment.setText("");
        mMap.clear();
        botonContinuar.setEnabled(false);
        viewModel.getEstablishment().setAddress(null);
        viewModel.getEstablishment().setLatitude(null);
        viewModel.getEstablishment().setLongitude(null);
    }


    @Override
    public void onCameraMoveStarted(int i) {
        if (i == REASON_GESTURE) {
            invalidar();
        }

    }

    class BuscarPosicion extends AsyncTask<LatLng, Void, String> {

        @Override
        protected String doInBackground(LatLng... points) {
            Log.i(TAG, "Buscando");

            try {
                Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(points[0].latitude, points[0].longitude, 1);
                Log.i(TAG, addresses.toString());
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);

                Log.i(TAG, "ciudad:" + cityName);
                Log.i(TAG, "estado:" + cityName);

                viewModel.getEstablishment().setAddress(cityName);
                viewModel.getEstablishment().setLatitude(points[0].latitude + "");
                viewModel.getEstablishment().setLongitude(points[0].longitude + "");
                return cityName;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                showToastMessage("Error de geolocalizacion");
                return;
            }
            autocompleteFragment.setText(s);
            botonContinuar.setEnabled(true);
        }
    }


}
