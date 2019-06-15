package com.miedo.dtodoaqui.presentation.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.se.omapi.Session;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.adapters.ProfileInfoAdapter;
import com.miedo.dtodoaqui.core.BaseFragment;
import com.miedo.dtodoaqui.data.ProfileTO;
import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.viewmodels.ProfileViewModel;

import java.util.ArrayList;


public class LoggedFragment extends BaseFragment {

    public static final String TAG = LoggedFragment.class.getSimpleName();

    ProfileViewModel viewModel;
    ArrayList<ProfileInfoAdapter.ProfileItem> items = new ArrayList<>();
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
        setUpStateView(view, view.findViewById(R.id.container));
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        listView = (ListView) view.findViewById(R.id.list_details);




        viewModel.getProfileState().observe(getViewLifecycleOwner(),
                new Observer<ProfileViewModel.ProfileState>() {
                    @Override
                    public void onChanged(ProfileViewModel.ProfileState profileState) {
                        switch (profileState) {
                            case VERIFICANDO_PERFIL:
                                getStateView().showLoadingWithTitle("Verificando perfil");
                                viewModel.verificarPerfil();
                                break;
                            case CON_PERFIL:
                                loadItems(viewModel.getCurrentProfile());
                                adapter = new ProfileInfoAdapter(getContext(), items);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                getStateView().hideStateView();
                                Log.i(TAG, "Estoy aca csmre");
                                break;
                            case OBTENIENDO_PERFIL:
                                getStateView().showLoadingWithTitle("Obteniendo perfil");
                                viewModel.obtenerPerfil(SessionManager.getInstance(getContext()).getCurrentSession().getJwt());
                                break;

                            case SIN_PERFIL:
                                getStateView().showActionState(
                                        "No tienes registrado un perfil de usuario.",
                                        "Configura tu perfil, así podrás publicar reseñas y decirle al mundo quien eres.",
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showMessage("A ber");
                                            }
                                        }
                                );
                                break;

                            case ERROR_STATE:
                                showMessageError("Algo salió mal");
                                break;
                        }
                    }
                }
        );


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
