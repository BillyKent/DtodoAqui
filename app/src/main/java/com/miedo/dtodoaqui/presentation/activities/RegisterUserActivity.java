package com.miedo.dtodoaqui.presentation.activities;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.util.Strings;
import com.google.android.material.textfield.TextInputEditText;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseActivity;
import com.miedo.dtodoaqui.data.UserTO;
import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;
import com.miedo.dtodoaqui.presentation.fragments.UnloggedProfileFragment;
import com.miedo.dtodoaqui.utils.JSONUtils;
import com.miedo.dtodoaqui.utils.ValidatorUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class RegisterUserActivity extends BaseActivity {

    private static final String TAG = RegisterUserActivity.class.getSimpleName();

    View focusedView;

    @BindView(R.id.registerButton)
    Button registerButton;

    @BindView(R.id.emailEditText)
    TextInputEditText emailEditText;

    @BindView(R.id.usernameEditText)
    TextInputEditText usernameEditText;

    @BindView(R.id.passwordEditText)
    TextInputEditText passwordEditText;

    @BindView(R.id.passwordConfirmEditText)
    TextInputEditText passwordConfirmEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        View viewParent = findViewById(R.id.parent);
        View container = findViewById(R.id.container);
        setUpStateView(viewParent, container);
        ButterKnife.bind(this);

        registerButton.setOnClickListener(v -> {
            clearErrorFields();
            String email = emailEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String passwordConfirmation = passwordConfirmEditText.getText().toString();

            RegisterTask task = new RegisterTask();

            if (validateFields(email, username, password, passwordConfirmation)) {
                task.execute(email, username, password);
                Log.i(TAG, JSONUtils.getRegisterUserRequestJSON(email, username, password));
            }


        });

    }

    private void clearErrorFields() {
        emailEditText.setError(null);
        usernameEditText.setError(null);
        passwordEditText.setError(null);
        passwordConfirmEditText.setError(null);
    }

    private boolean validateFields(String email, String username, String password, String passwordConfirmation) {
        boolean continueFlag = true;
        // Campos vacios
        if (Strings.isEmptyOrWhitespace(passwordConfirmation)) {
            passwordConfirmEditText.setError(getString(R.string.empty_field_error));
            focusedView = passwordConfirmEditText;
            continueFlag = false;
        }
        if (Strings.isEmptyOrWhitespace(password)) {
            passwordEditText.setError(getString(R.string.empty_field_error));
            focusedView = passwordEditText;
            continueFlag = false;
        }
        if (Strings.isEmptyOrWhitespace(username)) {
            usernameEditText.setError(getString(R.string.empty_field_error));
            focusedView = usernameEditText;
            continueFlag = false;
        }
        if (Strings.isEmptyOrWhitespace(email)) {
            emailEditText.setError(getString(R.string.empty_field_error));
            focusedView = emailEditText;
            continueFlag = false;
        }
        // si algun campo esta vacio retorna
        if (!continueFlag) return false;

        // Password diferentes
        if (!password.equals(passwordConfirmation)) {
            passwordConfirmEditText.setError("Contraseña incorrecta");
            focusedView = passwordConfirmEditText;
            continueFlag = false;
        }
        // Email invalido
        if (!ValidatorUtils.isValidEmail(email)) {
            emailEditText.setError("Email inválido");
            focusedView = emailEditText;
            continueFlag = false;
        }

        return continueFlag;
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
                        setResult(UnloggedProfileFragment.REGISTER_CANCELLED);
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

    private class RegisterTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            getStateView().showLoadingWithTitle("Registrando");
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean successful = false;
            // Creamos el RequestBody para la peticion
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                    JSONUtils.getRegisterUserRequestJSON(strings[0], strings[1], strings[2]));

            // Obtenemos una instancia de la api
            DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);

            // Preparamos la peticion
            Call<ResponseBody> call = api.registerUser(requestBody);


            try {
                Response response = call.execute();
                if (response.isSuccessful()) {
                    ResponseBody responseBody = (ResponseBody) response.body();

                    // Creamos el objeto UserTO para iniciar session
                    UserTO user = new UserTO();
                    user.setUsername(strings[1]);
                    user.setPassword(strings[2]);
                    user.setJwt(JSONUtils.getJWT(responseBody.string()));
                    SessionManager sessionManager = SessionManager.getInstance(getApplicationContext());
                    sessionManager.startSession(user);

                    successful = true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return successful;
        }

        @Override
        protected void onPostExecute(Boolean rpta) {
            getStateView().hideStateView();

            if (rpta) {
                setResult(UnloggedProfileFragment.REGISTER_SUCCESSFUL);
                finish();
            } else {
                showMessageError("No se pudo registrar");
            }


        }


    }

}
