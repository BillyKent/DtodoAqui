package com.miedo.dtodoaqui.presentation.activityscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseActivity;
import com.miedo.dtodoaqui.presentation.fragments.LoggedFragment;
import com.miedo.dtodoaqui.presentation.fragments.UnloggedProfileFragment;
import com.miedo.dtodoaqui.utils.StateView;

public class RegisterUserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        View viewParent = findViewById(R.id.parent);
        View container = findViewById(R.id.container);

        setUpStateView(viewParent, container);

        findViewById(R.id.registerButton).setOnClickListener(v -> {
            getStateView().showCustomState(StateView.REGISTRANDO_USUARIO);

            new Thread() {
                @Override
                public void run() {

                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    getStateView().hideStateView();

                    showToastMessage("Usuario creado correctamente");
                    finish();

                }
            }.start();


        });

    }
}
