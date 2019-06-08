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

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.adapters.ProfileInfoAdapter;
import com.miedo.dtodoaqui.core.BaseFragment;

import java.util.ArrayList;


public class LoggedFragment extends BaseFragment {

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
        items = new ArrayList<>();
        loadItems();
        adapter = new ProfileInfoAdapter(getContext(),items);
        listView.setAdapter(adapter);
    }

    void loadItems() {

        items.add(new ProfileInfoAdapter.ProfileItem(R.drawable.ic_account_box_black_24dp, "Nombre", "Sopa do macaco"));
        items.add(new ProfileInfoAdapter.ProfileItem(R.drawable.ic_phone_black_24dp, "Telefono", "+51994224498"));
        items.add(new ProfileInfoAdapter.ProfileItem(R.drawable.ic_location_on_black_24dp, "Dirección", "Av.Brasil #322"));
        items.add(new ProfileInfoAdapter.ProfileItem(R.drawable.ic_facebook, "Nombre", "https://www.facebook.com/sopademacacodelicia/"));

    }

}
