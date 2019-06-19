package com.miedo.dtodoaqui.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.miedo.dtodoaqui.R;

public class BaseFragment extends Fragment {

    private View content;
    private StateView stateView;

    /**
     * Lanza un intent solicitando abir una url.
     *
     * @param url
     */
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    /**
     * Oculta el teclado en pantalla.
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * Metodo para verificar si el teclado virtual esta en pantalla
     *
     * @return
     */
    public boolean isOpenKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            return true;
        } else {
            return false;
        }
    }


    public void showSnackMessage(String message, int colorResource) {
        View container = getView().findViewById(R.id.container);
        if (container != null) {
            Snackbar snackbar = Snackbar.make(container, message, Snackbar.LENGTH_LONG);

            /*snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, colorResource));
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);*/
            snackbar.show();
        } else {
            Toast toast =
                    Toast.makeText(getContext(),
                            message, Toast.LENGTH_LONG);
            toast.show();
        }

    }

    public void showMessageError(String message) {
        // aun por definir el color del snackbar para el error
        showSnackMessage(message, R.color.colorPrimaryDark);
    }

    public void showMessage(String message) {
        showSnackMessage(message, com.google.android.material.R.color.error_color_material_light);
    }

    public void showToastMessage(String message) {
        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                    }
                });

    }


    public void setUpStateView(View anotherView) {
        stateView = new StateView(anotherView);
    }

    public StateView getStateView() {
        return stateView;
    }
}

