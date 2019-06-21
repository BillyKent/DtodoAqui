package com.miedo.dtodoaqui.presentation.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ActivityFragment extends BaseFragment {

    @BindView(R.id.container)
    public TextView anotherView;

    public ActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        getStateView().showLoadingTitle("Cargando gaaaaaaaaaaa");

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getStateView().showTitleMessageIcon("Ste men", "JAsjsjs uy", R.drawable.ic_not_found);

            }
        }.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        ButterKnife.bind(this, view);
        setUpStateView(anotherView);

        return view;

    }


}