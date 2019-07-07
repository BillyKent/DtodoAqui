package com.miedo.dtodoaqui.presentation.activities;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ZoomControls;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.miedo.dtodoaqui.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener,GoogleMap.OnCameraMoveListener {

    private GoogleMap mMap;
    private AutocompleteSupportFragment autocompleteFragment;
    private static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

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

                LatLng ubicacion = place.getLatLng();
                mMap.clear();
                //mMap.addMarker(new MarkerOptions().position(ubicacion).title("Place").draggable(true));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button)
                .setOnClickListener(v -> {
                    autocompleteFragment.setText("");
                    v.setVisibility(View.GONE);
                    mMap.clear();

                });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng posInicial = new LatLng(-12, -77);
        /*mMap.addMarker(new MarkerOptions()
                .position(posInicial)
                .draggable(true)
        );*/

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posInicial, 16));
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveListener(this);

        currentZoom = mMap.getCameraPosition().zoom;




    }


    @Override
    public void onCameraIdle() {
        Log.i(TAG, "determinando ubicacion");
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        LatLng point = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();

        /*mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(point)
                .draggable(true)
        );*/
        try {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
            Log.i(TAG, addresses.toString());
            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            autocompleteFragment.setText(cityName);

            Log.i(TAG, "ciudad:" + cityName);
            Log.i(TAG, "estado:" + cityName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private double currentZoom;

    @Override
    public void onCameraMove() {
        if (mMap.getCameraPosition().zoom != currentZoom) {
            Log.i(TAG, "moviendo zoom:");
            currentZoom = mMap.getCameraPosition().zoom;

        }
        Log.i(TAG, "zoom actual :" + mMap.getCameraPosition().zoom);
    }
}