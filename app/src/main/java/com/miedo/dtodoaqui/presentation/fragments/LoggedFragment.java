package com.miedo.dtodoaqui.presentation.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.adapters.ProfileInfoAdapter;
import com.miedo.dtodoaqui.core.BaseFragment;
import com.miedo.dtodoaqui.core.StateView;
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
        // obtenemos el viewmodel
        viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        View view = inflater.inflate(R.layout.fragment_logged_profile, container, false);
        listView = (ListView) view.findViewById(R.id.list_details);
        adapter = new ProfileInfoAdapter(getContext(), items);
        listView.setAdapter(adapter);
        stateView = new StateView(view.findViewById(R.id.container));
        return view;
    }


    StateView stateView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        if (viewModel.getCurrentProfile() == null) {
            viewModel.obtenerPerfil(SessionManager.getInstance(getContext()).getCurrentSession().getJwt());
        } else {
            loadItems(viewModel.getCurrentProfile());
            adapter.notifyDataSetChanged();
        }

        viewModel.getProfileState().observe(getViewLifecycleOwner(), profileState -> {
            stateView.hideStateView();
            switch (profileState) {
                case CON_PERFIL:
                    loadItems(viewModel.getCurrentProfile());
                    adapter.notifyDataSetChanged();
                    break;
                case SIN_PERFIL:
                    stateView.showTitleMessageAction(
                            "Sin perfil",
                            "No encontramos un perfil asociado a tu cuenta," +
                                    "configura uno ahora mismo es fácil y rápido.",
                            v -> {
                                showMessage("GAAAA ABER");
                                viewModel.obtenerPerfil(SessionManager.getInstance(getContext()).getCurrentSession().getJwt());
                                stateView.showLoadingTitle("Buscando");
                            }
                    );
                    break;

            }
        });


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
