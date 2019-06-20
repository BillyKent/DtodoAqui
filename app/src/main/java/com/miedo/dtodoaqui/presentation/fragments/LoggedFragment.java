package com.miedo.dtodoaqui.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.util.Strings;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.adapters.ProfileInfoAdapter;
import com.miedo.dtodoaqui.core.BaseFragment;
import com.miedo.dtodoaqui.data.ProfileTO;
import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.presentation.activities.MainActivity;
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
    Toolbar toolbar;
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
        toolbar = (Toolbar) view.findViewById(R.id.profile_toolbar);
        listView = (ListView) view.findViewById(R.id.list_details);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Perfil");

        viewModel.setCurrentUser(SessionManager.getInstance(requireContext()).getCurrentSession());

        setHasOptionsMenu(true);

        adapter = new ProfileInfoAdapter(getContext(), items);
        listView.setAdapter(adapter);
        setUpStateView(view.findViewById(R.id.container));

        if (viewModel.isCurrentProfileActive()) { // si la cuenta activa no es null
            showItems();
        } else {
            viewModel.obtenerPerfil();
        }

        viewModel.getProfileState().observe(getViewLifecycleOwner(), profileState -> {

            switch (profileState) {
                case OBTENIENDO:
                    getStateView().forceLoadingTitle("Obteniendo perfil");
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
                    SessionManager.getInstance(requireContext()).closeSession();
                    ((MainActivity) requireActivity()).navigateTo(R.id.profile_tab);
                    break;
            }


        });

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit_option:

                    Intent intent = new Intent(requireContext(), ModifyProfileActivity.class);
                    intent.putExtra("create", false);

                    intent.putExtra("profile", viewModel.getCurrentProfile());
                    startActivityForResult(intent, MODIFIY_PROFILE_REQUEST_CODE);


                    return true;
                case R.id.refresh_option:
                    viewModel.obtenerPerfil();
                    return true;


                case R.id.signout_option:
                    SessionManager.getInstance(requireContext()).closeSession();
                    ((MainActivity) requireActivity()).navigateTo(R.id.profile_tab);
                    return true;
            }
            return false;

        });

        return view;
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


    void showItems() {
        ProfileTO profile = viewModel.getCurrentProfile();
        SessionManager.getInstance(requireContext()).setJwtToken(viewModel.getCurrentUser().getJwt());
        Log.i(TAG, "Perfil a mostrar : " + profile);
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
        adapter.notifyDataSetChanged();

    }


}
