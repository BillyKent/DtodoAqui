package com.miedo.dtodoaqui.presentation.activities;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseActivity;
import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.viewmodels.ProfileViewModel;


public class MainActivity extends BaseActivity {


    private NavController navController;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtenemos la barra de navegacion inferior
        navView = findViewById(R.id.nav_view);

        // Definimos el host
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        // Obtenemos el nav controller
        navController = host.getNavController();

        // Listener para el cambio de destinos
        // lo utilizamos para actualizar el item seleccionado del navView
        navController.addOnDestinationChangedListener(mOnDestinationChangedListener);

        // listener para la seleccion de tabs
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (!Places.isInitialized()) {
            try {
                ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                Bundle bundle = ai.metaData;
                String apiKey = bundle.getString("com.google.android.geo.API_KEY");
                Places.initialize(getApplicationContext(), apiKey);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        ProfileViewModel profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        SessionManager sm = SessionManager.getInstance(this);
        if (sm.isUserLogged()) {
            if (sm.getCurrentSession().getJwt() == null) {
                profileViewModel.setCurrentUser(sm.getCurrentSession());
                profileViewModel.obtenerPerfil();
            }
        }

        //SessionManager.getInstance(this).closeSession();
    }


    // listener para la seleccion de tabs
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            navigateTo(item.getItemId());
            return false;
        }
    };

    // listener para actualizar la interfaz del bottom bar
    private NavController.OnDestinationChangedListener mOnDestinationChangedListener = new NavController.OnDestinationChangedListener() {
        @Override
        public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
            switch (destination.getId()) {
                case R.id.search_dest:
                    navView.getMenu().findItem(R.id.search_tab).setChecked(true);
                    break;
                /*case R.id.activity_dest:
                    navView.getMenu().findItem(R.id.activity_tab).setChecked(true);
                    break;*/
                case R.id.logged_dest:
                case R.id.unlogged_dest:
                    // Si se navega al perfil se debe marcar el tab Profile
                    navView.getMenu().findItem(R.id.profile_tab).setChecked(true);
                    break;
            }
        }
    };


    public void navigateTo(int itemId) {
        // Define la animacion de transicion y si se agrega al backstack
        // Si estamos en el tab search no se agrega al backstack
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.search_dest, (itemId == R.id.search_tab) ? true : false)
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .setPopEnterAnim(R.anim.fade_in)
                .setPopExitAnim(R.anim.fade_out)
                .build();

        int dest = -1;
        switch (itemId) {
            case R.id.profile_tab:
                dest = (SessionManager.getInstance(getApplicationContext()).isUserLogged()) ? R.id.logged_dest : R.id.unlogged_dest;
                break;
            case R.id.search_tab:
                dest = R.id.search_dest;
                break;
            /*case R.id.activity_tab:
                dest = R.id.activity_dest;
                break;*/
        }
        if (dest != -1) {
            navController.navigate(dest, null, navOptions);
        }


    }
}
