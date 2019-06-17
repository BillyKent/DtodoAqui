package com.miedo.dtodoaqui.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.util.Strings;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.adapters.ProfileInfoAdapter;
import com.miedo.dtodoaqui.core.BaseFragment;
import com.miedo.dtodoaqui.core.StateView;
import com.miedo.dtodoaqui.data.ProfileTO;
import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.presentation.activities.ModifyProfileActivity;
import com.miedo.dtodoaqui.viewmodels.ProfileViewModel;

import java.util.ArrayList;


public class LoggedFragment extends BaseFragment {

    public static final String TAG = LoggedFragment.class.getSimpleName();

    public static final int MODIFIY_PROFILE_REQUEST_CODE = 100;

    public static final int MODIFY_OK = 40;
    public static final int MODIFY_CANCEL = 30;


    ProfileViewModel viewModel;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ArrayList<ProfileInfoAdapter.ProfileItem> items = new ArrayList<>();
    ListView listView;
    private ProfileInfoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // obtenemos el viewmodel
        viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        View view = inflater.inflate(R.layout.fragment_logged_profile, container, false);
        listView = (ListView) view.findViewById(R.id.list_details);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Perfil");

        adapter = new ProfileInfoAdapter(getContext(), items);
        listView.setAdapter(adapter);
        setUpStateView(view.findViewById(R.id.container));

        if (viewModel.isCurrentProfileActive()) { // si la cuenta activa no es null
            showItems();
        } else {
            viewModel.obtenerPerfil(SessionManager.getInstance(requireContext()).getCurrentSession().getJwt());
        }

        viewModel.getProfileState().observe(getViewLifecycleOwner(), profileState -> {

            switch (profileState) {
                case OBTENIENDO:
                    if (getStateView() == null) {
                        Log.i(TAG, "stateview es nulo");
                    }
                    getStateView().forceLoadingTitle("Obteniendo perfil");
                    break;
                case SIN_PERFIL:
                    getStateView().showTitleMessageButtonAction("No configuraste un perfil",
                            "Configura uno ahora mismo es fácil y rápido",
                            "Configurar"
                            ,
                            v -> {

                                Intent intent = new Intent(requireContext(), ModifyProfileActivity.class);
                                startActivityForResult(intent, MODIFIY_PROFILE_REQUEST_CODE);

                            }
                    );
                    break;
                case CON_PERFIL:
                    getStateView().hideStateView();
                    showItems();
                    break;

                case ERROR_STATE:
                    getStateView().showTitleMessageButtonAction("Error",
                            "Algo salió mal :s .",
                            "Refrescar"
                            ,
                            v -> {
                                showMessage("Reintentado :v");
                            });
                    break;
            }


        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        /*if (viewModel.getCurrentProfile() == null) {
            viewModel.obtenerPerfil(SessionManager.getInstance(getContext()).getCurrentSession().getJwt());
        } else {
            loadItems(viewModel.getCurrentProfile());
            adapter.notifyDataSetChanged();
        }

        viewModel.getProfileState().observe(getViewLifecycleOwner(), profileState -> {

            switch (profileState) {
                case CON_PERFIL:
                    getStateView().hideStateView();
                    loadItems(viewModel.getCurrentProfile());
                    adapter.notifyDataSetChanged();
                    break;
                case SIN_PERFIL:
                    appBarLayout.setExpanded(false, false);

                    stateView.forceTitleMessageAction(
                            "Sin perfil",
                            "No encontramos un perfil asociado a tu cuenta," +
                                    "configura uno ahora mismo es fácil y rápido.",
                            v -> {
                                ((Button) v).setText("Configurar");
                                showMessage("GAAAA ABER");
                                viewModel.obtenerPerfil(SessionManager.getInstance(getContext()).getCurrentSession().getJwt());
                                stateView.showLoadingTitle("Buscando");
                            }
                    );
                    break;

            }
        });
*/

        /*new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);

                    stateView.hideStateView();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();*/


        // configuracion del stateView
        //setUpStateView(view, view.findViewById(R.id.container));

        /*viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        adapter = new ProfileInfoAdapter(getContext(), items);
        listView = (ListView) view.findViewById(R.id.listview_profile);
        listView.setAdapter(adapter);


        viewModel.getProfileState().observe(getViewLifecycleOwner(),
                new Observer<ProfileViewModel.ProfileState>() {
                    @Override
                    public void onChanged(ProfileViewModel.ProfileState profileState) {
                        switch (profileState) {
                            case VERIFICANDO_PERFIL:
                                //getStateView().showLoadingWithTitle("Verificando perfil");
                                viewModel.verificarPerfil();
                                break;
                            case CON_PERFIL:
                                loadItems(viewModel.getCurrentProfile());
                                adapter.notifyDataSetChanged();
                                //getStateView().hideStateView();
                                break;
                            case OBTENIENDO_PERFIL:
                                //getStateView().showLoadingWithTitle("Obteniendo perfil");
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
*/

        //listView.setAdapter(adapter);
    }

    void showItems() {
        ProfileTO profile = viewModel.getCurrentProfile();
        if (profile == null) return;

        items.clear();


        // nombre
        if (!Strings.isEmptyOrWhitespace(profile.getFirstName()) && !Strings.isEmptyOrWhitespace(profile.getLastName())) {
            items.add(new ProfileInfoAdapter.ProfileItem(R.drawable.ic_account_box_black_24dp, "Nombre",
                    profile.getFirstName() + " " + profile.getLastName()
            ));
        }

        // telefono
        if (!Strings.isEmptyOrWhitespace(profile.getPhone())) {
            items.add(new ProfileInfoAdapter.ProfileItem(R.drawable.ic_phone_black_24dp, "Telefono",
                    profile.getPhone()
            ));
        }

        // direccion
        if (!Strings.isEmptyOrWhitespace(profile.getAddress())) {
            items.add(new ProfileInfoAdapter.ProfileItem(R.drawable.ic_location_on_black_24dp, "Dirección",
                    profile.getAddress()
            ));
        }

        // facebook
        if (!Strings.isEmptyOrWhitespace(profile.getFacebookUrl())) {
            items.add(new ProfileInfoAdapter.ProfileItem(R.drawable.ic_facebook, "Facebook",
                    profile.getFacebookUrl()
            ));

        }

    }

}
