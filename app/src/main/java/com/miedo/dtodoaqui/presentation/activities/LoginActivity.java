package com.miedo.dtodoaqui.presentation.activities;

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
import com.miedo.dtodoaqui.utils.KeyboardUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.usernameEditText)
    TextInputEditText usernameEditText;

    @BindView(R.id.passwordEditText)
    TextInputEditText passwordEditText;

    @BindView(R.id.loginButton)
    Button buttonLogin;

    View focusedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View container = findViewById(R.id.container);
        setUpStateView(container);
        ButterKnife.bind(this);

        buttonLogin.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(this);

            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (validateFields(username, password)) {
                LoginTask loginTask = new LoginTask();
                loginTask.execute(username, password);

            }


        });

    }

    private boolean validateFields(String username, String password) {
        boolean continueFlag = true;
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
        return continueFlag;
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            getStateView().showLoadingTitle("Iniciando sesión como " + usernameEditText.getText().toString().trim());
        }

        @Override
        protected void onPostExecute(Boolean rpta) {
            getStateView().hideStateView();
            if (rpta) {
                setResult(UnloggedProfileFragment.LOGIN_SUCCESSFUL);
                finish();
            } else {
                showMessageError("Usuario no encontrado");
            }

        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean successful = false;

            String username = strings[0];
            String password = strings[1];

            // Armamos un JSON en forma de string para el body de la peticion
            String requestString = JSONUtils.getLoginRequestJSON(username, password);
            // Creamos el requestBody
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestString);

            //Obtenemos una instancia de la api
            DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);

            // Preparamos la peticion
            Call<ResponseBody> call = api.loginUser(requestBody);

            // Realizamos la peticion sincrona

            try {
                Response response = call.execute();
                if (response.isSuccessful()) {


                    UserTO user = new UserTO();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setJwt(JSONUtils.getJWT(((ResponseBody) response.body()).string()));
                    Log.i(TAG, "JWT del usuario encontrado es :" + user.getJwt());

                    SessionManager.getInstance(getApplicationContext()).startSession(user);
                    successful = true;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


            return successful;
        }


    }

}
