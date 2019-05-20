package com.miedo.dtodoaqui.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    private boolean loading = false;

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

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

    /**
     * Alterna la visibilidad de un view pasado por parametro
     *
     * @param view
     */
    public void toggleVisibilityView(View view) {
        if (view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }

    }

}

