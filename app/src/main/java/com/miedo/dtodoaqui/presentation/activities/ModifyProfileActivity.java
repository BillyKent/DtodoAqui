package com.miedo.dtodoaqui.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseActivity;
import com.miedo.dtodoaqui.data.ProfileTO;
import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.viewmodels.ModifyProfileViewModel;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);
        setUpStateView(findViewById(R.id.container));

        // Bindamos las vistas
        ButterKnife.bind(this);

        // Obtenemos el viewmodel y le seteamos el jwt
        viewModel = ViewModelProviders.of(this).get(ModifyProfileViewModel.class);
        viewModel.setJwt(SessionManager.getInstance(getApplicationContext()).getCurrentSession().getJwt());

        modifyButton.setOnClickListener(v -> {
            clearErrorFields();

            if (validateFields()) {
                boolean create = getIntent().getBooleanExtra("create", false);

                ProfileTO newProfile = createTransferObject();


                if (create) {
                    viewModel.createProfile(newProfile);
                }

            }

        });

    }

    private ProfileTO createTransferObject() {
        ProfileTO nuevo = new ProfileTO();

        for (int i = 0; i < editTextList.size(); i++) {
            String fieldString = editTextList.get(i).toString().trim();
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
