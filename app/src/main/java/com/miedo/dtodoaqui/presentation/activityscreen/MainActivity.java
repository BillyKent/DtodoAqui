package com.miedo.dtodoaqui.presentation.activityscreen;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseActivity;
import com.miedo.dtodoaqui.data.local.SessionManager;


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
                case R.id.activity_dest:
                    navView.getMenu().findItem(destination.getId()).setChecked(true);
                    break;
                case R.id.logged_dest:
                case R.id.unlogged_dest:
                    // Si se navega al perfil se debe marcar el tab Profile
                    navView.getMenu().findItem(R.id.logged_dest).setChecked(true);
                    break;
            }
        }
    };


    private void navigateTo(int itemId) {
        // Define la animacion de transicion y si se agrega al backstack
        // Si estamos en el tab search no se agrega al backstack
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.search_dest, (itemId == R.id.search_dest) ? true : false)
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .setPopEnterAnim(R.anim.fade_in)
                .setPopExitAnim(R.anim.fade_out)
                .build();
        int dest = itemId;
        switch (itemId) {
            case R.id.logged_dest:
                dest = (SessionManager.getInstance(getApplicationContext()).isUserLogged()) ? R.id.logged_dest : R.id.unlogged_dest;
                break;
            case R.id.search_dest:
            case R.id.activity_dest:
                dest = itemId;
                break;
        }
        navController.navigate(dest, null, navOptions);

    }
}
