package com.miedo.dtodoaqui.presentation;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseActivity;


public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtenemos la barra de navegacion inferior

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Definimos el host
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        // Obtenemos el nav controller
        NavController navController = host.getNavController();


        //Seteamos la navegacion del navview al nav controller
        NavigationUI.setupWithNavController(navView, navController);

    }
}
