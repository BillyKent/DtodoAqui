package com.miedo.dtodoaqui.presentation.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.util.Strings;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.adapters.ProfileFakeInfoAdapter;
import com.miedo.dtodoaqui.adapters.ProfileInfoAdapter;
import com.miedo.dtodoaqui.core.BaseFragment;
import com.miedo.dtodoaqui.data.ProfileTO;
import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.presentation.activities.MainActivity;
import com.miedo.dtodoaqui.presentation.activities.ModifyProfileActivity;
import com.miedo.dtodoaqui.viewmodels.ProfileViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoggedFragment extends BaseFragment {

    public static final String TAG = LoggedFragment.class.getSimpleName();

    public static final int MODIFIY_PROFILE_REQUEST_CODE = 100;

    public static final int MODIFY_OK = 40;
    public static final int MODIFY_CANCEL = 30;

    @BindView(R.id.listaxd)
    public ListView listView;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    // Lista de items y el adapter del listview
    private ArrayList<ProfileFakeInfoAdapter.ProfileItem> items = new ArrayList<>();
    private ProfileFakeInfoAdapter adapter;

    // Viewmodel
    private ProfileViewModel viewModel;

    public LoggedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == MODIFIY_PROFILE_REQUEST_CODE) {
            if (resultCode == MODIFY_OK) {
                viewModel.setCurrentProfile((ProfileTO) data.getSerializableExtra("newProfile"));
                showItems();

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Obtenemos el viewmodel y le asignamos el usuario de la sesion actual
        viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        viewModel.setCurrentUser(SessionManager.getInstance(requireContext()).getCurrentSession());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logged_profile, container, false);
        ButterKnife.bind(this, view);

        // Seteamos el stateview
        setUpStateView(view.findViewById(R.id.container));

        // Instanciamos y seteamos el adapter del listview
        adapter = new ProfileFakeInfoAdapter(requireContext(), items);
        listView.setAdapter(adapter);

        if (viewModel.isCurrentProfileActive()) { // si la cuenta activa no es null
            getStateView().forceHideStateView();
            showItems();
        } else {
            viewModel.obtenerPerfil();
        }

        // Observamos el estado del viewmodel
        viewModel.getProfileState().observe(getViewLifecycleOwner(), profileState -> {

            switch (profileState) {
                case OBTENIENDO:
                    getStateView().forceLoadingTitle("Obteniendo perfil");
                    //showMessage("Obteniendo perfil");
                    break;
                case SIN_PERFIL:
                    getStateView().showTitleMessageButtonAction("No configuraste un perfil",
                            "Configura uno ahora mismo es fácil y rápido",
                            "Configurar"
                            ,
                            v -> {

                                Intent intent = new Intent(requireContext(), ModifyProfileActivity.class);
                                intent.putExtra("create", viewModel.getToCreate());
                                startActivityForResult(intent, MODIFIY_PROFILE_REQUEST_CODE);


                            }
                    );
                    //showMessage("No se encontro perfil");
                    break;
                case CON_PERFIL:
                    getStateView().hideStateView();
                    showItems();
                    break;

                case ERROR_STATE:
                    items.clear();
                    adapter.notifyDataSetChanged();
                    getStateView().showTitleMessageIcon(
                            "Error",
                            "Algo salió mal.",
                            R.drawable.perrito
                    );
                    break;

                case INVALID_CREDENTIALS:
                    showToastMessage("Usuario y contraseña no válidos");
                    viewModel.setCurrentProfile(null);
                    SessionManager.getInstance(requireContext()).closeSession();
                    ((MainActivity) requireActivity()).navigateTo(R.id.profile_tab);
                    break;
            }


        });

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {

                case R.id.refresh_option:
                    viewModel.obtenerPerfil();
                    return true;

                case R.id.edit_option:

                    Intent intent = new Intent(requireContext(), ModifyProfileActivity.class);
                    intent.putExtra("create", false);

                    intent.putExtra("profile", viewModel.getCurrentProfile());
                    startActivityForResult(intent, MODIFIY_PROFILE_REQUEST_CODE);

                    return true;

                case R.id.logout_option:

                    new AlertDialog.Builder(requireContext())
                            .setTitle("Cerrar sesión")
                            .setCancelable(true)
                            .setMessage("¿Seguro que desea cerrar sesión?")
                            .setPositiveButton("Cerrar sesión", (dialog, which) -> {
                                viewModel.setCurrentProfile(null);
                                SessionManager.getInstance(requireContext()).closeSession();
                                ((MainActivity) requireActivity()).navigateTo(R.id.profile_tab);
                            })
                            .setNegativeButton("Cancelar", (dialog, which) -> {

                            })
                            .show();

                    return true;

            }
            return false;

        });

        return view;

    }


    void showItems() {
        ProfileTO profile = viewModel.getCurrentProfile();
        SessionManager.getInstance(requireContext()).setJwtToken(viewModel.getCurrentUser().getJwt());
        Log.i(TAG, "Perfil a mostrar : " + profile);

        if (profile == null) return;
        items.clear();
        // nombre
        if (!Strings.isEmptyOrWhitespace(profile.getFirstName()) && !Strings.isEmptyOrWhitespace(profile.getLastName())) {
            items.add(new ProfileFakeInfoAdapter.ProfileItem(R.drawable.ic_account_box_black_24dp,
                    profile.getFirstName() + " " + profile.getLastName()
            ));
        }

        // telefono
        if (!Strings.isEmptyOrWhitespace(profile.getPhone())) {
            items.add(new ProfileFakeInfoAdapter.ProfileItem(R.drawable.ic_phone_black_24dp,
                    profile.getPhone()
            ));
        }

        // direccion
        if (!Strings.isEmptyOrWhitespace(profile.getAddress())) {
            items.add(new ProfileFakeInfoAdapter.ProfileItem(R.drawable.ic_location_on_black_24dp,
                    profile.getAddress()
            ));
        }

        // facebook
        if (!Strings.isEmptyOrWhitespace(profile.getFacebookUrl())) {
            items.add(new ProfileFakeInfoAdapter.ProfileItem(R.drawable.ic_facebook,
                    profile.getFacebookUrl()
            ));

        }

        if (!Strings.isEmptyOrWhitespace(profile.getFirstName()) && !Strings.isEmptyOrWhitespace(profile.getLastName())) {
            items.add(new ProfileFakeInfoAdapter.ProfileItem(R.drawable.ic_account_box_black_24dp,
                    profile.getFirstName() + " " + profile.getLastName() + profile.getFirstName() + " " + profile.getLastName() + profile.getFirstName() + " " + profile.getLastName()
            ));
        }

        // telefono
        if (!Strings.isEmptyOrWhitespace(profile.getPhone())) {
            items.add(new ProfileFakeInfoAdapter.ProfileItem(R.drawable.ic_phone_black_24dp,
                    profile.getPhone()
            ));
        }

        // direccion
        if (!Strings.isEmptyOrWhitespace(profile.getAddress())) {
            items.add(new ProfileFakeInfoAdapter.ProfileItem(R.drawable.ic_location_on_black_24dp,
                    profile.getAddress()
            ));
        }

        // facebook
        if (!Strings.isEmptyOrWhitespace(profile.getFacebookUrl())) {
            items.add(new ProfileFakeInfoAdapter.ProfileItem(R.drawable.ic_facebook,
                    profile.getFacebookUrl()
            ));

        }

        if (!Strings.isEmptyOrWhitespace(profile.getFirstName()) && !Strings.isEmptyOrWhitespace(profile.getLastName())) {
            items.add(new ProfileFakeInfoAdapter.ProfileItem(R.drawable.ic_account_box_black_24dp,
                    profile.getFirstName() + " " + profile.getLastName()
            ));
        }

        // telefono
        if (!Strings.isEmptyOrWhitespace(profile.getPhone())) {
            items.add(new ProfileFakeInfoAdapter.ProfileItem(R.drawable.ic_phone_black_24dp,
                    profile.getPhone()
            ));
        }

        // direccion
        if (!Strings.isEmptyOrWhitespace(profile.getAddress())) {
            items.add(new ProfileFakeInfoAdapter.ProfileItem(R.drawable.ic_location_on_black_24dp,
                    profile.getAddress()
            ));
        }

        // facebook
        if (!Strings.isEmptyOrWhitespace(profile.getFacebookUrl())) {
            items.add(new ProfileFakeInfoAdapter.ProfileItem(R.drawable.ic_facebook,
                    profile.getFacebookUrl()
            ));

        }
        adapter.notifyDataSetChanged();

    }


}
