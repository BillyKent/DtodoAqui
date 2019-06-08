package com.miedo.dtodoaqui.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.miedo.dtodoaqui.R;

/**
 * Helper para la visualización de estados en pantalla
 */
public class StateView {


    public static final int SIN_INTERNET_STATE = 1;
    public static final int SIN_RESULTADOS_BUSQUEDA_STATE = 2;
    public static final int SIN_RESULTADOS_ESTABLECIMIENTOS_STATE = 3;
    public static final int REGISTRANDO_USUARIO = 4;

    private View stateView;
    private View anotherView;
    private TextView titleView;
    private TextView descriptionView;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Button buttonAction;

    private int shortAnimationDuration = 2000;

    public StateView(View viewParent, View anotherView) {
        this.anotherView = anotherView;
        stateView = viewParent.findViewById(R.id.state_view);
        if (stateView != null) {

            titleView = stateView.findViewById(R.id.state_title);
            descriptionView = stateView.findViewById(R.id.state_description);
            imageView = stateView.findViewById(R.id.state_icon);
            buttonAction = stateView.findViewById(R.id.state_button);
            progressBar = stateView.findViewById(R.id.state_loading_progress_bar);

            hideStateView();
        }
        if (anotherView != null) {
            shortAnimationDuration = anotherView.getContext().getResources().getInteger(android.R.integer.config_longAnimTime);
        }
    }

    public void showLoadingWithTitle(int title) {

        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        // configuramos el state view
                        titleView.setText(title);

                        progressBar.setVisibility(View.VISIBLE);
                        titleView.setVisibility(View.VISIBLE);
                        descriptionView.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        buttonAction.setVisibility(View.GONE);

                        finalAnimation(stateView, anotherView);

                    }
                });

    }

    public void showLoadingWithTitle(String title) {

        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        // configuramos el state view
                        titleView.setText(title);

                        progressBar.setVisibility(View.VISIBLE);
                        titleView.setVisibility(View.VISIBLE);
                        descriptionView.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        buttonAction.setVisibility(View.GONE);

                        finalAnimation(stateView, anotherView);

                    }
                });

    }


    public void showLoading() {

        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        // configuramos el state view
                        progressBar.setVisibility(View.VISIBLE);
                        titleView.setVisibility(View.GONE);
                        descriptionView.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        buttonAction.setVisibility(View.GONE);


                        finalAnimation(stateView, anotherView);


                    }
                });


    }

    public void hideStateView() {


        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        // configuramos el state view
                        progressBar.setVisibility(View.GONE);
                        titleView.setVisibility(View.GONE);
                        descriptionView.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        buttonAction.setVisibility(View.GONE);

                        finalAnimation(anotherView, stateView);

                    }
                });
    }


    public void showState(int resTitle, int resDescription, int resIcon, View.OnClickListener listener) {
        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        stateView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        titleView.setText(resTitle);
                        descriptionView.setText(resDescription);
                        imageView.setImageResource(resIcon);
                        buttonAction.setOnClickListener(listener);

                        descriptionView.setVisibility(View.VISIBLE);
                        titleView.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        buttonAction.setVisibility((listener != null) ? View.VISIBLE : View.GONE);


                        finalAnimation(stateView, anotherView);
                    }
                });
    }

    public void showActionState(String title, String description, View.OnClickListener listener) {
        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        stateView.setVisibility(View.GONE);
                        titleView.setText(title);
                        descriptionView.setText(description);
                        buttonAction.setOnClickListener(listener);


                        titleView.setVisibility(View.VISIBLE);
                        descriptionView.setVisibility(View.VISIBLE);
                        buttonAction.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                        finalAnimation(stateView, anotherView);

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
            case REGISTRANDO_USUARIO:
                showLoadingWithTitle(R.string.registrando_usuario_title);
        }

    }

    private void finalAnimation(View toRevealView, View toHideView) {

        toRevealView.setAlpha(0f);
        toRevealView.setVisibility(View.VISIBLE);
        toRevealView.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        // primero desaparecemos la otra vista
        toHideView.setVisibility(View.VISIBLE);
        toHideView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toHideView.setVisibility(View.GONE);
                    }
                });
    }

    /**
     stateView.setAlpha(0f);
     stateView.setVisibility(View.VISIBLE);
     stateView.animate()
     .alpha(1f)
     .setDuration(shortAnimationDuration)
     .setListener(null);

     // primero desaparecemos la otra vista
     anotherView.setVisibility(View.VISIBLE);
     anotherView.animate()
     .alpha(0f)
     .setDuration(shortAnimationDuration)
     .setListener(new AnimatorListenerAdapter() {
    @Override public void onAnimationEnd(Animator animation) {
    anotherView.setVisibility(View.GONE);
    }
    });
     */


}
