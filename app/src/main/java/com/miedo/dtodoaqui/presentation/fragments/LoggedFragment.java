package com.miedo.dtodoaqui.presentation.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.adapters.ProfileInfoAdapter;
import com.miedo.dtodoaqui.core.BaseFragment;
import com.miedo.dtodoaqui.data.ProfileTO;
import com.miedo.dtodoaqui.viewmodels.ProfileViewModel;

import java.util.ArrayList;


public class LoggedFragment extends BaseFragment {

    ProfileViewModel viewModel;
    ArrayList<ProfileInfoAdapter.ProfileItem> items;
    ListView listView;
    private ProfileInfoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_logged_profile, container, false);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        listView = (ListView) view.findViewById(R.id.list_details);
        adapter = new ProfileInfoAdapter(getContext(), new ArrayList<>());


        //listView.setAdapter(adapter);
    }

    void loadItems(ProfileTO profile) {
        items.clear();
        // nombre
        items.add(new ProfileInfoAdapter.ProfileItem(R.drawable.ic_account_box_black_24dp, "Nombre",
                profile.getFirstName() + " " + profile.getLastName()
        ));
        // telefono
        items.add(new ProfileInfoAdapter.ProfileItem(R.drawable.ic_phone_black_24dp, "Telefono",
                profile.getPhone()
        ));
        // direccion
        items.add(new ProfileInfoAdapter.ProfileItem(R.drawable.ic_location_on_black_24dp, "Dirección",
                profile.getAddress()
        ));
        // facebook
        items.add(new ProfileInfoAdapter.ProfileItem(R.drawable.ic_facebook, "Facebook",
                profile.getFacebookUrl()
        ));

    }

}
