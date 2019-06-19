package com.miedo.dtodoaqui.presentation.activities;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseActivity;
import com.miedo.dtodoaqui.data.ProfileTO;
import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.presentation.fragments.LoggedFragment;
import com.miedo.dtodoaqui.viewmodels.ModifyProfileViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class ModifyProfileActivity extends BaseActivity {

    private ModifyProfileViewModel viewModel;

    View focusedView;

    @BindViews({R.id.nombresEditText, R.id.apellidosEditText, R.id.descripcionEditText, R.id.telefonoEditText,
            R.id.paisEditText, R.id.direccionEditText, R.id.facebookEditText
    })
    List<TextInputEditText> editTextList;

    @BindView(R.id.modificarButton)
    Button modifyButton;

    ProfileTO newProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);
        setUpStateView(findViewById(R.id.container));

        // Extra que se obtiene del intent al momento de llamar al activity
        boolean create = getIntent().getBooleanExtra("create", false);

        // Bindamos las vistas
        ButterKnife.bind(this);

        // Bindamos las vistas con los datos del profile si se quiere editar:
        if (!create) {
            modifyButton.setText("Actualizar Perfil");
            BindFields();
        }

        // Obtenemos el viewmodel y le seteamos el jwt
        viewModel = ViewModelProviders.of(this).get(ModifyProfileViewModel.class);
        viewModel.setJwt(SessionManager.getInstance(getApplicationContext()).getCurrentSession().getJwt());

        modifyButton.setOnClickListener(v -> {
            clearErrorFields();

            if (validateFields()) {

                newProfile = createTransferObject();
                viewModel.createOrUpdateProfile(newProfile, create);
            }

        });

        viewModel.getCurrentState().observe(this, state -> {
            switch (state) {
                case CREATING:
                    getStateView().showLoadingTitle("Creando perfil");
                    break;
                case UPDATING:
                    getStateView().showLoadingTitle("Actualizando perfil");
                    break;
                case SUCCESSFUL:
                    Intent intent = new Intent();
                    intent.putExtra("newProfile", newProfile);
                    setResult(LoggedFragment.MODIFY_OK, intent);
                    finish();
                    break;
                case ERROR:
                    getStateView().hideStateView();
                    showToastMessage("Algo ocurrió mal");
                    break;
            }
        });

    }

    private void BindFields() {
        Intent intent = getIntent();
        ProfileTO profile = (ProfileTO) intent.getSerializableExtra("profile");
        for (int i = 0; i < editTextList.size(); i++) {
            TextInputEditText field = editTextList.get(i);
            switch (i) {
                case 0:
                    field.setText(profile.getFirstName());
                    break;
                case 1:
                    field.setText(profile.getLastName());
                    break;

                case 2:
                    field.setText(profile.getDescription());
                    break;

                case 3:
                    field.setText(profile.getPhone());
                    break;

                case 4:
                    field.setText(profile.getCountry());
                    break;

                case 5:
                    field.setText(profile.getAddress());
                    break;

                case 6:
                    field.setText(profile.getFacebookUrl());
                    break;

            }
        }

    }

    private ProfileTO createTransferObject() {
        ProfileTO nuevo = new ProfileTO();

        for (int i = 0; i < editTextList.size(); i++) {
            String fieldString = editTextList.get(i).getText().toString().trim();
            switch (i) {
                case 0:
                    nuevo.setFirstName(fieldString);
                    break;

                case 1:
                    nuevo.setLastName(fieldString);
                    break;

                case 2:
                    nuevo.setDescription(fieldString);
                    break;

                case 3:
                    nuevo.setPhone(fieldString);
                    break;

                case 4:
                    nuevo.setCountry(fieldString);
                    break;

                case 5:
                    nuevo.setAddress(fieldString);
                    break;

                case 6:
                    nuevo.setFacebookUrl(fieldString.isEmpty() ? null : fieldString); // facebook puede ser null
                    break;

            }
        }

        return nuevo;
    }

    private void clearErrorFields() {
        for (TextInputEditText et : editTextList) {
            if (et.getId() == R.id.facebookEditText) continue; // El facebook puede ser vacio
            et.setError(null);
        }
    }

    private boolean validateFields() {
        boolean continueFlag = true;

        for (int i = editTextList.size() - 1; i >= 0; i--) {
            if (editTextList.get(i).getId() == R.id.facebookEditText) continue;

            if (editTextList.get(i).getText().toString().isEmpty()) {
                editTextList.get(i).setError("Este campo no puede estar vacío");
                continueFlag = false;
                focusedView = editTextList.get(i);
            }
        }

        if (!continueFlag) return false;

        return continueFlag;
    }


}
