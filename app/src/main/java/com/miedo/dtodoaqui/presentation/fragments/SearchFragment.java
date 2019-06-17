package com.miedo.dtodoaqui.presentation.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.adapters.EstablishmentSearchAdapter;
import com.miedo.dtodoaqui.data.EstablishmentSearchTO;
import com.miedo.dtodoaqui.data.EstablishmentTO;
import com.miedo.dtodoaqui.presentation.activities.EstablishmentActivity;
import com.miedo.dtodoaqui.viewmodels.EstablishmentsSearchViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchFragment extends Fragment{

    AppBarLayout appBarLayout;
    EditText keywordSearchParam;
    EditText locationSearchParam;
    Spinner categoriesSearchParam;
    Button searchButton;
    //ProgressBar progressSearchBar;

    RecyclerView establishmentsSearchResult;

    private EstablishmentsSearchViewModel viewModel;

    private boolean colapsed = false;


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
        locationSearchParam = searchLayout.findViewById(R.id.locationSearchET);
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
                viewModel.SearchEstablishments(keywordSearchParam.getText().toString(),locationSearchParam.getText().toString(),categoriesSearchParam.getSelectedItem().toString());
            }
        });

        establishmentsSearchResult = (RecyclerView) view.findViewById(R.id.establishmentsSearchRV);

        establishmentsSearchResult.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        return view;
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
