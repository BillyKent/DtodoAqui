package com.miedo.dtodoaqui.presentation.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseFragment;

public class StepLoading extends BaseFragment {

    public StepLoading() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_loading, container, false);
        setUpStateView(view.findViewById(R.id.container));
        getStateView().forceLoadingTitle("Preparando el registro");
        return view;
    }

}
