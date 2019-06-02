package com.miedo.dtodoaqui.presentation.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.adapters.EstablishmentSearchAdapter;
import com.miedo.dtodoaqui.data.EstablishmentSearchTO;


public class SearchFragment extends Fragment implements EstablishmentSearchAdapter.OnClickViewHolder {

    private final int searchContainerColapsedHeight = 150;
    private final int searchContainerExpandedHeight = 450;

    private LinearLayout searchLayout;
    private EditText keywordSearchParam;
    private EditText locationSearchParam;
    private Spinner categoriesSearchParam;
    private Button searchButton;
    private Button colapseButton;
    private ProgressBar progressSearchBar;

    private RecyclerView establishmentsSearchResult;

    /* private EstablishmentsSearchViewModel viewModel;
     */
    private boolean colapsed = false;


    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // viewModel = ViewModelProviders.of(this).get(EstablishmentsSearchViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        /*searchLayout = view.findViewById(R.id.searchLayout);
        keywordSearchParam = (EditText) view.findViewById(R.id.keywordSearchET);
        locationSearchParam = (EditText) view.findViewById(R.id.locationSearchET);
        categoriesSearchParam = (Spinner) view.findViewById(R.id.categoriesSearchSpinner);
        searchButton = (Button) view.findViewById(R.id.searchButton);
        colapseButton = (Button) view.findViewById(R.id.colapseButton);
        progressSearchBar = (ProgressBar) view.findViewById(R.id.progressSearchBar);


        viewModel.getSearchData().observe(this, new Observer<List<EstablishmentSearch>>() {
            @Override
            public void onChanged(List<EstablishmentSearch> establishmentSearches) {
                progressSearchBar.setVisibility(View.GONE);
                establishmentsSearchResult.setAdapter(new EstablishmentSearchAdapter(establishmentSearches, getContext()));
            }
        });

        //Listeners
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                collapseSearchLayout(150);
                establishmentsSearchResult.setAdapter(null);
                progressSearchBar.setVisibility(View.VISIBLE);
                //Búsqueda
                viewModel.SearchEstablishments(keywordSearchParam.getText().toString(),locationSearchParam.getText().toString(),"");
            }
        });
        colapseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(colapsed){
                    expandSearchLayout(150);

                }else {
                    collapseSearchLayout(150);
                }
            }
        });
        keywordSearchParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandSearchLayout(300);
            }
        });

        establishmentsSearchResult = (RecyclerView) view.findViewById(R.id.establishmentsSearchRV);

        establishmentsSearchResult.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
*/
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


    //Animations
    public void expandSearchLayout(int duration) {
        if (colapsed) {
            int prevHeight = searchLayout.getHeight();
            int targetHeight = dpToPx(searchContainerExpandedHeight);
            /*searchLayout.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int targetHeight = searchLayout.getMeasuredHeight();*/

            searchLayout.setVisibility(View.VISIBLE);
            ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    searchLayout.getLayoutParams().height = (int) animation.getAnimatedValue();
                    searchLayout.requestLayout();
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    colapseButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_arrow_up_black_24dp);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    locationSearchParam.setVisibility(View.VISIBLE);
                    categoriesSearchParam.setVisibility(View.VISIBLE);
                }
            });
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.setDuration(duration);
            valueAnimator.start();
            colapsed = false;
        }
    }

    public void collapseSearchLayout(int duration) {
        if (!colapsed) {
            int prevHeight = searchLayout.getHeight();
            int targetHeight = dpToPx(searchContainerColapsedHeight);

            ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    searchLayout.getLayoutParams().height = (int) animation.getAnimatedValue();
                    searchLayout.requestLayout();
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    locationSearchParam.setVisibility(View.INVISIBLE);
                    categoriesSearchParam.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    colapseButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_arrow_up_black_24dp);
                }
            });
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.setDuration(duration);
            valueAnimator.start();
            colapsed = true;
        }
    }

    @Override
    public void clickViewHolder(EstablishmentSearchTO est) {

        //TODO insertar logica de navegacion hacia la pantalla de EstablishmentDetail


    }
}
