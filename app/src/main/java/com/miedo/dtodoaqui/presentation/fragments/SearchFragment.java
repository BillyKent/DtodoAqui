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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.adapters.EstablishmentSearchAdapter;
import com.miedo.dtodoaqui.data.EstablishmentTO;
import com.miedo.dtodoaqui.model.CategoriesModel;
import com.miedo.dtodoaqui.model.LocationsModel;
import com.miedo.dtodoaqui.presentation.activities.EstablishmentActivity;
import com.miedo.dtodoaqui.viewmodels.EstablishmentsSearchViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchFragment extends Fragment{

    AppBarLayout appBarLayout;
    EditText keywordSearchParam;
    Spinner locationSearchParam;
    Spinner categoriesSearchParam;
    Button searchButton;
    //ProgressBar progressSearchBar;

    RecyclerView establishmentsSearchResult;

    private EstablishmentsSearchViewModel viewModel;

    private Map<Integer,String> categoriesMap = null;
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

        viewModel.getSearchData().observe(this, new Observer<List<EstablishmentTO>>() {
            @Override
            public void onChanged(List<EstablishmentTO> establishmentSearches) {
                //progressSearchBar.setVisibility(View.GONE);
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
                //progressSearchBar.setVisibility(View.VISIBLE);
                //Búsqueda
                viewModel.SearchEstablishments(keywordSearchParam.getText().toString(),String.valueOf(locationMap.get((String)locationSearchParam.getSelectedItem())),String.valueOf(getCategoryId(categoriesSearchParam.getSelectedItem().toString())));
            }
        });

        establishmentsSearchResult = (RecyclerView) view.findViewById(R.id.establishmentsSearchRV);

        establishmentsSearchResult.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        getCategories();
        getLocatios();

        return view;
    }

    private void getCategories(){
        CategoriesModel categoriesModel = new CategoriesModel();
        categoriesModel.GetCategories(new CategoriesModel.Callback<Map<Integer, String>>() {
            @Override
            public void onResult(Map<Integer, String> arg) {
                categoriesMap = arg;
                List<String> categories = new ArrayList();
                for(Map.Entry<Integer,String> entry : arg.entrySet()){
                    categories.add(entry.getValue());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,categories);

                categoriesSearchParam.setAdapter(adapter);
            }

            @Override
            public void onFailure() {
                //Error
            }
        });
    }

    private void getLocatios(){
        LocationsModel locationsModel= new LocationsModel();
        locationsModel.getLocations(new LocationsModel.Callback<HashMap<String, Integer>>() {
            @Override
            public void onResult(HashMap<String, Integer> arg) {
                locationMap = arg;
                List<String> locations = new ArrayList();
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
    private int getCategoryId(String name){
        int id = -1;
        if(categoriesMap.containsValue(name)){
            for(Map.Entry<Integer,String> entry : categoriesMap.entrySet()){
                if(name.equals(entry.getValue())){
                    id = entry.getKey();
                    break;
                }
            }
        }
        return id;
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
