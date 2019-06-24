package com.miedo.dtodoaqui.presentation.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.presentation.fragments.LoggedFragment;
import com.miedo.dtodoaqui.presentation.fragments.UnloggedProfileFragment;

public class RegisterEstablishmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_establishment);


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Salir del registro")
                .setCancelable(true)
                .setMessage("¿Seguro que desea salir?")
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(LoggedFragment.REGISTER_ESTABLISHMENT_CANCELLED);
                        finish();
                    }
                })
                .setNegativeButton("Quedarse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
