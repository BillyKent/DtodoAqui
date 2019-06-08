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
import com.miedo.dtodoaqui.presentation.activities.LoginActivity;
import com.miedo.dtodoaqui.presentation.activities.MainActivity;
import com.miedo.dtodoaqui.presentation.activities.RegisterUserActivity;


public class UnloggedProfileFragment extends BaseFragment {

    public static final int REGISTER_REQUEST = 1;
    public static final int REGISTER_SUCCESSFUL = 2;
    public static final int REGISTER_CANCELLED = 3;

    public static final int LOGIN_REQUEST = 4;
    public static final int LOGIN_SUCCESSFUL = 5;
    public static final int LOGIN_REJECTED = 5;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REGISTER_REQUEST) {
            if (resultCode == REGISTER_SUCCESSFUL) {
                // se navega al LoggedProfile
                ((MainActivity) requireActivity()).navigateTo(R.id.profile_tab);
            } else if (resultCode == REGISTER_CANCELLED) {

            }
        } else if (requestCode == LOGIN_REQUEST) {
            if (resultCode == LOGIN_SUCCESSFUL) {
                // se navega al LoggedProfile
                ((MainActivity) requireActivity()).navigateTo(R.id.profile_tab);
            } else if (resultCode == LOGIN_REJECTED) {

            }

        }
    }

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
            startActivityForResult(intent, REGISTER_REQUEST);
        });

        view.findViewById(R.id.unloggedLoginButton).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST);

        });

    }
}