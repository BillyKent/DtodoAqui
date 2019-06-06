package com.miedo.dtodoaqui.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseFragment;
import com.miedo.dtodoaqui.presentation.activities.RegisterUserActivity;


public class UnloggedProfileFragment extends BaseFragment {

    public static final int REGISTER_SUCCESSFUL = 1;
    public static final int REGISTER_CANCELLED = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unlogged_profile, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        view.findViewById(R.id.registerButton).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), RegisterUserActivity.class);
            startActivityForResult(intent, REGISTER_SUCCESSFUL);
        });

    }
}