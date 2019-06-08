package com.miedo.dtodoaqui.presentation.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseFragment;


public class LoggedFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_logged_profile, container, false);

        return view;
    }

}
