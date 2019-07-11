package com.miedo.dtodoaqui.presentation.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.adapters.EstablishmentSearchAdapter;
import com.miedo.dtodoaqui.core.BaseFragment;
import com.miedo.dtodoaqui.data.EstablishmentTO;
import com.miedo.dtodoaqui.model.CategoriesModel;
import com.miedo.dtodoaqui.model.LocationsModel;
import com.miedo.dtodoaqui.presentation.activities.EstablishmentActivity;
import com.miedo.dtodoaqui.utils.CallbackUtils;
import com.miedo.dtodoaqui.viewmodels.EstablishmentsSearchViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchFragment extends BaseFragment {

    AppBarLayout appBarLayout;
    EditText keywordSearchParam;
    Spinner locationSearchParam;
    Spinner categoriesSearchParam;
    Button searchButton;
    //ProgressBar progressSearchBar;

    RecyclerView establishmentsSearchResult;

    private EstablishmentsSearchViewModel viewModel;

    private Map<String,Integer> categoriesMap = null;
    private Map<String,Integer> locationMap = null;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         viewModel = ViewModelProviders.of(this).get(EstablishmentsSearchViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        appBarLayout = view.findViewById(R.id.searchAppBarLayout);
        ConstraintLayout searchLayout = appBarLayout.findViewById(R.id.searchLayout);
        keywordSearchParam = searchLayout.findViewById(R.id.keywordSearchET);
        locationSearchParam = searchLayout.findViewById(R.id.locationSearchSP);
        categoriesSearchParam = searchLayout.findViewById(R.id.categoriesSearchSP);
        searchButton = searchLayout.findViewById(R.id.searchSearchBT);

        //progressSearchBar = view.findViewById(R.id.progressSearchPB);

        establishmentsSearchResult = view.findViewById(R.id.establishmentsSearchRV);

        //Test
        appBarLayout.setExpanded(false);

        setUpStateView(view.findViewById(R.id.search_establishment_container));
        getStateView().forceTitleMessageIcon("¡Empieza a buscar aqui!", "Bienvenido a DTODOAQUI, desliza hacia abajo para empezar a buscar.",R.drawable.ic_search_black_24dp);
        //---

        viewModel.getSearchData().observe(this, new Observer<List<EstablishmentTO>>() {
            @Override
            public void onChanged(List<EstablishmentTO> establishmentSearches) {

                if(establishmentSearches == null || establishmentSearches.size() == 0){
                    getStateView().forceTitleMessageIcon("No hay establecimientos.","No se encontraron establecimientos en tu búsqueda, por favor trata nuevamente.",R.drawable.ic_dead);
                }else{
                    getStateView().forceHideStateView();
                }



                establishmentsSearchResult.setAdapter(new EstablishmentSearchAdapter(new EstablishmentSearchAdapter.OnClickViewHolder() {
                    @Override
                    public void clickViewHolder(EstablishmentTO est) {
                        Intent intent = new Intent(requireContext(), EstablishmentActivity.class);
                        intent.putExtra("establishment_id",est.getId());
                        startActivity(intent);

                    }
                }, establishmentSearches, getContext()));
            }
        });

        //Listeners
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                establishmentsSearchResult.setAdapter(null);
                String location = String.valueOf(locationMap.get((String)locationSearchParam.getSelectedItem()));
                String category = String.valueOf(categoriesMap.get((String)categoriesSearchParam.getSelectedItem()));
                String keyword = keywordSearchParam.getText().toString();
                location = location.equals("null")?"":location;
                category = category.equals("null")?"":category;
                if(location.isEmpty() && category.isEmpty() && keyword.isEmpty()){
                    Toast.makeText(getContext(), "¡Debe seleccionar al menos un criterio de búsqueda!", Toast.LENGTH_SHORT).show();
                }else{
                    viewModel.SearchEstablishments(keywordSearchParam.getText().toString(),location,category);
                    appBarLayout.setExpanded(false);
                    getStateView().forceLoadingTitle("Buscando establecimientos");
                }
            }
        });

        establishmentsSearchResult = (RecyclerView) view.findViewById(R.id.establishmentsSearchRV);

        establishmentsSearchResult.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        getCategories();
        getLocations();

        return view;
    }

    private void getCategories(){
        CategoriesModel categoriesModel = new CategoriesModel();
        categoriesModel.GetCategories(new CallbackUtils.SimpleCallback<Map<String, Integer>>() {
            @Override
            public void OnResult(Map<String, Integer> arg) {
                categoriesMap = arg;
                List<String> categories = new ArrayList();
                categories.add("Seleccione una categoría");
                for(Map.Entry<String,Integer> entry : arg.entrySet()){
                    categories.add(entry.getKey());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,categories);

                categoriesSearchParam.setAdapter(adapter);
            }

            @Override
            public void OnFailure(String response) {
                //Error
            }

        });
    }

    private void getLocations(){
        LocationsModel locationsModel= new LocationsModel();
        locationsModel.getLocations(new LocationsModel.Callback<HashMap<String, Integer>>() {
            @Override
            public void onResult(HashMap<String, Integer> arg) {
                locationMap = arg;
                List<String> locations = new ArrayList();
                locations.add("Seleccione un distrito");
                for(Map.Entry<String, Integer> entry : arg.entrySet()){
                    locations.add(entry.getKey());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,locations);

                locationSearchParam.setAdapter(adapter);
            }

            @Override
            public void onFailure() {
                //Error
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //keywordSearchParam.requestFocus();
    }
    //Utilities

    public int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
