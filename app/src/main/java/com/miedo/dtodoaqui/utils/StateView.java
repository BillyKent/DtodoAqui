package com.miedo.dtodoaqui.utils;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Minitutorial de como usar esta wea
 *
 * 1. Incluye el layout stateView donde vas a mostrar el StateView.
 *    De preferencia elige que sea hijo del ViewGroup raiz.
 *
 * 2. En el OnCreateView del fragment, pasa el view que se infla por parametro
 *    en el constructor al momento de instanciar el StateView
 *
 * 3. Disfruta, gaaaaa.
 *
 * Por implementar: Alternar con el viewgroup padre con animaciones
 */


import com.miedo.dtodoaqui.R;


public class StateView {

    public static final int SIN_INTERNET_STATE = 1;
    public static final int SIN_RESULTADOS_BUSQUEDA_STATE = 2;
    public static final int SIN_RESULTADOS_ESTABLECIMIENTOS_STATE = 3;

    private View stateView;
    private View anotherView;
    private TextView titleView;
    private TextView descriptionView;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Button buttonAction;

    public StateView(View viewParent, View anotherView) {
        this.anotherView = anotherView;
        stateView = viewParent.findViewById(R.id.state_view);
        if (stateView != null) {

            titleView = stateView.findViewById(R.id.state_title);
            descriptionView = stateView.findViewById(R.id.state_description);
            imageView = stateView.findViewById(R.id.state_icon);
            buttonAction = stateView.findViewById(R.id.state_button);
            progressBar = stateView.findViewById(R.id.state_loading_progress_bar);

            finishLoading();
        }
    }


    public void showLoading() {

        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {

                        if (stateView != null && anotherView != null) {
                            anotherView.setVisibility(View.GONE);

                            titleView.setVisibility(View.GONE);
                            descriptionView.setVisibility(View.GONE);
                            imageView.setVisibility(View.GONE);
                            buttonAction.setVisibility(View.GONE);

                            progressBar.setVisibility(View.VISIBLE);

                            stateView.setVisibility(View.VISIBLE);
                        }


                    }
                });


    }

    public void finishLoading() {


        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        if (stateView != null) {
                            stateView.setVisibility(View.GONE);
                            anotherView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    public void showState(String title, String description, int resIcon, View.OnClickListener listener) {


        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        if (stateView != null && anotherView != null) {

                            progressBar.setVisibility(View.GONE);
                            titleView.setText(title);
                            descriptionView.setText(description);
                            imageView.setImageResource(resIcon);
                            buttonAction.setOnClickListener(listener);

                            descriptionView.setVisibility((description != null) ? View.VISIBLE : View.GONE);
                            titleView.setVisibility((title != null) ? View.VISIBLE : View.GONE);
                            buttonAction.setVisibility((listener != null) ? View.VISIBLE : View.GONE);
                           imageView.setVisibility(View.VISIBLE);

                            stateView.setVisibility(View.VISIBLE);
                        }
                    }
                });


    }

    public void showState(int resTitle, int resDescription, int resIcon, View.OnClickListener listener) {
        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        if (stateView != null) {

                            progressBar.setVisibility(View.GONE);
                            titleView.setText(resTitle);
                            descriptionView.setText(resDescription);
                            imageView.setImageResource(resIcon);
                            buttonAction.setOnClickListener(listener);

                            descriptionView.setVisibility(View.VISIBLE);
                            titleView.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.VISIBLE);
                            buttonAction.setVisibility((listener != null) ? View.VISIBLE : View.GONE);


                            stateView.setVisibility(View.VISIBLE);
                        }
                    }


                });

    }

    public void showCustomState(int state) {

        switch (state) {

            case SIN_RESULTADOS_BUSQUEDA_STATE:
                showState(R.string.sin_resultados_busqueda_title, R.string.sin_resultados_busqueda_descrip, R.drawable.ic_not_found, null);
                break;

            case SIN_RESULTADOS_ESTABLECIMIENTOS_STATE:
                showState(R.string.sin_resultados_establecimientos_title, R.string.sin_resultados_establecimientos_descrip, R.drawable.ic_not_found,
                        null);
                break;

            case SIN_INTERNET_STATE:
                showState(R.string.sin_internet_title, R.string.sin_internet_descrip, R.drawable.ic_dead, null);
                break;
        }

    }


}
